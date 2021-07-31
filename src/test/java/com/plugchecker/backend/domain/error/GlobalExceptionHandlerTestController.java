package com.plugchecker.backend.domain.error;

import com.plugchecker.backend.global.error.ErrorCode;
import com.plugchecker.backend.global.error.exception.GlobalException;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Profile("test")
@RestController
public class GlobalExceptionHandlerTestController {

    @PostMapping("/testMethodArgumentNotValidException")
    public void testMethodArgumentNotValidException(@Valid @RequestBody GlobalExceptionHandlerTestDto request){
    }

    @GetMapping("/testGlobalException")
    public void testGlobalException(){
        throw new GlobalException(ErrorCode.NOT_FOUND);
    }

    @GetMapping("/testException")
    public void testException() throws Exception {
        throw new Exception();
    }
}
