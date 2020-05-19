package com.paratroops.gui;

import javax.swing.*;

import com.paratroops.dto.GameDTO;
import com.paratroops.entity.Team;
import com.paratroops.gui.util.Block;
import com.paratroops.util.impl.CipherUtilsImpl;

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
        this.setLayout(null);
        this.setBounds(10, 10, mapSize[1] * Block.BLOCK_WIDTH + 20, mapSize[0] * Block.BLOCK_HEIGHT + 20);

        blocks = new Block[mapSize[0]][mapSize[1]];
        for (int i=0; i<mapSize[0]; ++i) {
            for (int j=0; j<mapSize[1]; ++j) {
                blocks[i][j] = new Block(10 + j * 100, 10 + i * 100);
                
                this.add(blocks[i][j]);
            }
        } 

        // soldiers
        // for (int i=0; i<mapSize[0]; ++i) {
        //     for (int j=0; j<mapSize[1]; ++j) {
        //         if ((i + j) % 2 == 0) {
        //             blocks[i][j].add((new JSoldier(0, CipherUtilsImpl.getInstance(), i, j, Team.RED)).getPicture(), Integer.valueOf(1));
        //         } else {
        //             blocks[i][j].add((new JSoldier(0, CipherUtilsImpl.getInstance(), i, j, Team.BLUE)).getPicture(), Integer.valueOf(1));
        //         }
        //     }
        // } 
    }

    /**
     * 获取某位置的单元格
     * @param x 行坐标
     * @param y 列坐标
     * @return
     */
    public Block getPosition(int x, int y) {
        return blocks[x][y];
    }

    public void clearMap() {
        for (Block[] row: blocks) {
            for (Block block: row) {
                if (block.containsSoldier()) {
                    block.remove(block.getSoldier().getPicture());
                }
            }
        }
    }
}