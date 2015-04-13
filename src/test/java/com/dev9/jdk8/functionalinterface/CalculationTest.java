package com.dev9.jdk8.functionalinterface;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculationTest {

    private static final int TEST_VALUE = 5;
    private Calculation randomCalculation;
    private Calculation alternativeCalculation;
    private Calculation squareRootCalculation;

    @Before
    public void setUp(){

        randomCalculation = new RandomCalculation();
        alternativeCalculation = new AlternativeCalculation();
        squareRootCalculation = new SquareRootCalculation();
    }

    @Test
    public void calculateVariesBetweenRandomAndAlternative() {

        double randomResult = randomCalculation.calculate(TEST_VALUE);
        double alternativeResult = alternativeCalculation.calculate(TEST_VALUE);

        assertThat(randomResult).isNotEqualTo(alternativeResult);
    }

    @Test
    public void calculateVariesBetweenRandomAndSquareRoot() {

        double randomResult = randomCalculation.calculate(TEST_VALUE);
        double squareRootResult = squareRootCalculation.solve(TEST_VALUE);

        assertThat(randomResult).isNotEqualTo(squareRootResult);
    }

    @Test
    public void calculateVariesBetweenAlternativeAndSquareRoot() {

        double randomResult = randomCalculation.calculate(TEST_VALUE);
        double alternativeResult = alternativeCalculation.calculate(TEST_VALUE);

        assertThat(randomResult).isNotEqualTo(alternativeResult);
    }

    @Test
    public void solveIsCustomizedInAlternativeCalculation(){

        double randomResult = randomCalculation.solve(TEST_VALUE);
        double alternativeResult = alternativeCalculation.solve(TEST_VALUE);
        double squareRootResult = squareRootCalculation.solve(TEST_VALUE);

        assertThat(alternativeResult).isNotEqualTo(randomResult);
        assertThat(alternativeResult).isNotEqualTo(squareRootResult);
    }

    @Test
    public void solveIsDefaultForRandomAndSquareRoot(){

        double randomResult = randomCalculation.solve(TEST_VALUE);
        double squareRootResult = squareRootCalculation.solve(TEST_VALUE);

        assertThat(squareRootResult).isEqualTo(randomResult);
    }

    @Test
    public void calculationDefinedInLambda(){

        Calculation lambdaCalculation = (int i) -> i + 5;

        assertThat(lambdaCalculation.calculate(5)).isEqualTo(10);
    }
}