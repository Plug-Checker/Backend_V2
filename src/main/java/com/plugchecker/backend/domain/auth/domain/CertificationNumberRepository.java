package com.plugchecker.backend.domain.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CertificationNumberRepository extends CrudRepository<CertificationNumber, Long> {
    Optional<CertificationNumber> findByEmail(String email);
    void delete(CertificationNumber entity);
}
