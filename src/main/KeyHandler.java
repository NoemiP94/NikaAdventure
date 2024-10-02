package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {//interface for receiving keyboard events
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, spacePressed;
    //DEBUG
    boolean showDebugText = false;
    public boolean godModeOn = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { //when user press the button
        int code = e.getKeyCode(); //returns the integer keyCode associated with the key in the event
        //TITLE STATE
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        //PLAY STATE
        else if(gp.gameState == gp.playState){
            playState(code);
        }
        //PAUSE STATE
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }
        //DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        //CHARACTER STATE
        else if(gp.gameState == gp.characterState){
            characterState(code);
        }
        //OPTION STATE
        else if(gp.gameState == gp.optionState){
            optionsState(code);
        }
        //GAME OVER STATE
        else if(gp.gameState == gp.gameOverState){
            gameOverState(code);
        }
        //TRADE STATE
        else if(gp.gameState == gp.tradeState){
            tradeState(code);
        }
        //MAP STATE
        else if(gp.gameState == gp.mapState){
            mapState(code);
        }
    }

    public void titleState(int code){
        if(code == KeyEvent.VK_W) { //if the user press W
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 2;
            }
        }
        if(code == KeyEvent.VK_S) { //if the user press S
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2){
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){ //new game
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum == 1){ //load game
               gp.saveLoad.load();
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum == 2){//quit the game
                System.exit(0);
            }
        }
    }
    public void playState(int code){
        if(code == KeyEvent.VK_W) { //if the user press W
            upPressed = true;
        }
        if(code == KeyEvent.VK_S) { //if the user press S
            downPressed = true;
        }
        if(code == KeyEvent.VK_A) { //if the user press A
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D) { //if the user press D
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P) { //if the user press P
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C){ //if the user press C
            gp.gameState = gp.characterState;
        }
        if(code == KeyEvent.VK_ENTER) { //if the user press ENTER
            enterPressed = true;
        }
        if(code == KeyEvent.VK_F) { //if the user press F
            shotKeyPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) { //if the user press ESC
            gp.gameState = gp.optionState;
        }
        if(code == KeyEvent.VK_M) { //if the user press M
            gp.gameState = gp.mapState;
        }
        if(code == KeyEvent.VK_X) { //if the user press X
            if(gp.map.miniMapOn == false){
                gp.map.miniMapOn = true;
            }
            else {
                gp.map.miniMapOn = false;
            }
        }
        if(code == KeyEvent.VK_SPACE) { //if the user press SPACE
            spacePressed = true;
        }



        //DEBUG
        if(code == KeyEvent.VK_T){//show debug text
            if(showDebugText == false){
                showDebugText = true;
            }else if (showDebugText == true){
                showDebugText = false;
            }
        }
        if(code == KeyEvent.VK_R){ //refresh map changes
            switch(gp.currentMap){
                case 0:gp.tileM.loadMap("/maps/worldmap.txt",0); break;
                case 1:gp.tileM.loadMap("/maps/indoor01.txt",1); break;
            }

        }
        if(code == KeyEvent.VK_G){// godMode
            if(godModeOn == false){
                godModeOn = true;
            }else if (godModeOn == true){
                godModeOn = false;
            }
        }
    }
    public void pauseState(int code){
        if(code == KeyEvent.VK_P) { //if the user press P
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){//if the user press ENTER
            enterPressed = true;
        }
    }
    public void characterState(int code){
        if(code == KeyEvent.VK_C) { //if the user press C
            gp.gameState = gp.playState;
        }

        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
        playerInventory(code);
    }
    public void optionsState(int code){
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState){
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }
        if( code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            gp.playSE(9);
            if(gp.ui.commandNum < 0){ // 0 is full screen
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if( code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            gp.playSE(9);
            if(gp.ui.commandNum > maxCommandNum){
                gp.ui.commandNum = 0;
            }
        }
        if( code == KeyEvent.VK_A){
           if(gp.ui.subState == 0){
               if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0){ // 1 is music
                   gp.music.volumeScale--;
                   gp.music.checkVolume();
                   gp.playSE(9);
               }
               if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0){ // 2 is sound effect
                   gp.se.volumeScale--;
                   gp.playSE(9);
               }
           }
        }
        if( code == KeyEvent.VK_D){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5){ // 1 is music
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5){ // 2 is sound effect
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }

    }
    public void gameOverState(int code){
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1){
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){ //retry
                gp.gameState = gp.playState;
                gp.resetGame(false);
                //gp.playSE(0);
            } else if (gp.ui.commandNum == 1){ //quit
                gp.gameState = gp.titleState;
                gp.resetGame(true);

            }
        }

    }
    public void tradeState(int code){
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(gp.ui.subState == 0){
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            if(code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
        }
        if(gp.ui.subState == 1){
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }
        if(gp.ui.subState == 2){
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }
    }
    public void mapState(int code){
        if(code == KeyEvent.VK_M){
            gp.gameState = gp.playState;
        }
    }
    public void playerInventory(int code){
        if( code == KeyEvent.VK_W){
            if(gp.ui.playerSlotRow != 0){ //row cannot be negative
                gp.ui.playerSlotRow--;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_A){
            if(gp.ui.playerSlotCol != 0){
                gp.ui.playerSlotCol--;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_S){
            if(gp.ui.playerSlotRow != 3){
                gp.ui.playerSlotRow++;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_D){
            if(gp.ui.playerSlotCol != 4){
                gp.ui.playerSlotCol++;
                gp.playSE(9);
            }
        }
    }
    public void npcInventory(int code){
        if( code == KeyEvent.VK_W){
            if(gp.ui.npcSlotRow != 0){ //row cannot be negative
                gp.ui.npcSlotRow--;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_A){
            if(gp.ui.npcSlotCol != 0){
                gp.ui.npcSlotCol--;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_S){
            if(gp.ui.npcSlotRow != 3){
                gp.ui.npcSlotRow++;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_D){
            if(gp.ui.npcSlotCol != 4){
                gp.ui.npcSlotCol++;
                gp.playSE(9);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) { //when user release the button
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) { //if the user press W
            upPressed = false;
        }
        if(code == KeyEvent.VK_S) { //if the user press S
            downPressed = false;
        }
        if(code == KeyEvent.VK_A) { //if the user press A
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D) { //if the user press D
            rightPressed = false;
        }
        if(code == KeyEvent.VK_F) { //if the user press F
            shotKeyPressed = false;
        }
        if(code == KeyEvent.VK_ENTER) { //if the user press ENTER
            enterPressed = false;
        }
        if(code == KeyEvent.VK_SPACE) { //if the user press SPACE
            spacePressed = false;
        }

    }
}
