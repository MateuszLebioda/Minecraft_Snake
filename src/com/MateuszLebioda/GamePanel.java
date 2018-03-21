package com.MateuszLebioda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    public  GamePanel(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(time, this);
        timer.start();
        initCreeper();
    }

    Timer timer;

    static final int RIGHT = 0;
    static final int LEFT = 1;
    static final int UP = 2;
    static final int DOWN = 3;

    int moves = 0;

    int side;

    private int x[] = new int [] {24,73,122,171,220,269,318,367,416,465,514,563,612,661,710,759};
    private int y[] = new int [] {24,73,122,171,220,269,318,367,416,465,514,563,612,661,710,759};

    private int CreeperLength = 3;
    private List<Creeper> creeperBody = new ArrayList<>();


    int time = 100;

    private ImageIcon area;

    public void paint(Graphics g) {

        /*Paint background*/
        area = new ImageIcon("resources/Background.png");
        area.paintIcon(this, g, 24, 24);


        for (int i = 0; i < CreeperLength; i++) {
            if (i == 0) {
                switch (side) {
                    case GamePanel.RIGHT: {
                        setIcon(i, g, "resources/CreeperHeadRigth.png");
                        break;
                    }
                    case GamePanel.LEFT: {
                        setIcon(i, g, "resources/CreeperHeadLeft.png");
                        break;
                    }
                    case GamePanel.UP: {
                        setIcon(i, g, "resources/CreeperHeadUp.png");
                        break;
                    }
                    case GamePanel.DOWN: {
                        setIcon(i, g, "resources/CreeperHeadDown.png");
                        break;
                    }
                }
                creeperBody.get(i).side = side;
            } else {
                switch (creeperBody.get(i).side) {
                    case GamePanel.RIGHT: {
                        switch (creeperBody.get(i - 1).side) {
                            case GamePanel.LEFT:
                            case GamePanel.RIGHT: {
                                setIcon(i, g, "resources/SnakeBodyHorizontal.png");
                                break;
                            }
                            case GamePanel.DOWN: {
                                setIcon(i, g, "resources/SnakeBodyLeftDown.png");
                                break;
                            }
                            case GamePanel.UP: {
                                setIcon(i, g, "resources/SnakeBodyLeftDown.png");
                                break;
                            }
                        }
                        break;
                    }
                    case GamePanel.LEFT: {
                        switch (creeperBody.get(i - 1).side) {
                            case GamePanel.RIGHT:
                            case GamePanel.LEFT: {
                                setIcon(i, g, "resources/SnakeBodyHorizontal.png");
                                break;
                            }
                            case GamePanel.DOWN: {
                                setIcon(i, g, "resources/SnakeBodyRightDown.png");
                                break;
                            }
                            case GamePanel.UP: {
                                setIcon(i, g, "resources/SnakeBodyRightUp.png");
                                break;
                            }
                        }
                        break;
                    }
                    case GamePanel.UP: {
                        switch (creeperBody.get(i - 1).side) {
                            case GamePanel.UP:
                            case GamePanel.DOWN: {
                                setIcon(i, g, "resources/SnakeBodyVertical.png");
                                break;
                            }
                            case GamePanel.LEFT: {
                                setIcon(i, g, "resources/SnakeBodyLeftDown.png");
                                break;
                            }
                            case GamePanel.RIGHT: {
                                setIcon(i, g, "resources/SnakeBodyRightDown.png");
                                break;
                            }
                        }
                        break;
                    }case GamePanel.DOWN: {
                        switch (creeperBody.get(i - 1).side) {
                            case GamePanel.DOWN:
                            case GamePanel.UP: {
                                setIcon(i, g, "resources/SnakeBodyVertical.png");
                                break;
                            }
                            case GamePanel.LEFT: {
                                setIcon(i, g, "resources/SnakeBodyLeftUp.png");
                                break;
                            }
                            case GamePanel.RIGHT: {
                                setIcon(i, g, "resources/SnakeBodyRightUp.png");
                                break;
                            }
                        }
                        break;
                    }
                }
                //g.dispose();
            }
        }
    }



    void initCreeper(){
        for (int i = 0; i < 3; i++) {
            creeperBody.add(new Creeper());
        }
        side = GamePanel.RIGHT;

        creeperBody.get(0).x = 121;
        creeperBody.get(0).y = 120;
        creeperBody.get(0).side = GamePanel.RIGHT;

        creeperBody.get(1).x = 73;
        creeperBody.get(1).y = 120;
        creeperBody.get(1).side = GamePanel.RIGHT;

        creeperBody.get(2).y = 120;
        creeperBody.get(2).x = 25;
        creeperBody.get(2).side = GamePanel.RIGHT;
    }
    void setIcon(int i,Graphics g,String way){
        creeperBody.get(i).bodyIcon = new ImageIcon(way);
        creeperBody.get(i).bodyIcon.paintIcon(this,g,creeperBody.get(i).x,creeperBody.get(i).y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(side == GamePanel.RIGHT){
            for(int i = CreeperLength-1; i>0;i--){
                creeperBody.get(i).y = creeperBody.get(i-1).y;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = CreeperLength-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).x += 48;
                }else {
                    creeperBody.get(i).x = creeperBody.get(i-1).x;
                }
                /*if(creeperBody.get(i).x>750){
                    creeperBody.get(i).x = 24;
                }*/
            }
            repaint();
        }if(side == GamePanel.LEFT){
            for(int i = CreeperLength-1; i>0;i--){
                creeperBody.get(i).y = creeperBody.get(i-1).y;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = CreeperLength-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).x -= 48;
                }else {
                    creeperBody.get(i).x = creeperBody.get(i-1).x;
                }
            }
            repaint();
        }if(side == GamePanel.DOWN){
            for(int i = CreeperLength-1; i>0;i--){
                creeperBody.get(i).x = creeperBody.get(i-1).x;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = CreeperLength-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).y += 48;
                }else {
                    creeperBody.get(i).y = creeperBody.get(i-1).y;
                }
            }
            repaint();
        }if(side == GamePanel.UP){
            for(int i = CreeperLength-1; i>0;i--){
                creeperBody.get(i).x = creeperBody.get(i-1).x;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = CreeperLength-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).y -= 48;
                }else {
                    creeperBody.get(i).y = creeperBody.get(i-1).y;
                }
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT:{
                if(side != GamePanel.LEFT) {
                    moves++;
                    side = GamePanel.RIGHT;
                }break;
            }case KeyEvent.VK_LEFT:{
                if(side != GamePanel.RIGHT){
                    moves++;
                    side = GamePanel.LEFT;
                }break;
            }case KeyEvent.VK_UP:{
                moves++;
                side = GamePanel.UP;
                break;
            }case KeyEvent.VK_DOWN:{
                moves++;
                side = GamePanel.DOWN;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
