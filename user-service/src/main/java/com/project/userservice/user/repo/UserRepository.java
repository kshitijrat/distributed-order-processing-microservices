package com.project.userservice.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.userservice.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}