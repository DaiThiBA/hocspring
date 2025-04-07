package com.example.hocspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1002,"Password must be atleast 8 character", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND ),
    INVALID_USERNAME(1004,"Username must be atleast 3 character", HttpStatus.BAD_REQUEST),
    INVALID_ERROR_CODE(9000, "Invalide enum key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Unauthorized", HttpStatus.FORBIDDEN),//theo chuẩn của http (nên hơi confuse)
    INVALID_DOB(1008, "Invalid Date of Birth", HttpStatus.BAD_REQUEST),
    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;
    
    private ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
