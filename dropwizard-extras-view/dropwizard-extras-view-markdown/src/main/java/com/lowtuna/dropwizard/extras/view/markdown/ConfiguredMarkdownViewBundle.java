package com.lowtuna.dropwizard.extras.view.markdown;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import org.markdown4j.Markdown4jProcessor;

public class ConfiguredMarkdownViewBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public Markdown4jProcessor getInstance(T configuration) {
        return MarkdownViewRenderer.BASIC_MARKDOWN_PROCESSOR;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        Markdown4jProcessor instance = getInstance(configuration);
        MarkdownViewRenderer hsbRenderer = new MarkdownViewRenderer(instance);
        environment.jersey().register(new ViewMessageBodyWriter(environment.metrics(), ImmutableList.<ViewRenderer>of(hsbRenderer)));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        //nothing to do
    }
}
