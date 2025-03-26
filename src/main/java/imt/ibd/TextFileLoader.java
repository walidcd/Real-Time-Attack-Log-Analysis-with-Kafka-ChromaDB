package imt.ibd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextFileLoader {

    public static void loadTextFile(String filePath) throws IOException {
        // Read all lines from the text file
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        // Split the content into smaller chunks (e.g., by lines)
        String[] textChunks = content.split("\n");

        // Add each chunk to the Chroma store
        for (String chunk : textChunks) {
            if (chunk != null && !chunk.trim().isEmpty()) {
                ChromaInserter.addDocuments(chunk);
            }
        }
    }
}

