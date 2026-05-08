package com.omeralkan.policymicroservice.service;

import com.omeralkan.policymicroservice.dto.request.PolicyRequestDto;
import com.omeralkan.policymicroservice.dto.response.PolicyResponseDto;

import java.util.List;

public interface PolicyService {
    PolicyResponseDto createPolicy(PolicyRequestDto requestDto);
    PolicyResponseDto getPolicyById(Long id);
    List<PolicyResponseDto> getAllPolicies();
    void deletePolicy(Long id);
}