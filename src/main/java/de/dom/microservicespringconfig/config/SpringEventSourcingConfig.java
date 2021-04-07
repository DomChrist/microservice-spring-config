package de.dom.microservicespringconfig.config;

import de.dom.microservice.arch.eventsourcing.bus.EventBus;
import de.dom.microservice.arch.eventsourcing.command.CommandGateway;
import de.dom.microservice.arch.eventsourcing.command.CommandGatewayScope;
import de.dom.microservice.arch.eventsourcing.config.EventSourcingConfig;
import de.dom.microservice.arch.eventsourcing.config.EventSourcingScope;
import de.dom.microservice.arch.eventsourcing.config.EventSourcingScopeAware;
import de.dom.microservice.arch.eventsourcing.event.AbstractDomainEvent;
import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreInterface;
import de.dom.microservice.arch.eventsourcing.event.AggregateLifecycle;
import de.dom.microservice.arch.eventsourcing.gateways.QueryGateway;
import de.dom.microservicespringconfig.config.commandhandler.DefaultCommandGateway;
import de.dom.microservicespringconfig.config.eventhandler.SpringAggregateLifecycle;
import de.dom.microservicespringconfig.config.messages.DefaultEventBus;
import de.dom.microservicespringconfig.config.query.DefaultQueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SpringEventSourcingConfig extends EventSourcingConfig {

    private static Logger logger = LoggerFactory.getLogger(SpringEventSourcingConfig.class);

    public SpringEventSourcingConfig() {
        logger.info("--- EventSourcingConfig ----");
    }


    @Bean
    public JdbcTemplate template( @Autowired DataSource source ){
        return new JdbcTemplate(source);
    }

    @Bean
    public EventBus eventBus( @Autowired EventStoreInterface eventStoreInterface ){
        return new DefaultEventBus(eventStoreInterface);
    }

    @Bean
    @Override
    public AggregateLifecycle lifecycle() {
        return new SpringAggregateLifecycle();
    }

    @Bean
    public CommandGateway commandGateway(){
        return new DefaultCommandGateway();
    }



    @Bean
    public QueryGateway queryGateway(@Autowired EventBus eventBus){
        return new DefaultQueryGateway(eventBus);
    }

    @Bean
    public EventSourcingScopeAware scope(@Autowired AggregateLifecycle al , @Autowired CommandGateway gateway , @Autowired QueryGateway queryGateway, @Autowired EventBus eventBus ){
        logger.info("--- EventSourcingConfig ----");
        EventSourcingScopeAware aware = new EventSourcingScopeAware();
            aware.setLifecycle(al);
            EventSourcingScope.aggregateLifecycle = al;
            EventSourcingScope.eventBus = eventBus;
        logger.info("--- EventSourcingConfig ----");
        return aware;
    }

}
