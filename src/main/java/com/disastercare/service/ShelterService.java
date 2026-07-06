package com.disastercare.service;

import com.disastercare.model.Shelter;
import com.disastercare.model.ShelterRegistration;
import com.disastercare.repository.ShelterRegistrationRepository;
import com.disastercare.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShelterService {

    @Autowired private ShelterRepository shelterRepository;
    @Autowired private ShelterRegistrationRepository registrationRepository;

    public List<Shelter> getAllShelters() { return shelterRepository.findAll(); }

    public List<Shelter> getSheltersByDistrict(String district) {
        return shelterRepository.findByDistrict(district);
    }

    public Optional<Shelter> getShelterById(Long id) { return shelterRepository.findById(id); }

    public Shelter saveShelter(Shelter shelter) { return shelterRepository.save(shelter); }

    public void deleteShelter(Long id) { shelterRepository.deleteById(id); }

    public ShelterRegistration register(String userId, Long shelterId, String name, int age,
                                         ShelterRegistration.Gender gender, String bloodGroup,
                                         String identificationMark) {
        Optional<Shelter> shelterOpt = shelterRepository.findById(shelterId);
        if (shelterOpt.isEmpty() || shelterOpt.get().getStatus() == Shelter.Status.FULL) {
            throw new IllegalStateException("Shelter not available");
        }
        Shelter shelter = shelterOpt.get();
        ShelterRegistration reg = new ShelterRegistration();
        reg.setUserId(userId);
        reg.setShelter(shelter);
        reg.setName(name);
        reg.setAge(age);
        reg.setGender(gender);
        reg.setBloodGroup(bloodGroup);
        reg.setIdentificationMark(identificationMark);
        shelter.setOccupied(shelter.getOccupied() + 1);
        if (shelter.getOccupied() >= shelter.getCapacity()) {
            shelter.setStatus(Shelter.Status.FULL);
        }
        shelterRepository.save(shelter);
        return registrationRepository.save(reg);
    }

    public long countActive()   { return shelterRepository.countByStatus(Shelter.Status.ACTIVE); }
    public long countFull()     { return shelterRepository.countByStatus(Shelter.Status.FULL); }
    public long countTotal()    { return shelterRepository.count(); }
    public List<ShelterRegistration> getRegistrationsByShelter(Long shelterId) {
        return registrationRepository.findByShelterId(shelterId);
    }
}
