package main;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_Door_Iron;

import java.awt.*;

public class CutsceneManager {
    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;

    //Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        switch(sceneNum){
            case skeletonLord: scene_skeletonLord(); break;
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
}
