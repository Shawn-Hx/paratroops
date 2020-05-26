package com.paratroops.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.paratroops.App;
import com.paratroops.dto.GameDTO;
import com.paratroops.entity.Soldier;
import com.paratroops.entity.Team;
import com.paratroops.gui.util.Block;
import com.paratroops.gui.util.Picture;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.TroopUtils;
import com.paratroops.util.impl.CipherUtilsImpl;
import com.paratroops.util.impl.TroopUtilsImpl;

/**
 * 游戏页
 */
public class GamingPage extends JPanel {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 每个队伍的士兵数量
     */
    private int numSoldiersEach = 0;

    /**
     * 用来展示军衔排序的timer
     */
    private Timer randTimer = new Timer(1000, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    });

    /**
     * 军衔排序当前展示的Index
     */
    private int shownIndex = 0;

    /**
     * 游戏数据对象
     */
    private GameDTO gameDto;

    /**
     * 游戏地图对象（包括地图上的士兵）
     */
    private Map map;

    private TroopUtils troopUtils = TroopUtilsImpl.getInstance();

    private class RankCompareEachListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //check是不是有两个士兵被选中
            if(map.ifTwoSoldiersSelected()){
                //若是，则进行两两军衔比较
                List<Soldier> soldiers = map.getSelectedTwoSoilders();
                boolean result01 = false;
                boolean result02 = false;
                //check两个士兵是不是同一个阵营
                Soldier soldier0 = soldiers.get(0);
                Soldier soldier1 = soldiers.get(1);
                boolean isSameGroup = map.checkSelectedTwoSoildersIsSameGroup();
                if(!isSameGroup){
                    JOptionPane.showMessageDialog(null, "两个士兵是不同阵营不能比较", "标题",JOptionPane.ERROR_MESSAGE);
                    //将map重置为没有士兵被选中的样子
                    map.resetBlockSelection();
                    return;
                }

                result01 = troopUtils.compareRank(soldier0,soldier1);
                result02 = troopUtils.compareRank(soldier1,soldier0);

                if (result01&&!result02){
                    //如果是第一个选中的士兵军衔高
                    map.firstSolderHasHigherRank();
                }else if(!result01&&result02){
                    //如果是第二个选中的士兵军衔高
                    map.secondSolderHasHigherRand();
                }else{
                    //一样高
                    map.setEqualRank();
                }
                //1000 ms 后恢复原状
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        map.resetRankCompare();
                        //将map重置为没有士兵被选中的样子
                        map.resetBlockSelection();
                    }
                });

                timer.restart();
                timer.setRepeats(false);
//                timer.stop();

            }else{
                // do nothing
                JOptionPane.showMessageDialog(null, "请选中任意两个士兵", "标题",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private class IdentificationEachListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //check是不是有两个士兵被选中
            if(map.ifTwoSoldiersSelected()){
                //若是，则进行两两认证
                List<Soldier> soldiers = map.getSelectedTwoSoilders();
                boolean result = false;
                result = troopUtils.authenticate(soldiers.get(0),soldiers.get(1));
                if (result){
                    //如果是同一阵营，则显示为同一阵营
                    String sameTeamMessage = "同阵营";
                    JOptionPane.showMessageDialog(null, sameTeamMessage);
                    //显示这两个Block的颜色
                    map.authenticationSuccess();
                }else{
                    //如果不是，就提示不是同一阵营
                    String differentTeamMessage = "不同阵营";
                    JOptionPane.showMessageDialog(null, differentTeamMessage, "标题",JOptionPane.ERROR_MESSAGE);
                }
                //将map重置为没有士兵被选中的样子
                map.resetBlockSelection();

            }else{
                // do nothing
                JOptionPane.showMessageDialog(null, "请选中任意两个士兵", "标题",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RankCompareFinalRedListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            shownIndex = 0;
            randTimer.stop();
            map.showAuthenticationResult();

            //收集红方Soldier,然后选举指挥官
            List<Block> redBlocks = map.getRedGroupBlocks();

            List<Soldier> redSoldiers = new ArrayList<Soldier>();


            for (Block block:redBlocks) {
                redSoldiers.add(block.getSoldier());
            }

            Soldier commanderRed = troopUtils.selectLeader(redSoldiers);
            // 首先把指挥官的照片变成对应的加了红旗的

            for (Block block:redBlocks) {
                if(block.getSoldier().equals(commanderRed)) {
                    block.showCommander();
                }
            }

            //troopUtils.sortByRank(redSoldiers);
            //按照军衔顺序依次点亮，selectLeader 函数已经排过序，不需要再进行调用

            randTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    //将格子的选中效果重置
                    if (shownIndex == numSoldiersEach){
                        randTimer.stop();
                        return;
                    }
                    Soldier shownSoldier = redSoldiers.get(shownIndex);
                    for (Block block:redBlocks) {
                        if(block.getSoldier().equals(shownSoldier)) {
                            block.highLightForAWhile();
                            //并且在下面的对应位置显示这个士兵
                            map.showRedSortedSoldier(shownIndex,shownSoldier);
                        }
                    }
                    shownIndex += 1;
                }
            });

            randTimer.start();
            randTimer.setRepeats(true);
        }
    }

    private class RankCompareFinalBlueListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            shownIndex = 0;
            randTimer.stop();
            map.showAuthenticationResult();
            //收集蓝方Soldier,然后选举指挥官
            List<Block> blueBlocks = map.getBlueGroupBlocks();

            List<Soldier> blueSoldiers = new ArrayList<Soldier>();

            for (Block block:blueBlocks) {
                blueSoldiers.add(block.getSoldier());
            }


            Soldier commanderBlue = troopUtils.selectLeader(blueSoldiers);
            // 首先把指挥官的照片变成对应的加了红旗的

            for (Block block:blueBlocks) {
                if(block.getSoldier().equals(commanderBlue)) {
                    block.showCommander();
                }
            }

