package com.paratroops.gui;

import java.net.URL;

import javax.swing.*;

import com.paratroops.App;
import com.paratroops.dto.GameDTO;
import com.paratroops.gui.util.Picture;

/**
 * Primary container of GUI
 */
public class WindowPage extends JFrame {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 页面尺寸
     */
    public static final int[] SIZE = {1280, 720};

    private TitlePage titlePage;
    private GamingPage gamingPage;
    private OptionPage optionPage;
    private Picture background;     // 背景图片

    public WindowPage() {
        // 各页面初始化
        GameDTO gameDto = new GameDTO();
        titlePage = new TitlePage(this, gameDto);
        gamingPage = new GamingPage(this, gameDto);
        optionPage = new OptionPage(this, gameDto);
        // 背景图片
        URL bgUrl = App.class.getClassLoader().getResource("background.jpeg");
        background = new Picture(bgUrl, 0, 0, SIZE[0], SIZE[1]);

        this.add(titlePage);
        this.add(gamingPage);
        this.add(optionPage);
        this.add(background);

        this.setLayout(null);
        this.setSize(SIZE[0], SIZE[1]);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("paratroops");
        this.setVisible(true);
        this.toTitle();
    }
    
    /**
     * 进入游戏页
     */
    public void toGame() {
        gamingPage.newGame();
        titlePage.setVisible(false);
        gamingPage.setVisible(true);
        optionPage.setVisible(false);
        background.setVisible(false);
    }

    /**
     * 进入标题页
     */
    public void toTitle() {
        titlePage.setVisible(true);
        gamingPage.setVisible(false);
        optionPage.setVisible(false);
        background.setVisible(true);
    }

    /**
     * 进入设置页
     */
    public void toOption() {
        titlePage.setVisible(false);
        gamingPage.setVisible(false);
        optionPage.setVisible(true);
        background.setVisible(true);
    }
}