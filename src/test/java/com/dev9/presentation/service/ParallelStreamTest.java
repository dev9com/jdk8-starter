package com.dev9.presentation.service;


import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;


public class ParallelStreamTest {

    @Test
    public void test_stream_inline() {

        final List<Integer> primes = IntStream.range(2, 1_000_000)
                .boxed()
                .filter(Prime::isPrime)
                .collect(Collectors.toList());

        assertThat(primes.size()).isEqualTo(78498);
    }



    @Test
    public void test_parallel_inline() {

        final List<Integer> primes = IntStream.range(2, 1_000_000)
                .boxed()
                .parallel()
                .filter(Prime::isPrime)
                .collect(Collectors.toList());

        assertThat(primes.size()).isEqualTo(78498);
    }

    @Test
    public void test_parallel_supplied_forkJoinPool() throws ExecutionException, InterruptedException {

        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        List<Integer> primes = forkJoinPool.submit(
                () ->
                        IntStream.range(2, 1_000_000)
                                .parallel()
                                .boxed()
                                .filter(Prime::isPrime)
                                .collect(Collectors.toList())
                ).get();

        assertThat(primes.size()).isEqualTo(78498);
    }


    public static class Prime {
        public static boolean isPrime(int candidate) {
            for (int i = 2; i * i <= candidate; ++i) {
                if (candidate % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
