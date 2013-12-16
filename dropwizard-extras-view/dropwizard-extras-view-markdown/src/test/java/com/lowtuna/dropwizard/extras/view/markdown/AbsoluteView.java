package com.lowtuna.dropwizard.extras.view.markdown;

import io.dropwizard.views.View;

public class AbsoluteView extends View {
    private final String name;

    public AbsoluteView(String name) {
        super("/example.md");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
