package com.dev9.jdk8.functionalinterface;

@FunctionalInterface
public interface Calculation {

    double calculate(int a);

    default double solve(int a){
        return a;
    }
}
