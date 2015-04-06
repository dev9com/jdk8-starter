package com.dev9.presentation.model;

import java.util.function.Function;

public class LadiesPersonDecorator implements Function<Person, DecoratedPerson> {

    public static final String PREFIX = "Mrs";

    @Override public DecoratedPerson apply(Person person) {
        return new DecoratedPerson(person, PREFIX);
    }
}
