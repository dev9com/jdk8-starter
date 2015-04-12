package com.dev9.jdk8.functionalinterface;

public class AlternativeCalculation implements Calculation {

    private static final int MAGIC_NUMBER_A = 100;
    private static final int MAGIC_NUMBER_B = 27;
    private static final int MAGIC_NUMBER_C = -1;

    @Override
    public double calculate(int a) {
        return a * MAGIC_NUMBER_A % MAGIC_NUMBER_B;
    }

    @Override
    public double solve(int a) {
        return MAGIC_NUMBER_C;
    }
}
