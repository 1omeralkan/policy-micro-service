package com.omeralkan.policymicroservice.repository;

import com.omeralkan.policymicroservice.entity.ErrorMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessageEntity, Long> {

    Optional<ErrorMessageEntity> findByErrorCodeAndLanguageAndIsActiveTrue(String errorCode, String language);
}