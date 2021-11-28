package ru.otus.hw16.crm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("addresses")
@EqualsAndHashCode(exclude = "id")
public class Address {

    @Id
    private Long id;
    private String street;

    public Address(String street) {
        this(null, street);
    }

    @PersistenceConstructor
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
