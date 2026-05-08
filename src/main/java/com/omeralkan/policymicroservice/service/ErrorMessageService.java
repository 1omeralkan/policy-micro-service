package com.omeralkan.policymicroservice.service;

import com.omeralkan.policymicroservice.entity.ErrorMessageEntity;
import com.omeralkan.policymicroservice.repository.ErrorMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;

    public String getMessage(String errorCode, String language) {
        String lang = (language == null || language.trim().isEmpty()) ? "tr" : language;

        return errorMessageRepository.findByErrorCodeAndLanguageAndIsActiveTrue(errorCode, lang)
                .map(ErrorMessageEntity::getMessage)
                .orElse("Bilinmeyen bir hata oluştu / Unknown error occurred");
    }
}