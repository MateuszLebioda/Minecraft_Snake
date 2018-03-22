package com.MateuszLebioda;

import javax.swing.*;

public class Game {



    Game() {
        GamePanel gamePanel = new GamePanel();
        JFrame frame = new JFrame("Snake II");

        frame.setResizable(false);
        frame.setSize(784,806);
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
