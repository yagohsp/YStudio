package com.ystudios.main;

import com.ystudios.graphics.UI;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {

    public String[] options = {"Novo jogo", "Carregar jogo", "Sair"};

    public int currentOption = 0;
    public int maxOptions = options.length - 1;

    public boolean up, down, enter;

    public static boolean pause = false, saveExists = false, saveGame = false;

    public void tick() {
        File file = new File("save.txt");
        if (file.exists()) {
            saveExists = true;
        } else {
            saveExists = false;
        }
        if (enter) {
            switch (options[currentOption]) {
                case "Novo Jogo":
                    Game.gameState = "NORMAL";
                    file = new File("save.txt");
                    file.delete();
                    break;

                case "Continuar":
                    Game.gameState = "NORMAL";
                    break;

                case "Carregar jogo":
                    if (file.exists()) {
                        String saver = loadGame(10);
                        applyLoadGame(saver);
                    }
                    break;

                case "Sair":
                    System.exit(1);
                    break;
            }
            enter = false;
            currentOption = 0;
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

        Font font = new Font("Roboto", Font.BOLD, 45);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Jogo Top", UI.stringSizeWidth(g, font, "Jogo Top"), (Game.HEIGHT * Game.SCALE / 2) - 150);

        font = new Font("Roboto", Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.white);
        
        if (Game.gameState == "PAUSE") {
            options[0] = "Continuar";
        } else if (Game.gameState == "MENU") {
            options[0] = "Novo Jogo";
        }
        
        for (int i = 0; i <= maxOptions; i++) {
            g.drawString(options[i], UI.stringSizeWidth(g, font, options[i]), (Game.HEIGHT * Game.SCALE / 2) + (i * 50) - 50);
            if (currentOption == i) {
                g.drawString(">", UI.stringSizeWidth(g, font, options[i]) - 25, (Game.HEIGHT * Game.SCALE / 2) + (i * 50) - 50);
            }
        }
    }

    ////////////
    public static void saveGame(String[] chave, int[] valor, int encode) {
        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {
        }
        for (int i = 0; i < chave.length; i++) {
            String current = chave[i];
            current += ":";
            char[] value = Integer.toString(valor[i]).toCharArray();
            for (int n = 0; n < value.length; n++) {
                value[n] += encode;
                current += value[n];
            }
            try {
                write.write(current);
                if (i < chave.length - 1) {
                    write.newLine();
                }
            } catch (IOException e) {
            }
        }
        try {
            write.flush();
            write.close();
        } catch (IOException e) {

        }
    }

    public static void applyLoadGame(String str) {
        String[] todasChaves = str.split("/");
        for (int i = 0; i < todasChaves.length; i++) {
            String[] chave = todasChaves[i].split(":");
            switch (chave[0]) {
                case "level":
                    Game.restartGame(chave[1]);
                    Game.gameState = "NORMAL";

                    break;

            }
        }
    }

    public static String loadGame(int encode) {
        String line = "";
        File file = new File("save.txt");
        if (file.exists()) {
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
                try {
                    while ((singleLine = reader.readLine()) != null) {
                        String[] chave = singleLine.split(":");
                        char[] valor = chave[1].toCharArray();
                        chave[1] = "";
                        for (int i = 0; i < valor.length; i++) {
                            valor[i] -= encode;
                            chave[1] += valor[i];
                        }
                        line += chave[0];
                        line += ":";
                        line += chave[1];
                        line += "/";
                    }
                } catch (IOException e) {

                }
            } catch (FileNotFoundException e) {

            }
        }
        return line;
    }
}
