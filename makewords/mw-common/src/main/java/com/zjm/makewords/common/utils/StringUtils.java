package com.zjm.makewords.common.utils;

import java.util.List;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/7 0007 10:05
 */
public class StringUtils {

    /**
     * java递归实现全排列
     *
     * @param charbuffer 需要排序的字符数组
     * @param textList   全排序后候选字符串列表
     * @param index      递归开始的索引
     * @return 全排序后候选字符串列表
     */
    public static List<String> permutation(char[] charbuffer, List<String> textList, int index) {

        if (charbuffer == null || index < 0 || index > charbuffer.length) {
            return textList;
        }

        if (index == charbuffer.length - 1) {
            textList.add(new String(charbuffer));
        } else {
            for (int j = index; j < charbuffer.length; j++) {
                //交换前缀,使之产生下一个前缀
                char temp = charbuffer[j];
                charbuffer[j] = charbuffer[index];
                charbuffer[index] = temp;
                permutation(charbuffer, textList, index + 1);
                //将前缀换回来,继续做上一个的前缀排列.
                temp = charbuffer[j];
                charbuffer[j] = charbuffer[index];
                charbuffer[index] = temp;
            }
        }
        return textList;
    }
}
