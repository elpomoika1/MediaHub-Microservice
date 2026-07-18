package me.elpomoika.AuthenticationService.outbox;

import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
