package com.disastercare.repository;
import com.disastercare.model.DistrictStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DistrictStatusRepository extends JpaRepository<DistrictStatus, Long> {
    Optional<DistrictStatus> findByDistrictName(String districtName);
}
