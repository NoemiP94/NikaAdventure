package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16; //size of the objects 16x16 pixels
    final int scale = 3; //to scale the 16x16 objects = 48 pixels

    public final int tileSize = originalTileSize * scale; //final size of the object 48x48
    final int maxScreenCol = 16; //how many tiles vertically
    final int maxScreenRow = 12; //how many tiles horizontally
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //time in game
    Player player = new Player(this, keyH);

    //Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;


    //constructor
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);// all the drawing from this component will be done in offscreen painting buffer -> improve rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true); //GamePanel can be 'focused' to receive input
    }

    public void startGameThread(){ //create a new Thread and start it
        gameThread = new Thread(this);
        gameThread.start();
    }

    //SLEEP METHOD -> non accurated
   /* @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        //create a game loop
        while(gameThread != null){
            //long currentTime = System.nanoTime(); // return the current value of the running JVM high resolution time source in nanoseconds
            //System.out.println("current time: "+ currentTime);

            //System.out.println("The game loop is running");

            //1. UPDATE: update information such as character positions
            update();
            //2. DRAW: draw the screen with the updated information
            repaint();


            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000; //convert in millis

                if(remainingTime <0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }*/

    //DELTA/ACCUMULATOR METHOD
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            //every loop we add the past time divided to drawInterval to delta
            // and when delta reaches this delta, we update and repaint and reset delta
            if(delta>1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer>= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }


        }
    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){ //Graphics is a class that has many functions to draw the screen
        super.paintComponent(g);
        //Graphics2D provide more sophisticated controls
        Graphics2D g2 = (Graphics2D)g; //Cast Graphics to Graphics2D
        player.draw(g2);
        g2.dispose(); //dispose of this graphics context and release any system resources that is using
    }
}
