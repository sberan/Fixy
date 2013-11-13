package fixy;

import com.petstore.ImmutableOwner;
import com.petstore.Order;
import com.petstore.Owner;
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

public class JPAFixyTest {
    EntityManager petstore;
    Fixy fixtures;
    Fixy detachedFixtures;
    Fixy fieldAccessFixtures;

    @Before public void setup() {
        petstore = Persistence.createEntityManagerFactory("petstore").createEntityManager();
        petstore.getTransaction().begin();
        fixtures = new JpaFixyBuilder(petstore).withDefaultPackage("com.petstore").build();
        detachedFixtures = new JpaFixyBuilder(petstore).withDefaultPackage("com.petstore").mergeEntities().build();
        fieldAccessFixtures = new JpaFixyBuilder(petstore).withDefaultPackage("com.petstore").useFieldAccess().build();
    }

    @After public void tearDown() {
        petstore.getTransaction().rollback();
    }

    @Test public void testFieldAccessEntities() {
        fieldAccessFixtures.load("owners-fieldaccess.yaml");

        ImmutableOwner owner = petstore.createQuery("select o from ImmutableOwner o where o.name = 'John'", ImmutableOwner.class).getSingleResult();

        assertThat(owner.getOwnerName(), is("John"));
    }

    @Test public void testDetachedEntities() {
        detachedFixtures.load("owners.yaml");

        Owner owner = petstore.createQuery("select o from Owner o where o.name = 'John'", Owner.class).getSingleResult();

        assertThat(owner.getName(), is("John"));
    }

    @Test public void testPetTypes() {
        fixtures.load("pet_types.yaml");

        PetType dog = petstore.createQuery("select type from PetType type where type.name = 'Dog'", PetType.class).getSingleResult();

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
    public void testAddress() {
        fixtures.load("address.yaml");

        Order order = petstore.createQuery("select o from Order o where o.pet.name= 'Fido'", Order.class).getSingleResult();

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

        User user = petstore.createQuery("select u from User u where u.name = 'George Washington'", User.class).getSingleResult();

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

        User petOwner = petstore.createQuery("select u from User u  where u.name = 'Fido'", User.class).getSingleResult();

        assertThat(petOwner.getName(), is("Fido"));
    }
}
