package com.plugchecker.backend.global.error.exception;

import com.plugchecker.backend.global.error.ErrorCode;

public class AlreadyExistException extends GlobalException{
    public AlreadyExistException(String name) {
        super(name + " is already exist", ErrorCode.INVALID_INPUT_VALUE);
    }
}
