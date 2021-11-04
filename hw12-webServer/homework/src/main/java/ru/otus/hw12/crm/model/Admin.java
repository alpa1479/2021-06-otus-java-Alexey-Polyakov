package ru.otus.hw12.crm.model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "login")
    private String login;

    @Expose
    @Column(name = "password")
    private String password;

    public Admin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Admin clone() {
        return new Admin(id, login, password);
    }
}
