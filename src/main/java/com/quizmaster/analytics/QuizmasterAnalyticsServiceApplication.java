package com.quizmaster.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Analytics-service — a CQRS read model. It consumes {@code quiz.attempt.graded}
 * and maintains its own denormalized attempt store, then serves admin/student
 * stats and reports from that store. No synchronous calls to other services.
 */
@SpringBootApplication
@EnableKafka
public class QuizmasterAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizmasterAnalyticsServiceApplication.class, args);
    }
}
