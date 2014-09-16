package com.lowtuna.dropwizard.extras.view.thymeleaf;

import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.ws.rs.WebApplicationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.Map;

public class ThymeleafViewRenderer implements ViewRenderer {
    private final TemplateEngine templateEngine;

    public ThymeleafViewRenderer() {
        TemplateEngine templateEngine = new TemplateEngine();

        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(new ClassPathResourceResolver());
        templateEngine.addTemplateResolver(resolver);

        this.templateEngine = templateEngine;
    }

    public ThymeleafViewRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".html") ||
                view.getTemplateName().endsWith(".xhtml") ||
                view.getTemplateName().endsWith(".xml");
    }

    @Override
    public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        AbstractContext context = new Context(locale);

        if(view instanceof MapBackedView)
        {
            for(Map.Entry<String,Object> entry : ((MapBackedView)view).getMap().entrySet() )
            {
                context.setVariable(entry.getKey(), entry.getValue());
            }

        }
        else
        {
            context.setVariable(view.getClass().getSimpleName(), view);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);

        try {
            templateEngine.process(view.getTemplateName(), context, outputStreamWriter);
        } catch (TemplateProcessingException e) {
            if (e instanceof TemplateInputException) {
                throw new FileNotFoundException(view.getTemplateName());
            }
            throw e;
        } finally {
            outputStreamWriter.close();
        }
    }

}
