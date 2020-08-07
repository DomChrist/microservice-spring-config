package de.dom.microservicespringconfig.config.repository;

import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreEntity;
import de.dom.microservice.arch.eventsourcing.eventstore.EventStoreInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class EventStoreRepository implements EventStoreInterface {

    @Value("${application.eventsourcing.db.schema}")
    private String SCHEMA;

    @Value("${application.eventsourcing.db.table}")
    private String TABLE;

    private NamedParameterJdbcTemplate template;

    public EventStoreRepository(JdbcTemplate jdbcTemplate){
        this.template = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public List<EventStoreEntity> aggregate(String reference, Class<?> aggregate) {
        MapSqlParameterSource source = new MapSqlParameterSource("reference", reference).addValue("aggregate", aggregate.getName());
        List<EventStoreEntity> storeEntities = template.query(selectAll().concat(" where aggregate = :aggregate and reference = :reference "), source, new EventEntityRowMapper());
        return storeEntities;
    }

    @Override
    public long sequence(String reference, Class<?> aggregate) {
        long next = 1;
        String query = new StringBuilder("select max(sequence) + 1 AS NEXTSEQUENCE from ").append(SCHEMA).append(".").append(TABLE)
                .append(" WHERE ")
                .append("aggregate = :aggregate")
                .append(" AND ")
                .append("reference = :reference").toString();
        MapSqlParameterSource source = new MapSqlParameterSource("aggregate" , aggregate.getName()).addValue("reference",reference);
        Map<String, Object> objectMap = template.queryForMap(query, source);
            Object nextsequence = objectMap.get("NEXTSEQUENCE");
            if( objectMap.isEmpty() ) return next;
            if( nextsequence == null) return next;
            return (long)(int) nextsequence;
    }

    @Override
    public void save(EventStoreEntity entity) {
        String insert = String.format("INSERT INTO %s.%s" , SCHEMA,TABLE);
        String columns = "(reference,eventgroup,event,aggregate,version,sequence,payload,creator,created)";
        String values = "(:reference,:eventGroup,:event,:aggregate,:version,:sequence,:payload,:creator,:created)";
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);

        String query = new StringBuilder(insert).append(columns).append(" VALUES ").append(values).toString();

        template.update(query , parameterSource );
    }

    public String selectAll(){
        return String.format("select * from %s.%s" , SCHEMA , TABLE);
    }


}
