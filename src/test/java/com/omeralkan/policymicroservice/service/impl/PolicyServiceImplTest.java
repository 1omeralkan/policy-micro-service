package com.omeralkan.policymicroservice.service.impl;

import com.omeralkan.policymicroservice.dto.request.PolicyRequestDto;
import com.omeralkan.policymicroservice.dto.response.PolicyResponseDto;
import com.omeralkan.policymicroservice.entity.PolicyEntity;
import com.omeralkan.policymicroservice.entity.PolicyStatus;
import com.omeralkan.policymicroservice.exception.BusinessException;
import com.omeralkan.policymicroservice.mapper.PolicyMapper;
import com.omeralkan.policymicroservice.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceImplTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyMapper policyMapper;

    @InjectMocks
    private PolicyServiceImpl policyService;

    @Test
    void createPolicy_ShouldReturnResponseDto_WhenRequestIsValid() {
        PolicyRequestDto requestDto = new PolicyRequestDto();
        requestDto.setProductId(101L);
        requestDto.setAmount(new BigDecimal("1500.00"));
        requestDto.setCurrencyCode("TRY");

        PolicyEntity mockedEntity = new PolicyEntity();
        mockedEntity.setId(1L);
        mockedEntity.setProductId(101L);
        mockedEntity.setPolicyStatus(PolicyStatus.ACTIVE);

        PolicyResponseDto mockedResponse = new PolicyResponseDto();
        mockedResponse.setId(1L);
        mockedResponse.setProductId(101L);

        when(policyMapper.toEntity(requestDto)).thenReturn(mockedEntity);
        when(policyRepository.save(any(PolicyEntity.class))).thenReturn(mockedEntity);
        when(policyMapper.toResponse(mockedEntity)).thenReturn(mockedResponse);

        PolicyResponseDto result = policyService.createPolicy(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(policyRepository, times(1)).save(any(PolicyEntity.class));
    }

    @Test
    void getPolicyById_ShouldThrowBusinessException_WhenPolicyNotFoundOrInactive() {
        Long invalidId = 999L;
        when(policyRepository.findByIdAndIsActiveTrue(invalidId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            policyService.getPolicyById(invalidId);
        });

        assertNotNull(exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        verify(policyMapper, never()).toResponse(any());
    }

    @Test
    void deletePolicy_ShouldSetIsActiveToFalse_WhenPolicyExists() {
        Long validId = 1L;
        PolicyEntity existingEntity = new PolicyEntity();
        existingEntity.setId(validId);
        existingEntity.setIsActive(true);

        when(policyRepository.findByIdAndIsActiveTrue(validId)).thenReturn(Optional.of(existingEntity));

        policyService.deletePolicy(validId);

        assertFalse(existingEntity.getIsActive());
        verify(policyRepository, times(1)).save(existingEntity);
    }
}