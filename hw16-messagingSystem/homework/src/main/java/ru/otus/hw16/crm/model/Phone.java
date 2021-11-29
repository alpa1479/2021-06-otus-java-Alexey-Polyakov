package ru.otus.hw16.crm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("phones")
@EqualsAndHashCode(exclude = "id")
public class Phone {

    @Id
    private Long id;
    private String number;

    public Phone(String number) {
        this(null, number);
    }

    @PersistenceConstructor
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
