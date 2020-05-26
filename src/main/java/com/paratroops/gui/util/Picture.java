package com.paratroops.gui.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Picture extends JPanel {
    private static final long serialVersionUID = 1L;

    Image image;
    
	public Picture(URL url, int x, int y, int w, int h) {
		image = new ImageIcon(url).getImage();
		this.setBounds(x, y, Math.min(image.getWidth(this), w), Math.min(image.getHeight(this), h));
		this.setOpaque(false);			// 上层Picture的像素不会遮挡下层Picture的像素，在JLayedPane里叠加图片需要
    }
    
	public void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, this);
	}
}
