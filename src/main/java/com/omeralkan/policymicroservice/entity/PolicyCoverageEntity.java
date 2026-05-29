package com.omeralkan.policymicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "policy_coverages", uniqueConstraints = {
        @UniqueConstraint(name = "uq_policy_coverage_code", columnNames = {"policy_id", "coverage_code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCoverageEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private PolicyEntity policy;

    @Column(name = "coverage_code", nullable = false, length = 50)
    private String coverageCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
}