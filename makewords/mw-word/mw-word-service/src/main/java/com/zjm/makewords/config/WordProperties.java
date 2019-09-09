package com.zjm.makewords.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/7 0007 16:30
 */
@Data
@ConfigurationProperties(prefix = "words")
public class WordProperties {
    Integer defaultLength;
    Integer maxWordLength;
    Integer minWordLength;
    String textRule;
    String wordFilePath;
    String analysisPropertiesPath;
}
