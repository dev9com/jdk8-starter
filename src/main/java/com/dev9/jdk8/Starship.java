package com.dev9.jdk8;


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

}
