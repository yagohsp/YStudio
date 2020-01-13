package com.ystudios.entities;

import com.ystudios.main.Game;
import com.ystudios.world.Camera;
import com.ystudios.world.Node;
import com.ystudios.world.Vector2i;
import com.ystudios.world.World;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Entity {

    public static BufferedImage LIFE_POTION_EN = Game.spritesheet.getSprite(64, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(32, 0, 16, 16);
    public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(48, 0, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(0, 48, 16, 16);

    protected double x, y;
    private int width, height;

    private final BufferedImage sprite;

    protected List<Node> path;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }

    public void tick() {

    }

    public void followPath(List<Node> path) {
        if (path != null) {
            if (path.size() > 0) {
                Vector2i target = path.get(path.size() - 1).tile;
//                xprev = x;
//                yprev = y;
                if (x < target.x * 16) {
                    x++;
                } else if (x > target.x * 16) {
                    x--;
                }
                if (y < target.y * 16) {
                    y++;
                } else if (y > target.y * 16) {
                    y--;
                }

                if (x == target.x * 16 && y == target.y * 16) {
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    public boolean isColidding(int xNext, int yNext) {
        Rectangle enemyCurrent = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);

        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy e = Game.enemies.get(i);
            if (e == this) {
                continue;
            }
            Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
            if (enemyCurrent.intersects(targetEnemy)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isColidding(Entity ent1, Entity ent2) {
        Rectangle rect1 = new Rectangle(ent1.getX(), ent1.getY(), ent1.getHeight(), ent1.getWidth());
        Rectangle rect2 = new Rectangle(ent2.getX(), ent2.getY(), ent2.getHeight(), ent2.getWidth());

        return rect1.intersects(rect2);
    }
}
