package com.lowtuna.dropwizard.extras.view.thymeleaf;

import io.dropwizard.views.View;

import java.util.HashMap;
import java.util.Map;

public class MapBackedView extends View
{
    protected Map<String,Object> map;

    public MapBackedView(String template){
        super(template);
        map  = new HashMap<String,Object>();
    }

    public MapBackedView(String template, Map<String,Object> map){
        super(template);
        this.map = map;
    }

    public MapBackedView put(String k, Object v){
        map.put(k,v);
        return this;
    }

    Map<String,Object> getMap(){
        return map;
    }
}

