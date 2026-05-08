package com.omeralkan.policymicroservice.exception;

import com.omeralkan.policymicroservice.service.ErrorMessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMessageService errorMessageService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        // Postman'den veya Swagger'dan gelen dil header'ını alıyoruz
        String language = request.getHeader("Accept-Language");

        // Veritabanından o dile ait mesajı çekiyoruz (Örn: POL-404)
        String dynamicMessage = errorMessageService.getMessage(ex.getMessage(), language);

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                dynamicMessage, // Dinamik mesajımız buraya geldi!
                null,
                LocalDateTime.now()
        );

        log.error("İş Kuralı Hatası: [{}] - {}", ex.getMessage(), dynamicMessage);
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_ERROR",
                "Girdi doğrulama hatası",
                details,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Beklenmeyen Hata: ", ex);
        String language = request.getHeader("Accept-Language");
        String dynamicMessage = errorMessageService.getMessage("SYS-500", language);

        ErrorResponse errorResponse = new ErrorResponse(
                "SYS-500",
                dynamicMessage,
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}