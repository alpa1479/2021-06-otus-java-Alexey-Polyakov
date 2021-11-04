package ru.otus.hw12.crm.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "number")
    private String number;

    @Expose(serialize = false, deserialize = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(String number) {
        this.number = number;
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public Phone clone() {
        return new Phone(id, number);
    }
}
