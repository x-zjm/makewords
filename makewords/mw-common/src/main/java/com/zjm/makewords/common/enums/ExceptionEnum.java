package com.zjm.makewords.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    WORDFILE_NOT_FOUND(404, "文字文件未找到！"),
    WORDFILE_READ_ERROR(500, "文字文件读取失败！"),
    ANALYZER_WRITE_ERROR(500, "分词结果输出失败！"),
    WORDLENGTH_MIN_LIMTIT_ERROR (400, "字数低于最小限制！"),
    WORDLENGTH_MAX_LIMTIT_ERROR (400, "字数超过最大限制！"),
    IS_NOT_CHAR (400, "字符串含有非中文汉字！"),
    ;
    private Integer code;
    private String msg;
}
