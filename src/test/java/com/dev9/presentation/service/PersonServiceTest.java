package com.dev9.presentation.service;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;

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
        Person person = personService.getPerson(PersonService.ID_1).get();

        String formattedName = person.getName(
            new Function<Person, String>() {
                @Override public String apply(Person person) {
                    return String.format("%s %s %s", "Mrs",
                            person.getFirstName(), person.getLastName());
                }
            }
        );

        assertThat(formattedName).startsWith("Mrs");
    }

    // Decorator class with static method
    public static class DefaultDecorator {
        public static final String DEFAULT_DECORATOR = "Dear";
        static String defaultDecorate(Person person) {
            return String.format("%s %s %s", DEFAULT_DECORATOR,
                    person.getFirstName(), person.getLastName());
        }
    }

    @Test
    public void getDecoratedPerson_static_method_reference() {
        Person person = personService.getPerson(PersonService.ID_1).get();

        String formattedName = person.getName(
                DefaultDecorator::defaultDecorate); // <- static method reference

        assertThat(formattedName).startsWith(DefaultDecorator.DEFAULT_DECORATOR);
    }

    @Test
    public void getDecoratedPerson_lambda() {
        Person person = personService.getPerson(PersonService.ID_1).get();

        String formattedName = person.getName(
                p ->  // <- lambda
                        String.format("SIR %s %s", p.getFirstName(), p.getLastName()));

        assertThat(formattedName).startsWith("SIR");
    }

    // generic decorator class
    public static class GenericDecorator {
        private final String decorator;

        public GenericDecorator(String decorator) {
            this.decorator = decorator;
        }

        public String decorate(Person person) {
            return String.format("%s %s %s", decorator, person.getFirstName(), person.getLastName());
        }
    }

    @Test
    public void getDecoratedPerson_instance_method_refrence() {

        Person person = personService.getPerson(PersonService.ID_1).get();
        GenericDecorator madameDecorator = new GenericDecorator("MADAME");

        String formattedName =
                person.getName(madameDecorator::decorate); // <- instance method reference

        assertThat(formattedName).startsWith("MADAME");
    }


    // Streams and exceptions
    @Test
    public void getPerson_stream_throws_exception() {

//        List<CallResult<Person>> peopleResults = LongStream.range(1, 3)
//                .boxed()
//                .map(id -> personService.getPersonThrowsCheckedException(id))
//                .collect(Collectors.toList());
    }

    @Test
    public void getPerson_stream_no_exception() {

//        List<CallResult<Person>> peopleResults = LongStream.range(1, 5)
//                .boxed()
//                .map(personService::getPerson)
//                .collect(Collectors.toList());
//
//        List<Person> people = peopleResults
//                .stream()
//                .allMatch(cr -> cr.isError())

    }



}
