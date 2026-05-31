package com.omeralkan.policymicroservice.mapper;

import com.omeralkan.policymicroservice.dto.request.PolicyRequestDto;
import com.omeralkan.policymicroservice.dto.response.PolicyCoverageResponseDto;
import com.omeralkan.policymicroservice.dto.response.PolicyResponseDto;
import com.omeralkan.policymicroservice.entity.PolicyCoverageEntity;
import com.omeralkan.policymicroservice.entity.PolicyEntity;
import com.omeralkan.policymicroservice.entity.PolicyStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

        if (entity.getCoverages() != null) {
            dto.setCoverages(entity.getCoverages().stream()
                    .map(this::toCoverageResponse)
                    .collect(Collectors.toList()));
        }

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

        if (requestDto.getCoverages() != null) {
            entity.setCoverages(requestDto.getCoverages().stream()
                    .map(covDto -> {
                        PolicyCoverageEntity covEntity = new PolicyCoverageEntity();
                        covEntity.setCoverageCode(covDto.getCoverageCode());
                        covEntity.setName(covDto.getName());
                        covEntity.setAmount(covDto.getAmount());
                        covEntity.setPolicy(entity);
                        return covEntity;
                    })
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private PolicyCoverageResponseDto toCoverageResponse(PolicyCoverageEntity covEntity) {
        PolicyCoverageResponseDto covDto = new PolicyCoverageResponseDto();
        covDto.setId(covEntity.getId());
        covDto.setCoverageCode(covEntity.getCoverageCode());
        covDto.setName(covEntity.getName());
        covDto.setAmount(covEntity.getAmount());
        return covDto;
    }
}