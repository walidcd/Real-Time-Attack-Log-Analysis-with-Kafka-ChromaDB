package imt.ibd;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;

public class Chroma {

    public static final EmbeddingStore<TextSegment> embeddingStore =
            ChromaEmbeddingStore.builder()
                    .baseUrl("http://localhost:8000/")
                    .collectionName("my-collection14")
                    .build();

    public static final EmbeddingModel embeddingModel =
            OpenAiEmbeddingModel.builder()
                    .apiKey("*")
                    .modelName("text-embedding-ada-002")
                    .build();
}