package com.paratroops.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import com.paratroops.dto.GameDTO;
import com.paratroops.entity.Soldier;
import com.paratroops.gui.util.Block;
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
                boolean result = false;
                result = troopUtils.compareRank(soldiers.get(0),soldiers.get(1));
                if (result){
                    //如果是第一个选中的士兵军衔高
                    map.firstSolderHasHigherRank();
                }else{
                    //如果是第二个选中的士兵军衔高
                    map.secondSolderHasHigherRand();
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

                timer.start();
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

//    private class RankCompareFinalListener implements ActionListener{
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            Soldier commander1 = null;
//            Soldier commander2 = null;
//            // 首先把指挥官的照片变成对应的加了红旗的
//
//        }
//    }

    private class IdentificationFinalListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            map.showAuthenticationResult();
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
//        JButton rankCompareFromGroups = new JButton("选举军官");
        JButton rankCompareFinal = new JButton("选举结果");
        JButton openBox = new JButton("打开补给");

        procedurePanel.setLayout(new GridLayout(5,1));
        procedurePanel.add(identificationEach);
        procedurePanel.add(identificationFinal);
        procedurePanel.add(rankCompareEach);
        procedurePanel.add(rankCompareFinal);
        procedurePanel.add(openBox);

        returnButton.addActionListener(e -> {
            map.resetBlockSelection();
            window.toTitle();
        });

        this.setBounds(0, 0, WindowPage.SIZE[0], WindowPage.SIZE[1]);
        this.setLayout(new BorderLayout());
        this.add(returnButton, BorderLayout.NORTH);
        this.add(procedurePanel,BorderLayout.EAST);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        map = new Map(gameDto);
        buttonPanel.add(map);
        this.add(buttonPanel);
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
    }

    /**
     * 随机初始化士兵位置并将士兵绘制在该位置上
     */
    private void placeSoldiers(List<JSoldier> soldiers) {
        int numSoldiers = soldiers.size(), i = 0;
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
//            block.add(soldier.getPicture(),Integer.valueOf(2));
            block.repaint();
            block.setSoldier(soldier);
        }
    }


}