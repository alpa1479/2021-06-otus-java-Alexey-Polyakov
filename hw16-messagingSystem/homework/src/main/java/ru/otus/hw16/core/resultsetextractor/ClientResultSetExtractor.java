package ru.otus.hw16.core.resultsetextractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.hw16.crm.model.Address;
import ru.otus.hw16.crm.model.Client;
import ru.otus.hw16.crm.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClientResultSetExtractor implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clients = new ArrayList<Client>();
        Map<Long, Client> previousClients = new HashMap<>();
        while (rs.next()) {
            var clientId = rs.getLong("id");
            var previousClient = previousClients.get(clientId);
            if (previousClient == null) {
                previousClient = new Client(clientId, rs.getString("name"), extractAddress(rs), createPhonesSet(rs));
                clients.add(previousClient);
                previousClients.put(clientId, previousClient);
            } else {
                Phone phone = extractPhone(rs);
                previousClient.addPhone(phone);
            }
        }
        return clients;
    }

    private Address extractAddress(ResultSet rs) throws SQLException {
        return new Address(rs.getLong("address_id"), rs.getString("address_street"));
    }

    private Phone extractPhone(ResultSet rs) throws SQLException {
        return new Phone(rs.getLong("phone_id"), rs.getString("phone_number"));
    }

    private Set<Phone> createPhonesSet(ResultSet rs) throws SQLException {
        Set<Phone> phones = new HashSet<>();
        phones.add(extractPhone(rs));
        return phones;
    }
}
