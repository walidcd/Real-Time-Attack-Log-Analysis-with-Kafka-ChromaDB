package imt.ibd;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class LogConsumer {

    public static void main(String[] args) {
        // Kafka consumer configuration
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "log-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start from the earliest message

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("attack-logs"));

        Scanner scanner = new Scanner(System.in);

        Thread logConsumerThread = new Thread(() -> {
            while (true) {
                var records = consumer.poll(java.time.Duration.ofMillis(1000)); // Poll for new records

                records.forEach(record -> {
                    String log = record.value();
                    //System.out.println("Consumed log: " + log);

                    // Process the log: calculate embeddings and store
                    ChromaInserter.addDocuments(log);
                });
            }
        });

        logConsumerThread.start(); // Start consuming logs in the background

        // Continuously ask questions in the terminal
        while (true) {
            System.out.print("Ask a question: ");
            String question = scanner.nextLine(); // Get user input

            if (question.equalsIgnoreCase("exit")) {
                break; // Exit the loop if the user types "exit"
            }

            queryDatabase(question); // Query the Chroma database with the question
        }

        scanner.close();
        consumer.close(); // Close the consumer when done
    }

    public static void queryDatabase(String query) {
        List<EmbeddingMatch<TextSegment>> searchResults = ChromaSearcher.search(query, 1);
        if (!searchResults.isEmpty()) {
            System.out.printf("Score: %f\nResult: %s\n", searchResults.get(0).score(), searchResults.get(0).embedded().text());
        } else {
            System.out.println("No relevant result found.");
        }
    }
}
