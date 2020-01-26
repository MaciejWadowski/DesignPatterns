package agh.dp;

import javax.persistence.*;

@Entity
public class Przedmiot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    public Przedmiot(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Przedmiot(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Przedmiot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
