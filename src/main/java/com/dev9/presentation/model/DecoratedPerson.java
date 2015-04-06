package com.dev9.presentation.model;

public class DecoratedPerson {

    public static final String DEFAULT_DECORATOR = "Dear";
    private final Person person;
    private final String decoration;

    public DecoratedPerson(Person person, String decoration) {
        this.person = person;
        this.decoration = decoration;
    }

    public String getName() {
        return String.format("%s %s %s", decoration, person.getFirstName(), person.getLastName());
    }


    public static DecoratedPerson defaultDecorate(Person person) {
        return new DecoratedPerson(person, DEFAULT_DECORATOR);
    }

    @Override public String toString() {
        return "DecoratedPerson{" +
                "person=" + person +
                ", decoration='" + decoration + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
