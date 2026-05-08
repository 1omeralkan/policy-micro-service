package com.omeralkan.policymicroservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PolicyResponseDto {

    private Long id;
    private Long productId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private String currencyCode;
    private String policyStatus;
    private Boolean isActive;

}