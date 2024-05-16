package org.example.memoryauthentication.entity;


import jakarta.persistence.*;

@Entity
@Table(name ="AUTH_ROLES")
public class Role {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
