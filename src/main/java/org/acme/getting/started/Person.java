package org.acme.getting.started;


import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import javax.persistence.*;
@Entity
@Cacheable
public class Person extends PanacheEntityBase{
    @Column(nullable = false)
    @Id
    @GeneratedValue
    Long id;

    @Column(length = 30)
    String name;

    @Column(length = 30)
    String email;

    public Person(){}
    public Person(Long id,String name,String email){
        this.id=id;
        this.name=name;
        this.email=email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

}
