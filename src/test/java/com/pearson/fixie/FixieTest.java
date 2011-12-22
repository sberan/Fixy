package com.pearson.fixie;

import com.petstore.Order;
import com.petstore.Pet;
import com.petstore.PetType;
import com.petstore.users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FixieTest {
    EntityManager petstore;
    EntityConstructor fixtures;

    @Before public void setup() {
        petstore = Persistence.createEntityManagerFactory("petstore").createEntityManager();
        petstore.getTransaction().begin();
        fixtures = new EntityConstructor(petstore, "com.petstore");
    }

    @After public void tearDown() {
        petstore.getTransaction().rollback();
    }

    @Test public void testPetTypes() {
        fixtures.load("pet_types.yaml");

        PetType dog = petstore.createQuery(
                "select type from PetType type where type.name = 'Dog'", PetType.class)
                .getSingleResult();

        assertThat(dog.getName(), is("Dog"));
    }
    
    @Test public void testPets() {
        fixtures.load("pets.yaml");

        Pet fido = petstore.createQuery("select p from Pet p where p.name = 'Fido'", Pet.class).getSingleResult();

        assertThat(fido.getName(), is("Fido"));
        assertThat(fido.getType().getName(), is("Dog"));
        
    }

    @Test
    public void testOrders() {
        fixtures.load("orders.yaml");

        Order order = petstore.createQuery("select o from Order o where o.pet.name= 'Fido'", Order.class).getSingleResult();
        
        assertThat(order.getPet().getName(), is("Fido"));
    }
    
    @Test
    public void testPostProcessor() {
        EntityPostProcessor<User> defaultPassword = new EntityPostProcessor<User>(User.class) {
            @Override public void process(User user) {
                user.setPassword("TEST_PASS");
            }
        };

        fixtures.addPostProcessor(defaultPassword);

        fixtures.load("users.yaml");

        User user = petstore.createQuery("select u from User u where u.name = 'George Washington'", User.class).getSingleResult();
        
        assertThat(user.getPassword(), is("TEST_PASS"));
    }
}
