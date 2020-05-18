package com.paratroops.gui;

import java.awt.*;
import javax.swing.*;

import com.paratroops.dto.GameDTO;
import com.paratroops.gui.util.Block;

/**
 * 游戏地图
 */
public class Map extends JPanel {
    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 地图单元格阵列
     */
    private Block[][] blocks;

    public Map(GameDTO gameDto) {
        int[] mapSize = gameDto.getSIZE();
        // GridLayout grid = new GridLayout(mapSize[0], mapSize[1]);
        // grid.setHgap(0);
        // grid.setVgap(0);
        this.setLayout(null);
        this.setBounds(10, 10, mapSize[1] * Block.BLOCK_WIDTH, mapSize[0] * Block.BLOCK_HEIGHT);

        blocks = new Block[mapSize[0]][mapSize[1]];
        for (int i=0; i<mapSize[0]; ++i) {
            for (int j=0; j<mapSize[1]; ++j) {
                blocks[i][j] = new Block(10 + j * 100, 10 + i * 100);
                this.add(blocks[i][j]);
            }
        }
    }
}