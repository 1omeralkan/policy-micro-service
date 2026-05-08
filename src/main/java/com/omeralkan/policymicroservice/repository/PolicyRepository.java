package com.omeralkan.policymicroservice.repository;

import com.omeralkan.policymicroservice.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    List<PolicyEntity> findAllByIsActiveTrue();

    Optional<PolicyEntity> findByIdAndIsActiveTrue(Long id);
}