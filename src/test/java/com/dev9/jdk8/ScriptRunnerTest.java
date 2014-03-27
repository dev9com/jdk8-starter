package com.dev9.jdk8;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ScriptRunnerTest {

    @Test
    public void runScript() {

        ScriptRunner runner = new ScriptRunner();

        runner.eval("print('Hello, World from JS to Java!');");

        System.out.println(runner.eval("'Interesting.'").get().toString());
    }

    @Test
    public void runBadScript() {

        ScriptRunner runner = new ScriptRunner();

        assertThat(runner.lastError).isNull();

        runner.eval("askjdfkd");

        assertThat(runner.lastError).isNotNull();
    }

}
