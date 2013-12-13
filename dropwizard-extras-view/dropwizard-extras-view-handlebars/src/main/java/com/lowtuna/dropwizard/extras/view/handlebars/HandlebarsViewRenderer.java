package com.lowtuna.dropwizard.extras.view.handlebars;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;
import org.apache.commons.lang3.StringUtils;

public class HandlebarsViewRenderer implements ViewRenderer {
    public static final String LOCAL_MODEL_ATTR_NAME = "locale";

    private final Handlebars handlebars;

    public HandlebarsViewRenderer() {
        this.handlebars = new Handlebars().with(new ClassPathTemplateLoader(StringUtils.EMPTY, StringUtils.EMPTY)).with(new HighConcurrencyTemplateCache());
    }

    @Override
    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".hbs") || view.getTemplateName().endsWith(".handlebars");
    }

    @Override
    public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        Template template = handlebars.compile(view.getTemplateName());
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try {
            Context.Builder contextBuilder = Context
                    .newBuilder(view)
                    .combine(LOCAL_MODEL_ATTR_NAME, locale)
                    .resolver(
                            JavaBeanValueResolver.INSTANCE,
                            FieldValueResolver.INSTANCE,
                            MethodValueResolver.INSTANCE
                    );
            template.apply(contextBuilder.build(), writer);
        } finally {
            writer.close();
        }

    }

}
