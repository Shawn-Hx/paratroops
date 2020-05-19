package com.paratroops.gui.util;

import java.net.URL;
import javax.swing.JLayeredPane;

import com.paratroops.App;
import com.paratroops.gui.JSoldier;

/**
 * 地图单元格，一个JLayedPane上面放置一个草地的Picture
 */
public class Block extends JLayeredPane {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    public static final int BLOCK_WIDTH = 100, BLOCK_HEIGHT = 100;

    private static final URL BLOCK_URL = App.class.getClassLoader().getResource("block.jpg");

    private JSoldier soldier = null;

    /**
     * @param x distance left to the left bounder of the whole map
     * @param y distance up to the upper bounder of the whole map
     */
    public Block(int x, int y) {
        this.setBounds(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
        Picture pic = new Picture(BLOCK_URL, 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);
        this.add(pic, Integer.valueOf(0));           // 把草地图片放在最底层
    }

    /**
     * 判断单元格中是否有士兵
     */
    public boolean containsSoldier() {
        return !(soldier == null);
    }

    /**
     * 返回单元格中的士兵对象
     */
    public JSoldier getSoldier() {
        return soldier;
    }

    public void setSoldier(JSoldier soldier) {
        this.soldier = soldier;
    }
}