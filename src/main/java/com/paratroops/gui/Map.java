package com.paratroops.gui;

import javax.swing.*;

import com.paratroops.dto.GameDTO;
import com.paratroops.entity.Soldier;
import com.paratroops.gui.util.Block;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

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

    private Soldier first_solder_selected = null;

    private Soldier second_solder_selected = null;

    private List<Block> selectedBlocks = new ArrayList<Block>();


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
     * 将map重置为没有士兵被选中的样子
     */
    public void resetBlockSelection(){
        one_block_selected = false;
        two_block_selected = false;
        first_solder_selected = null;
        second_solder_selected = null;
        for (Block block: selectedBlocks) {
            block.resetSelected();
        }
        selectedBlocks.clear();
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
     * 检验是否有两个士兵被选中
     * @return
     */
    public boolean ifTwoSoldiersSelected(){
        if (one_block_selected && two_block_selected){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 返回被选中的两个士兵
     * @return
     */
    public List<Soldier> getSelectedTwoSoilders(){
        List<Soldier> soldiers = new ArrayList<Soldier>();
        soldiers.add(first_solder_selected);
        soldiers.add(second_solder_selected);
        return soldiers;
    }

    /**
     * 如果是第一个选中的士兵军衔较高
     */
    public void firstSolderHasHigherRank(){
        Block firstSelected = selectedBlocks.get(0);
        Block secondSelected = selectedBlocks.get(1);
        firstSelected.showHigherRankResult();
        secondSelected.showLowerRankResult();
    }

    /**
     * 如果是第二个选中的士兵军衔较高
     */
    public void secondSolderHasHigherRand(){
        Block firstSelected = selectedBlocks.get(0);
        Block secondSelected = selectedBlocks.get(1);
        firstSelected.showLowerRankResult();
        secondSelected.showHigherRankResult();
    }

    /**
     * 恢复被比较的两个Block的样子
     */
    public void resetRankCompare() {
        Block firstSelected = selectedBlocks.get(0);
        Block secondSelected = selectedBlocks.get(1);
        firstSelected.resetRankCompareBlock();
        secondSelected.resetRankCompareBlock();
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
                        selectedBlocks.add(blockClicked);
                        //格子变色
                        blockClicked.setSelected();
                        second_solder_selected = blockClicked.getSoldier();
                    }
                }else{
                    //如果这是第一个
                    one_block_selected = true;
                    selectedBlocks.add(blockClicked);
                    //格子变色
                    blockClicked.setSelected();
                    first_solder_selected = blockClicked.getSoldier();
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