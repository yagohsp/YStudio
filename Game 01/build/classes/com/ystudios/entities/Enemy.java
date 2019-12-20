package com.ystudios.entities;

import com.ystudios.main.Game;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 1;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if ((int)x < Game.player.getX()) {
            x += speed;
        } else if ((int)x > Game.player.getX()) {
            x -= speed;
        }

        else if ((int)y < Game.player.getY()) {
            y += speed;
        } else if ((int)y > Game.player.getY()) {
            y -= speed;
        }
    }
}
