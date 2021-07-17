package com.plugchecker.backend.domain.plug.domain;

import com.plugchecker.backend.domain.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlugRepository extends JpaRepository<Plug, Integer> {
    List<Plug> findByUser(User user);
    List<Plug> findByUserAndElectricity(User user, boolean electricity);
    Optional<Plug> findByNameAndUser(String name, User user);
}
