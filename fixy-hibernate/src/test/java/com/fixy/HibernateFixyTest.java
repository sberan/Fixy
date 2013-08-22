package com.fixy;

import com.petstore.*;
import com.petstore.users.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HibernateFixyTest {

    SessionFactory sessionFactory;
    Session petstore;

    Fixy fixtures;
    Fixy detachedFixtures;
    Fixy fieldAccessFixtures;

    @Before public void setup() {

        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        petstore = sessionFactory.getCurrentSession();
        petstore.beginTransaction();

        fixtures = new HibernateFixyBuilder(sessionFactory).withDefaultPackage("com.petstore").build();
        detachedFixtures = new HibernateFixyBuilder(sessionFactory).withDefaultPackage("com.petstore").mergeEntities().build();
        fieldAccessFixtures = new HibernateFixyBuilder(sessionFactory).withDefaultPackage("com.petstore").useFieldAccess().build();
    }

    @After public void tearDown() {
        petstore.getTransaction().rollback();
        sessionFactory.close();
    }

    @Test public void testFieldAccessEntities() {
        fieldAccessFixtures.load("owners-fieldaccess.yaml");

        ImmutableOwner owner = (ImmutableOwner)petstore.createQuery("select o from ImmutableOwner o where o.name = 'John'").uniqueResult();

        assertThat(owner.getOwnerName(), is("John"));
    }

    @Test public void testDetachedEntities() {
        detachedFixtures.load("owners.yaml");

        Owner owner = (Owner)petstore.createQuery("select o from Owner o where o.name = 'John'").uniqueResult();

        assertThat(owner.getName(), is("John"));
    }

    @Test public void testPetTypes() {
        fixtures.load("pet_types.yaml");

        PetType dog = (PetType)petstore.createQuery("select type from PetType type where type.name = 'Dog'").uniqueResult();

        assertThat(dog.getName(), is("Dog"));
    }

    @Test public void testPets() {
        fixtures.load("pets.yaml");

        Pet fido = (Pet) petstore.createQuery("select p from Pet p where p.name = 'Fido'").uniqueResult();

        assertThat(fido.getName(), is("Fido"));
        assertThat(fido.getType().getName(), is("Dog"));

    }

    @Test
    public void testOrders() {
        fixtures.load("orders.yaml");

        Order order = (Order) petstore.createQuery("select o from Order o where o.pet.name= 'Fido'").uniqueResult();

        assertThat(order.getPet().getName(), is("Fido"));
    }

    @Test
    public void testAddress() {
        fixtures.load("address.yaml");

        Order order = (Order) petstore.createQuery("select o from Order o where o.pet.name= 'Fido'").uniqueResult();

        assertThat(order.getAddress().getCity(), is("Paris"));
    }

    @Test
    public void testPostProcessor() {
        Processor<User> defaultPassword = new Processor<User>(User.class) {
            @Override public void process(User user) {
                user.setPassword("TEST_PASS");
            }
        };

        fixtures.addProcessor(defaultPassword);

        fixtures.load("users.yaml");

        User user = (User) petstore.createQuery("select u from User u where u.name = 'George Washington'").uniqueResult();

        assertThat(user.getPassword(), is("TEST_PASS"));
    }

    @Test public void testPostProcessorCanCreateEntities() {
        Processor<Pet> createPetOwner = new Processor<Pet>(Pet.class) {
            @Override public void process(Pet pet) {
                User petOwner = new User();
                petOwner.setName(pet.getName());
                addEntity(petOwner);
            }
        };

        fixtures.addProcessor(createPetOwner);

        fixtures.load("pets.yaml");

        User petOwner = (User) petstore.createQuery("select u from User u  where u.name = 'Fido'").uniqueResult();

        assertThat(petOwner.getName(), is("Fido"));
    }
}
