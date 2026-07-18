package me.elpomoika.AuthenticationService.outbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;
import me.elpomoika.AuthenticationService.repository.OutboxEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxPublisher outboxPublisher;

    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:5000}")
    @Transactional
    public void processBatch() {
        Instant now = Instant.now();
        List<OutboxEvent> events = outboxEventRepository.findReady(
                List.of(OutboxStatus.NEW.name(), OutboxStatus.FAILED.name()), now);

        for (OutboxEvent event : events) {
            try {
                outboxPublisher.publish(event);
                markSent(event, now);
            } catch (Exception ex) {
                markFailed(event, now, ex);
            }
        }
    }

    private void markSent(OutboxEvent event, Instant now) {
        event.setStatus(OutboxStatus.SENT);
        event.setUpdatedAt(now);
    }

    private void markFailed(OutboxEvent event, Instant now, Exception ex) {
        int nextRetry = event.getRetryCount() + 1;
        Duration backoff = retryBackoff(nextRetry);
        event.setRetryCount(nextRetry);
        event.setStatus(OutboxStatus.FAILED);
        event.setAvailableAt(now.plus(backoff));
        event.setUpdatedAt(now);
        log.warn("Outbox publish failed id={}, retry in {}s", event.getId(), backoff.toSeconds(), ex);
    }

    private Duration retryBackoff(int retryCount) {
        if (retryCount <= 3)
            return Duration.ofSeconds(15);
        if (retryCount <= 6)
            return Duration.ofMinutes(1);
        if (retryCount <= 10)
            return Duration.ofMinutes(5);
        if (retryCount <= 15)
            return Duration.ofMinutes(15);
        return Duration.ofHours(1);
    }
}
