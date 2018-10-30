package com.MateuszLebioda;

import javafx.scene.media.AudioClip;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class GamePanel extends JComponent implements KeyListener, ActionListener {

    private Random random = new Random();

    private AudioClip creeper1;
    private AudioClip creeper2;
    private AudioClip music;
    private AudioClip boom1;
    private AudioClip boom2;

    private JButton respawnButton = new JButton();
    private JButton menuButton = new JButton();
    private JButton exitButton = new JButton();

    private boolean menu;
    private boolean lose;
    private boolean option = true;
    private boolean pause = false;

    private int back=0;
    private boolean anim = true;

    private int score;

    private Font font;

    private GroundPowder groundPowder;

    GamePanel(){

        try {
            font = Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream("/resources/Minecraftia.ttf"));
            font = font.deriveFont(Font.PLAIN,36);
        }catch (Exception e){
            System.out.println("Blad z czcionka");
        }

        respawnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu = true;
                timer.stop();
                repaint();
            }
        });

        groundPowder  = new GroundPowder();
        menu = true;
        lose = false;

        respawnButton.setVisible(false);
        menuButton.setVisible(false);
        exitButton.setVisible(false);
        initMusic();
        addKeyListener(this);
        setFocusable(true);
        int time = 100;
        timer = new Timer(time, this);
    }

    private Timer timer;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private int side;

    private List<Creeper> creeperBody;

    public void paint(Graphics g) {
        if(!menu){
            ImageIcon area;
            if(anim) {
                if (back < 5) {
                    back++;
                    area = new ImageIcon(getClass().getResource("/resources/Background.png"));
                } else {
                    back++;
                    if (back > 10) {
                        back = 0;
                    }
                    area = new ImageIcon(getClass().getResource("/resources/Background2.png"));
                }
            }else{
                area = new ImageIcon(getClass().getResource("/resources/Background.png"));
            }

            area.paintIcon(this, g, 0, 0);
            ImageIcon border = new ImageIcon(getClass().getResource("/resources/border.png"));
            border.paintIcon(this, g, 0, 0);

            for (int i = 0; i < creeperBody.size(); i++) {
                if (i == 0) {
                    switch (side) {
                        case GamePanel.RIGHT: {
                            setIcon(i, g, "/resources/CreeperHeadRigth.png");
                            break;
                        }
                        case GamePanel.LEFT: {
                            setIcon(i, g, "/resources/CreeperHeadLeft.png");
                            break;
                        }
                        case GamePanel.UP: {
                            setIcon(i, g, "/resources/CreeperHeadUp.png");
                            break;
                        }
                        case GamePanel.DOWN: {
                            setIcon(i, g, "/resources/CreeperHeadDown.png");
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
                                    setIcon(i, g, "/resources/SnakeBodyHorizontal.png");
                                    break;
                                }
                                case GamePanel.DOWN: {
                                    setIcon(i, g, "/resources/SnakeBodyLeftDown.png");
                                    break;
                                }
                                case GamePanel.UP: {
                                    setIcon(i, g, "/resources/SnakeBodyLeftUp.png");
                                    break;
                                }
                            }
                            break;
                        }
                        case GamePanel.LEFT: {
                            switch (creeperBody.get(i - 1).side) {
                                case GamePanel.RIGHT:
                                case GamePanel.LEFT: {
                                    setIcon(i, g, "/resources/SnakeBodyHorizontal.png");
                                    break;
                                }
                                case GamePanel.DOWN: {
                                    setIcon(i, g, "/resources/SnakeBodyRightDown.png");
                                    break;
                                }
                                case GamePanel.UP: {
                                    setIcon(i, g, "/resources/SnakeBodyRightUp.png");
                                    break;
                                }
                            }
                            break;
                        }
                        case GamePanel.UP: {
                            switch (creeperBody.get(i - 1).side) {
                                case GamePanel.UP:
                                case GamePanel.DOWN: {
                                    setIcon(i, g, "/resources/SnakeBodyVertical.png");
                                    break;
                                }
                                case GamePanel.LEFT: {
                                    setIcon(i, g, "/resources/SnakeBodyLeftDown.png");
                                    break;
                                }
                                case GamePanel.RIGHT: {
                                    setIcon(i, g, "/resources/SnakeBodyRightDown.png");
                                    break;
                                }
                            }
                            break;
                        }
                        case GamePanel.DOWN: {
                            switch (creeperBody.get(i - 1).side) {
                                case GamePanel.DOWN:
                                case GamePanel.UP: {
                                    setIcon(i, g, "/resources/SnakeBodyVertical.png");
                                    break;
                                }
                                case GamePanel.LEFT: {
                                    setIcon(i, g, "/resources/SnakeBodyLeftUp.png");
                                    break;
                                }
                                case GamePanel.RIGHT: {
                                    setIcon(i, g, "/resources/SnakeBodyRightUp.png");
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }

            ImageIcon groundPowderIcon = new ImageIcon(getClass().getResource("/resources/GroundPowder.png"));

            if (creeperBody.get(0).x == groundPowder.x && creeperBody.get(0).y == groundPowder.y) {
                if (random.nextBoolean()) {
                    creeper1.play();
                } else {
                    creeper2.play();
                }
                creeperBody.add(new Creeper());
                rollGroundPowder();
                score++;
            }

            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Score : " + score,13,759);

            groundPowderIcon.paintIcon(this, g, groundPowder.x, groundPowder.y);

            if (lose) {
                ImageIcon loseBackground = new ImageIcon(getClass().getResource("/resources/LoseBacground.png"));
                loseBackground.paintIcon(this, g, 0, 0);

                g.setFont(font);
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score,300,330);

                respawnButton.setBounds(150, 420, 470, 50);
                respawnButton.setIcon(new ImageIcon(getClass().getResource("/resources/respawnButton.png")));

                menuButton.setBounds(150, 520, 470, 50);
                menuButton.setIcon(new ImageIcon(getClass().getResource("/resources/titleMenuButton.png")));

                exitButton.setBounds(150, 620, 470, 50);
                exitButton.setIcon(new ImageIcon(getClass().getResource("/resources/exitButton.png")));

                this.add(respawnButton);
                respawnButton.setVisible(true);

                this.add(menuButton);
                menuButton.setVisible(true);

                this.add(exitButton);
                exitButton.setVisible(true);

            }

            if(pause){
                g.setFont(font);
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score,300,430);
                g.setFont(g.getFont().deriveFont(Font.PLAIN,145));
                g.drawString("Pause",125,300);
            }
        }
        if(menu){
            ImageIcon menuIcon = new ImageIcon(getClass().getResource("/resources/menu.png"));
            menuIcon.paintIcon(this,g,0,0);

            BufferedImage snake = null;
            BufferedImage x = null;
            try {
                 snake = ImageIO.read(getClass().getResource("/resources/16.png"));
                 x = snake.getSubimage(0,90,110,100);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("xa");
            }

            respawnButton.setVisible(false);
            menuButton.setVisible(false);
            exitButton.setVisible(false);
            if(option){
                g.drawImage(x,600,420,100,100,null);
                g.drawLine(200,500,580,500);

            }else {
                g.drawImage(x,600,570,100,100,null);
                g.drawLine(200,650,580,650);

            }
        }
    }

    private ArrayList<Creeper> initCreeper(){
        ArrayList<Creeper> creeperBody = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            creeperBody.add(new Creeper());
            creeperBody.get(i).y = 48*2;
            creeperBody.get(i).x = 48*(3-i);
            creeperBody.get(i).side = GamePanel.RIGHT;
        }
        side = GamePanel.RIGHT;
        return creeperBody;
    }

    private void setIcon(int i, Graphics g, String way){
        creeperBody.get(i).bodyIcon = new ImageIcon(getClass().getResource(way));
        creeperBody.get(i).bodyIcon.paintIcon(this,g,creeperBody.get(i).x,creeperBody.get(i).y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(!music.isPlaying()){
            music.play();
        }
        if(side == GamePanel.RIGHT){
            for(int i = creeperBody.size()-1; i>0;i--){
                creeperBody.get(i).y = creeperBody.get(i-1).y;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = creeperBody.size()-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).x += 48;
                }else {
                    creeperBody.get(i).x = creeperBody.get(i-1).x;
                }
            }
        }if(side == GamePanel.LEFT){
            for(int i = creeperBody.size()-1; i>0;i--){
                creeperBody.get(i).y = creeperBody.get(i-1).y;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = creeperBody.size()-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).x -= 48;
                }else {
                    creeperBody.get(i).x = creeperBody.get(i-1).x;
                }
            }
        }if(side == GamePanel.DOWN){
            for(int i = creeperBody.size()-1; i>0;i--){
                creeperBody.get(i).x = creeperBody.get(i-1).x;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = creeperBody.size()-1;i>=0;i--){
                if(i==0){
                    creeperBody.get(i).y += 48;
                }else {
                    creeperBody.get(i).y = creeperBody.get(i-1).y;
                }
            }
        }if(side == GamePanel.UP){
            for(int i = creeperBody.size()-1; i>0;i--){
                creeperBody.get(i).x = creeperBody.get(i-1).x;
                creeperBody.get(i).side=creeperBody.get(i-1).side;

            }for(int i = creeperBody.size()-1;i>=0;i--) {
                if (i == 0) {
                    creeperBody.get(i).y -= 48;
                } else {
                    creeperBody.get(i).y = creeperBody.get(i - 1).y;
                }
            }
        }if(creeperBody.get(0).y<48||creeperBody.get(0).y>680||creeperBody.get(0).x<48||creeperBody.get(0).x>680){
            timer.stop();
            if(creeperBody.get(0).y<48) creeperBody.get(0).y += 48;
            else if(creeperBody.get(0).y>680) creeperBody.get(0).y -= 48;
            else if(creeperBody.get(0).x<48) creeperBody.get(0).x += 48;
            else if(creeperBody.get(0).x>48) creeperBody.get(0).x -= 48;
                if(random.nextBoolean()){
                    boom1.play();
                }else{
                    boom2.play();
                }
            if(!lose){
                repaint();
            }lose=true;
        }for(int x = 1;x < creeperBody.size(); x++){
            if(creeperBody.get(x).x==creeperBody.get(0).x && creeperBody.get(x).y==creeperBody.get(0).y){
                timer.stop();
                if(random.nextBoolean()){
                    boom1.play();
                }else{
                    boom2.play();
                }
                if(!lose){
                    repaint();
                }lose=true;
            }
        }
        if(!lose) {
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
                    if(creeperBody.get(0).side != GamePanel.LEFT) {
                        side = GamePanel.RIGHT;
                    }break;
                }case KeyEvent.VK_LEFT:{
                    if(creeperBody.get(0).side != GamePanel.RIGHT){
                        side = GamePanel.LEFT;
                    }break;
                }case KeyEvent.VK_UP:{
                    if(menu) {
                        option = true;
                        repaint();
                    } else if(!lose){
                        if(creeperBody.get(0).side != GamePanel.DOWN)
                            side = GamePanel.UP;
                    }
                    break;
                }case KeyEvent.VK_DOWN:{
                    if(menu) {
                        option = false;
                        repaint();
                    } else if(!lose){
                        if(creeperBody.get(0).side != GamePanel.UP)
                        side = GamePanel.DOWN;
                    }
                    break;
                }case KeyEvent.VK_ENTER:{
                    if(lose){
                        lose = false;
                        creeperBody = initCreeper();
                        startNewGame();
                        repaint();
                        timer.start();
                        respawnButton.setFocusable(false);
                        respawnButton.setVisible(false);
                    }if(menu){
                        if(option)startNewGame();
                        else System.exit(0);
                        break;
                    }break;
                }case KeyEvent.VK_ESCAPE:{
                    if(lose){
                        System.exit(0);
                    }else{
                        menu = true;
                        timer.stop();
                        repaint();
                    }
                    break;
                }case  KeyEvent.VK_SPACE: {
                    if(pause && !lose) {
                        pause = false;
                        timer.start();
                    }
                    else if(!pause && !lose){
                        pause = true;
                        timer.stop();
                        repaint();
                    }
                    break;
                }
                case  KeyEvent.VK_A:{
                    if(anim){
                        anim = false;
                    }else{
                        anim = true;
                    }
                }
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void rollGroundPowder(){
        groundPowder.x = (random.nextInt(13)+1)*48;
        groundPowder.y = (random.nextInt(13)+1)*48;

        for(int i = 0; i < creeperBody.size() ; i++){
            if(creeperBody.get(i).x == groundPowder.x && groundPowder.y == creeperBody.get(i).y){
                groundPowder.x = (random.nextInt(13)+1)*48;
                groundPowder.y = (random.nextInt(13)+1)*48;
                i = 0;
            }
        }
    }

    private void initMusic(){
        try {
            creeper1 = new AudioClip(getClass().getResource("/resources/Creeper1.mp3").toURI().toString());
            creeper2 = new AudioClip(getClass().getResource("/resources/Creeper2.mp3").toURI().toString());
            music = new AudioClip(getClass().getResource("/resources/miusic.mp3").toURI().toString());
            boom1 = new AudioClip(getClass().getResource("/resources/Boom1.mp3").toURI().toString());
            boom2 = new AudioClip(getClass().getResource("/resources/Boom2.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            System.err.println("Dzwiek nie zostal prawidlowo wczytany.");
        }
        music.play();
        creeper1.setVolume(0.1);
        creeper2.setVolume(0.1);
        boom2.setVolume(0.1);
        boom1.setVolume(0.1);
        music.setVolume(0.2);
    }
    private void startNewGame(){
        lose = false;
        menu = false;
        creeperBody = initCreeper();
        repaint();
        timer.start();
        score = 0;
        respawnButton.setFocusable(false);
        respawnButton.setVisible(false);

        menuButton.setFocusable(false);
        menuButton.setVisible(false);
        exitButton.setVisible(false);
        rollGroundPowder();
    }

}
