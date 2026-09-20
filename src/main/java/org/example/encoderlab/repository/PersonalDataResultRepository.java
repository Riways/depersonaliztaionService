package org.example.encoderlab.repository;

import org.example.encoderlab.entity.PersonalDataResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataResultRepository extends JpaRepository<PersonalDataResult, Long> {
}