package com.dev9.jdk8;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.fest.assertions.api.Assertions.assertThat;

public class StreamExamplesTest {

    private static final int SIZE = 1000000;

    List<Integer> integers;

    Predicate<Integer> oddChecker = (x) -> x % 2 != 0;

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Before
    public void setupData() {
        integers = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
            integers.add(i);
    }

    @Test
    public void classicJava() {

        List<Integer> oddOnly = new ArrayList<>();

        for (Integer i : integers) {
            if (i % 2 != 0)
                oddOnly.add(i);
        }

        assertThat(oddOnly.size()).isEqualTo(SIZE / 2);
        //oddOnly.forEach((x) -> System.out.println(x));
    }


    @Test
    public void simpleStreamExample() {

        List<Integer> oddOnly = new ArrayList<>();

        integers.stream().filter(oddChecker).forEach((x) -> oddOnly.add(x));

        assertThat(oddOnly.size()).isEqualTo(SIZE / 2);
        //oddOnly.forEach((x) -> System.out.println(x));
    }


    @Test
    public void badParallelStreamStatefulExample() {

        // Note the use of syncronizedList. Without this, the forEach will break. */
        List<Integer> oddOnly = Collections.synchronizedList(new ArrayList<>());

        // You aren't really supposed to do this - the oddOnly list is considered
        // an external modification.
        integers.parallelStream().filter(oddChecker).forEach((x) -> oddOnly.add(x));

        //System.out.println("randomly ordered:");
        //oddOnly.forEach((x) -> System.out.println(x));
        assertThat(oddOnly.size()).isEqualTo(SIZE / 2);
    }

    @Test
    public void goodParallelStreamStatefulExample() {
        List<Integer> oddOnly = integers
                .parallelStream()
                .filter(oddChecker)
                .collect(Collectors.toList());

        //System.out.println("not randomly ordered");
        //oddOnly.forEach((x) -> System.out.println(x));
        assertThat(oddOnly.size()).isEqualTo(SIZE / 2);
    }

}
