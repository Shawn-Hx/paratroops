package com.paratroops.gui;

import java.awt.*;
import javax.swing.*;

import com.paratroops.dto.GameDTO;

/**
 * 标题页（打开游戏的首页）
 */
public class TitlePage extends JPanel {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 页面边界
     */
    private static final int[] BOUNDS = {340, 160, 600, 400};

    /**
     * 界面字体
     */
    private static final Font font = new Font("宋体", Font.BOLD, 36); 
    
    public TitlePage(WindowPage window, GameDTO gameDto) {
        JLabel titleLabel = new JLabel("伞兵集结", JLabel.CENTER);
        titleLabel.setFont(font);

        JButton startButton = new JButton("开始"),
                optionButton = new JButton("设置"),
                exitButton = new JButton("退出");
        startButton.setFont(font);
        optionButton.setFont(font);
        exitButton.setFont(font);

        startButton.addActionListener(e -> {
            window.toGame();
        });

        optionButton.addActionListener(e -> {
            window.toOption();
        });

        exitButton.addActionListener(e -> {
            this.setVisible(false);
			window.dispose();
        });

        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setBounds(BOUNDS[0], BOUNDS[1], BOUNDS[2], BOUNDS[3]);
        this.setLayout(new GridLayout(4, 1));
        this.add(titleLabel);
		this.add(startButton);
		this.add(optionButton);
        this.add(exitButton);
    }
    
}