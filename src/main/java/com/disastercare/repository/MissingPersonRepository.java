package com.disastercare.repository;
import com.disastercare.model.MissingPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MissingPersonRepository extends JpaRepository<MissingPerson, Long> {
    List<MissingPerson> findByStatus(MissingPerson.MissingStatus status);
    long countByStatus(MissingPerson.MissingStatus status);
}
