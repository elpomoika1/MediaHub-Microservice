package me.elpomoika.AuthenticationService.infrastructure.producer.outbox;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entities.OutboxEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class KafkaOutboxPublisher implements OutboxPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOutboxPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.outbox.topic}")
    private String outboxTopic;

    @Override
    public void publish(OutboxEvent event) {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                outboxTopic,
                null,
                event.getAggregateId(),
                event.getPayload()
        );
        record.headers().add("eventType", event.getEventType().getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
    }
}
