package com.plugchecker.backend.global.error.exception;

import com.plugchecker.backend.global.error.ErrorCode;

public class NotFoundException extends GlobalException{

    public NotFoundException(int id) {
        super("plug" + id + " not found", ErrorCode.NOT_FOUND);
    }

    public NotFoundException(String email) {
        super(email + " not found", ErrorCode.NOT_FOUND);
    }
}
