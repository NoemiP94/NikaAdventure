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

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues(){
        //player position on world map
        worldX = gp.tileSize * 23;
        worldY= gp.tileSize * 21;

        speed = 4;
        direction = "down";

        //PLAYER STATUS -> 1 life = 2 hearts
        maxLife = 6;
        life = maxLife;
    }

    //load player images
    public void getPlayerImage(){
        up1 = setup("/player/nika_up1", gp.tileSize,gp.tileSize);
        up2 = setup("/player/nika_up2", gp.tileSize,gp.tileSize);
        down1 = setup("/player/nika_down-1", gp.tileSize,gp.tileSize);
        down2 = setup("/player/nika_down-2", gp.tileSize,gp.tileSize);
        left1 = setup("/player/nika_left_run1", gp.tileSize,gp.tileSize);
        left2 = setup("/player/nika_left_run2", gp.tileSize,gp.tileSize);
        right1 = setup("/player/nika_right_run1", gp.tileSize,gp.tileSize);
        right2 = setup("/player/nika_right_run2", gp.tileSize,gp.tileSize);
    }

    public void getPlayerAttackImage(){
        attackUp1 = setup("/player/nika_attack_up_1", gp.tileSize,gp.tileSize*2);
        attackUp2 = setup("/player/nika_attack_up_2", gp.tileSize,gp.tileSize*2);
        attackDown1 = setup("/player/nika_attack_down_1", gp.tileSize,gp.tileSize*2);
        attackDown2 = setup("/player/nika_attack_down_2", gp.tileSize,gp.tileSize*2);
        attackLeft1 = setup("/player/nika_attack_left_1", gp.tileSize*2,gp.tileSize);
        attackLeft2 = setup("/player/nika_attack_left_2", gp.tileSize*2,gp.tileSize);
        attackRight1 = setup("/player/nika_attack_right_1", gp.tileSize*2,gp.tileSize);
        attackRight2 = setup("/player/nika_attack_right_2", gp.tileSize*2,gp.tileSize);
    }


    public void update(){

        if(attacking == true){

attacking();

        } else if(keyH.upPressed == true || keyH.downPressed == true ||
        keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {

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

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            contactMonster(monsterIndex);


            //CHECK EVENT
            gp.eHandler.checkEvent();



            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false && keyH.enterPressed == false){
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

            gp.keyH.enterPressed = false;

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

        //this needs to be outside of key if statement
        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    public void attacking(){
        //attack animation
        spriteCounter++;

        if(spriteCounter <= 5){
            spriteNum = 1; //0-5 frame ca.
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2; //6-25 frame ca.

            //check where weapon collision is, not to player collision is
            //save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX and worldY for the AttackArea
            switch(direction){
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }
            //attack area becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);
            worldX = currentWorldX;
            worldY = currentWorldY;
            //after checking collision, restore the original data
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if(spriteCounter > 25 ){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false; //reset -> finish attack animation
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

        if(gp.keyH.enterPressed == true){
            if(i != 999){
                 gp.gameState = gp.dialogueState;
                 gp.npc[i].speak();
            } else {
                 attacking = true;
            }
        }
    }

    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false){
                life -= 1;
                invincible = true;
            }

        }
    }

    public void damageMonster(int i){
        if(i != 999){
           if(gp.monster[i].invincible == false){
               gp.monster[i].life -= 1;
               gp.monster[i].invincible = true;

               if(gp.monster[i].life <= 0){
                   gp.monster[i] = null;
               }
           }
        }
    }
    public void draw(Graphics2D g2){
       // g2.setColor(Color.white);
       // g2.fillRect(x, y, gp.tileSize, gp.tileSize); //fillRect( x, y, width, height ) -> draw a rectangle

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction){
            case "up":
                if(attacking == false){
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                }
                if(attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1){
                        image = attackUp1;
                    }
                    if(spriteNum == 2){
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if(attacking == false){
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2) {
                        image = down2;
                    }
                }
                if(attacking == true){
                    if(spriteNum == 1){
                        image = attackDown1;
                    }
                    if(spriteNum == 2) {
                        image = attackDown2;
                    }
                }

                break;
            case "left":
                if(attacking == false){
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                }

                if(attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1){
                        image = attackLeft1;
                    }
                    if(spriteNum == 2){
                        image = attackLeft2;
                    }
                }

                break;
            case "right":
                if(attacking == false){
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                }
                if(attacking == true){
                    if(spriteNum == 1){
                        image = attackRight1;
                    }
                    if(spriteNum == 2){
                        image = attackRight2;
                    }
                }

                break;
        }

        //visual effect to invincible state
        if(invincible == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); //transparance
        }
        g2.drawImage(image, tempScreenX,tempScreenY,  null);

        //Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //DEBUG
        //g2.setFont(new Font("Arial", Font.PLAIN, 26));
        //g2.setColor(Color.white);
        //g2.drawString("Invincible: " + invincibleCounter,10,400);
    }
}
