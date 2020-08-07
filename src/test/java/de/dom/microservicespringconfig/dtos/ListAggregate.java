package de.dom.microservicespringconfig.dtos;

import de.dom.microservice.arch.ddd.annotations.Aggregate;
import de.dom.microservice.arch.eventsourcing.annotations.AggregateCommandHandler;
import de.dom.microservice.arch.eventsourcing.annotations.AggregateEventHandler;
import de.dom.microservice.arch.eventsourcing.event.AggregateLifecycle;

@Aggregate
public class ListAggregate {

    public static boolean created = false;

    private String reference;
    private String name;

    public ListAggregate() {
    }

    @AggregateCommandHandler
    public ListAggregate( CreateNewList cmd ){
        NewListCreated created = new NewListCreated(cmd.getReference(), cmd.getName());
        AggregateLifecycle.apply( created );
    }

    @AggregateEventHandler
    public void newListCreated( NewListCreated event ){
        this.reference = event.getReference();
        this.name = event.getName();
        ListAggregate.created = true;
    }

    public String getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }
}
