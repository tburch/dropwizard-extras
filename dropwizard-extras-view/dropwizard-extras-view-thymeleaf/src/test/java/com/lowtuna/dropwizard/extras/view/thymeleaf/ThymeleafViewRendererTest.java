package com.lowtuna.dropwizard.extras.view.thymeleaf;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import io.dropwizard.logging.LoggingFactory;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.fest.assertions.api.Assertions.assertThat;

public class ThymeleafViewRendererTest extends JerseyTest {
    static {
        LoggingFactory.bootstrap();
    }

    @Path("/test/")
    @Produces(MediaType.TEXT_HTML)
    public static class ExampleResource {
        @GET
        @Path("/absolute")
        public AbsoluteView showAbsolute() {
            return new AbsoluteView("Sally");
        }

        @GET
        @Path("/relative")
        public RelativeView showRelative() {
            return new RelativeView();
        }

        @GET
        @Path("/bad")
        public BadView showBad() {
            return new BadView();
        }

        @GET
        @Path("/map")
        public MapBackedView showMapData() { return new MapBackedView("/mapBackedView.html")
                                                        .put("msg","OK")
                                                        .put("name", "Map Thyme"); }
    }

    @Override
    protected AppDescriptor configure() {
        DefaultResourceConfig config = new DefaultResourceConfig();
        ViewRenderer renderer = new ThymeleafViewRenderer();
        config.getSingletons().add(new ViewMessageBodyWriter(new MetricRegistry(), ImmutableList.of(renderer)));
        config.getSingletons().add(new ExampleResource());
        return new LowLevelAppDescriptor.Builder(config).build();
    }

    @Test
    public void rendersViewsWithAbsoluteTemplatePaths() throws Exception {
        String response = client().resource(getBaseURI() + "test/absolute").get(String.class);
        assertThat(response).isEqualTo("<h1>Woop woop. <span>Hello Sally</span></h1>");
    }

    @Test
    public void rendersViewsWithRelativeTemplatePaths() throws Exception {
        String response = client().resource(getBaseURI() + "test/relative").get(String.class);
        assertThat(response).isEqualTo("<p>Ok.</p>\n");
    }

    @Test
    public void returnsA500ForViewsWithBadTemplatePaths() throws Exception {
        try {
            client().resource(getBaseURI() + "test/bad").get(String.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(500);

            assertThat(e.getResponse().getEntity(String.class))
                    .isEqualTo("<html><head><title>Missing Template</title></head><body><h1>Missing Template</h1><p>/woo-oo-ahh.html</p></body></html>");
        }
    }

    @Test
    public void testMapBackedView() throws Exception
    {
        String response = client().resource(getBaseURI() + "test/map").get(String.class);
        assertThat(response).isEqualTo("<h1><span>Map Thyme</span></h1><h2><span>OK</span></h2>");
    }

}
