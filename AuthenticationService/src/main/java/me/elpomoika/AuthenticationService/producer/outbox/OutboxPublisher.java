package me.elpomoika.AuthenticationService.producer.outbox;

import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
