package com.paratroops.gui;

import java.awt.*;
import javax.swing.*;

import com.paratroops.dto.GameDTO;

/**
 * 设置页
 */
public class OptionPage extends JPanel {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 页面边界
     */
    private static final int[] BOUNDS = {140, 160, 1000, 400};

    /**
     * 界面字体
     */
    private static final Font   titleFont = new Font("宋体", Font.BOLD, 36),
                                textFont = new Font("宋体", Font.PLAIN, 24); 
    
    public OptionPage(WindowPage window, GameDTO gameDto) {
        JLabel titleLabel = new JLabel("设置", JLabel.CENTER);
        titleLabel.setFont(titleFont);

        JPanel  numRedPanel = new JPanel(new GridLayout(1, 2)),
                numBluePanel = new JPanel(new GridLayout(1, 2)),
                threshRedPanel = new JPanel(new GridLayout(1, 2)),
                threshBluePanel = new JPanel(new GridLayout(1, 2));

        JLabel  numRedLabel = new JLabel("  红方队员(nr, " + String.valueOf(GameDTO.MIN_NUM) + " <= nr <= " + String.valueOf(GameDTO.MAX_NUM) + "): "),
                numBlueLabel = new JLabel("  蓝方队员(nb, " + String.valueOf(GameDTO.MIN_NUM) + " <= nb <= " + String.valueOf(GameDTO.MAX_NUM) + "): "),
                threshRedLabel = new JLabel("  红方开箱阈值人数(tr, 1 <= tr <= nr): "),
                threshBlueLabel = new JLabel("  蓝方开箱阈值人数(tb, 1 <= tb <= nb): ");
        numRedLabel.setFont(textFont);
        numBlueLabel.setFont(textFont);
        threshRedLabel.setFont(textFont);
        threshBlueLabel.setFont(textFont);

        JTextField  numRedText = new JTextField(String.valueOf(GameDTO.DEF_NUM_RED)),
                    numBlueText = new JTextField(String.valueOf(GameDTO.DEF_NUM_BLUE)),
                    threshRedText = new JTextField(String.valueOf(GameDTO.DEF_THRESH_RED)),
                    threshBlueText = new JTextField(String.valueOf(GameDTO.DEF_THRESH_BLUE));
        numRedText.setFont(textFont);
        numBlueText.setFont(textFont);
        threshRedText.setFont(textFont);
        threshBlueText.setFont(textFont);
        
        numRedPanel.add(numRedLabel);
        numRedPanel.add(numRedText);
        numBluePanel.add(numBlueLabel);
        numBluePanel.add(numBlueText);
        threshRedPanel.add(threshRedLabel);
        threshRedPanel.add(threshRedText);
        threshBluePanel.add(threshBlueLabel);
        threshBluePanel.add(threshBlueText);

        JButton saveButton = new JButton("确认");
        saveButton.setFont(textFont);

        saveButton.addActionListener(e -> {
            // 保存参数
            gameDto.setNumRed(Integer.parseInt(numRedText.getText()));
            gameDto.setNumBlue(Integer.parseInt(numBlueText.getText()));
            gameDto.setThreshRed(Integer.parseInt(threshRedText.getText()));
            gameDto.setThreshBlue(Integer.parseInt(threshBlueText.getText()));

            // 退出设置页
            window.toTitle();
        });

        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setBounds(BOUNDS[0], BOUNDS[1], BOUNDS[2], BOUNDS[3]);
        this.setLayout(new GridLayout(6, 1));
        this.add(titleLabel);
		this.add(numRedPanel);
		this.add(numBluePanel);
        this.add(threshRedPanel);
        this.add(threshBluePanel);
        this.add(saveButton);
    }
}