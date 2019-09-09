package com.zjm.makewords.service;

import com.zjm.makewords.pojo.WordsCard;

public interface WordService {

    /**
     * 根据用户选定的字数产生对应的随机字
     *
     * @param selectedWordLength 随机字数量
     * @return 返回生成的随机汉字对应的标签卡对象
     */
    WordsCard wordTextByNumber(Integer selectedWordLength);

    /**
     * 根据汉字组成不同长度的有意义的词
     *
     * @param chars 汉字字符串
     * @return 返回带有有意义词组的标签卡对象
     */
    WordsCard makeWords(String chars, Long id);
}
