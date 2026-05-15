package com.rabbit.ai.config;

import com.rabbit.ai.properties.*;
import dev.langchain4j.community.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiStreamingChatModel;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class GLMConfig {

    @Resource
    private ChatModelProperties chatModelProperties;

    @Resource
    private PictureModelProperties pictureModelProperties;

    @Resource
    private EmbeddingModelProperties embeddingModelProperties;

    @Resource
    private StreamingModelProperties streamingModelProperties;

    @Resource
    private VisionModelProperties visionModelProperties;

    /**
     * 简单聊天模型
     */
    @Bean(value = "glmChatModel")
    public ZhipuAiChatModel glmChatModel() {
        return ZhipuAiChatModel.builder()
                .model(chatModelProperties.getModelName()) // 模型名称
                .temperature(chatModelProperties.getTemperature()) // 生成文本的随机程度，值越大越随机
                .maxToken(chatModelProperties.getMaxTokens()) // 生成文本的最大长度
                .baseUrl(chatModelProperties.getBaseUrl()) // 模型baseURL
                .apiKey(chatModelProperties.getApiKey()) // 模型API密钥
                .connectTimeout(Duration.ofSeconds(chatModelProperties.getConnectTimeout()))
                .build();
    }

    /**
     * 视觉模型
     */
    @Bean(value = "glmVisionModel")
    public ZhipuAiChatModel glmVisionModel() {
        return ZhipuAiChatModel.builder()
                .model(visionModelProperties.getModelName()) // 模型名称
                .temperature(visionModelProperties.getTemperature()) // 生成文本的随机程度，值越大越随机
                .maxToken(visionModelProperties.getMaxTokens()) // 生成文本的最大长度
                .baseUrl(visionModelProperties.getBaseUrl()) // 模型baseURL
                .apiKey(visionModelProperties.getApiKey()) // 模型API密钥
                .connectTimeout(Duration.ofSeconds(chatModelProperties.getConnectTimeout()))
                .build();
    }



    /**
     * 图像生成模型
     */
    @Bean
    public ZhipuAiImageModel glmImageModel() {
        return ZhipuAiImageModel.builder()
                .model(pictureModelProperties.getModelName()) // 模型名称
                .baseUrl(pictureModelProperties.getBaseUrl()) // 模型baseURL
                .apiKey(pictureModelProperties.getApiKey()) // 模型API密钥
                .connectTimeout(Duration.ofSeconds(pictureModelProperties.getConnectTimeout()))
                .build();
    }

    /**
     * 向量模型
     */
    @Bean
    public ZhipuAiEmbeddingModel glmEmbeddingModel() {
        return ZhipuAiEmbeddingModel.builder()
                .model(embeddingModelProperties.getModelName()) // 模型名称
                .baseUrl(embeddingModelProperties.getBaseUrl()) // 模型baseURL
                .apiKey(embeddingModelProperties.getApiKey()) // 模型API密钥
                .connectTimeout(Duration.ofSeconds(embeddingModelProperties.getConnectTimeout()))
                .build();
    }

    @Bean
    public ZhipuAiStreamingChatModel glmStreamingChatModel() {
        return ZhipuAiStreamingChatModel.builder()
                .model(streamingModelProperties.getModelName()) // 模型名称
                .baseUrl(streamingModelProperties.getBaseUrl()) // 模型baseURL
                .apiKey(streamingModelProperties.getApiKey()) // 模型API密钥
                .connectTimeout(Duration.ofSeconds(streamingModelProperties.getConnectTimeout()))
                .maxToken(streamingModelProperties.getMaxTokens()) // 生成文本的最大长度
                .temperature(streamingModelProperties.getTemperature()) // 生成文本的随机程度，值越大越随机
                .build();
    }
}
