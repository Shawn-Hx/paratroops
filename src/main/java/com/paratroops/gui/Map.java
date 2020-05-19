package com.paratroops.gui;

import javax.swing.*;

import com.paratroops.dto.GameDTO;
import com.paratroops.gui.util.Block;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 游戏地图
 */
public class Map extends JPanel {
    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    private static boolean one_block_selected = false;

    private static boolean two_block_selected = false;

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
                Block temp = new Block(10 + j * 100, 10 + i * 100);
                temp.addMouseListener(new BlockMouseListener());
                blocks[i][j] = temp;
                this.add(blocks[i][j]);
            }
        }
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

    /**
     * Block Listener，用于选中两个士兵(背景变红)
     */
    private class BlockMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Block blockClicked = (Block) e.getSource();
            //判断这个格子上是不是有soldier
            //没有就什么都不做
            if(!blockClicked.containsSoldier()){
                return;
            }else{
                //check 是不是已经有格子被选中了
                if (one_block_selected){
                    //那么这个格子也被选中
                    if(two_block_selected){
                        //什么都不做，直到有什么过程结束
                    }else{
                        two_block_selected = true;
                        //格子变色
                        blockClicked.setSelected();
                    }
                }else{
                    //如果这是第一个
                    one_block_selected = true;
                    //格子变色
                    blockClicked.setSelected();
                }

            }




        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
}