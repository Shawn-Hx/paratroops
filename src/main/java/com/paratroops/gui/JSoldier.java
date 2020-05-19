package com.paratroops.gui;

import com.paratroops.entity.Soldier;
import com.paratroops.entity.Team;
import com.paratroops.gui.util.Picture;
import com.paratroops.util.CipherUtils;
import com.paratroops.gui.util.Block;

/**
 * 士兵GUI对象
 */
public class JSoldier extends Soldier {
    /**
     * 在地图上的坐标
     */
    public int posX, posY;

    /**
     * 队伍：红方/蓝方
     */
    public Team team;

    /**
     * 士兵图片对象
     */
    private Picture pic;

    public JSoldier(int rank, CipherUtils cipherUtils, int posX, int posY, Team team) {
        super(rank, cipherUtils);
        this.posX = posX;
        this.posY = posY;
        this.team = team;

        pic = new Picture(team.getResourceURL(), 0, 0, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
    }

    public Picture getPicture() {
        return pic;
    }

    public void setPosX(int x) {
        posX = x;
    }

    public void setPosY(int y) {
        posY = y;
    }
}