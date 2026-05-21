package fsa.grp4.clinic_appointment.service.implementation;

import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.publicapi.PublicDoctorResponse;
import fsa.grp4.clinic_appointment.dto.publicapi.PublicSpecialtyResponse;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.service.contract.IAdminService;
import fsa.grp4.clinic_appointment.service.contract.IPublicContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

@Service
public class PublicContentServiceImpl implements IPublicContentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicContentServiceImpl.class);

    private final IAdminService adminService;
    private final RestTemplate restTemplate;
    private final String internalApiBaseUrl;
    private final String internalAdminToken;

    public PublicContentServiceImpl(
            IAdminService adminService,
            @Value("${internal.api.base-url:http://localhost:8080}") String internalApiBaseUrl,
            @Value("${internal.api.admin-token:}") String internalAdminToken
    ) {
        this.adminService = adminService;
        this.restTemplate = new RestTemplate();
        this.internalApiBaseUrl = internalApiBaseUrl;
        this.internalAdminToken = internalAdminToken;
    }

    @Override
    public List<PublicSpecialtyResponse> getPublicSpecialties() {
        List<SpecialtyResponse> specialties = fetchFromInternalApi(
                "/api/admin/specialties",
                new ParameterizedTypeReference<List<SpecialtyResponse>>() {
                },
                adminService::getAllSpecialties
        );

        return specialties.stream()
                .map(item -> new PublicSpecialtyResponse(item.getId(), item.getName(), item.getDescription()))
                .toList();
    }

    @Override
    public List<PublicDoctorResponse> getPublicDoctors() {
        List<AdminDoctorResponse> doctors = fetchFromInternalApi(
                "/api/admin/doctors",
                new ParameterizedTypeReference<List<AdminDoctorResponse>>() {
                },
                adminService::getAllDoctors
        );

        return doctors.stream()
                .map(this::toPublicDoctorResponse)
                .toList();
    }

    @Override
    public List<PublicDoctorResponse> getPublicDoctorsBySpecialtyId(int specialtyId) {
        return getPublicDoctors().stream()
                .filter(doctor -> doctor.getSpecialtyId() == specialtyId)
                .toList();
    }

    private PublicDoctorResponse toPublicDoctorResponse(AdminDoctorResponse doctor) {
        return new PublicDoctorResponse(
                doctor.getId(),
                doctor.getSpecialtyId(),
                doctor.getFullName(),
                doctor.getSpecialtyName(),
                doctor.getExperience(),
                doctor.getAvaUrl()
        );
    }

    private <T> T fetchFromInternalApi(String path, ParameterizedTypeReference<T> responseType, Supplier<T> fallbackSupplier) {
        if (internalAdminToken == null || internalAdminToken.isBlank()) {
            return fallbackSupplier.get();
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(internalAdminToken);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(
                    buildInternalUrl(path),
                    HttpMethod.GET,
                    requestEntity,
                    responseType
            );

            if (response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException exception) {
            LOGGER.warn("Falling back to admin service for path {}: {}", path, exception.getMessage());
        }

        return fallbackSupplier.get();
    }

    private String buildInternalUrl(String path) {
        String normalizedBaseUrl = internalApiBaseUrl.endsWith("/")
                ? internalApiBaseUrl.substring(0, internalApiBaseUrl.length() - 1)
                : internalApiBaseUrl;

        return normalizedBaseUrl + path;
    }
}
