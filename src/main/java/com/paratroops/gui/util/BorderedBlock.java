package com.paratroops.gui.util;

import com.paratroops.App;

/**
 * 带红/蓝边框的Block对象
 */
public class BorderedBlock extends Block {
    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 边框颜色
     */
    public enum Color {
        BLUE, RED;
    }

    public BorderedBlock(int x, int y, Color color) {
        super(x, y);
        Picture borderPic = null;
        switch(color) {
            case BLUE:
                borderPic = new Picture(App.class.getClassLoader().getResource("borderblue.png"), 0, 0, BLOCK_HEIGHT, BLOCK_WIDTH);
                break;
            case RED:
                borderPic = new Picture(App.class.getClassLoader().getResource("borderred.png"), 0, 0, BLOCK_HEIGHT, BLOCK_WIDTH);
                break;
        }
        if (borderPic != null) {
            this.add(borderPic, Integer.valueOf(0), 0);         // 边框显示在第0层，但位于第0层的草地上方
        }
    }
}