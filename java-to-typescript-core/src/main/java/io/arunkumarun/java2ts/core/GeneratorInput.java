package io.arunkumarun.java2ts.core;

import java.util.List;

public class GeneratorInput {
    private final List<String> includePackages;

    public GeneratorInput(List<String> includePackages) {
        this.includePackages = includePackages;
    }

    public List<String> getIncludePackages() {
        return includePackages;
    }
}
