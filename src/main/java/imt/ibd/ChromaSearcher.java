package imt.ibd;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import java.util.List;

import static imt.ibd.Chroma.embeddingModel;
import static imt.ibd.Chroma.embeddingStore;


public class ChromaSearcher {

    public static List<EmbeddingMatch<TextSegment>> search(String query,
                                                           int maxResults) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        return embeddingStore.findRelevant(queryEmbedding, maxResults);
    }
}
