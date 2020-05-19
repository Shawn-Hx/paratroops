package com.paratroops.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.paratroops.dto.GameDTO;

/**
 * 游戏页
 */
public class GamingPage extends JPanel {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    private GameDTO gameDto;
    
    public GamingPage(WindowPage window, GameDTO gameDto) {
        this.gameDto = gameDto;

        JButton returnButton = new JButton("退出");

        returnButton.addActionListener(e -> {
            window.toTitle();
        });

        this.setBounds(0, 0, WindowPage.SIZE[0], WindowPage.SIZE[1]);
        this.setLayout(new BorderLayout());
        this.add(returnButton, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        Map map = new Map(gameDto);
        buttonPanel.add(map);
        this.add(buttonPanel);
    }

    /**
     * 根据{@code gameDto}重新初始化地图
     */
    public void newGame() {
        
    }
}