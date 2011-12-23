
Fixy : Yaml fixtures for JPA
========
![Fixy](https://raw.github.com/sberan/Fixy/master/fixy.jpg)

Fixy makes unit testing your JPA entities much easier using YAML to
create test fixtures and persist them to your database. It's mostly
a rip off of Rails and Play! Framework's test fixtures, with a few
goodies added such as **package declaration**, **imports**, and **processors**



Example
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

Now use your fixtures from Java:

    //load fixtures
    Fixy fixtures = new Fixy(entityManager);
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
Processors allow you to simulate stored procedures and other junk before your entities get persisted.
You can add new entities via the processor as well.

Java Example:

    Fixy fixtures = new Fixy(entityManager);
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
      <url>https://github.com/sberan/mvn-repo/raw/master/releases</url>
    </repository>

And the Fixy dependency:

    <dependency>
        <groupId>com.pearson</groupId>
        <artifactId>fixy</artifactId>
        <version>1.1</version>
    </dependency>


More Info
-----------

For now, have a look at the unit tests. If people start using this I'll add a wiki or something.
