package com.plugchecker.backend.global.error.exception;

import com.plugchecker.backend.global.error.ErrorCode;

public class InvalidCertificationNumberException extends GlobalException {

    public InvalidCertificationNumberException(String number) {
        super(number + " is wrong certification number", ErrorCode.INVALID_CERTIFICATION_NUMBER);
    }
}
