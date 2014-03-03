package com.lowtuna.dropwizard.extras.view.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import org.apache.commons.lang3.StringUtils;

public class ConfiguredHandlebarsViewRenderBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public Handlebars getInstance(T configuration) {
        return new Handlebars().with(new ClassPathTemplateLoader(StringUtils.EMPTY, StringUtils.EMPTY)).with(new HighConcurrencyTemplateCache());
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
