package com.ystudios.entities;

import com.ystudios.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public boolean left, right, up, down, moved = false;
    public double speed = 1.5;
    public int dLeft = 0, dRight = 1, direction = dRight;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 4;
    private BufferedImage[] rightPlayer, leftPlayer;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[5];
        leftPlayer = new BufferedImage[5];

        for (int i = 0; i <= 4; i++) {
            leftPlayer[i] = Game.spritesheet.getSprite((i * 16), 16, 16, 16);
        }
        for (int i = 0; i <= 4; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite((i * 16), 0, 16, 16);
        }

    }

    public void tick() {
        moved = false;
        if (right) {
            if (this.getX() < 160) {
                moved = true;
                direction = dRight;
                this.setX(this.x + speed);
            }else{
                moved = true;
                direction = dLeft;
                this.setX(-16);
            }
        } else if (left) {
            if (this.getX() > -16) {
                moved = true;
                direction = dLeft;
                this.setX(this.x - speed);
            }else{
                moved = true;
                direction = dLeft;
                this.setX(160);
            }
        }

        if (up) {
            moved = true;
            this.setY(this.y - speed);
        } else if (down) {
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
    }

    public void render(Graphics g) {
        if (direction == dRight) {
            g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);
        }
        if (direction == dLeft) {
            g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);
        }

    }
}
