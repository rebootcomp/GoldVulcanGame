package com.app.lionnews.drawer.menu;

import java.io.Serializable;

/**
 * This file is part of the Sport News template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author AppsVilla
 * Copyright 2018
 */
public class Action implements Serializable{

    public String name;
    public String url;

    public Action(String name, String url){
        this.name = name;
        this.url = url;
    }

}
