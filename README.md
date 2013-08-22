Fixy : Yaml fixtures for Java
========
[![Build Status](https://secure.travis-ci.org/sberan/Fixy.png?branch=master)](http://travis-ci.org/sberan/Fixy)

 *Winner - 2012 10gen Open Source Hackathon*

 *"Fixy has made my unit-testing life (outside of using Play framework) quite a bit easier" -Fixy User*

Fixy makes unit testing your persistence layer much easier using [SnakeYAML](http://code.google.com/p/snakeyaml/) to
create test fixtures and persist them to your database. It's similar to Rails and Play! Framework's test
fixtures, with a few goodies added such as **package declaration**, **imports**, and **processors**


Supported Persistence Frameworks:
---------------------------------
 - JPA
 - Morphia (MongoDB)


Example (using JPA)
------------
employees.yaml:

    - !package com.innotech

    - Employee(bill):
        firstName: Bill
        lastName: Lumbergh

    - Employee(peter):
        firstName: Peter
        lastName: Gibbons
        manager: Employee(bill)

Now use your fixtures from Java: (assuming you have a transaction active)

    //load fixtures
    Fixy fixtures = new JpaFixyBuilder(entityManager).build();
    fixtures.load("employees.yaml");

    //run query
    Employee peter = entityManager.createNamedQuery("developers", Employee.class).getSingleResult();

    //profit
    assertEquals("Gibbons", peter.getLastName());
    assertEquals("Lumbergh", peter.getManager().getLastName());


Imports
-----------
Fixy allows you to import fixtures between files. You can even refer to entities created in other files.

departments.yaml:

    - !package com.innotech

    - !import employees.yaml

    - Department(development):
        employees:
            - Employee(peter)
            - Employee(samir)
            - Employee(michael)

Package declarations are scoped to the file, and files are guaranteed to only import one time, regardless of how many
times they are declared.

Processors
-------------
Processors allow you to simulate stored procedures and other logic before your entities get persisted.
New entities can be added via the processor as well.

Example:

    Fixy fixtures = new JpaFixyBuilder(entityManager).build();
    fixtures.addProcessor(new Processor<Employee>(Employee.class) {
        public void process(Employee emp) {
            emp.setName(emp.getFirstName() + " " + emp.getLastName());
        }
    });


Installing
---------------
The Fixy jars are located in [My Repository](https://github.com/sberan/mvn-repo/).

If you're using Maven, simply add my Maven repository:

    <repository>
      <id>sberan-github</id>
      <name>sberan-github</name>
      <url>https://raw.github.com/sberan/mvn-repo/master/releases</url>
    </repository>

And the Fixy dependency:

###JPA:

    <dependency>
        <groupId>com.fixy</groupId>
        <artifactId>fixy-jpa</artifactId>
        <version>2.1</version>
    </dependency>


###Hibernate:

    <dependency>
        <groupId>com.fixy</groupId>
        <artifactId>fixy-hibernate</artifactId>
        <version>2.1</version>
    </dependency>


### Morphia:

    <dependency>
        <groupId>com.fixy</groupId>
        <artifactId>fixy-morphia</artifactId>
        <version>2.1</version>
    </dependency>

Contributors
-------------
 - Sam Beran (@sberan)
 - Chris Collison (@collisonchris)
 - Mohamed Mounirou (@mmounirou)
 - Jens Nehlmeier (@jnehlmeier)

Additional Documentation
-----------

See the [JPA unit tests](https://github.com/sberan/Fixy/tree/master/fixy-jpa/src/test) for some great usage examples.
