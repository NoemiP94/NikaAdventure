package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {//interface for receiving keyboard events
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    //DEBUG
    boolean showDebugText = false;

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
            if(gp.ui.commandNum == 0){
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum == 1){
                //load game
            }
            if(gp.ui.commandNum == 2){
                //quit the game
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



        //DEBUG
        if(code == KeyEvent.VK_T){//show debug text
            if(showDebugText == false){
                showDebugText = true;
            }else if (showDebugText == true){
                showDebugText = false;
            }
        }
        if(code == KeyEvent.VK_R){ //refresh map changes
            gp.tileM.loadMap("/maps/worldV2.txt");
        }
    }
    public void pauseState(int code){
        if(code == KeyEvent.VK_P) { //if the user press P
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){//if the user press ENTER
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        if(code == KeyEvent.VK_C) { //if the user press C
            gp.gameState = gp.playState;
        }
        if( code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0){ //row cannot be negative
                gp.ui.slotRow--;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_A){
            if(gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(9);
            }
        }
        if( code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
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
    }
}
