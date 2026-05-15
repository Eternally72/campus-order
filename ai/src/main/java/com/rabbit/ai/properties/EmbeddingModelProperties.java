package com.rabbit.ai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "langchain4j.model.embedding")
@Data
public class EmbeddingModelProperties {
    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型baseURL
     */
    private String baseUrl;

    /**
     * 模型API密钥
     */
    private String apiKey;

    /**
     * 连接超时时间
     */
    private Long  connectTimeout;

    /**
     * 最大token数
     */
    private int maxTokens;

    /**
     * 温度，控制生成文本的随机程度，值越大越随机，值越小越确定
     */
    private double temperature;
}
