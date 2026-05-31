package com.omeralkan.policymicroservice.service.impl;

import com.omeralkan.policymicroservice.dto.request.PolicyRequestDto;
import com.omeralkan.policymicroservice.dto.response.PolicyResponseDto;
import com.omeralkan.policymicroservice.entity.PolicyEntity;
import com.omeralkan.policymicroservice.exception.BusinessException;
import com.omeralkan.policymicroservice.exception.ErrorCodes;
import com.omeralkan.policymicroservice.mapper.PolicyMapper;
import com.omeralkan.policymicroservice.repository.PolicyRepository;
import com.omeralkan.policymicroservice.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    @Override
    @Transactional
    public PolicyResponseDto createPolicy(PolicyRequestDto requestDto) {
        log.info("İlk taksit ödendi, yeni poliçe oluşturuluyor. Product ID: {}", requestDto.getProductId());

        PolicyEntity entity = policyMapper.toEntity(requestDto);
        PolicyEntity savedEntity = policyRepository.save(entity);

        log.info("Poliçe başarıyla oluşturuldu. Poliçe ID: {}", savedEntity.getId());
        return policyMapper.toResponse(savedEntity);
    }

    @Override
    public PolicyResponseDto getPolicyById(Long id) {
        PolicyEntity entity = findActivePolicyOrThrow(id);
        return policyMapper.toResponse(entity);
    }

    @Override
    public List<PolicyResponseDto> getAllPolicies() {
        return policyRepository.findAllByIsActiveTrue()
                .stream()
                .map(policyMapper::toResponse)
                .toList();
    }

    @Override
    public List<PolicyResponseDto> getPoliciesByCustomerId(Long customerId) {
        log.info("Müşteriye ait poliçeler getiriliyor. Customer ID: {}", customerId);
        return policyRepository.findAllByCustomerIdAndIsActiveTrue(customerId)
                .stream()
                .map(policyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deletePolicy(Long id) {
        PolicyEntity entity = findActivePolicyOrThrow(id);

        entity.setIsActive(false);
        policyRepository.save(entity);

        log.info("Poliçe silindi (Soft Delete). ID: {}", id);
    }

    private PolicyEntity findActivePolicyOrThrow(Long id) {
        return policyRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POLICY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}