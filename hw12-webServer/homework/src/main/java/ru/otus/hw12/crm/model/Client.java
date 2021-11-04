package ru.otus.hw12.crm.model;

import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "name")
    private String name;

    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Expose
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

    public Set<Phone> getPhones() {
        return getClonedPhones();
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
            Set<Phone> clonedPhones = getClonedPhones();
            client.addAllPhones(clonedPhones);
        }
        return client;
    }

    private Set<Phone> getClonedPhones() {
        return phones.stream().map(Phone::clone).collect(Collectors.toSet());
    }
}
