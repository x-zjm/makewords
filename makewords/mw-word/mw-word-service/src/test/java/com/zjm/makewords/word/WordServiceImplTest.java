package com.zjm.makewords.word;

import com.zjm.makewords.common.utils.StringUtils;
import com.zjm.makewords.pojo.WordsCard;
import com.zjm.makewords.serviceImpl.WordServiceImpl;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/2 0002 23:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordServiceImplTest {

    @Autowired
    private WordServiceImpl wordService;

    @Test
    public void testLoadWordFile() {
        InputStream is = null;
        BufferedReader reader = null;

        // 读取文件
        Resource resource = new ClassPathResource("static/500ChineseCharacter.txt");
        if (!resource.exists()) {
            System.out.println("[组词服务] 汉字文件未找到！");
        }
        try {
            is = resource.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println("line = " + line);
                System.out.println("line.length = " + line.length());
            }
        } catch (IOException e) {
            System.out.println("[组词服务] 汉字文件读取失败！");
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
            } catch (IOException e) {
                System.out.println("[组词服务] 汉字文件读取失败！");
            }
        }
    }

    @Test
    public void testRandomWordByNumber() {
        System.out.println("randomWord = " + wordService.wordTextByNumber(5));
    }

    @Test
    public void testpermutation() {
        List<String> permutation = new ArrayList<>();
        permutation = StringUtils.permutation(new char[]{'一','二','三','四'}, permutation, 0);
        for (String s : permutation) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void testPaodingAnalyzer() throws IOException {
        List<String> wordList = new ArrayList();
        Analyzer analyzer = new PaodingAnalyzer("classpath:paoding/paoding-analysis.properties");
        String text = "山省真满先角张始出";
        TokenStream tokenStream = analyzer.tokenStream("", text);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = tokenStream
                    .addAttribute(CharTermAttribute.class);
            // System.out.println(charTermAttribute.toString());
            wordList.add(charTermAttribute.toString());
        }
        for (String s : wordList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void textMakeWords() {
        WordsCard wordsCard = wordService.makeWords("天安门", null);
        Set twoWords = wordsCard.getTwoWords();
        for (Object twoWord : twoWords) {
            System.out.println("twoWord = " + twoWord);
        }
    }
}
