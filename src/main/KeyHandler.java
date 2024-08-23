package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {//interface for receiving keyboard events

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    //DEBUG
    boolean checkDrawTime = false;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { //when user press the button
        int code = e.getKeyCode(); //returns the integer keyCode associated with the key in the event
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

        //DEBUG
        if(code == KeyEvent.VK_T){
            if(checkDrawTime == false){
                checkDrawTime = true;
            }else if (checkDrawTime == true){
                checkDrawTime = false;
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
