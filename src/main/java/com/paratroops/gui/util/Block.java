package com.paratroops.gui.util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private static final URL BLOCK_SELECTED = App.class.getClassLoader().getResource("block_selected.jpg");

    private static final URL SHOW_HIGHER_RANK_RESULT = App.class.getClassLoader().getResource("Higher_rank.png");

    private static final URL SHOW_LOWER_RANK_RESULT = App.class.getClassLoader().getResource("Lower_rank.png");


    private int x;

    private int y;
    /**
     * @param x distance left to the left bounder of the whole map
     * @param y distance up to the upper bounder of the whole map
     */
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.setBounds(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
        Picture pic = new Picture(BLOCK_URL, 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);
        this.add(pic, Integer.valueOf(0));           // 把草地图片放在最底层
        this.repaint();
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

    /**
     * 将单元格设置为被选中的样子(红色背景)
     */
    public void setSelected(){
        Picture selected_background = new Picture(BLOCK_SELECTED, 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);
        this.add(selected_background,Integer.valueOf(1));
        this.repaint();
    }

    /**
     * 使被选中的格子恢复原来的背景
     */
    public void resetSelected(){
        this.remove(Integer.valueOf(1));
        this.repaint();
    }

    /**
     * 如果该格的士兵军衔更高，则在士兵的大头上显示大
     */
    public void showHigherRankResult(){
        Picture higher_rank_background = new Picture(SHOW_HIGHER_RANK_RESULT,0,0,BLOCK_WIDTH, BLOCK_HEIGHT);
        this.add(higher_rank_background,Integer.valueOf(3));
        this.repaint();
    }

    /**
     * 如果该格的士兵军衔更低，则在士兵的大头上显示小
     */
    public void showLowerRankResult(){
        Picture lower_rank_background = new Picture(SHOW_LOWER_RANK_RESULT,0,0,BLOCK_WIDTH, BLOCK_HEIGHT);
        this.add(lower_rank_background,Integer.valueOf(3));
        this.repaint();
    }


}