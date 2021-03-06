package com.ystudios.graphics;

import com.ystudios.entities.Player;
import com.ystudios.main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class UI {

    public void render(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(7, 13, 50, 8);
        g.setColor(Color.green);
        g.fillRect(7, 13, (int) (Game.player.life / Game.player.maxLife * 50), 8);
        g.setColor(Color.white);
        g.setFont(new Font("roboto", Font.BOLD, 8));
        g.drawString(Game.player.life + "/" + Game.player.maxLife, 10, 10);
    }

    public static int stringSizeWidth(Graphics g, Font font, String string) {
        Rectangle rect = new Rectangle(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(string)) / 2;
        
        return x;
    }
}
