package com.lowtuna.dropwizard.extras.view.twig;

import io.dropwizard.views.View;

public class AbsoluteView extends View {
    private final String name;

    public AbsoluteView(String name) {
        super("/example.twig");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
