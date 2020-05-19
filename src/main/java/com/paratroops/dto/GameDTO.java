package com.paratroops.dto;

import com.paratroops.entity.Team;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.impl.CipherUtilsImpl;

/**
 * 游戏数据对象
 */
public class GameDTO {
    /**
     * 地图尺寸
     * SIZE[0]: 行数
     * SIZE[1]: 列数
     */
    private int[] SIZE = {6, 12};

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
     * 默认初始化每个队伍最高军衔人数
     */
    public static final int DEF_HIGHEST_RANK = 1;

    /**
     * 红/蓝方队伍人数, MIN_NUM <= num <= MAX_NUM
     */
    private int numRed, numBlue;

    /**
     * 红/蓝方开箱阈值人数, MIN_THRESH <= thresh <= MAX_THRESH
     */
    private int threshRed, threshBlue;

    /**
     * 红/蓝方最高军衔人数
     */
    private int numHighestRankRed, numHighestRankBlue;
    
    /**
     * 红/蓝方队伍数据
     */
    private TeamDTO redTeamDto, blueTeamDto;

    /**
     * 设置默认数据
     */
    public GameDTO() {
        numRed = DEF_NUM_RED;
        numBlue = DEF_NUM_BLUE;
        threshRed = DEF_THRESH_RED;
        threshBlue = DEF_THRESH_BLUE;
        numHighestRankRed = DEF_HIGHEST_RANK;
        numHighestRankBlue = DEF_HIGHEST_RANK;
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

    public TeamDTO getRedTeamDTO() {
        return redTeamDto;
    }

    public TeamDTO getBlueTeamDTO() {
        return blueTeamDto;
    }

    public int getNumHighestRankRed() {
        return numHighestRankRed;
    }

    public void setNumHighestRankRed(int numHighestRankRed) {
        this.numHighestRankRed = numHighestRankRed;
    }

    public int getNumHighestRankBlue() {
        return numHighestRankBlue;
    }

    public void setNumHighestRankBlue(int numHighestRankBlue) {
        this.numHighestRankBlue = numHighestRankBlue;
    }

    /**
     * 初始化红/蓝方队伍数据
     */
    public void init() {
        CipherUtils cipherUtils = CipherUtilsImpl.getInstance();
        redTeamDto = new TeamDTO(Team.RED, numRed, threshRed, numHighestRankRed, cipherUtils);
        blueTeamDto = new TeamDTO(Team.BLUE, numBlue, threshBlue, numHighestRankBlue, cipherUtils);
    }
}