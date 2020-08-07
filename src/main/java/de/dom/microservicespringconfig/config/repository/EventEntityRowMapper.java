package de.dom.microservicespringconfig.config.repository;

import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EventEntityRowMapper implements RowMapper<EventStoreEntity> {

    @Override
    public EventStoreEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        EventStoreEntity e = new EventStoreEntity();
            e.setAggregate( string(resultSet.getString("aggregate")) );
            e.setEvent( string(resultSet.getString("event")) );
            e.setEventGroup( string(resultSet.getString("eventgroup")) );
            e.setId( resultSet.getInt("id") );
            e.setSequence( resultSet.getLong("sequence") );
            e.setVersion( resultSet.getInt("version") );
            e.setPayload( string(resultSet.getString("payload")) );
            e.setReference( string(resultSet.getString("reference")) );
            e.setCreator( string(resultSet.getString("creator")) );
        return e;
    }


    public String string( String s ){
        return Optional.ofNullable(s).orElse("").trim();
    }


}
