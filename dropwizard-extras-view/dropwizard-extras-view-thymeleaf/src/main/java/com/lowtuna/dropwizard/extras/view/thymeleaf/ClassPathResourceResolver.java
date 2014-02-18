package com.lowtuna.dropwizard.extras.view.thymeleaf;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import java.io.InputStream;

public class ClassPathResourceResolver implements IResourceResolver {
    public static final String NAME = "CLASSPATH";

    public ClassPathResourceResolver() {
        super();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }
}
