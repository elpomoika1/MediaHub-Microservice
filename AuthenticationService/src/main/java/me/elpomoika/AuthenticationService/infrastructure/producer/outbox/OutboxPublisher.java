package me.elpomoika.AuthenticationService.infrastructure.producer.outbox;

import me.elpomoika.AuthenticationService.domain.entities.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
