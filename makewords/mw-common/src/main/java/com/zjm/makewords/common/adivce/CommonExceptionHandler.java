package com.zjm.makewords.common.adivce;

import com.zjm.makewords.common.enums.ExceptionEnum;
import com.zjm.makewords.common.exception.MwException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/2 0002 14:38
 */
@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(MwException.class)
    public ResponseEntity<String> handleException(MwException e) {
        ExceptionEnum em = e.getExceptionEnum();
        return ResponseEntity.status(em.getCode()).body(em.getMsg());
    }
}
