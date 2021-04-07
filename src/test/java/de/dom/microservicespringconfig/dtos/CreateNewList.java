package de.dom.microservicespringconfig.dtos;

import de.dom.microservice.arch.eventsourcing.command.AbstractDomainCommand;

import java.util.UUID;

public class CreateNewList extends AbstractDomainCommand {

    private String reference;

    private String name;

    @Override
    public void setReference(String reference) {
         this.reference = reference;
    }

    @Override
    public String getReference() {
        if( reference == null ){
            reference = UUID.randomUUID().toString();
        }
        return reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void verify() {

    }
}
