package com.app.topizasport.drawer.menu;

import android.view.Menu;
import android.view.MenuItem;

/**
 * This file is part of the Sport News template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author AppsVilla
 * Copyright 2018
 */
public class SimpleMenu extends SimpleAbstractMenu {

    public SimpleMenu(Menu menu, MenuItemCallback callback){
        super();
        this.menu = menu;
        this.callback = callback;
    }

    public MenuItem add(String title, int drawable, Action action) {
        return add(menu, title, drawable, action);
    }

}
