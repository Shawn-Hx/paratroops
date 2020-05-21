package com.paratroops.entity;

import java.net.URL;

import com.paratroops.App;

public enum Team {
    RED, BLUE;

    /**
     * 根据队伍和军衔选择对应的图片资源
     */
    public URL getResourceURL(int rank) {
        String srcUrlString = new String();
        switch(this) {
            case RED:
                srcUrlString = "RedSoldier_rank_" + String.valueOf(rank) + ".png";
                break;
            case BLUE:
                srcUrlString = "BlueSoldier_rank_" + String.valueOf(rank) + ".png";
                break;
        }  
        // srcUrlString = "RedSoldier_Marine_cartoon_no_bg.png"; 
        return App.class.getClassLoader().getResource(srcUrlString);
    }
}