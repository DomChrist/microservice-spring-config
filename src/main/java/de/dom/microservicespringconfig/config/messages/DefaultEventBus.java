package de.dom.microservicespringconfig.config.messages;

import de.dom.microservice.arch.eventsourcing.aggregates.AggregateProxy;
import de.dom.microservice.arch.eventsourcing.bus.EventBus;
import de.dom.microservice.arch.eventsourcing.event.AbstractDomainEvent;
import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreEntity;
import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreInterface;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultEventBus implements EventBus {

    private EventStoreInterface eventStoreInterface;

    public DefaultEventBus(EventStoreInterface eventStoreInterface) {
        this.eventStoreInterface = eventStoreInterface;
    }

    @Override
    public Set<AbstractDomainEvent> read(String key, Class<?> aggregate) {
        List<EventStoreEntity> entities = eventStoreInterface.aggregate(key, aggregate);
        AggregateProxy<?> proxy = new AggregateProxy<>(aggregate);
        Set<AbstractDomainEvent> sets = new HashSet<>();
            sets.addAll(proxy.convertAggregateEvents(entities));
       return sets;
    }

    @Override
    public void apply(String key, Class<?> aggregate, AbstractDomainEvent event) {
        long sequence = eventStoreInterface.sequence(event.getReference(), aggregate);
        EventStoreEntity from = EventStoreEntity.from(aggregate, event, sequence);
        eventStoreInterface.save(from);
    }

}
