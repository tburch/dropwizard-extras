package com.lowtuna.dropwizard.extras.view.markdown;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;
import org.apache.commons.io.IOUtils;
import org.markdown4j.Markdown4jProcessor;

import javax.ws.rs.WebApplicationException;
import java.io.*;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MarkdownViewRenderer implements ViewRenderer {
    private final LoadingCache<View, String> templateCache;
    private final Markdown4jProcessor processor;

    public MarkdownViewRenderer() {
        this(new Markdown4jProcessor());
    }

    public MarkdownViewRenderer(Markdown4jProcessor processor) {
        this.processor = processor;
        this.templateCache = CacheBuilder.newBuilder()
                .build(new CacheLoader<View, String>() {

                    @Override
                    public String load(View key) throws Exception {
                        InputStream is = getClass().getResourceAsStream(key.getTemplateName());
                        if (is == null) {
                            throw new FileNotFoundException("Template " + key.getTemplateName() + " not found");
                        }
                        try {
                            StringWriter writer = new StringWriter();
                            IOUtils.copy(is, writer);
                            return writer.toString();
                        } finally {
                            is.close();
                        }
                    }
                });
    }

    @Override
    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".md") || view.getTemplateName().endsWith(".markdown");
    }

    @Override
    public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        try {
            String template = templateCache.get(view);
            Writer writer = new PrintWriter(output);
            try {
                writer.write(processor.process(template));
            } finally {
                writer.close();
            }
        } catch (ExecutionException e) {
            if (FileNotFoundException.class.equals(e.getCause().getClass())) {
                throw new FileNotFoundException(view.getTemplateName());
            }
        }
    }

}
