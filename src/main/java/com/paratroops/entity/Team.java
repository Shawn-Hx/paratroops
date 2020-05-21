package com.paratroops.entity;

import java.net.URL;

import com.paratroops.App;

public enum Team {
    RED, BLUE;

    public URL getResourceURL() {
        switch(this) {
            case RED:
                return App.class.getClassLoader().getResource("RedSoldier_Marine_cartoon_no_bg.png");
            case BLUE:
                return App.class.getClassLoader().getResource("BlueSoldier_Marine_cartoon_no_bg.png");
            default:
                return App.class.getClassLoader().getResource("RedSoldier_Marine_cartoon_no_bg.png");
        }
    }
}