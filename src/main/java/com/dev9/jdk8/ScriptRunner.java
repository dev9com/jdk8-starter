package com.dev9.jdk8;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Optional;

public class ScriptRunner {

    ScriptException lastError;

    ScriptEngineManager factory = new ScriptEngineManager();

    public Optional<Object> eval(String script) {

        // create a Nashorn script engine
        ScriptEngine engine = factory.getEngineByName("nashorn");

        // Have to clear error before re-use.
        if(lastError != null)
            return Optional.empty();

        // evaluate JavaScript statement.
        try {
            // Note that Optional is not part of the engine.eval, so we have to retrofit...
            Object result = engine.eval(script);

            if (result == null)
                return Optional.empty();
            else
                return Optional.of(result);
        } catch (final ScriptException se) {
            lastError = se;
        }

        return Optional.empty();
    }
}
