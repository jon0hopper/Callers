package com.jhop.callers.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {
}