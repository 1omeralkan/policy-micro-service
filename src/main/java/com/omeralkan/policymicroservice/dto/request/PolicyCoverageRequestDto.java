package com.omeralkan.policymicroservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PolicyCoverageRequestDto {
    private String coverageCode;
    private String name;
    private BigDecimal amount;
}