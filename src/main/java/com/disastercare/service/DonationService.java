package com.disastercare.service;

import com.disastercare.model.Donation;
import com.disastercare.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {
    @Autowired private DonationRepository donationRepository;

    public Donation save(Donation donation) { return donationRepository.save(donation); }
    public List<Donation> getAll() { return donationRepository.findAll(); }
    public Optional<Donation> getById(Long id) { return donationRepository.findById(id); }

    public Donation updateStatus(Long id, Donation.DonationStatus status) {
        Donation d = donationRepository.findById(id).orElseThrow();
        d.setStatus(status);
        return donationRepository.save(d);
    }

    public long countTotal() { return donationRepository.count(); }
    public long countPending() { return donationRepository.countByStatus(Donation.DonationStatus.PENDING); }
}
