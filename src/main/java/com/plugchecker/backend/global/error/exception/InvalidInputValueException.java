package com.plugchecker.backend.global.error.exception;

import com.plugchecker.backend.global.error.ErrorCode;

public class InvalidInputValueException extends GlobalException {
    public InvalidInputValueException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
}
