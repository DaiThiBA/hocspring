package com.example.hocspring.exception;

public enum ErrorCode {

    UNCATEGORIZED_ERROR(9999, "Uncategorized error"),
    USER_EXISTED(1001, "User existed"),
    INVALID_PASSWORD(1002,"Password must be atleast 8 character"),
    USER_NOT_FOUND(1003, "User not found"),
    INVALID_USERNAME(1004,"Username must be atleast 3 character"),
    INVALID_ERROR_CODE(9000, "Invalide enum key")
    ;
    int code;
    String message;
    
    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }


}
