package fsa.grp4.clinic_appointment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface IGenericRepository<T, ID> {

    T add(T entity);

    T update(T entity);

    void delete(T entity);

    Optional<T> getById(ID id);

    List<T> getAll();

    Page<T> search(Specification<T> specification, Pageable pageable);

    long count(Specification<T> specification);
}

