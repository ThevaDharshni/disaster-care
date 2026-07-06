package com.disastercare.repository;
import com.disastercare.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    List<Shelter> findByDistrict(String district);
    long countByStatus(Shelter.Status status);
}
