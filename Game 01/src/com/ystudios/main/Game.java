package com.ystudios.main;

import com.ystudios.entities.Bullet;
import com.ystudios.entities.Enemy;
import com.ystudios.entities.Entity;
import com.ystudios.entities.Player;
import com.ystudios.graphics.Spritesheet;
import com.ystudios.graphics.UI;
import com.ystudios.main.Menu;
import com.ystudios.world.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    public static final int SCALE = 3;

    private BufferedImage image;

    private Font font;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Bullet> bullets;
    public static Player player;

    public static int currentLevel = 1, maxLevel = 2;

    public static Spritesheet spritesheet;

    public static World world;
    public UI ui;
    public Menu menu;

    public static String gameState = "MENU";

    private boolean enterPressed, escPressed;

    public boolean saveGame = false;

    public Game() {
        Sound.musicBackground.loop();

        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        //Inicializando Objetos
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        ui = new UI();
        menu = new Menu();

        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

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
        if (gameState == "NORMAL") {
            if (saveGame) {
                saveGame = false;
                String[] chave = {"level"};
                int[] valor = {currentLevel};
                Menu.saveGame(chave, valor, 10);
            }
            enterPressed = false;
            if (escPressed == true) {
                gameState = "PAUSE";
                escPressed = false;
            } else {
                enterPressed = false;
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
        } else if (gameState == "PAUSE" || gameState == "MENU") {
            if (enterPressed == true) {
                menu.enter = true;
                enterPressed = false;
            }
            menu.tick();
        } else if (gameState == "GAME_OVER") {
            if (enterPressed == true) {
                gameState = "NORMAL";
                restartGame(Integer.toString(currentLevel));
            }
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
                g.setFont(font);
                g.setColor(Color.white);
                g.drawString("YOU WIN", UI.stringSizeWidth(g, font, "YOU WIN"), rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent());
            } else {
                restartGame(Integer.toString(currentLevel));
            }
        }

        if (gameState == "MENU" || gameState == "PAUSE") {
            menu.render(g);
        } else if (gameState == "GAME_OVER") {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

            font = new Font("roboto", Font.BOLD, 40);
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("GAME OVER", UI.stringSizeWidth(g, font, "GAME OVER"), HEIGHT * SCALE / 2 - 40);

            font = new Font("roboto", Font.BOLD, 25);
            g.setFont(font);
            g.drawString(">Pressione enter para reiniciar<", UI.stringSizeWidth(g, font, ">Pressione enter para reiniciar<"), HEIGHT * SCALE / 2 + 20);
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

    public static void restartGame(String level) {
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
            menu.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
            menu.down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameState == "NORMAL") {
                this.saveGame = true;
            }
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
