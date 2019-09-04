package com.training.project.service.infrastructure

import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents
import org.springframework.util.Assert.notNull
import java.util.ArrayList
import java.util.Collections.unmodifiableList

/**
 * Aggregate Root representation extending {@link AbstractBaseEntity} and {@link AbstractAggregateRoot}.
 */
abstract class AggregateRoot<EventType> {

    @Transient
    protected val domainEvents = ArrayList<EventType>()

    /**
     * All domain events currently captured by the aggregate.
     */
    @DomainEvents
    fun domainEvents(): Collection<EventType> = unmodifiableList<EventType>(domainEvents)

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save methods.
     */
    protected fun publish(event: EventType): EventType {
        notNull(event, "Null event type can not be registered")
        domainEvents.add(event)
        return event
    }

    /**
     * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    protected fun clearDomainEvents() {
        domainEvents.clear()
    }
}