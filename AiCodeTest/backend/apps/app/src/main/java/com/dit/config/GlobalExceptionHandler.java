package com.dit.config;

import com.dit.common.exception.BusinessException;
import com.dit.common.exception.ErrorCode;
import com.dit.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 글로벌 예외 처리 핸들러
 * 컨트롤러에서 발생하는 예외를 공통으로 처리하여 ApiResponse 포맷으로 반환합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리 (BusinessException)
     * 의도적으로 발생시킨 비즈니스 로직 예외 → 400 Bad Request
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("비즈니스 예외 발생: code={}, message={}", e.getErrorCode().getCode(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
    }

    /**
     * 입력값 검증 실패 처리 (@Valid, @Validated 검증 실패 시)
     * DTO의 @NotBlank, @Size, @Email 등 Jakarta Validation 위반 → 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        // 첫 번째 검증 오류 메시지를 사용자 친화적으로 조합
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("입력값 검증 실패: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT.getCode(), errorMessage));
    }

    /**
     * 그 외 처리되지 않은 예외 (서버 내부 오류)
     * 민감 정보(스택 트레이스) 노출 없이 일반 메시지만 반환 → 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        // 스택 트레이스는 서버 로그에만 기록 — 응답에는 절대 포함하지 않음
        log.error("처리되지 않은 예외 발생: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                ));
    }
}
