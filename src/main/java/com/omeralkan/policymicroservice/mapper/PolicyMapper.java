package com.omeralkan.policymicroservice.mapper;

import com.omeralkan.policymicroservice.dto.request.PolicyRequestDto;
import com.omeralkan.policymicroservice.dto.response.PolicyResponseDto;
import com.omeralkan.policymicroservice.entity.PolicyEntity;
import com.omeralkan.policymicroservice.entity.PolicyStatus;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapper {

    public PolicyResponseDto toResponse(PolicyEntity entity) {
        if (entity == null) {
            return null;
        }

        PolicyResponseDto dto = new PolicyResponseDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setAmount(entity.getAmount());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setPolicyStatus(entity.getPolicyStatus().name());
        dto.setIsActive(entity.getIsActive());

        return dto;
    }

    public PolicyEntity toEntity(PolicyRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        PolicyEntity entity = new PolicyEntity();
        entity.setProductId(requestDto.getProductId());
        entity.setStartDate(requestDto.getStartDate());
        entity.setEndDate(requestDto.getEndDate());
        entity.setAmount(requestDto.getAmount());
        entity.setCurrencyCode(requestDto.getCurrencyCode().toUpperCase());

        entity.setPolicyStatus(PolicyStatus.ACTIVE);

        return entity;
    }
}