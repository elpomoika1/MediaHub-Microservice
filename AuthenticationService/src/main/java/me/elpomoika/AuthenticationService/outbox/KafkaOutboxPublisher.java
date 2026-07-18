package me.elpomoika.AuthenticationService.outbox;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaOutboxPublisher implements OutboxPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.register}")
    private String registerTopic;

    @Override
    public void publish(OutboxEvent event) {
        kafkaTemplate.send(registerTopic, event.getAggregateId(), event.getPayload());
    }
}
