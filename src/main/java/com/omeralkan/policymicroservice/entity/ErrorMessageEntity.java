package com.omeralkan.policymicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "error_messages", uniqueConstraints = {
        @UniqueConstraint(name = "uq_error_code_language", columnNames = {"error_code", "language"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageEntity extends BaseEntity {

    @Column(name = "error_code", nullable = false, length = 50)
    private String errorCode;

    @Column(name = "language", nullable = false, length = 10)
    private String language;

    @Column(name = "message", nullable = false)
    private String message;
}