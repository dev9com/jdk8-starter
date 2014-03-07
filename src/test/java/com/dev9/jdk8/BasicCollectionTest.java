package com.dev9.jdk8;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

import static org.fest.assertions.api.Assertions.assertThat;

public class BasicCollectionTest {


    int i = 0; // Bad runnable? Exposes side effect here?

    @Test
    public void runnableExample() {

        assertThat(i).isEqualTo(0);

        Runnable r = () -> i++; // Bad runnable? Exposes side effect here?

        r.run();

        assertThat(i).isEqualTo(1);
    }

    @Test
    public void comparatorExample() {
        Comparator<String> c = (a, b) -> a.compareTo(b);

        List<String> names = Lists.newArrayList("alpha", "gamma", "beta", "zeta");

        Collections.sort(names, c);

        assertThat(names).isSorted();
    }

    // Represents a predicate (boolean-valued function) of one argument.
    @Test
    public void predicateExample() {
        Predicate<String> p = (a) -> !(a.isEmpty());

        assertThat(p.test("")).isEqualTo(false);
        assertThat(p.test("something")).isEqualTo(true);
    }


    // Consumer: An action to be performed with the object passed as argument
    @Test
    public void consumerExample() {
        Consumer<Starship> powerUp = (a) -> a.shields++;
        Consumer<Starship> takeHit = (a) -> a.shields--;

        Starship starship = new Starship();

        powerUp.accept(starship);
        powerUp.accept(starship);
        takeHit.accept(starship);

        assertThat(starship.shields).isEqualTo(1);
    }

    //    Function: Transform a T to a U
    @Test
    public void transformFunctionExample() {

        Function<Starship, Salvage> destroy = (a) -> new Salvage(a);

        Starship starship = new Starship("Intrepid");

        Salvage wreck = destroy.apply(starship);

        assertThat(wreck.creator.name).isEqualTo(starship.name);
    }

    @Test
    //    Supplier: Provide an instance of a T (such as a factory)
    public void supplierExample() {

        Supplier<Starship> federation = () -> new Starship("USS Enterprise");
        Supplier<Starship> klingon = () -> new Starship("K'Tinza");

        Starship ship = federation.get();
        assertThat(ship.name).contains("USS");

        ship = klingon.get();
        assertThat(ship.name).contains("K'");
    }

    @Test
    //    UnaryOperator: A unary operator from T -> T
    public void unaryOperatorExample() {

        UnaryOperator<Starship> powerUp = (a) -> {
            a.shields++;
            return a;
        };

        Starship ship = new Starship("Excelsior");

        Starship poweredUp = powerUp.apply(ship);

        assertThat(poweredUp.shields).isEqualTo(1);
    }

    Starship toughGuy = new Starship("Slugger");
    Starship wimpy = new Starship();
    Starship normalGuy = new Starship("Lagrange");

    private List<Starship> getSomeShips() {
        toughGuy.superPower = (a) -> a.shields = a.shields + 5;
        normalGuy.superPower = (a) -> a.shields++;

        return Lists.newArrayList(toughGuy, wimpy, normalGuy);
    }

    @Test
    public void newCollectionForEachWithLambdaExample() {
        List<Starship> ships = getSomeShips();


        // Fancy new collection method
        ships.forEach((ship) -> {
            if (ship.superPower != null)  // No function assigned to wimpy!
                ship.superPower.accept(ship);
        });

        assertThat(toughGuy.shields).isEqualTo(5);

        assertThat(wimpy.shields).isEqualTo(0);

        assertThat(normalGuy.shields).isEqualTo(1);

    }

    @Test
    public void newMethodOperator() {

        List<Starship> ships = getSomeShips();

        ships.forEach((Starship::takeDamage));

        assertThat(toughGuy.shields).isEqualTo(-1);
    }

}
