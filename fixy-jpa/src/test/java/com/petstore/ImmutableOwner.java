package com.petstore;

import javax.persistence.*;

@Entity
public class ImmutableOwner {

    @javax.persistence.Id @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    private ImmutableOwner() {
        //required
    }

    public ImmutableOwner(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    /*
     * Intentionally not named 'getName()' to further illustrate field access
     * instead of property access in com.fixy.JPAFixyTest#testFieldAccessEntities()
     */
    public String getOwnerName() {
        return name;
    }

}
