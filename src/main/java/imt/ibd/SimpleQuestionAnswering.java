package imt.ibd;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import java.io.IOException;
import java.util.List;

public class SimpleQuestionAnswering {

    public static void main(String[] args) throws IOException {
        // Load the text file into the database
        TextFileLoader.loadTextFile("sample.txt");

        // Ask a question and search for relevant results
        List<EmbeddingMatch<TextSegment>> searchResults = ChromaSearcher.search("What sport is popular?", 1);

        // Print the results
        if (!searchResults.isEmpty()) {
            System.out.printf("Score: %f\nResult: %s\n", searchResults.get(0).score(), searchResults.get(0).embedded().text());
        } else {
            System.out.println("No relevant result found.");
        }
    }
}

