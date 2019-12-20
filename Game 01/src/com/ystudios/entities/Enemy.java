package com.ystudios.entities;

import com.ystudios.main.Game;
import com.ystudios.world.Camera;
import com.ystudios.world.World;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    public boolean left, right, up, down, moved = false;
    public int dLeft = 0, dRight = 1, direction = dRight;

    private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3, damagedFrames = 0;
    private BufferedImage[] rightSprite, leftSprite, rightSpriteDamaged, leftSpriteDamaged;
    private double speed = 1.3;

    private int life = 5;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        rightSprite = new BufferedImage[4];
        leftSprite = new BufferedImage[4];
        rightSpriteDamaged = new BufferedImage[4];
        leftSpriteDamaged = new BufferedImage[4];

        for (int i = 0; i <= 3; i++) {
            rightSprite[i] = Game.spritesheet.getSprite((i * 16) + 96, 16, 16, 16);
            leftSprite[i] = Game.spritesheet.getSprite((i * 16) + 96, 32, 16, 16);
            rightSpriteDamaged[i] = Game.spritesheet.getSprite((i * 16) + 96, 48, 16, 16);
            leftSpriteDamaged[i] = Game.spritesheet.getSprite((i * 16) + 96, 64, 16, 16);
        }
    }

    public void tick() {
        moved = false;
        if (itsCloseToPlayer(5 * World.TILE_SIZE)) {
            if (!isColiddingWithPlayer()) {
                if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY()) && !isColidding((int) (x + speed), this.getY())) {
                    x += speed;
                    moved = true;
                    direction = dRight;
                } else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY()) && !isColidding((int) (x - speed), this.getY())) {
                    x -= speed;
                    moved = true;
                    direction = dLeft;
                }
                if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed)) && !isColidding(this.getX(), (int) (y + speed))) {
                    y += speed;
                    moved = true;
                } else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed)) && !isColidding(this.getX(), (int) (y - speed))) {
                    y -= speed;
                    moved = true;
                }
            } else {
                if (Game.player.life <= 0) {
//                System.exit(1);
                }
                Game.player.life--;
                Game.player.isDamaged = true;
            }
        }
        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        } else {
            index = 0;
        }
        coliddingWithBullet();
    }

    public void render(Graphics g) {
        if (damagedFrames == 0) {
            if (direction == dRight) {
                g.drawImage(rightSprite[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            } else {
                g.drawImage(leftSprite[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        } else {
            if (direction == dRight) {
                g.drawImage(rightSpriteDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            } else {
                g.drawImage(leftSpriteDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
            damagedFrames--;
        }
    }

    public boolean isColiddingWithPlayer() {
        return Entity.isColidding(this, Game.player);
    }

    public void coliddingWithBullet() {
        for (int i = 0; i < Game.bullets.size(); i++) {
            if (Entity.isColidding(this, Game.bullets.get(i))) {
                Game.bullets.remove(Game.bullets.get(i));
                life--;
                damagedFrames = 5;
                if (life == 0) {
                    Game.enemies.remove(this);
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

    public boolean itsCloseToPlayer(int distance) {
        if (x - Game.player.getX() < distance
                && x - Game.player.getX() > -distance
                && y - Game.player.getY() < distance
                && y - Game.player.getY() > -distance) {
            return true;
        }
        return false;
    }
}
