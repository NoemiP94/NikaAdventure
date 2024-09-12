package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;


public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16; //size of the objects 16x16 pixels
    final int scale = 3; //to scale the 16x16 objects = 48 pixels

    public final int tileSize = originalTileSize * scale; //final size of the object 48x48
    public final int maxScreenCol = 20; //how many tiles vertically
    public final int maxScreenRow = 12; //how many tiles horizontally
    public final int screenWidth = tileSize * maxScreenCol; //960 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;


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
    public Entity[] obj = new Entity[20];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    public InteractiveTile[] iTile = new InteractiveTile[50];
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();

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
        aSetter.setInteractiveTile();
        //playMusic(0);
        //stopMusic();
        gameState  = titleState;

        //full screen
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        //setFullScreen();
    }
    public void setFullScreen(){
        // get local screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //get full screen width and height
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread(){ //create a new Thread and start it
        gameThread = new Thread(this);
        gameThread.start();
    }


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
                drawToTempScreen(); //draw everything to the buffered image
                drawToScreen(); // draw the buffered image to the screen
                delta--;
                drawCount++;
            }

            if(timer>= 1000000000){
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
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }

            //PROJECTILE
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).alive == true){
                        projectileList.get(i).update();
                    }
                    if(projectileList.get(i).alive == false){
                        projectileList.remove(i);
                    }
                }
            }
            //PARTICLE
            for(int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    if(particleList.get(i).alive == true){
                        particleList.get(i).update();
                    }
                    if(particleList.get(i).alive == false){
                        particleList.remove(i);
                    }
                }
            }
            //INTERACTIVE TILE
            for(int i = 0; i < iTile.length; i++ ){
                if(iTile[i] != null){
                    iTile[i].update();
                }
            }
        }
        if(gameState == pauseState){
            //nothing
        }

    }

    public void drawToTempScreen(){
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
            //INTERACTIVE TILE
            for( int i = 0; i< iTile.length; i++){
                if(iTile[i] != null){
                    iTile[i].draw(g2);
                }
            }

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
            //PROJECTILE
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }
            //PARTICLE
            for(int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    entityList.add(particleList.get(i));
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
    }

    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0,0, screenWidth2, screenHeight2, null);
        g.dispose();
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
