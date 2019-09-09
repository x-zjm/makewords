package com.zjm.makewords.serviceImpl;

import com.zjm.makewords.common.enums.ExceptionEnum;
import com.zjm.makewords.common.exception.MwException;
import com.zjm.makewords.common.utils.IdWorker;
import com.zjm.makewords.common.utils.StringUtils;
import com.zjm.makewords.config.WordProperties;
import com.zjm.makewords.pojo.WordsCard;
import com.zjm.makewords.service.WordService;
import lombok.extern.slf4j.Slf4j;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 处理组词相关逻辑的具体实现类
 * @author: zjm
 * @time: 2019/9/2 0002 18:59
 */
@Slf4j
@Service
@EnableConfigurationProperties(WordProperties.class)
public class WordServiceImpl implements WordService {

    // 注入配置文件
    private final WordProperties wordProperties;

    public WordServiceImpl(WordProperties wordProperties) {
        this.wordProperties = wordProperties;
    }

    @Override
    public WordsCard wordTextByNumber(Integer selectedWordLength) {

        // 服务端校验数据
        // 低于最小限制
        if (selectedWordLength < wordProperties.getMinWordLength()) {
            throw new MwException(ExceptionEnum.WORDLENGTH_MIN_LIMTIT_ERROR);
        }
        // 超过最大限制
        if (selectedWordLength > wordProperties.getMaxWordLength()) {
            throw new MwException(ExceptionEnum.WORDLENGTH_MAX_LIMTIT_ERROR);
        }

        // 文件输入流
        InputStream is = null;
        // 整行读取文件，提高效率
        BufferedReader reader = null;
        // 所有的汉字
        String wordCharLine;
        // 随机产生的汉字
        StringBuilder wordText = new StringBuilder();

        // 读取文件
        Resource resource = new ClassPathResource(wordProperties.getWordFilePath());
        // 如果文件未找到
        if (!resource.exists()) {
            log.error("[组词服务] 汉字文件未找到！");
            throw new MwException(ExceptionEnum.WORDFILE_NOT_FOUND);
        }
        try {
            is = resource.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            // 逐行读取文件
            wordCharLine = reader.readLine();
        } catch (IOException e) {
            log.error("[组词服务] 汉字文件读取失败！", e);
            throw new MwException(ExceptionEnum.WORDFILE_READ_ERROR);
        } finally {
            // 统一关闭流
            try {
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("[组词服务] 汉字文件输入流关闭失败！", e);
            }
        }

        // 进行随机抽取指定数量的汉字
        for (int i = 0; i < selectedWordLength; i++) {
            // 将随机产生的每个汉字拼接到随机汉字字符串中
            wordText.append(wordCharLine.charAt((int) (Math.random() * wordCharLine.length())));
        }

        // 封装进对象
        WordsCard wordsCard = new WordsCard();
        wordsCard.setId(new IdWorker().nextId());
        wordsCard.setWordText(wordText.toString());

        // 服务端处理时间
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String dateNowStr = simpleDateFormat.format(new Date());
        // Date dateNow = null;
        // try {
        //     dateNow = simpleDateFormat.parse(dateNowStr);
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // }
        // wordsCard.setCreatedTime(dateNow);

        return wordsCard;
    }

    @Override
    public WordsCard makeWords(String chars, Long id) {

        // 服务端校验数据
        // 低于最小限制
        if (chars.length() < wordProperties.getMinWordLength()) {
            throw new MwException(ExceptionEnum.WORDLENGTH_MIN_LIMTIT_ERROR);
        }
        // 超过最大限制
        if (chars.length() > wordProperties.getMaxWordLength()) {
            throw new MwException(ExceptionEnum.WORDLENGTH_MAX_LIMTIT_ERROR);
        }
        // 含有非中文字符
        if (chars.matches(wordProperties.getTextRule())) {
            throw new MwException(ExceptionEnum.IS_NOT_CHAR);
        }

        // 全排序后候选字符串列表
        List<String> textList = new ArrayList<>();
        // 不同长度的词语对应的数组
        Set<String> twoWordsList = new HashSet<>();
        Set<String> threeWordsList = new HashSet<>();
        Set<String> fourWordsList = new HashSet<>();
        TokenStream tokenStream = null;

        // 实现字符的全排序
        textList = StringUtils.permutation(chars.toCharArray(), textList, 0);

        // 实例化分词操作对象
        Analyzer analyzer = new PaodingAnalyzer(wordProperties.getAnalysisPropertiesPath());
        // 对列表中候选字符串进行分词操作
        for (String text : textList) {
            try {
                tokenStream = analyzer.tokenStream("", text);
                tokenStream.reset();
                // 每一次的分词结果添加到列表中
                while (tokenStream.incrementToken()) {
                    CharTermAttribute charTermAttribute = tokenStream
                            .addAttribute(CharTermAttribute.class);
                    // 根据生成词的长度将其归为不同长度的词组列表中
                    switch (charTermAttribute.length()) {
                        case 2:
                            twoWordsList.add(charTermAttribute.toString());
                            break;
                        case 3:
                            threeWordsList.add(charTermAttribute.toString());
                            break;
                        case 4:
                            fourWordsList.add(charTermAttribute.toString());
                            break;
                    }
                }
            } catch (IOException e) {
                log.error("[组词服务] 分词结果输出失败!");
                throw new MwException(ExceptionEnum.ANALYZER_WRITE_ERROR);
            } finally {
                if (tokenStream != null) {
                    try {
                        tokenStream.close();
                    } catch (IOException e) {
                        log.error("[组词服务] 分词结果输出失败!");
                    }
                }
            }
        }

        // 封装到选项卡对象
        WordsCard wordsCard = new WordsCard();

        // 判断是否有id，没有id则设置
        if (id == null) {
            wordsCard.setId(new IdWorker().nextId());
        } else {
            wordsCard.setId(id);
        }
        wordsCard.setWordText(chars);
        wordsCard.setTwoWords(twoWordsList);
        wordsCard.setThreeWords(threeWordsList);
        wordsCard.setFourWords(fourWordsList);
        return wordsCard;
    }
}
