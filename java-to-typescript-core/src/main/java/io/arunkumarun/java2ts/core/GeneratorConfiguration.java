package io.arunkumarun.java2ts.core;

import java.net.URLClassLoader;

public class GeneratorConfiguration {
    private final URLClassLoader classLoader;

    public GeneratorConfiguration(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
