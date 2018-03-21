package com.MateuszLebioda;

import javax.swing.*;

public class Game {



    Game() {
        GamePanel gamePanel = new GamePanel();
        JFrame frame = new JFrame("Snake II");
        frame.add(gamePanel);
        frame.setResizable(false);
        frame.setSize(834,868);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
