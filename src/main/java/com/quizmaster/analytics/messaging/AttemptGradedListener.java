package com.quizmaster.analytics.messaging;

import com.quizmaster.analytics.event.AttemptGradedEvent;
import com.quizmaster.analytics.service.AnalyticsIngestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttemptGradedListener {

    private final AnalyticsIngestService ingestService;

    @KafkaListener(topics = KafkaTopics.ATTEMPT_GRADED, groupId = "analytics-service",
            autoStartup = "${app.kafka.enabled:true}")
    public void onAttemptGraded(AttemptGradedEvent event) {
        ingestService.ingest(event);
    }
}
