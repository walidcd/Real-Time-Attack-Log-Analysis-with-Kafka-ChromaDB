package imt.ibd;


import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class LogProducer {
    public static void main(String[] args) {
        // Kafka producer configuration
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16 KB
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5); // Small batching delay
        props.put(ProducerConfig.ACKS_CONFIG, "1"); // Wait for leader only
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4"); // Lightweight compression for better performance

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        Faker faker = new Faker();

        long startTime = System.currentTimeMillis();
        int logCount = 0;

        while (true) {
            for (int i = 0; i < 10; i++) { // Generate logs in batches
                String log = generateFakeLog(faker);
                producer.send(new ProducerRecord<>("attack-logs", log), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });

                logCount++;
                System.out.println("Produced log: " + log);
            }

            // Monitor throughput every 1000 logs
            if (logCount % 1000 == 0) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Logs per second: " + (logCount * 1000 / elapsedTime));
            }

            // Sleep to control log generation speed (adjust as needed)
            try {
                Thread.sleep(4000); // Reduce sleep time for faster log generation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateFakeLog(Faker faker) {
        return String.format("{\"timestamp\": \"%s\", \"sourceIP\": \"%s\", \"targetIP\": \"%s\", " +
                        "\"attackType\": \"%s\", \"endpoint\": \"%s\", \"severity\": \"%s\"}",
                faker.date().past(10, java.util.concurrent.TimeUnit.DAYS).toString(),
                faker.internet().ipV4Address(),
                faker.internet().ipV4Address(),
                faker.options().option("SQL Injection", "XSS", "DDoS", "RCE"),
                faker.internet().url(),
                faker.options().option("Low", "Medium", "High"));
    }
}
