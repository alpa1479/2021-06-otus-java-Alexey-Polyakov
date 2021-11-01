package ru.otus.hw10.crm.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Phone> phones = new HashSet<>();

    public Client(String name) {
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Client(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Client addPhone(Phone phone) {
        phone.setClient(this);
        phones.add(phone);
        return this;
    }

    public Client addAllPhones(Set<Phone> phones) {
        phones.forEach(this::addPhone);
        return this;
    }

    @Override
    public Client clone() {
        Client client = new Client(id, name);
        if (address != null) {
            client.setAddress(address.clone());
        }
        if (phones != null && !phones.isEmpty()) {
            Set<Phone> clonedPhones = new HashSet<>();
            for (Phone phone : phones) {
                final Phone clone = phone.clone();
                clonedPhones.add(clone);
            }
            client.addAllPhones(clonedPhones);
        }
        return client;
    }
}
