package me.elpomoika.AuthenticationService.repository;

import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    @Query(value = "select * from outbox_events where status in (:statuses) and available_at <= :now " +
            "order by created_at limit 50 for update skip locked", nativeQuery = true)
    List<OutboxEvent> findReady(@Param("statuses") List<String> statuses, @Param("now") Instant now);
}
