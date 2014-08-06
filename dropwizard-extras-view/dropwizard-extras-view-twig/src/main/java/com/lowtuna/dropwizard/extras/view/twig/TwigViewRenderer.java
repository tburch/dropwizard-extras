package com.lowtuna.dropwizard.extras.view.twig;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.functions.repository.AbstractFunctionRepository;
import com.lyncode.jtwig.functions.repository.DefaultFunctionRepository;
import com.lyncode.jtwig.tree.api.Content;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TwigViewRenderer implements ViewRenderer {
    private final LoadingCache<View, Content> templateCache;
    private final AbstractFunctionRepository functionRepository;

    public TwigViewRenderer() {
        this(new DefaultFunctionRepository());
    }

    public TwigViewRenderer(AbstractFunctionRepository functionRepository) {
        this.functionRepository = functionRepository;

        this.templateCache = CacheBuilder.newBuilder()
                .build(new CacheLoader<View, Content>() {
                    @Override
                    public Content load(View key) throws Exception {
                        InputStream is = getClass().getResourceAsStream(key.getTemplateName());
                        if (is == null) {
                            throw new FileNotFoundException("Template " + key.getTemplateName() + " not found");
                        }
                        try {
                            StringWriter writer = new StringWriter();
                            IOUtils.copy(is, writer);
                            JtwigTemplate template = new JtwigTemplate(writer.toString());
                            return template.compile();
                        } finally {
                            is.close();
                        }
                    }
                });
    }

    @Override
    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".twig");
    }

    @Override
    public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        try {
            Content template = templateCache.get(view);
            ViewBackedJtwigModelMap modelMap = new ViewBackedJtwigModelMap(view);
            JtwigContext context = new JtwigContext(modelMap, functionRepository);
            template.render(output, context);
        } catch (ExecutionException e) {
            if (FileNotFoundException.class.equals(e.getCause().getClass())) {
                throw new FileNotFoundException(view.getTemplateName());
            }
        } catch (RenderException e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static class ViewBackedJtwigModelMap extends JtwigModelMap {
        public ViewBackedJtwigModelMap(View view) {
            processFields(view, view.getClass());
        }

        private void processFields(View view, Class<?> clazz) {
            if (clazz == View.class) { //we want to stop the recursion at View because we don't want it's properties
                return;
            }

            Class superClazz = clazz.getSuperclass();
            if (superClazz != null){
                processFields(view, superClazz);
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(view);
                    add(field.getName(), value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Couldn't access field " + field.getName() + " in class " + clazz.getCanonicalName(), e);
                }
            }
        }
    }


}
