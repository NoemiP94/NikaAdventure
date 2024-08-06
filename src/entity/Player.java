package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
    }

    public void setDefaultValues(){
        x = 100;
        y= 100;
        speed = 4;
    }

    public void update(){
        if(keyH.upPressed== true) {
            y -= speed; //player goes up
        } else if(keyH.downPressed == true){
            y += speed;//player goes down
        } else if(keyH.leftPressed == true){
            x -= speed; //player goes left
        } else if(keyH.rightPressed == true){
            x += speed; //player goes right
        }
    }
    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize); //fillRect( x, y, width, height ) -> draw a rectangle
    }
}
