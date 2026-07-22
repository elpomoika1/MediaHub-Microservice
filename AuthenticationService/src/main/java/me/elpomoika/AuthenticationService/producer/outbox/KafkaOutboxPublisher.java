package me.elpomoika.AuthenticationService.producer.outbox;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;
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

    @Value("${kafka.topic.register}")
    private String registerTopic;

    @Override
    public void publish(OutboxEvent event) {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                registerTopic,
                null,
                event.getAggregateId(),
                event.getPayload()
        );
        record.headers().add("eventType", event.getEventType().getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
        LOGGER.info("success send to kafka {}, {}, {}", registerTopic, event.getAggregateId(), event.getPayload());
    }
}
