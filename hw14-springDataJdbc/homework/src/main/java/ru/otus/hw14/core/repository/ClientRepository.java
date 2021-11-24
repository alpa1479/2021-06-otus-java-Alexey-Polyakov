package ru.otus.hw14.core.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw14.core.resultsetextractor.ClientResultSetExtractor;
import ru.otus.hw14.crm.model.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    @Query(value = """
            select c.id       as id,
                   c.name     as name,
                   a.id       as address_id,
                   a.street   as address_street,
                   p.id       as phone_id,
                   p.number   as phone_number
            from clients c
                left outer join addresses a
                                on a.client_id = c.id
                left outer join phones p
                                on p.client_id = c.id
            """,
            resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findAll();
}
