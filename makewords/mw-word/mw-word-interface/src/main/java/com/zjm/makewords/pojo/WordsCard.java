package com.zjm.makewords.pojo;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @description: 一套字和词对应实体类
 * @author: zjm
 * @time: 2019/9/2 0002 20:53
 */
@Data
public class WordsCard {

    // 主键编号
    private Long id;
    // 汉字数组
    private String wordText;
    // 生成卡时间
    private Date createdTime;
    // 二字词组
    private Set<String> twoWords;
    // 三字词组
    private Set<String> threeWords;
    // 四字词组
    private Set<String> fourWords;
}
