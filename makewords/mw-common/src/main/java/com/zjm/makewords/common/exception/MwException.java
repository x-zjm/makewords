package com.zjm.makewords.common.exception;

import com.zjm.makewords.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/2 0002 16:35
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MwException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
