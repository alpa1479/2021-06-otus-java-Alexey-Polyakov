package ru.otus.hw12.crm.model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "street")
    private String street;

    public Address(String street) {
        this.street = street;
    }

    @Override
    public Address clone() {
        return new Address(id, street);
    }
}
