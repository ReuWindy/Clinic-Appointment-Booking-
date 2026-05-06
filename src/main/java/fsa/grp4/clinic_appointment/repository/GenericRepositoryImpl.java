package fsa.grp4.clinic_appointment.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class GenericRepositoryImpl<T, ID> implements IGenericRepository<T, ID> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    protected GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public T add(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public Optional<T> getById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Page<T> search(Specification<T> specification, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);

        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, cq, cb);
            if (predicate != null) {
                cq.where(predicate);
            }
        }

        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                Order jpaOrder = order.isAscending()
                        ? cb.asc(root.get(order.getProperty()))
                        : cb.desc(root.get(order.getProperty()));
                cq.orderBy(jpaOrder);
            });
        }

        TypedQuery<T> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<T> content = query.getResultList();
        long total = count(specification);

        return new PageImpl<T>(content, pageable, total);
    }

    @Override
    public long count(Specification<T> specification) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));

        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, cq, cb);
            if (predicate != null) {
                cq.where(predicate);
            }
        }

        return em.createQuery(cq).getSingleResult();
    }
}

