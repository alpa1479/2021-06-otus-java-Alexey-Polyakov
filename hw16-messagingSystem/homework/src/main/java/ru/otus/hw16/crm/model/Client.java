package ru.otus.hw16.crm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

import java.util.HashSet;
import java.util.Set;

@Data
@Table("clients")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Client implements ResultDataType {

    @Id
    private Long id;
    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    public Client(String name, Address address, Set<Phone> phones) {
        this(null, name, address, phones);
    }

    @PersistenceConstructor
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public void addPhone(Phone phone) {
        if (phones == null) {
            phones = new HashSet<>();
        }
        phones.add(phone);
    }

    @JsonIgnore
    public boolean isEmptyPhones() {
        return phones == null || phones.isEmpty();
    }
}
