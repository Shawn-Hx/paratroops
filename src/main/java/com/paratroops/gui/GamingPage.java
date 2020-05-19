package com.paratroops.gui;

import java.awt.*;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.paratroops.dto.GameDTO;
import com.paratroops.gui.util.Block;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.impl.CipherUtilsImpl;

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
    
    public GamingPage(WindowPage window, GameDTO gameDto) {
        this.gameDto = gameDto;

        JButton returnButton = new JButton("退出");
        JPanel procedurePanel = new JPanel();
        //初始化关键过程的各个button
        JButton identificationEach = new JButton("两两认证");
        JButton identificationFinal = new JButton("认证结果");
        JButton rankCompareEach = new JButton("军衔比较");
        JButton rankCompareFinal = new JButton("选举结果");
        JButton openBox = new JButton("打开补给");

        procedurePanel.setLayout(new GridLayout(5,1));
        procedurePanel.add(identificationEach);
        procedurePanel.add(identificationFinal);
        procedurePanel.add(rankCompareEach);
        procedurePanel.add(rankCompareFinal);
        procedurePanel.add(openBox);

        returnButton.addActionListener(e -> {
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
            block.add(soldier.getPicture(), Integer.valueOf(1));
            block.setSoldier(soldier);
        }
    }
}