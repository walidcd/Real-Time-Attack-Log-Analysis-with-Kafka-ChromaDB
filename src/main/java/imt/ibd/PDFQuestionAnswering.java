package imt.ibd;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import java.io.IOException;
import java.util.List;

public class PDFQuestionAnswering {

    public static void main(String[] args) throws IOException {
        // Load a PDF into the database
        PDFTextLoader.loadPDF("path/to/your/file.pdf");

        // Now you can ask questions about the PDF
        List<EmbeddingMatch<TextSegment>> searchResults = ChromaSearcher.search("What is the main topic of the document?", 1);
        System.out.printf("Score: %f\nResult: %s\n", searchResults.get(0).score(), searchResults.get(0).embedded().text());
    }
}
