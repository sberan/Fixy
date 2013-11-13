package fixy;

import com.google.code.morphia.Datastore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MorphiaFixyTest {
    @Mock Datastore ds;

    @Test public void testMorphia() {
        Fixy fixtures = new MorphiaFixyBuilder(ds).build();
        fixtures.load("people.yaml");

        Person expected = new Person();
        expected.firstName = "Jerry";
        expected.lastName = "Seinfeld";

        Mockito.verify(ds).save(expected);
    }

    @Test public void testMorphiaNoPackage() {
        Fixy fixtures = new MorphiaFixyBuilder(ds).withDefaultPackage("fixy").build();
        fixtures.load("people-nopackage.yaml");

        Person expected = new Person();
        expected.firstName = "Luke";
        expected.lastName = "Skywalker";

        Mockito.verify(ds).save(expected);
    }
}
