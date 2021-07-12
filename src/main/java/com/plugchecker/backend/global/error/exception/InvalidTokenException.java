package com.plugchecker.backend.global.error.exception;

import com.plugchecker.backend.global.error.ErrorCode;

public class InvalidTokenException extends GlobalException{

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
