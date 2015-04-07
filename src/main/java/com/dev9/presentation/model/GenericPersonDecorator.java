package com.dev9.presentation.model;

public class GenericPersonDecorator {

    private final String decorator;

    public GenericPersonDecorator(String decorator) {
        this.decorator = decorator;
    }

    public DecoratedPerson decorate(Person person) {
        return new DecoratedPerson(person, decorator);
    }
}
