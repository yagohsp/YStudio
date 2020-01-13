package com.ystudios.world;

import com.ystudios.entities.Ammo;
import com.ystudios.entities.Enemy;
import com.ystudios.entities.Entity;
import com.ystudios.entities.LifePotion;
import com.ystudios.entities.Weapon;
import com.ystudios.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class World {

    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static int TILE_SIZE = 16;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));

            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[(map.getWidth() * map.getHeight())];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    int pixelAtual = pixels[x + (y * map.getWidth())];
                    tiles[x + (y * WIDTH)] = new FloorTile(x * TILE_SIZE, y * TILE_SIZE, Tile.TILE_FLOOR);

                    if (pixelAtual == 0xFF000000) {
                        tiles[x + (y * WIDTH)] = new FloorTile(x * TILE_SIZE, y * TILE_SIZE, Tile.TILE_FLOOR);

                    } else if (pixelAtual == 0xFFFFFFFF) {
                        tiles[x + (y * WIDTH)] = new WallTile(x * TILE_SIZE, y * TILE_SIZE, Tile.TILE_WALL);

                    } else if (pixelAtual == 0xFF639bff) {
                        //Player
                        Game.player.setX(x * TILE_SIZE);
                        Game.player.setY(y * TILE_SIZE);

                    } else if (pixelAtual == 0xFFac3232) {
                        //Enemy
                        Enemy en = new Enemy(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENEMY_EN);
                        Game.enemies.add(en);

                    } else if (pixelAtual == 0xFF306082) {
                        //Weapon
                        Game.entities.add(new Weapon(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.WEAPON_EN));

                    } else if (pixelAtual == 0xFFfbf236) {
                        //Ammo
                        Game.entities.add(new Ammo(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.AMMO_EN));

                    } else if (pixelAtual == 0xFFd77bba) {
                        //Life potion
                        Game.entities.add(new LifePotion(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.LIFE_POTION_EN));

                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isFree(int xNext, int yNext) {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
                || (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
                || (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
                || (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

    }

    public void render(Graphics g) {
        int xStart = Camera.x / TILE_SIZE;
        int yStart = Camera.y / TILE_SIZE;
        int xEnd = xStart + (Game.WIDTH >> 4);
        int yEnd = yStart + (Game.HEIGHT >> 4);

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
                    continue;
                }
                Tile tile = tiles[x + (y * WIDTH)];
                tile.render(g);
            }
        }
    }
}
