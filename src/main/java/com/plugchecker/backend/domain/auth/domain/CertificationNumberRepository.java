package com.plugchecker.backend.domain.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CertificationNumberRepository extends CrudRepository<CertificationNumber, Long> {
    Optional<CertificationNumber> findByEmail(String email);
    void delete(CertificationNumber entity);

    default void saveCertificationNumber(String email, String number, String id, String password) {
        CertificationNumber certificationNumber = CertificationNumber.builder()
                .email(email)
                .number(number)
                .user_id(id)
                .user_password(password)
                .exp(300000L)
                .build();
        this.save(certificationNumber);
    }
}
