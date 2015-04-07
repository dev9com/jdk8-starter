package com.dev9.presentation.service;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.dev9.presentation.model.DecoratedPerson;
import com.dev9.presentation.model.GenericPersonDecorator;
import com.dev9.presentation.model.LadiesPersonDecorator;
import com.dev9.presentation.model.Person;


public class PersonServiceTest {

    PersonService personService = new PersonService();

    @Test
    public void getPerson_in_repository() {
        Person person = personService.getPerson(PersonService.ID_1).get();
        assertThat(person.getFirstName()).isEqualTo(PersonService.FIRST_NAME_1);
    }

    @Test
    public void getDecoratedPerson_interface_implementation() {
        DecoratedPerson decoratedPerson = personService.getDecoratedPerson(PersonService.ID_2, new LadiesPersonDecorator()).get();
        assertThat(decoratedPerson.getName()).startsWith(LadiesPersonDecorator.PREFIX);
        System.out.println("Interface implementation:" + decoratedPerson);
    }

    @Test
    public void getDecoratedPerson_static_method_reference() {
        DecoratedPerson decoratedPerson = personService.getDecoratedPerson(PersonService.ID_1, DecoratedPerson::defaultDecorate).get();
        assertThat(decoratedPerson.getName()).startsWith(DecoratedPerson.DEFAULT_DECORATOR);
        System.out.println("Static method reference:" + decoratedPerson);
    }

    @Test
    public void getDecoratedPerson_lambda() {
        DecoratedPerson decoratedPerson = personService.getDecoratedPerson(PersonService.ID_1,
                (person) -> new DecoratedPerson(person, "SIR")).get();
        assertThat(decoratedPerson.getName()).startsWith("SIR");
        System.out.println("Lambda passed in:" + decoratedPerson);
    }

    @Test
    public void getDecoratedPerson_instance_method_refrence() {
        DecoratedPerson decoratedPerson = personService.getDecoratedPerson(PersonService.ID_2,
                new GenericPersonDecorator("MADAME")::decorate).get();
        assertThat(decoratedPerson.getName()).startsWith("MADAME");
        System.out.println("Instance method reference:" + decoratedPerson);
    }
}
