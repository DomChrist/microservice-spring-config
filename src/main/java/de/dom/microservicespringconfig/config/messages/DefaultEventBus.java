package de.dom.microservicespringconfig.config.messages;

import de.dom.microservice.arch.eventsourcing.aggregates.AggregateProxy;
import de.dom.microservice.arch.eventsourcing.bus.EventBus;
import de.dom.microservice.arch.eventsourcing.bus.EventBusMessage;
import de.dom.microservice.arch.eventsourcing.bus.EventBusResult;
import de.dom.microservice.arch.eventsourcing.event.AbstractDomainEvent;
import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreEntity;
import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultEventBus implements EventBus {

    private EventStoreInterface eventStoreInterface;

    public DefaultEventBus(EventStoreInterface eventStoreInterface) {
        this.eventStoreInterface = eventStoreInterface;
    }

    @Override
    public EventBusResult read(String key, Class<?> aggregate) {
        List<EventStoreEntity> entities = eventStoreInterface.aggregate(key, aggregate);
        AggregateProxy proxy = new AggregateProxy(aggregate);
        List<AbstractDomainEvent> sets = new ArrayList<>();
            sets.addAll(proxy.convertAggregateEvents(entities));
       EventBusResult r = new EventBusResult( sets );

       return r;
    }

    @Override
    public void apply(EventBusMessage eventBusMessage) {

    }


}
