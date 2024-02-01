package com.userservice.user.repository;

import com.userservice.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String loginId);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<User> findByEmailKey(String emailKey);
}
