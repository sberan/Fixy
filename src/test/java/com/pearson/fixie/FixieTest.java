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
    EntityManager petstore;
    EntityConstructor constructor;

    @Before public void setup() {
        petstore = Persistence.createEntityManagerFactory("petstore").createEntityManager();
        petstore.getTransaction().begin();
        constructor = new EntityConstructor(petstore);
    }

    @After public void tearDown() {
        petstore.getTransaction().rollback();
    }

    @Test public void testPetTypes() {
        constructor.load("pet_types.yaml");

        PetType dog = petstore.createQuery(
                "select type from PetType type where type.name = 'Dog'", PetType.class)
                .getSingleResult();

        assertThat(dog, is(notNullValue()));
        assertThat(dog.getName(), is("Dog"));
    }
    
    @Test public void testPets() {

    }
}
