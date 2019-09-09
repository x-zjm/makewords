package com.zjm.makewords.web;

import com.zjm.makewords.pojo.WordsCard;
import com.zjm.makewords.serviceImpl.WordServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 处理由字组词逻辑的控制层
 * @author: zjm
 * @time: 2019/9/2 0002 18:47
 */
@RestController
public class WordController {

    private final WordServiceImpl wordService;

    public WordController(WordServiceImpl wordService) {
        this.wordService = wordService;
    }

    /**
     * 根据用户选定的字数产生对应的随机字
     * @param selectedWordLength 随机字数量
     * @return 返回生成的随机汉字对应的标签卡对象
     */
    @GetMapping(value = "/randomword")
    public ResponseEntity<WordsCard> randomWordByNumber(
            @RequestParam(value = "selectedWordLength", defaultValue = "4") Integer selectedWordLength
    ) {
        return ResponseEntity.ok(wordService.wordTextByNumber(selectedWordLength));
    }

    /**
     * 根据汉字组成不同长度的有意义的词
     * @param chars 汉字字符串
     * @return 返回带有有意义词组的标签卡对象
     */
    @GetMapping(value = "/makewords")
    public ResponseEntity<WordsCard> makeWords(
            @RequestParam(value = "chars") String chars,
            @RequestParam(value = "id" ,required = false) Long id
    ) {
        return ResponseEntity.ok(wordService.makeWords(chars, id));
    }
}
