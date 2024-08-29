package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {//interface for receiving keyboard events
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    //DEBUG
    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { //when user press the button
        int code = e.getKeyCode(); //returns the integer keyCode associated with the key in the event

        //PLAY STATE
        if(gp.gameState == gp.playState){
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
            if(code == KeyEvent.VK_ENTER) { //if the user press ENTER
                enterPressed = true;
            }


            //DEBUG
            if(code == KeyEvent.VK_T){
                if(checkDrawTime == false){
                    checkDrawTime = true;
                }else if (checkDrawTime == true){
                    checkDrawTime = false;
                }
            }
        }

        //PAUSE STATE
        else if(gp.gameState == gp.pauseState){
            if(code == KeyEvent.VK_P) { //if the user press P
                gp.gameState = gp.playState;
            }
        }
        //DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState){ //if the user press ENTER
            if(code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
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
    }
}
