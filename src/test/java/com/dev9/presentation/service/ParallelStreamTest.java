package com.dev9.presentation.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


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
        public static boolean isPrime(int n) {
            if (n <= 3) {
                return n > 1;
            } else if (n % 2 == 0 || n % 3 == 0) {
                return false;
            } else {
                double sqrtN = Math.floor(Math.sqrt(n));
                for (int i = 5; i <= sqrtN; i += 6) {
                    if (n % i == 0 || n % (i + 2) == 0) {
                        return false;
                    }
                }
                return true;
            }

        }
    }
}
