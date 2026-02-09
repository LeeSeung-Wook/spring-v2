package com.example.boardv1._core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.example.boardv1._core.errors.ex.Exception400;

@Aspect
@Component
public class ValidationHandler {

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping)") // import package name
    public void validationCheck(JoinPoint jp) {
        // 메서드의 모든 파라미터를 순회
        for (Object arg : jp.getArgs()) {
            if (arg instanceof Errors errors) {
                // 유효성 검사 실패 시
                if (errors.hasErrors()) {
                    throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
                }
            }
        }
    }
}
