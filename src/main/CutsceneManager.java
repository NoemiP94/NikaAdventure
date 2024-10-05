package main;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_BlueHeart;
import object.OBJ_Door_Iron;

import java.awt.*;

public class CutsceneManager {
    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    //Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;
    public final int ending = 2;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
         endCredit = "Thanks to RyiSnow for creating\n this tutorial."
                 + "\n"
                 + "Program/Music/Art\n"
                 + "by RyiSnow\n"
                 + "\n"
                 + "Nika design\n"
                 + "Noemi"
                 + "\n\n\n\n\n\n\n\n"
                 + "Thank you for playing!";
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        switch(sceneNum){
            case skeletonLord: scene_skeletonLord(); break;
            case ending: scene_ending(); break;
        }
    }

    public void scene_skeletonLord(){
        if(scenePhase == 0){ //placing iron door
            gp.bossBattleOn = true;

            //shut the iron door
            for(int i = 0; i < gp.obj[1].length; i++){//check vacant slot in obj array
                if(gp.obj[gp.currentMap][i] == null){
                    gp.obj[gp.currentMap][i] = new OBJ_Door_Iron(gp);
                    gp.obj[gp.currentMap][i].worldX = gp.tileSize*25;
                    gp.obj[gp.currentMap][i].worldY = gp.tileSize*28;
                    gp.obj[gp.currentMap][i].temp = true; //this obj is available only during this boss fight
                    gp.playSE(21);
                    break;
                }
            }
            //search a vacant slot for the dummy
            for(int i = 0; i < gp.npc[1].length; i++){
                if(gp.npc[gp.currentMap][i] == null){
                    gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                    gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                    gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npc[gp.currentMap][i].direction = gp.player.direction;
                    break;
                }
            }

            gp.player.drawing = false;
            scenePhase++;
        }
        if(scenePhase == 1){ //moving the camera
            gp.player.worldY -= 2; //the camera goes up

            if(gp.player.worldY < gp.tileSize*16){
                scenePhase++;
            }
        }
        if(scenePhase == 2){ //waking up the boss
            //search the boss
            for(int i = 0; i < gp.monster[1].length; i++){
                if(gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i].name.equals(MON_SkeletonLord.monName) ){
                    gp.monster[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monster[gp.currentMap][i]; //pass to npc to let him speak
                    scenePhase++;
                    break;
                }
            }
        }
        if(scenePhase == 3){ //letting the boss speak
            gp.ui.drawDialogueScreen();
        }
        if(scenePhase == 4){ //returning to player
            //search the dummy
            for(int i = 0; i < gp.npc[1].length; i++){
                if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)){
                    //restore player's position
                    gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                    //delete the dummy
                    gp.npc[gp.currentMap][i] = null;
                    break;
                }

            }
            //start drawing the player
            gp.player.drawing = true;

            //reset
            sceneNum = NA;
            scenePhase = 0;
            gp.gameState = gp.playState;

            //change music
            gp.stopMusic();
            gp.playMusic(22);
        }
    }
    public void scene_ending(){
        if(scenePhase == 0){
            gp.stopMusic();
            gp.ui.npc = new OBJ_BlueHeart(gp);
            scenePhase++;
        }
        if(scenePhase == 1){ //display dialogues
            gp.ui.drawDialogueScreen();
            //scenePhase++;
        }
        if(scenePhase == 2){ //play the fanfare
            gp.playSE(4);
            scenePhase++;
        }
        if(scenePhase == 3){
            //wait until the sound effect ending
            if(counterReached(300) == true){ //5 seconds
                scenePhase++;
            }
        }
        if(scenePhase == 4){
            //The screen gets darker
            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }
            drawBlackBackground(alpha);

            if(alpha == 1f){
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 5){ //display narrative message
            drawBlackBackground(1f);

            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }

            String text = "After the fierce battle with the Skeleton Lord, \n"
                    + "Nika finally found the legendary treasure. \n"
                    + "But this is not the end of her journey. \n"
                    + "The Nika's adventure has just begun.";

            drawString(alpha, 38f, 200, text, 70);

            if(counterReached(600) == true){ //10 seconds
                gp.playMusic(0);
                scenePhase++;
            }
        }
        if(scenePhase == 6){
            drawBlackBackground(1f);
            drawString(1f, 120f, gp.screenHeight/2, "Nika's Adventure", 40);
            if(counterReached(480) == true){ //8 seconds
                scenePhase++;
            }
        }
        if(scenePhase == 7){
            drawBlackBackground(1f);
            y = gp.screenHeight/2;
            drawString(1f, 38f, y, endCredit, 40);

            if(counterReached(480) == true){ //8 seconds
                scenePhase++;
            }
        }
        if(scenePhase == 8){
            //text move in the upward direction
            drawBlackBackground(1f);
            y--;
            drawString(1f, 38f, y, endCredit, 40);

        }

    }

    public boolean counterReached(int target){
        boolean counterReached = false;
        counter++;
        if(counter > target){
            counterReached = true;
            counter = 0;
        }
        return counterReached;
    }
    public void drawBlackBackground(float alpha){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.black);
        g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    }
    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for(String line : text.split("\n") ){
            int x = gp.ui.getXForCenteredText(line);
            g2.drawString(line, x,y);
            y += lineHeight;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
