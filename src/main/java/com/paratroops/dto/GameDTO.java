package com.paratroops.dto;

/**
 * 游戏数据对象
 */
public class GameDTO {
    /**
     * 地图尺寸
     */
    private int[] SIZE = {10, 10};

    /**
     * 队伍人数上下限
     */
    public static final int MIN_NUM = 2, MAX_NUM = 19;

    /**
     * 默认初始化的红/蓝方队伍人数
     */
    public static final int DEF_NUM_RED = 11, DEF_NUM_BLUE = 11;
    
    /**
     * 红/蓝方开箱阈值人数上下限
     */
    private int MIN_THRESH = 1, MAX_THRESH_RED = 19, MAX_THRESH_BLUE = 19;

    /**
     * 默认初始化的红/蓝方队伍人数
     */
    public static final int DEF_THRESH_RED = 6, DEF_THRESH_BLUE = 6;

    /**
     * 红/蓝方队伍人数, MIN_NUM <= num <= MAX_NUM
     */
    private int numRed, numBlue;

    /**
     * 红/蓝方开箱阈值人数, MIN_THRESH <= thresh <= MAX_THRESH
     */
    private int threshRed, threshBlue;

    /**
     * 设置默认数据
     */
    public GameDTO() {
        numRed = DEF_NUM_RED;
        numBlue = DEF_NUM_BLUE;
        threshRed = DEF_THRESH_RED;
        threshBlue = DEF_THRESH_BLUE;
    }

    public int[] getSIZE() {
        return SIZE;
    }

    public int getNumRed() {
        return numRed;
    }

    public void setNumRed(int numRed) {
        this.numRed = Math.max(Math.min(numRed, MAX_NUM), MIN_NUM);
        this.MAX_THRESH_RED = numRed;
    }

    public int getNumBlue() {
        return numBlue;
    }

    public void setNumBlue(int numBlue) {
        this.numBlue = Math.max(Math.min(numBlue, MAX_NUM), MIN_NUM);
        this.MAX_THRESH_BLUE = numBlue;
    }

    public int getThreshRed() {
        return threshRed;
    }

    public void setThreshRed(int threshRed) {
        this.threshRed = Math.max(Math.min(threshRed, MAX_THRESH_RED), MIN_THRESH);
    }

    public int getThreshBlue() {
        return threshBlue;
    }

    public void setThreshBlue(int threshBlue) {
        this.threshBlue = Math.max(Math.min(threshBlue, MAX_THRESH_BLUE), MIN_THRESH);
    }
}