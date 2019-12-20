package com.ystudios.entities;

import com.ystudios.main.Game;
import com.ystudios.world.Camera;
import com.ystudios.world.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet extends Entity {

    private double dx, dy, speed = 4;
    private int distance = 30;

    public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    public void tick() {
        x += dx * speed;
        y += dy * speed;
        distance--;
        if (distance == 0) {
            Game.bullets.remove(this);
        }

//        if (!(World.isFree((int) (x + speed), this.getY()) && World.isFree((int) (x - speed), this.getY()) && World.isFree(this.getX(), (int) (y + speed)) && World.isFree(this.getX(), (int) (y - speed)))) {
//            Game.bullets.remove(this);
//        }
    }

    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
    }
}
