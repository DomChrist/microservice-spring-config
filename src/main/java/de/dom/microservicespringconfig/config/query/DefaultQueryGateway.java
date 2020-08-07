package de.dom.microservicespringconfig.config.query;

import de.dom.microservice.arch.eventsourcing.bus.EventBus;
import de.dom.microservice.arch.eventsourcing.gateways.QueryGateway;

public class DefaultQueryGateway extends QueryGateway {

    public DefaultQueryGateway(EventBus eventBus) {
        this.eventBus = eventBus;
        QueryGateway.eventBus = eventBus;
    }

}
