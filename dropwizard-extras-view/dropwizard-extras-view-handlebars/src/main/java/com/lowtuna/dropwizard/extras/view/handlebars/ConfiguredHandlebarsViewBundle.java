package com.lowtuna.dropwizard.extras.view.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;

public class ConfiguredHandlebarsViewBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public Handlebars getInstance(T configuration) {
        return HandlebarsViewRenderer.CLASSPATH_CACHING_HANDLEBARS;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        Handlebars instance = getInstance(configuration);
        HandlebarsViewRenderer hsbRenderer = new HandlebarsViewRenderer(instance);
        environment.jersey().register(new ViewMessageBodyWriter(environment.metrics(), ImmutableList.<ViewRenderer>of(hsbRenderer)));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        //nothing to do
    }
}
