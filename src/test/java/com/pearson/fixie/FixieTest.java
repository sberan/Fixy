package com.pearson.fixie;

import com.petstore.PetType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class FixieTest {
    private EntityManager petstore;

    @Before public void setup() {
        petstore = Persistence.createEntityManagerFactory("petstore").createEntityManager();
        petstore.getTransaction().begin();
        EntityConstructor constructor = new EntityConstructor("com.petstore");
        constructor.loadEntities(getClass().getResourceAsStream("fixtures.yaml"));
        constructor.persistEntities(petstore);
    }

    @After public void tearDown() {
        petstore.getTransaction().rollback();
    }

    @Test public void testFoo() {
        PetType dog = petstore.createQuery("select type from PetType type where type.name = 'Dog'", PetType.class).getSingleResult();

        assertThat(dog, is(notNullValue()));
    }
}
