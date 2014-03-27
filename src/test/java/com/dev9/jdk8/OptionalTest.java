package com.dev9.jdk8;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;


/** Great writeup of Optional can be found here:
 *
 * http://blog.informatech.cr/2013/04/10/java-optional-objects/
 */
public class OptionalTest {

    /**
     * Optional provides a nicer syntax for dealing with nullable return values...
     */
    @Test
    public void optionalExample() {
        assertThat(Starship.findShip("Enterprise").isPresent()).isEqualTo(true);

        assertThat(Starship.findShip("Haberdasher").isPresent()).isEqualTo(false);

        assertThat(Starship.findShip("Enterprise").get().name).isEqualTo("Enterprise");
    }

    /**
     * But alas, does not completely protect you from runtime exceptions.
     */
    @Test(expected = java.util.NoSuchElementException.class)
    public void optionalFailureExample() {

        assertThat(Starship.findShip("Haberdasher").get().name).isEqualTo("Haberdasher");
    }
}
