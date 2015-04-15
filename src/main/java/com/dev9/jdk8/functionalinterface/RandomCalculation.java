package com.dev9.jdk8.functionalinterface;

public class RandomCalculation implements Calculation {

    @Override
    public double calculate(int a) {
        return Math.random();
    }
}
