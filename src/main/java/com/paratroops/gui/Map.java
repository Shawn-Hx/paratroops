package com.paratroops.gui;

import javax.swing.*;

import com.paratroops.dto.GameDTO;
import com.paratroops.entity.Soldier;
import com.paratroops.entity.Team;
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

    /**
     * 地图左上角相对于容器在X轴和Y轴方向的偏移
     */
    private static final int PX_OFFSET = 2, PY_OFFSET = 0;

    private static boolean one_block_selected = false;

    private static boolean two_block_selected = false;

    private Soldier first_solder_selected = null;

    private Soldier second_solder_selected = null;

    /**
     * 该部分用于两两认证以及两两军衔比较
     */
    private List<Block> selectedBlocks = new ArrayList<Block>();

    private GameDTO gameDto;

    private boolean select_multi_blocks = false;

    private List<Block> blocksToOpenBox = new ArrayList<Block>();

    /**
     * 首/尾行单元格阵列，第一个单元格放箱子，从第二个开始按军衔高低放士兵
     */
    private Block[] headLine, tailLine;

    /**
     * 地图单元格阵列
     */
    private Block[][] blocks;

    public Map(GameDTO gameDto) {
        this.gameDto = gameDto;
        int[] mapSize = gameDto.getSIZE();
        this.setLayout(null);
        this.setBounds(PX_OFFSET, PY_OFFSET, mapSize[1] * Block.BLOCK_WIDTH, (mapSize[0] + 2) * Block.BLOCK_HEIGHT);

        headLine = new Block[mapSize[1]];
        tailLine = new Block[mapSize[1]];
        blocks = new Block[mapSize[0]][mapSize[1]];
        for (int j=0; j<mapSize[1]; ++j) {
            headLine[j] = new Block(PX_OFFSET + j * Block.BLOCK_WIDTH, PY_OFFSET);
            this.add(headLine[j]);
            tailLine[j] = new Block(PX_OFFSET + j * Block.BLOCK_WIDTH, PY_OFFSET + (mapSize[0] + 1) * Block.BLOCK_WIDTH);
            this.add(tailLine[j]);
        }
        for (int i=0; i<mapSize[0]; ++i) {
            for (int j=0; j<mapSize[1]; ++j) {
                Block temp = new Block(PX_OFFSET + j * Block.BLOCK_WIDTH, PY_OFFSET + (i + 1) * Block.BLOCK_WIDTH);
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
     * 获取地图中某位置的单元格
     * @param x 行坐标
     * @param y 列坐标
     * @return
     */
    public Block getPosition(int x, int y) {
        return blocks[x][y];
    }

    /**
     * 获取首行某位置的单元格
     */
    public Block getHeadPosition(int y) {
        return headLine[y];
    }

    /**
     * 获取尾行某位置的单元格
     */
    public Block getTailPosition(int y) {
        return tailLine[y];
    }

    public void clearMap() {
        for (Block[] row: blocks) {
            for (Block block: row) {
                if (block.containsSoldier()) {
                    block.remove(block.getSoldier().getPicture());
                    block.resetBlock();
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
     * 认证成功则显示这两个BLock应该对应的颜色
     */
    public void authenticationSuccess() {
        Block firstSelected = selectedBlocks.get(0);
        Block secondSelected = selectedBlocks.get(1);
        firstSelected.authenticationSuccess();
        secondSelected.authenticationSuccess();
    }

    /**
     * 一键认证按钮被按下之后，所有有士兵的Block显示它该有的颜色
     */
    public void showAuthenticationResult() {
        int[] mapSize = this.gameDto.getSIZE();

        for (int i=0; i<mapSize[0]; ++i) {
            for (int j=0; j<mapSize[1]; ++j) {
                if(blocks[i][j].containsSoldier()){
                    blocks[i][j].authenticationSuccess();
                }
            }
        }
    }

    /**
     * 返回所有包含蓝色士兵的block
     */
    public List<Block> getBlueGroupBlocks() {
        ArrayList<Block> blueGroupBlocks = new ArrayList<Block>();
        int[] mapSize = this.gameDto.getSIZE();

        for (int i=0; i<mapSize[0]; ++i) {
            for (int j=0; j<mapSize[1]; ++j) {
                if(blocks[i][j].containsSoldier()&&blocks[i][j].getSoldier().team == Team.BLUE){
                    blueGroupBlocks.add(blocks[i][j]);
                }
            }
        }
        return blueGroupBlocks;
    }

    /**
     * 返回所有包含红色士兵的block
     */
    public List<Block> getRedGroupBlocks() {
        ArrayList<Block> blueGroupBlocks = new ArrayList<Block>();
        int[] mapSize = this.gameDto.getSIZE();

        for (int i=0; i<mapSize[0]; ++i) {
            for (int j=0; j<mapSize[1]; ++j) {
                if(blocks[i][j].containsSoldier()&&blocks[i][j].getSoldier().team == Team.RED){
                    blueGroupBlocks.add(blocks[i][j]);
                }
            }
        }
        return blueGroupBlocks;
    }

    /**
     * 将map设置为可以选择多个士兵的状态
     */
    public void setSelectBlocksToOpenBox() {
        this.select_multi_blocks = true;
    }

    /**
     *
     * @return 返回被选中用于开箱的blocks
     */
    public List<Block> getSelectedBlocksToOpenBox() {
        return blocksToOpenBox;
    }

    /**
     * 开箱成功后重置map的状态
     */
    public void resetSelectBlocksToOpenBox() {
        this.select_multi_blocks = false;
        for (Block block: blocksToOpenBox) {
            block.resetSelected();
        }
        blocksToOpenBox.clear();
    }

    /**
     * 检查被选中的两个士兵是不是同一阵营
     * @return
     */
    public boolean checkSelectedTwoSoildersIsSameGroup() {
        Block block0 = this.selectedBlocks.get(0);
        Block block1 = this.selectedBlocks.get(1);
        return (block0.getSoldier().team == block1.getSoldier().team);
    }


    /**
     * Block Listener，用于选中两个士兵(背景变红)
     */
    private class BlockMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Block blockClicked = (Block) e.getSource();
            if (!select_multi_blocks){
                //判断这个格子上是不是有soldier
                //没有就什么都不做
                if(!blockClicked.containsSoldier()){
                    return;
                }else{
                    //check 是不是已经有格子被选中了
                    if (one_block_selected){

                        //进一步判断是否是一样的Soldier,如果一样就取消选择
                        if (selectedBlocks.get(0).equals(blockClicked)){
                            //取消选择
                            blockClicked.resetSelected();
                            one_block_selected = false;
                            first_solder_selected = null;
                            selectedBlocks.remove(0);
                        }else{
                            //那么这个格子也被选中
                            if(two_block_selected){
                                //什么都不做，直到有什么过程结束
                                if(selectedBlocks.get(1).equals(blockClicked)){
                                    //同理取消选择
                                    blockClicked.resetSelected();
                                    two_block_selected = false;
                                    second_solder_selected = null;
                                    selectedBlocks.remove(1);
                                }
                            }else{
                                two_block_selected = true;
                                selectedBlocks.add(blockClicked);
                                //格子变色
                                blockClicked.setSelected();
                                second_solder_selected = blockClicked.getSoldier();
                            }
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
            }else{
                //此时可以选择多个士兵，用于开箱
                if(!blockClicked.containsSoldier()){
                    return;
                }else{
                    //check 是不是同组的，不同组不让选
                    if (blocksToOpenBox.size() == 0){
                        blockClicked.setSelected();
                        blocksToOpenBox.add(blockClicked);
                    }else{
                        boolean isSelected = false;
                        for (Block block:blocksToOpenBox){
                            if (blockClicked.equals(block)) {
                                isSelected = true;
                            }
                        }
                        //每次判断是不是已经选过
                        if (isSelected){
                            JOptionPane.showMessageDialog(null, "不要多次选择同一个人", "标题",JOptionPane.ERROR_MESSAGE);
                        }else{
                            if(blockClicked.getSoldier().team != blocksToOpenBox.get(0).getSoldier().team){
                                JOptionPane.showMessageDialog(null, "不能选择不同队伍的人来开箱", "标题",JOptionPane.ERROR_MESSAGE);
                            }else{
                                blockClicked.setSelected();
                                blocksToOpenBox.add(blockClicked);
                            }
                        }
                    }

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