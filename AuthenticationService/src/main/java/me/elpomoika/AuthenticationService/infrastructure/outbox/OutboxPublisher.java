package me.elpomoika.AuthenticationService.infrastructure.outbox;

import me.elpomoika.AuthenticationService.domain.entities.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
