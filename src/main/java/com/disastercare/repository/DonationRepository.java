package com.disastercare.repository;
import com.disastercare.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DonationRepository extends JpaRepository<Donation, Long> {
    long countByStatus(Donation.DonationStatus status);
}
