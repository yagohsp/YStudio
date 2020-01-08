package com.ystudios.entities;

import com.ystudios.graphics.Spritesheet;
import com.ystudios.main.Game;
import static com.ystudios.main.Game.spritesheet;
import com.ystudios.world.Camera;
import com.ystudios.world.World;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    public boolean left, right, up, down, moved;
    public int dLeft = 0, dRight = 1, direction = dRight;
    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 4;

    public double speed = 1.5;

    public double life = 100, maxLife = 100;
    public boolean isDamaged = false;

    public static int ammo, damageFrames = 0;
    public boolean hasGun = false, isShooting = false;
    public int mx, my;

    private BufferedImage[] rightSprite, leftSprite, rightSpriteDamaged, leftSpriteDamaged;
    private BufferedImage[] rightSpriteGun, leftSpriteGun, rightSpriteDamagedGun, leftSpriteDamagedGun;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        ammo = 0;

        rightSprite = new BufferedImage[5];
        leftSprite = new BufferedImage[5];
        rightSpriteDamaged = new BufferedImage[5];
        leftSpriteDamaged = new BufferedImage[5];

        rightSpriteGun = new BufferedImage[5];
        leftSpriteGun = new BufferedImage[5];
        rightSpriteDamagedGun = new BufferedImage[5];
        leftSpriteDamagedGun = new BufferedImage[5];

        for (int i = 0; i <= 4; i++) {
            rightSprite[i] = Game.spritesheet.getSprite((i * 16), 16, 16, 16);
            leftSprite[i] = Game.spritesheet.getSprite((i * 16), 32, 16, 16);
            rightSpriteDamaged[i] = Game.spritesheet.getSprite((i * 16), 48, 16, 16);
            leftSpriteDamaged[i] = Game.spritesheet.getSprite((i * 16), 64, 16, 16);

            rightSpriteGun[i] = Game.spritesheet.getSprite((i * 16), 80, 16, 16);
            leftSpriteGun[i] = Game.spritesheet.getSprite((i * 16), 96, 16, 16);
            rightSpriteDamagedGun[i] = Game.spritesheet.getSprite((i * 16), 112, 16, 16);
            leftSpriteDamagedGun[i] = Game.spritesheet.getSprite((i * 16), 128, 16, 16);
        }
    }

    @Override
    public void tick() {
        moved = false;
        if (right && World.isFree((int) (x + speed), this.getY())) {
            moved = true;
            direction = dRight;
            this.setX(this.x + speed);
        } else if (left && World.isFree((int) (x - speed), this.getY())) {
            moved = true;
            direction = dLeft;
            this.setX(this.x - speed);
        }

        if (up && World.isFree(this.getX(), (int) (y - speed))) {
            moved = true;
            this.setY(this.y - speed);
        } else if (down && World.isFree(this.getX(), (int) (y + speed))) {
            moved = true;
            this.setY(this.y + speed);
        }

        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 1;
                }
            }
        } else {
            index = 0;
        }

        if (isShooting) {
            isShooting = false;
            if (hasGun && ammo > 0) {
                ammo--;
                double angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));
                double dx = Math.cos(angle);
                double dy = Math.sin(angle);
                if (direction == dRight) {
                    Bullet bullet = new Bullet(this.getX() + 15, this.getY() + 8, 3, 3, null, dx, dy);
                    Game.bullets.add(bullet);
                } else {
                    Bullet bullet = new Bullet(this.getX() - 2, this.getY() + 8, 3, 3, null, dx, dy);
                    Game.bullets.add(bullet);

                }

            }
        }

        if (life <= 0) {
            life = 0;
            Game.gameState = "GAME_OVER";
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * World.TILE_SIZE - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * World.TILE_SIZE - Game.HEIGHT);

        checkCollisionItens();
    }

    public void render(Graphics g) {
        if (!isDamaging()) {
            if (hasGun) {
                if (direction == dRight) {
                    g.drawImage(rightSpriteGun[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                }
                if (direction == dLeft) {
                    g.drawImage(leftSpriteGun[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                }
            } else {
                if (direction == dRight) {
                    g.drawImage(rightSprite[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                }
                if (direction == dLeft) {
                    g.drawImage(leftSprite[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                }
            }
        } else if (hasGun) {
            if (direction == dRight) {
                g.drawImage(rightSpriteDamagedGun[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
            if (direction == dLeft) {
                g.drawImage(leftSpriteDamagedGun[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        } else {
            if (direction == dRight) {
                g.drawImage(rightSpriteDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
            if (direction == dLeft) {
                g.drawImage(leftSpriteDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        }
    }

    public void checkCollisionItens() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if (atual instanceof LifePotion) {
                if (Entity.isColidding(this, atual)) {
                    life += 30;
                    if (life > 100) {
                        life = 100;
                    }
                    Game.entities.remove(i);
                }
            } else {
                if (atual instanceof Ammo) {
                    if (Entity.isColidding(this, atual)) {
                        ammo += 10;
                        Game.entities.remove(i);
                    }
                } else if (atual instanceof Weapon) {
                    if (Entity.isColidding(this, atual)) {
                        hasGun = true;
                        Game.entities.remove(i);
                    }
                }
            }
        }
    }

    public boolean isDamaging() {
        if (isDamaged) {
            isDamaged = false;
            if (damageFrames <= 10) {
                damageFrames++;
                return true;
            } else {
                damageFrames++;
                if (damageFrames >= 20) {
                    damageFrames = 0;
                }
                return false;
            }
        }
        return false;
    }
}
