package com.omeralkan.policymicroservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PolicyCoverageResponseDto {
    private Long id;
    private String coverageCode;
    private String name;
    private BigDecimal amount;
}