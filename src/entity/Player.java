package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    //public int hasKey = 0; //how many keys the player currently has


    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);

        this.keyH = keyH;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        //collision area
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX =  solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        //player position on world map
        worldX = gp.tileSize * 23;
        worldY= gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    //load player images
    public void getPlayerImage(){
        up1 = setup("/player/nika_up1");
        up2 = setup("/player/nika_up2");
        down1 = setup("/player/nika_down-1");
        down2 = setup("/player/nika_down-2");
        left1 = setup("/player/nika_left_run1");
        left2 = setup("/player/nika_left_run2");
        right1 = setup("/player/nika_right_run1");
        right2 = setup("/player/nika_right_run2");
    }


    public void update(){
        if(keyH.upPressed == true || keyH.downPressed == true ||
        keyH.leftPressed == true || keyH.rightPressed == true){

            if(keyH.upPressed== true) {
                direction = "up";
            } else if(keyH.downPressed == true){
                direction = "down";
            } else if(keyH.leftPressed == true){
                direction = "left";
            } else if(keyH.rightPressed == true){
                direction = "right";
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
           int objIndex =  gp.cChecker.checkObject(this, true);
           pickUpObject(objIndex);

           //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false){
                switch(direction){
                    case "up":
                        worldY -= speed; //player goes up
                        break;
                    case "down":
                        worldY += speed;//player goes down
                        break;
                    case "left":
                        worldX -= speed; //player goes left
                        break;
                    case "right":
                        worldX += speed; //player goes right
                        break;
                }
            }

            //animation -> image changes every time frame
            spriteCounter++;
            if(spriteCounter > 12){
                if (spriteNum == 1){
                    spriteNum = 2;
                }
                else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }



    }

    public void pickUpObject(int i){
        if(i != 999){
            /*String objectName = gp.obj[i].name;
            switch(objectName){
                case "Key":
                    gp.playSE(1); //coin sound
                    hasKey++;
                    gp.obj[i] = null; //the object disappear when player touch it
                    gp.ui.showMessage("You got a key!");
                    break;
                case "Door":
                    gp.playSE(3); //unlock sound
                    //if player has a key the door disappear and decrements the number of key
                    if(hasKey > 0){
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("You opened the door!");
                    }
                    else{
                        gp.ui.showMessage("You need a key!");
                    }

                    break;
                case "Boots": //boots increase player speed
                    gp.playSE(2); //powerup sound
                    speed += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed up!");
                    break;
                case "Chest": //if the player take the chest -> win the game! = stop the game
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
            }*/
        }
    }

    public void interactNPC(int i){
        if(i != 999){
            if(gp.keyH.enterPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }

        }
        gp.keyH.enterPressed = false;
    }
    public void draw(Graphics2D g2){
       // g2.setColor(Color.white);
       // g2.fillRect(x, y, gp.tileSize, gp.tileSize); //fillRect( x, y, width, height ) -> draw a rectangle

        BufferedImage image = null;
        switch(direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX,screenY,  null);
    }
}
