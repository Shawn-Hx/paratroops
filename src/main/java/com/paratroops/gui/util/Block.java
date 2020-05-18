package com.paratroops.gui.util;

import java.net.URL;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

import com.paratroops.App;

/**
 * 地图单元格
 */
public class Block extends Picture {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    public static final int BLOCK_WIDTH = 100, BLOCK_HEIGHT = 100;

    private static final URL BLOCK_URL = App.class.getClassLoader().getResource("block.jpg");

    public Block(int x, int y) {
        super(BLOCK_URL, x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
    }
}