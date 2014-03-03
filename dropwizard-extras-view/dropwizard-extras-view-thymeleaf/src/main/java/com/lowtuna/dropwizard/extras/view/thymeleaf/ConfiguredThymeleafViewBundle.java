package com.lowtuna.dropwizard.extras.view.thymeleaf;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

public class ConfiguredThymeleafViewBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public TemplateEngine getInstance(T configuration) {
        TemplateEngine templateEngine = new TemplateEngine();

        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(new ClassPathResourceResolver());
        templateEngine.addTemplateResolver(resolver);

        return templateEngine;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        TemplateEngine instance = getInstance(configuration);
        ThymeleafViewRenderer hsbRenderer = new ThymeleafViewRenderer(instance);
        environment.jersey().register(new ViewMessageBodyWriter(environment.metrics(), ImmutableList.<ViewRenderer>of(hsbRenderer)));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        //nothing to do
    }
}
