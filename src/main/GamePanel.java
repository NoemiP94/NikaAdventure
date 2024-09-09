package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;


public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16; //size of the objects 16x16 pixels
    final int scale = 3; //to scale the 16x16 objects = 48 pixels

    public final int tileSize = originalTileSize * scale; //final size of the object 48x48
    public final int maxScreenCol = 16; //how many tiles vertically
    public final int maxScreenRow = 12; //how many tiles horizontally
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;


    //FPS
    int FPS = 60;

    //SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread; //time in game

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[10];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;


    //constructor
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);// all the drawing from this component will be done in offscreen painting buffer -> improve rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true); //GamePanel can be 'focused' to receive input
    }

    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        //playMusic(0);
        //stopMusic();
        gameState  = titleState;
    }

    public void startGameThread(){ //create a new Thread and start it
        gameThread = new Thread(this);
        gameThread.start();
    }

    //SLEEP METHOD -> not accurated
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
                //System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }


        }
    }

    public void update(){
        if(gameState == playState){
            //PLAYER
            player.update();
            //NPC
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            //MONSTER
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    if(monster[i].alive == true && monster[i].dying == false){
                        monster[i].update();
                    }
                    if(monster[i].alive == false){
                        monster[i] = null;
                    }
                }
            }
        }
        if(gameState == pauseState){
            //nothing
        }

    }

    public void paintComponent(Graphics g){ //Graphics is a class that has many functions to draw the screen
        super.paintComponent(g);
        //Graphics2D provide more sophisticated controls
        Graphics2D g2 = (Graphics2D)g; //Cast Graphics to Graphics2D

        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState){
            ui.draw(g2);
        }
        //OTHERS
        else{
            //TILE
            tileM.draw(g2);


            //add entities to the list
            //PLAYER
            entityList.add(player);
            //NPC
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }
            //OBJECT
            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }
            //MONSTER
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    entityList.add(monster[i]);
                }
            }

            //sort
            Collections.sort(entityList, (e1, e2) -> {
                int result = Integer.compare(e1.worldY,e2.worldY);
                return result;
            });

            //draw entities
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }
            //empty entity list
            entityList.clear();


            //UI
            ui.draw(g2);
        }




        //DEBUG
        if(keyH.showDebugText == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN,20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y);
            y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y);
            y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y);
            y += lineHeight;

            g2.drawString("Draw Time: " + passed, x,y);
            //System.out.println("Draw Time: " + passed);
        }


        g2.dispose(); //dispose of this graphics context and release any system resources that is using
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    //play sound effects
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
