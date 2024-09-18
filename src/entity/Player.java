package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Player extends Entity{

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public boolean attackCanceled = false;

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
        //attack area
        //attackArea.width = 36;
        //attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        //player position on world map
//        worldX = gp.tileSize * 23;
//        worldY= gp.tileSize * 21;
        worldX = gp.tileSize * 12;
        worldY= gp.tileSize * 12;
        gp.currentMap = 1;

        speed = 4;
        direction = "down";

        //PLAYER STATUS -> 1 life = 2 hearts
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        strength = 1; //more strength, more damage he gives
        dexterity = 1; //more dexterity, less damage receive
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        //projectile = new OBJ_Rock(gp);
        attack = getAttack(); // total attack value = strength + weapon
        defense = getDefense(); // total defense = dexterity + shield

    }
    public void setDefaultPositions(){
        //player position on world map
        worldX = gp.tileSize * 23;
        worldY= gp.tileSize * 21;
        direction = "down";
    }
    public void restoreLifeAndMana(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }
    public void setItems(){
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }
    public int getAttack(){
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
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
        if(currentWeapon.type == type_sword){
            attackUp1 = setup("/player/nika_attack_up_1", gp.tileSize,gp.tileSize*2);
            attackUp2 = setup("/player/nika_attack_up_2", gp.tileSize,gp.tileSize*2);
            attackDown1 = setup("/player/nika_attack_down_1", gp.tileSize,gp.tileSize*2);
            attackDown2 = setup("/player/nika_attack_down_2", gp.tileSize,gp.tileSize*2);
            attackLeft1 = setup("/player/nika_attack_left_1", gp.tileSize*2,gp.tileSize);
            attackLeft2 = setup("/player/nika_attack_left_2", gp.tileSize*2,gp.tileSize);
            attackRight1 = setup("/player/nika_attack_right_1", gp.tileSize*2,gp.tileSize);
            attackRight2 = setup("/player/nika_attack_right_2", gp.tileSize*2,gp.tileSize);
        }
        if(currentWeapon.type == type_axe){
            attackUp1 = setup("/player/nika_axe_up1", gp.tileSize,gp.tileSize*2);
            attackUp2 = setup("/player/nika_axe_up2", gp.tileSize,gp.tileSize*2);
            attackDown1 = setup("/player/nika_axe_down1", gp.tileSize,gp.tileSize*2);
            attackDown2 = setup("/player/nika_axe_down2", gp.tileSize,gp.tileSize*2);
            attackLeft1 = setup("/player/nika_axe_left1", gp.tileSize*2,gp.tileSize);
            attackLeft2 = setup("/player/nika_axe_left2", gp.tileSize*2,gp.tileSize);
            attackRight1 = setup("/player/nika_axe_right1", gp.tileSize*2,gp.tileSize);
            attackRight2 = setup("/player/nika_axe_right2", gp.tileSize*2,gp.tileSize);
        }

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

            //CHECK INTERACTIVE TILES COLLISION
            int iTileIndex = gp.cChecker.checkEntity(this,gp.iTile);

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

            if(keyH.enterPressed == true && attackCanceled == false){
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;

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

        if(gp.keyH.shotKeyPressed == true && projectile.alive == false
                && shotAvailableCounter == 30 && projectile.haveResource(this) == true){ //you cannot shoot a projectile if the previous projectile is still alive
            //set default coordinate, direction and user
            projectile.set(worldX, worldY, direction, true, this);

            //SUBTRACT COST
            projectile.subtractResource(this);

            //add it to the list
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
            gp.playSE(10);
        }

        //this needs to be outside of key if statement
        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        //you can't shoot another projectile in the next 30 seconds
        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        if(life > maxLife){
            life = maxLife;
        }
        if(mana > maxMana){
            mana = maxMana;
        }
        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.playSE(12);
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
            damageMonster(monsterIndex, attack); //sword or axe attack

            //check interactive tile collision
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            //restore original data
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
        //pickup only items
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly){
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
        //inventory items
        else {
            String text;
            if(inventory.size() != maxInventorySize){ //if inventory is not full
                inventory.add(gp.obj[gp.currentMap][i]);
                gp.playSE(1);
                text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
            } else {
                text = "You cannot carry any more!";
            }
            gp.ui.addMessage(text);
            gp.obj[gp.currentMap][i] = null;
            }
        }

    }

    public void interactNPC(int i){

        if(gp.keyH.enterPressed == true){
            if(i != 999){
                 attackCanceled = true;
                 gp.gameState = gp.dialogueState;
                 gp.npc[gp.currentMap][i].speak();

            }
        }
    }

    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false && gp.monster[gp.currentMap][i].dying == false){
                gp.playSE(6);
                int damage = attack + gp.monster[gp.currentMap][i].attack - defense;
                if(damage <= 0){
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }

        }
    }

    public void damageMonster(int i, int attack){
        if(i != 999){
           if(gp.monster[gp.currentMap][i].invincible == false){
               gp.playSE(5);

               int damage = attack - gp.monster[gp.currentMap][i].defense;
               if(damage <= 0){
                   damage = 0;
               }
               gp.monster[gp.currentMap][i].life -= damage;
               gp.ui.addMessage(damage + "damage!");
               gp.monster[gp.currentMap][i].invincible = true;
               gp.monster[gp.currentMap][i].damageReaction();

               if(gp.monster[gp.currentMap][i].life <= 0){
                   gp.monster[gp.currentMap][i].dying = true;
                   gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                   gp.ui.addMessage("Exp" + gp.monster[gp.currentMap][i].exp);
                   exp += gp.monster[gp.currentMap][i].exp;
                   checkLevelUp();

               }
           }
        }
    }
    public void damageInteractiveTile(int i){
        if(i != 999 && gp.iTile[gp.currentMap][i].destructible == true
                && gp.iTile[gp.currentMap][i].isCorrectItem(this) == true
                && gp.iTile[gp.currentMap][i].invincible == false) {
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            //generate particle
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if(gp.iTile[gp.currentMap][i].life == 0){
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }

    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp*2;
            maxLife += 2; //1 heart
            strength++;
            dexterity++;
            attack = getAttack(); //recalculate attack
            defense = getDefense(); //recalculate defense
            gp.playSE(8);

            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n" + "You feel stronger!";

        }
    }

    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot();
        if(itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);
            //if we are selected an item we check its type
            if(selectedItem.type == type_sword || selectedItem.type == type_axe){
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if(selectedItem.type == type_shield){
                currentShield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex); //remove item after using
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
