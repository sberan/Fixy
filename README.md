Fixy
========

    YAML test fixtures for JPA. Because sometimes you need to test your queries.



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
    Employee peter = entityManager.createNamedQuery("developers").getSingleResult();

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

Processors
-------------
Processors allow simulation of stored procedures and other junk. You can add new entities via the processor as well.

Java Example:

    Fixy fixtures = new Fixy(entityManager);
    fixtures.addProcessor(new Processor<Employee>(Employee.class) {
        public void process(Employee emp) {
            emp.setName(emp.getFirstName() + " " + emp.getLastName());
        }
    });




More Info
-----------

For now, have a look at the unit tests. If people start using this I'll add a wiki or something.