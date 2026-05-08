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

    // 1. BAŞARILI SENARYO (Happy Path) TESTİ
    @Test
    void createPolicy_ShouldReturnResponseDto_WhenRequestIsValid() {
        // Hazırlık (Given)
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

        // Mock Kuralları: Mapper'a veya Repository'ye bu değerler gelirse şunları dön
        when(policyMapper.toEntity(requestDto)).thenReturn(mockedEntity);
        when(policyRepository.save(any(PolicyEntity.class))).thenReturn(mockedEntity);
        when(policyMapper.toResponse(mockedEntity)).thenReturn(mockedResponse);

        // Eylem (When)
        PolicyResponseDto result = policyService.createPolicy(requestDto);

        // Kontrol (Then)
        assertNotNull(result);
        assertEquals(1L, result.getId());

        // Save metodu tam olarak 1 kere çağrıldı mı diye teyit ediyoruz (Mimarinin kalbi)
        verify(policyRepository, times(1)).save(any(PolicyEntity.class));
    }

    // 2. KAYIT BULUNAMADI / EXCEPTION TESTİ
    @Test
    void getPolicyById_ShouldThrowBusinessException_WhenPolicyNotFoundOrInactive() {
        // Hazırlık (Given)
        Long invalidId = 999L;
        when(policyRepository.findByIdAndIsActiveTrue(invalidId)).thenReturn(Optional.empty());

        // Eylem & Kontrol (When & Then)
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            policyService.getPolicyById(invalidId);
        });

        // Fırlatılan hatanın bizim kurumsal POL-404 mü olduğunu teyit ediyoruz
        assertNotNull(exception.getMessage()); // Exception mesajının boş olmadığını kontrol et
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        // Mapper'ın hiç çağrılmadığından emin oluyoruz (Çünkü exception fırlattı ve kod kesildi)
        verify(policyMapper, never()).toResponse(any());
    }

    // 3. SOFT DELETE TESTİ
    @Test
    void deletePolicy_ShouldSetIsActiveToFalse_WhenPolicyExists() {
        // Hazırlık (Given)
        Long validId = 1L;
        PolicyEntity existingEntity = new PolicyEntity();
        existingEntity.setId(validId);
        existingEntity.setIsActive(true);

        when(policyRepository.findByIdAndIsActiveTrue(validId)).thenReturn(Optional.of(existingEntity));

        // Eylem (When)
        policyService.deletePolicy(validId);

        // Kontrol (Then)
        assertFalse(existingEntity.getIsActive()); // isActive false oldu mu?
        verify(policyRepository, times(1)).save(existingEntity); // Güncel haliyle save çağrıldı mı?
    }
}