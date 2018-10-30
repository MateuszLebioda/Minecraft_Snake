package com.MateuszLebioda;

import javax.swing.*;


class Game {
        Game() {
        GamePanel gamePanel = new GamePanel();
        JFrame frame = new JFrame("Mateusz Lebioda");

        frame.setResizable(false);
        frame.setSize(784,806);
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
