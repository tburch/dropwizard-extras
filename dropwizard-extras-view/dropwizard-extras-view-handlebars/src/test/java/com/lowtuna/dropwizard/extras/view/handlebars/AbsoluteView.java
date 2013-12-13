package com.lowtuna.dropwizard.extras.view.handlebars;

import io.dropwizard.views.View;

public class AbsoluteView extends View {
    private final String name;

    public AbsoluteView(String name) {
        super("/example.handlebars");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
