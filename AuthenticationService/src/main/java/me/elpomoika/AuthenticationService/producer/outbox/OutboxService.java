package me.elpomoika.AuthenticationService.producer.outbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;
import me.elpomoika.AuthenticationService.domain.enums.OutboxStatus;
import me.elpomoika.AuthenticationService.producer.outbox.repository.OutboxEventRepository;
import me.elpomoika.AuthenticationService.util.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository repository;
    private final JsonParser jsonParser;

    @Transactional
    public void publish(Object event, String eventType, UUID aggregateId) {
        Instant now = Instant.now();

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(aggregateId.toString())
                .eventType(eventType)
                .payload(jsonParser.toJson(event))
                .status(OutboxStatus.NEW)
                .retryCount(0)
                .availableAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        repository.save(outboxEvent);
    }
}
