package com.dev9.jdk8;


import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Starship {

    public int shields = 0;
    public String name;

    public Starship()
    {
        name = "Unknown";
    }

    public Starship(String name)
    {
        this.name = name;
    }

    Consumer<Starship> superPower;

    public void takeDamage()
    {
        this.shields--;
    }

    public static Optional<Starship> findShip(String name)
    {
        if("Enterprise".equals(name))
            return Optional.of(new Starship(name));

        if("Serenity".equals(name))
            return Optional.of(new Starship(name));

        return Optional.empty();
    }

}
