package com.lowtuna.dropwizard.extras.view.twig;

import com.google.common.collect.ImmutableList;
import com.lyncode.jtwig.functions.repository.AbstractFunctionRepository;
import com.lyncode.jtwig.functions.repository.DefaultFunctionRepository;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;

public class ConfiguredTwigViewBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public AbstractFunctionRepository getFunctionRepository(T configuration) {
        return new DefaultFunctionRepository();
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        TwigViewRenderer twigRenderer = new TwigViewRenderer(getFunctionRepository((configuration)));
        environment.jersey().register(new ViewMessageBodyWriter(environment.metrics(), ImmutableList.<ViewRenderer>of(twigRenderer)));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        //nothing to do
    }
}
