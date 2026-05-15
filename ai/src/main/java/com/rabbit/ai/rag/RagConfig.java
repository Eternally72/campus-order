package com.rabbit.ai.rag;

import dev.langchain4j.community.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RagConfig {

    @Resource
    private ZhipuAiEmbeddingModel  zhipuAiEmbeddingModel;

    /**
     * 配置内容检索器
     *
     * @return 内容检索器实例
     */
    @Bean
    public ContentRetriever getContentRetriever() {
        // ------ Rag ------
        // 1.加载文件
        List<Document> document = FileSystemDocumentLoader.loadDocuments("src/main/resources/doc");

        // 2.文档切割
        DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter(1000, 200);

        // 3.自定义文档加载器
        // 创建 MilvusEmbeddingStore 实例
        EmbeddingStore<TextSegment> embeddingStore = MilvusEmbeddingStore.builder()
                .host("localhost")                         // Milvus 服务器主机
                .port(19530)                               // Milvus 服务器端口
                .collectionName("example_collection")      // 集合名称
                .dimension(128)                            // 向量维度
                .indexType(IndexType.FLAT)                 // 索引类型
                .metricType(MetricType.COSINE)             // 度量类型
                .consistencyLevel(ConsistencyLevelEnum.EVENTUALLY)  // 一致性级别
                .autoFlushOnInsert(true)                   // 插入后自动刷新
                .idFieldName("id")                         // ID 字段名称
                .textFieldName("text")                     // 文本字段名称
                .metadataFieldName("metadata")             // 元数据字段名称
                .vectorFieldName("vector")                 // 向量字段名称
                .build();                                  // 构建 MilvusEmbeddingStore 实例


        // 创建 EmbeddingStoreIngestor 实例
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(zhipuAiEmbeddingModel)
                .documentSplitter(documentByParagraphSplitter)
                .textSegmentTransformer(textSegment ->
                        TextSegment.from(textSegment.metadata().getString("file_name") + '\n' + textSegment.text(), textSegment.metadata())
                )
                .embeddingModel(zhipuAiEmbeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // 4.加载文档
        ingestor.ingest(document);

        // 5.自定义内容加载器
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(zhipuAiEmbeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(5)
                .minScore(0.75)
                .build();
    }
}
