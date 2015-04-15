package com.dev9.jdk8.functionalinterface;

public class SquareRootCalculation implements Calculation {

    @Override
    public double calculate(int a) {
        return Math.sqrt(a);
    }
}
