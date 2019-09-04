package com.training.project.messaging.publisher

import com.training.project.service.model.ProductEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class EventsPublisher(private val kafkaTemplate: KafkaTemplate<String, Any>) {

    @TransactionalEventListener
    fun handleEvent(event: ProductEvent) {
        kafkaTemplate.send("products-service", event);
    }
}
