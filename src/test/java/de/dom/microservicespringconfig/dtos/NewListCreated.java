package de.dom.microservicespringconfig.dtos;

import com.jayway.jsonpath.internal.filter.ValueNode;
import de.dom.microservice.arch.eventsourcing.event.AbstractDomainEvent;

public class NewListCreated extends AbstractDomainEvent {

    private String reference;
    private String name;

    public NewListCreated() {
    }

    public NewListCreated(String reference, String name) {
        this.reference = reference;
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getEventGroup() {
        return "list";
    }
}
