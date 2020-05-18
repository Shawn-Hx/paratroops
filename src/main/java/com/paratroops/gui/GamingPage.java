package com.paratroops.gui;

import java.awt.*;
import javax.swing.*;

import com.paratroops.dto.GameDTO;

/**
 * 游戏页
 */
public class GamingPage extends JPanel {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    private JLabel gameLabel;

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

        gameLabel = new JLabel("Game Picture\rnumRed: " + String.valueOf(gameDto.getNumRed()) + "\rnumBlue: " + String.valueOf(gameDto.getNumBlue()) + "\rthreshRed: " + String.valueOf(gameDto.getThreshRed()) + "\rthreshBlue: " + String.valueOf(gameDto.getThreshBlue()));
        this.add(gameLabel, BorderLayout.CENTER);
    }

    public void newGame() {
        gameLabel.setText("Game Picture\rnumRed: " + String.valueOf(gameDto.getNumRed()) + "\rnumBlue: " + String.valueOf(gameDto.getNumBlue()) + "\rthreshRed: " + String.valueOf(gameDto.getThreshRed()) + "\rthreshBlue: " + String.valueOf(gameDto.getThreshBlue()));
    }
}