package de.dom.microservicespringconfig;

import de.dom.microservice.arch.eventsourcing.command.CommandGateway;
import de.dom.microservicespringconfig.config.repository.EventStoreRepository;
import de.dom.microservicespringconfig.dtos.CreateNewList;
import de.dom.microservicespringconfig.dtos.ListAggregate;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
class MicroserviceSpringConfigApplicationTests {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private EventStoreRepository repository;

    @Test
    @Sql("/db/eventstore.sql")
    void contextLoads() {
        CreateNewList newList = new CreateNewList();
            newList.setName("TestList");
        CommandGateway.send( newList );
        assertTrue(ListAggregate.created);

        List<Map<String, Object>> maps = template.queryForList("select * from eventstore.events");
        assertTrue( !maps.isEmpty() );

        long sequence = repository.sequence(newList.getReference(), ListAggregate.class);
        assertTrue( sequence == 2);


    }

}
