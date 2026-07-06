package com.disastercare.repository;
import com.disastercare.model.ShelterRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ShelterRegistrationRepository extends JpaRepository<ShelterRegistration, Long> {
    List<ShelterRegistration> findByShelterId(Long shelterId);
    long countByShelterId(Long shelterId);
}
