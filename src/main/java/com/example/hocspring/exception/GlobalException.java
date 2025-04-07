package com.example.hocspring.exception;


import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.hocspring.dto.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice

@Slf4j
public class GlobalException {

    private static final String MIN_ATTRIBUTE = "min";

    //xử lí fallback exception, nếu không có exception nào khác xảy ra
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlerRuntimeException(Exception exception){
        ApiResponse<String> response = new ApiResponse<>();

        response.setCode(ErrorCode.UNCATEGORIZED_ERROR.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_ERROR.getMessage());

        return ResponseEntity.badRequest().body(response);
    
    }

    // xử lí unauthenticated exception
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlerAccessDeniedException(AccessDeniedException exception){
        
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                    .body(ApiResponse.<String>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    //xử lí app exception
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlerAppException(AppException exception){
        ApiResponse<String> response = new ApiResponse<>();

        ErrorCode errorCode = exception.getErrorCode();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlervalidationException(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();

        // ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ErrorCode errorCode = ErrorCode.INVALID_ERROR_CODE;

        Map<String, Object> attributes = null;


        
        try{
            errorCode = ErrorCode.valueOf(enumKey);

            var ConstraintViolation = exception.getBindingResult()
            .getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = ConstraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        }
        catch(IllegalArgumentException e){
           
        } 

        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(Objects.nonNull(attributes) ?
        mapAttribute(errorCode.getMessage(), attributes)
        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue); // chuẩn của java khi thay thế string
    }

}

    