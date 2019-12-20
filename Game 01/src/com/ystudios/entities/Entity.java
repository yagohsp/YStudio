package com.ystudios.entities;

import com.ystudios.main.Game;
import com.ystudios.world.Camera;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    
    public static BufferedImage LIFE_POTION_EN = Game.spritesheet.getSprite(64, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(32, 0, 16, 16);
    public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(48, 0, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(0, 48, 16, 16);
    
    protected double x, y;
    private int width, height;

    private BufferedImage sprite;
    
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
    
    public void render(Graphics g){
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
    
    public void tick(){
        
    }
    
    public static boolean isColidding(Entity ent1, Entity ent2){
        Rectangle rect1 = new Rectangle(ent1.getX(), ent1.getY(), ent1.getHeight(), ent1.getWidth());
        Rectangle rect2 = new Rectangle(ent2.getX(), ent2.getY(), ent2.getHeight(), ent2.getWidth());
        
        return rect1.intersects(rect2);
    }
}
