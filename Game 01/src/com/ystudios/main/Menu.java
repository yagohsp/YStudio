package com.ystudios.main;

import com.ystudios.graphics.UI;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Menu {

    public String[] options = {"Novo jogo", "Carregar jogo", "Sair"};

    public int currentOption = 0;
    public int maxOptions = options.length - 1;

    public boolean up, down, enter;

    public void tick() {
        if (enter) {
            switch (options[currentOption]) {
                case "Novo Jogo":
                    Game.gameState = "NORMAL";
                    break;
                case "Continuar":
                    Game.gameState = "NORMAL";
                    break;
                case "Sair":
                    System.exit(1);
                    break;
            }
            enter = false;
        }

        if (up) {
            currentOption--;
            if (currentOption < 0) {
                currentOption = maxOptions;
            }
            up = false;
        } else if (down) {
            currentOption++;
            if (currentOption > maxOptions) {
                currentOption = 0;
            }
            down = false;
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

        Font font = new Font("Roboto", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(Color.WHITE);
//        g.drawString("Jogo Top", UI.stringSizeWidth(g, font, "Jogo Top"), (Game.HEIGHT * Game.SCALE / 2) - 150);

        font = new Font("Roboto", Font.BOLD, 30);
        Rectangle rect = new Rectangle(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.white);

        g.setFont(font);
        if (Game.gameState == "PAUSE") {
            options[0] = "Continuar";
            for (int i = 0; i <= maxOptions; i++) {
                if (currentOption == i) {
                    g.drawString(">", UI.stringSizeWidth(g, font, options[i]) - 25, (Game.HEIGHT * Game.SCALE / 2) + (i * 50) - 50);
                }
                g.drawString(options[i], UI.stringSizeWidth(g, font, options[i]), (Game.HEIGHT * Game.SCALE / 2) + (i * 50) - 50);
            }
        } else if (Game.gameState == "MENU") {
            options[0] = "Novo Jogo";
            for (int i = 0; i <= maxOptions; i++) {
                if (currentOption == i) {
                    g.drawString(">", UI.stringSizeWidth(g, font, options[i]) - 25, (Game.HEIGHT * Game.SCALE / 2) + (i * 50) - 50);
                }
                g.drawString(options[i], UI.stringSizeWidth(g, font, options[i]), (Game.HEIGHT * Game.SCALE / 2) + (i * 50) - 50);
            }
        }
    }
}