//            troopUtils.sortByRank(blueSoldiers);
            //按照军衔顺序依次点亮，selectLeader 函数已经排过序，不需要再进行调用

            randTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    //将格子的选中效果重置
                    if (shownIndex == numSoldiersEach){
                        randTimer.stop();
                        return;
                    }
                    Soldier shownSoldier = blueSoldiers.get(shownIndex);
                    for (Block block:blueBlocks) {
                        if(block.getSoldier().equals(shownSoldier)) {
                            block.highLightForAWhile();
                            //并且在上面的对应位置显示这个士兵
                            map.showBlueSortedSoldier(shownIndex,shownSoldier);
                        }
                    }
                    shownIndex += 1;
                }
            });

            randTimer.start();
            randTimer.setRepeats(true);

        }
    }

    private class SelectMultiSoldiers implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            shownIndex = 0;
            randTimer.stop();
//            map.showAuthenticationResult();
            map.showAuthenticationResult();
            //弹出提示，选择多个士兵
            JOptionPane.showMessageDialog(null, "请选择多个士兵来打开补给");
            map.setSelectBlocksToOpenBox();
        }
    }

    /**
     * 打开补给按钮的监听器
     */
    private class OpenBoxListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            //check士兵数量，不能啥都不选
            List <Block> selectedBlocksToOpenBox = map.getSelectedBlocksToOpenBox();
            List <Soldier> selectedSoldiersToOpenBox = new ArrayList<Soldier>();
            for (Block block:selectedBlocksToOpenBox){
                selectedSoldiersToOpenBox.add(block.getSoldier());
            }
            if(selectedSoldiersToOpenBox.size() == 0){
                JOptionPane.showMessageDialog(null, "请至少选中一个", "标题",JOptionPane.ERROR_MESSAGE);
                return;
            }
            int relatedBoxKey = 0;
            if (selectedBlocksToOpenBox.get(0).getSoldier().team == Team.RED){
                relatedBoxKey = gameDto.getRedTeamDTO().getBoxKey();
            }else{
                relatedBoxKey = gameDto.getBlueTeamDTO().getBoxKey();
            }
            boolean canOpenBox = troopUtils.openBox(selectedSoldiersToOpenBox,relatedBoxKey);
            if (canOpenBox){
                //成功开箱就提示能够开箱
                JOptionPane.showMessageDialog(null, "开箱成功");
                //判断一下队伍的颜色，蓝色开head，红色开tai
                URL boxOpenedURL = App.class.getClassLoader().getResource("box_opened.png");
                if (selectedBlocksToOpenBox.get(0).getSoldier().team == Team.RED) {
                    map.getTailPosition(0).remove(0);
                    map.getTailPosition(0).add(new Picture(boxOpenedURL, 0, 0, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT), Integer.valueOf(2));
                }else{
                    map.getHeadPosition(0).remove(0);
                    map.getHeadPosition(0).add(new Picture(boxOpenedURL, 0, 0, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT), Integer.valueOf(2));
                }
                //成功后重置
                map.resetSelectBlocksToOpenBox();
            }else{
                //不成功提示开箱人数不足
                JOptionPane.showMessageDialog(null, "开箱人数不足", "标题",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private class IdentificationFinalListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            shownIndex = 0;
            randTimer.stop();
//            map.showAuthenticationResult();
            map.showAuthenticationResult();
            map.clearSortResult();
        }
    }

    public GamingPage(WindowPage window, GameDTO gameDto) {
        this.gameDto = gameDto;

        JButton returnButton = new JButton("退出");
        JPanel procedurePanel = new JPanel();
        //初始化关键过程的各个button
        JButton identificationEach = new JButton("两两认证");
        identificationEach.addActionListener(new IdentificationEachListener());
        JButton identificationFinal = new JButton("认证结果");
        identificationFinal.addActionListener(new IdentificationFinalListener());
        JButton rankCompareEach = new JButton("军衔比较");
        rankCompareEach.addActionListener(new RankCompareEachListener());
        JButton rankCompareFinalRed = new JButton("红方选举");
        rankCompareFinalRed.addActionListener(new RankCompareFinalRedListener());
        JButton rankCompareFinalBlue = new JButton("蓝方选举");
        rankCompareFinalBlue.addActionListener(new RankCompareFinalBlueListener());
        JButton selectMultiSoldiers = new JButton("选择多个");
        selectMultiSoldiers.addActionListener(new SelectMultiSoldiers());
        JButton openBox = new JButton("打开补给");
        openBox.addActionListener(new OpenBoxListener());
        // 下方控制台
        JTextArea console = new JTextArea("这是控制台，算法输出在这里");
        console.setRows(6);
        JScrollPane scroll = new JScrollPane(console);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

        procedurePanel.setLayout(new GridLayout(7,1));
        procedurePanel.add(identificationEach);
        procedurePanel.add(identificationFinal);
        procedurePanel.add(rankCompareEach);
        procedurePanel.add(rankCompareFinalRed);
        procedurePanel.add(rankCompareFinalBlue);
        procedurePanel.add(selectMultiSoldiers);
        procedurePanel.add(openBox);


        returnButton.addActionListener(e -> {
//            shownIndex = 0;
//            randTimer.stop();
//            map.clearSortResult();
//            map.showAuthenticationResult();
            identificationFinal.doClick();
//            map.resetBlockSelection();
            window.toTitle();
            if (map.getHeadPosition(0).getComponentCount()>1){
                map.getHeadPosition(0).remove(0);
            }
            if( map.getTailPosition(0).getComponentCount()>1){
                map.getTailPosition(0).remove(0);
            }
        });

        this.setBounds(0, 0, WindowPage.SIZE[0], WindowPage.SIZE[1]);
        this.setLayout(new BorderLayout());
        this.add(returnButton, BorderLayout.NORTH);
        this.add(procedurePanel,BorderLayout.EAST);
        this.add(scroll, BorderLayout.SOUTH);
        
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(null);
        map = new Map(gameDto);
        mapPanel.add(map);
        this.add(mapPanel, BorderLayout.CENTER);
    }

    /**
     * 根据{@code gameDto}重新初始化地图
     */
    public void newGame() {
        map.clearMap();                             // 清空地图上的士兵
        gameDto.init();                             // 重新生成队伍数据
        List<JSoldier> allSoldiers = gameDto.getRedTeamDTO().getJSoldierList();
        allSoldiers.addAll(gameDto.getBlueTeamDTO().getJSoldierList());
        placeSoldiers(allSoldiers);                 // 统一计算红蓝两队士兵的初始位置
        URL boxURL = App.class.getClassLoader().getResource("box_closed.png");
        map.getHeadPosition(0).add(new Picture(boxURL, 0, 0, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT), Integer.valueOf(2));
        map.getTailPosition(0).add(new Picture(boxURL, 0, 0, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT), Integer.valueOf(2));
    }

    /**
     * 随机初始化士兵位置并将士兵绘制在该位置上
     */
    private void placeSoldiers(List<JSoldier> soldiers) {
        int numSoldiers = soldiers.size(), i = 0;
        this.numSoldiersEach = numSoldiers/2;
        CipherUtils cipherUtils = CipherUtilsImpl.getInstance();
        int[] size = gameDto.getSIZE();
        List<Integer> posList = cipherUtils.samplePositions(size, numSoldiers);
        for (i=0; i<numSoldiers; ++i) {
            JSoldier soldier = soldiers.get(i);
            int x = posList.get(i) / size[1], y = posList.get(i) % size[1];
            soldier.setPosX(x);
            soldier.setPosY(y);
            Block block = map.getPosition(x, y);
            block.add(soldier.getDefaultPic(),Integer.valueOf(2));
            block.repaint();
            block.setSoldier(soldier);
        }
    }


}