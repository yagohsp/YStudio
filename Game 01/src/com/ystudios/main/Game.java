package com.ystudios.main;

import com.ystudios.entities.Bullet;
import com.ystudios.entities.Enemy;
import com.ystudios.entities.Entity;
import com.ystudios.entities.Player;
import com.ystudios.graphics.Spritesheet;
import com.ystudios.graphics.UI;
import com.ystudios.world.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    private Thread thread;
    private boolean isRunning = true;

    public static JFrame frame;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    private final int SCALE = 3;

    private BufferedImage image;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Bullet> bullets;
    public static Player player;

    public static int currentLevel = 1, maxLevel = 2;

    public static Spritesheet spritesheet;

    public static World world;
    public UI ui;

    public Game() {
        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        //Inicializando Objetos
        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<Bullet>();

        spritesheet = new Spritesheet("/res/spritesheet.png");

        player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
        entities.add(player);

        world = new World("/res/level1.png");
    }

    public void initFrame() {
        frame = new JFrame("Meu jogo topper");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {

    }

    public static void main(String args[]) {
        Game game = new Game();
        game.start();
    }

    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).tick();
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).tick();
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        /*RENDERIZANDO JOGO*/
        world.render(g);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).render(g);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }
        ui.render(g);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        g.setFont(new Font("roboto", Font.BOLD, 20));
        g.setColor(Color.white);
        g.drawString("Munição " + player.ammo, 20, 155 * SCALE);

        if (enemies.size() == 0) {
            currentLevel += 1;
            if (currentLevel > maxLevel) {
                Font font = new Font("roboto", Font.BOLD, 60);
                Rectangle rect = new Rectangle(WIDTH * SCALE, HEIGHT * SCALE);
                FontMetrics metrics = g.getFontMetrics(font);
                int x = rect.x + (rect.width - metrics.stringWidth("YOU WIN")) / 2;
                int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
                g.setFont(font);
                g.setColor(Color.white);
                g.drawString("YOU WIN", x, y);
            }else{
                restartGame();
            }
        }
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
    }

    public static void restartGame() {
        String level = new String("" + currentLevel);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        spritesheet = new Spritesheet("/res/spritesheet.png");
        player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
        entities.add(Game.player);
        world = new World("/res/level" + level + ".png");
        return;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player.down = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.isShooting = true;
        player.mx = (e.getX() / SCALE);
        player.my = (e.getY() / SCALE);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
