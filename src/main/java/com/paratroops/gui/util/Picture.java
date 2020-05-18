package com.paratroops.gui.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Picture extends JPanel {
    private static final long serialVersionUID = 1L;

    Image image;
    
	public Picture(URL url, int x, int y, int w, int h){
		super();
		image = new ImageIcon(url).getImage();
		this.setBounds(x, y, Math.min(image.getWidth(this), w), Math.min(image.getHeight(this), h));	
    }
    
	public void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, this);
	}
}
