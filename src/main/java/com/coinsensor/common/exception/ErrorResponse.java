package com.coinsensor.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    private int status;
    private String code;
    private String message;
    
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(500)
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}
