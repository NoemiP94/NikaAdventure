package main;

import entity.Entity;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_Mana_Crystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    public Font pixelify;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    //BufferedImage keyImage;
    public boolean messageOn = false;
    //public String message = "";
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    //int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity npc;
    int charIndex = 0;
    String combinedText = "";

    public UI(GamePanel gp){
        this.gp = gp;
        //arial_40 = new Font("Arial", Font.PLAIN, 40);//text font
        //arial_80B = new Font("Arial", Font.BOLD, 80);//text font

        //Custom Font -> import from file

        try {
            InputStream is = getClass().getResourceAsStream("/font/PixelOperator.ttf");
            pixelify = Font.createFont(Font.TRUETYPE_FONT, is);

        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //OBJ_Key key = new OBJ_Key(gp);
        //keyImage = key.image;

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_Mana_Crystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        coin = bronzeCoin.down1;
    }

    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(pixelify);
        g2.setColor(Color.white);

        //TITLE STATE
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        //PLAY STATE
        if(gp.gameState == gp.playState){
            drawMessage();
            drawMonsterLife();
            drawPlayerLife();
        }

        //PAUSE STATE
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();

        }

        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();

        }

        //CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        //OPTION STATE
        if(gp.gameState == gp.optionState){
            drawOptionsScreen();
        }
        //GAME OVER STATE
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        //TRANSITION STATE
        if(gp.gameState == gp.transitionState){
            drawTransition();
        }
        //TRADE STATE
        if(gp.gameState == gp.tradeState){
            drawTradeScreen();
        }
        //SLEEP STATE
        if(gp.gameState == gp.sleepState){
            drawSleepScreen();
        }

    }
    public void drawPlayerLife(){

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        int iconSize = 32;
        int manaStartX = (gp.tileSize/2)-5;
        int manaStartY = 0;

        //DRAW MAX LIFE
        while(i < gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y,iconSize,iconSize,null);
            i++;
            x += iconSize;
            manaStartY = y + 32;

            if(i % 8 == 0){
                x = gp.tileSize/2;
                y += iconSize;
            }
        }

        //RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //DRAW CURRENT LIFE
        while(i < gp.player.life){
            g2.drawImage(heart_half,x,y,iconSize,iconSize,null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full,x,y, iconSize,iconSize,null);
            }
            i++;
            x += iconSize;

            if(i % 16 == 0){
                x = gp.tileSize/2;
                y += iconSize;
            }
        }

        //DRAW MAX MANA
        x = manaStartX;
        y = manaStartY;
        i = 0;
        while(i < gp.player.maxMana){
            g2.drawImage(crystal_blank, x, y,iconSize,iconSize, null);
            i++;
            x += 20;

            if(i % 10 == 0){
                x = manaStartX;
                y += iconSize;
            }
        }

        //DRAW MANA
        x = manaStartX;
        y = manaStartY;
        i = 0;
        while(i < gp.player.mana){
            g2.drawImage(crystal_full, x, y,iconSize,iconSize, null);
            i++;
            x += 20;

            if(i % 10 == 0){
                x = manaStartX;
                y += iconSize;
            }
        }
    }
    public void drawMonsterLife(){

        for(int i = 0; i < gp.monster[1].length; i++){ //scan the monster array

            Entity monster = gp.monster[gp.currentMap][i];

            if(monster != null  //check if the slot is null
                    && monster.inCamera() == true){
                if(monster.hpBarOn == true && monster.boss == false){

                    double oneScale = (double)gp.tileSize/monster.maxLife; //divided hp bar in equal sections depending on maxLife
                    double hpBarValue = oneScale*monster.life;

                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(monster.getScreenX() -1 ,monster.getScreenY() - 16, gp.tileSize+2,12);
                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(monster.getScreenX(),monster.getScreenY() - 15, (int)hpBarValue,10);

                    monster.hpBarCounter++;

                    if(monster.hpBarCounter > 600){ //the hp bar disappear after 10 seconds
                        monster.hpBarCounter = 0;
                        monster.hpBarOn = false;
                    }
                } else if(monster.boss == true){
                    double oneScale = (double)gp.tileSize*8/monster.maxLife; //divided hp bar in equal sections depending on maxLife
                    double hpBarValue = oneScale*monster.life;

                    int x = gp.screenWidth/2 - gp.tileSize*4;
                    int y = gp.tileSize*10;

                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(x-1 ,y-1, gp.tileSize*8+2,22);
                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(x,y, (int)hpBarValue,20);

                    g2.setFont(g2.getFont().deriveFont(Font.BOLD,24f));
                    g2.setColor(Color.white);
                    g2.drawString(monster.name, x+4, y-10);
                }
            }
        }


    }
    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i<message.size();i++ ){
            if(message.get(i) != null){ //to avoid null pointer exception
                g2.setColor(Color.black);
                g2.drawString(message.get(i),messageX+2,messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i),messageX,messageY);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;

                if(messageCounter.get(i) > 180){ //180 -> 3 seconds
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    public void drawTitleScreen(){

        g2.setColor(new Color(0,0,0));
        g2.fillRect(0,0, gp.screenWidth,gp.screenHeight);
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String text = "Nika's Adventure";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;

        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text,x+5,y+5);

        //MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //PLAYER IMAGE
        x = gp.screenWidth/2 - (gp.tileSize*2)/2;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x,y, gp.tileSize*2, gp.tileSize*2, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize*3.5;
        g2.drawString(text, x,y);
        if(commandNum == 0){
            g2.drawString(">",x-gp.tileSize,y);
        }

        text = "LOAD GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x,y);
        if(commandNum == 1){
            g2.drawString(">",x-gp.tileSize,y);
        }

        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x,y);
        if(commandNum == 2){
            g2.drawString(">",x-gp.tileSize,y);
        }
    }
    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);

        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }
    public void drawDialogueScreen(){
        //WINDOW
        int x = gp.tileSize*3;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*6);
        int height = gp.tileSize*4;
        drawSubWindow(x,y,width,height);

        //TEXT
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
        x += gp.tileSize;
        y += gp.tileSize;

        //check if any text is in the dialogues array
        if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null){
            //display the text in the array
            //currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];

            //display text letter by letter
            char[] characters = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();
            if(charIndex < characters.length){
                gp.playSE(17);
                String s = String.valueOf(characters[charIndex]); //convert to a string
                combinedText = combinedText+s; //add to combinedText
                currentDialogue = combinedText; //set it to currentDialogue
                charIndex++;
            }

            if(gp.keyH.enterPressed == true){ //if we press ENTER in this array
                //reset charIndex and combinedText
                charIndex = 0;
                combinedText ="";
                if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutsceneState){ //and if we are in dialogueState
                    //increase dialogueIndex
                    npc.dialogueIndex++;
                    gp.keyH.enterPressed = false;
                }
            }
        }
        else { //if no text is in the array -> conversation is over
            npc.dialogueIndex = 0;
            if(gp.gameState == gp.dialogueState){
                gp.gameState = gp.playState;
            }
            if(gp.gameState == gp.cutsceneState){
                gp.csManager.scenePhase++; //continue the cutscene
            }
        }

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }
    }
    public void drawCharacterScreen(){
        //create a frame
        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(28F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        //NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life",textX, textY);
        textY += lineHeight;
        g2.drawString("Mana",textX, textY);
        textY += lineHeight;
        g2.drawString("Strength",textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity",textX, textY);
        textY += lineHeight;
        g2.drawString("Attack",textX, textY);
        textY += lineHeight;
        g2.drawString("Defense",textX, textY);
        textY += lineHeight;
        g2.drawString("Exp",textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level",textX, textY);
        textY += lineHeight;
        g2.drawString("Coin",textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon",textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield",textX, textY);
        textY += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) - 30; //display to the right
        //reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level); //the int value becomes a string to display
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-24, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY-24, null);
        textY += gp.tileSize;



//        value = String.valueOf(gp.player.currentWeapon);
//        textX = getXForAlignToRightText(value, tailX);
//        g2.drawString(value, textX, textY);
//        textY += lineHeight;

//        value = String.valueOf(gp.player.currentShield);
//        textX = getXForAlignToRightText(value, tailX);
//        g2.drawString(value, textX, textY);
    }
    public void drawInventory(Entity entity, boolean cursor){ //entity -> inventory entity, cursor -> if we need cursor
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int slotCol;
        int slotRow;

        if(entity == gp.player){ //if entity is player

            frameX = gp.tileSize*12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }
        else { //if it's not player(for example merchant)
            frameX = gp.tileSize*2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        //frame
        drawSubWindow(frameX, frameY, frameWidth,frameHeight);

        //slot -> 5 x 4
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize+3;

        //draw entity items
        for(int i = 0; i < entity.inventory.size(); i++){
            //equip cursor
            if(entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield ||
                    entity.inventory.get(i) == entity.currentLight){
                g2.setColor(new Color(240,190,90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize,gp.tileSize, 10 ,10);
            }
            g2.drawImage(entity.inventory.get(i).down1, slotX,slotY,null);

            //display amount
            if(entity == gp.player && entity.inventory.get(i).amount > 1){
                g2.setFont(g2.getFont().deriveFont(32F));
                int amountX;
                int amountY;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXForAlignToRightText(s, slotX+44);
                amountY = slotY + gp.tileSize;

                //shadow
                g2.setColor(new Color(60,60,60));
                g2.drawString(s,amountX,amountY);
                //number
                g2.setColor(Color.white);
                g2.drawString(s, amountX-3, amountY-3);
            }

            slotX += slotSize; //go to next slot
            if(i == 4 || i == 9 || i == 14){ // if i is 4 go to next col
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        //cursor
        if(cursor == true){
            int cursorX = slotXStart + slotSize * slotCol;
            int cursorY = slotYStart + slotSize * slotRow;
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;
            //draw cursor
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3)); //border
            g2.drawRoundRect(cursorX,cursorY, cursorWidth, cursorHeight, 10,10);

            //description frame
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize*3;


            //draw description text
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(26F));

            int itemIndex = getItemIndexOnSlot(slotCol,slotRow);

            if(itemIndex < entity.inventory.size()){
                drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);
                for(String line: entity.inventory.get(itemIndex).description.split("\n")){
                    g2.drawString(line,textX,textY);
                    textY += 32;
                }
            }
        }

    }
    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110F));

        text = "Game Over";

        //shadow
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text,x,y);
        //text
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        //retry
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
        text = "Retry";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x,y);
        if(commandNum == 0){
            g2.drawString(">", x-40, y);
        }

        //back to title screen
        text = "Quit";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x,y);
        if(commandNum == 1){
            g2.drawString(">", x-40, y);
        }
    }
    public void drawOptionsScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        //sub window
        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth,frameHeight);

        switch(subState){
            case 0:
                options_top(frameX,frameY);
                break;
            case 1:
                options_fullScreenNotification(frameX,frameY);
                break;
            case 2:
                options_control(frameX,frameY);
                break;
            case 3:
                options_endGameConfirmation(frameX,frameY);
                break;

        }
        gp.keyH.enterPressed = false;

    }
    public void options_top(int frameX, int frameY){
        int textX;
        int textY;

        //TITLE
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY+gp.tileSize;
        g2.drawString(text, textX,textY);

        //FULL SCREEN ON/OFF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Full Screen", textX,textY);
        if(commandNum == 0){ //cursor
            g2.drawString(">", textX - 25,textY);
            if(gp.keyH.enterPressed == true){
                if(gp.fullScreenOn == false){
                    gp.fullScreenOn = true;
                }
                else if(gp.fullScreenOn == true){
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }

        }

        //MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX,textY);
        if(commandNum == 1){
            g2.drawString(">", textX - 25,textY);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("SE", textX,textY);
        if(commandNum == 2){
            g2.drawString(">", textX - 25,textY);
        }

        //CONTROL
        textY += gp.tileSize;
        g2.drawString("Control", textX,textY);
        if(commandNum == 3){
            g2.drawString(">", textX - 25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 2;
                commandNum = 0;
            }
        }

        //END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX,textY);
        if(commandNum == 4){
            g2.drawString(">", textX - 25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 3;
                commandNum = 0;
            }
        }

        //BACK
        textY += gp.tileSize*2;
        g2.drawString("Back", textX,textY);
        if(commandNum == 5){
            g2.drawString(">", textX - 25,textY);
            if(gp.keyH.enterPressed == true){
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        //FULL SCREEN CHECK BOX
        textX = frameX + (int)(gp.tileSize*4.5);
        textY = frameY + gp.tileSize*2 + 24;
        g2.setStroke(new BasicStroke(3)); //line width
        g2.drawRect(textX, textY,24,24);
        if(gp.fullScreenOn == true){
            g2.fillRect(textX, textY, 24,24);
        }

        //MUSIC VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX,textY, 120,24); // 120/5 = 24 -> 1 slot
        int volumeWidth = 24* gp.music.volumeScale;
        g2.fillRect(textX,textY, volumeWidth, 24);

        //SE VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX,textY, 120,24);
        volumeWidth = 24* gp.se.volumeScale;
        g2.fillRect(textX,textY, volumeWidth, 24);

        gp.config.saveConfig(); //save in config.txt
    }
    public void options_fullScreenNotification(int frameX, int frameY){
        int textX = frameX +gp.tileSize;
        int textY = frameY + gp.tileSize*3;
        currentDialogue = "The change will take \neffect after restarting \nthe game.";
        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, textX,textY);
            textY += 40;
        }

        //BACK
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX,textY);
        if(commandNum == 0){
            g2.drawString(">", textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
            }
        }

    }
    public void options_control(int frameX, int frameY){
        int textX;
        int textY;

        //TITLE
        String text = "Control";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX,textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX,textY);
        textY += gp.tileSize;
        g2.drawString("Confirm/Attack", textX,textY);
        textY += gp.tileSize;
        g2.drawString("Shoot/Cast", textX,textY);
        textY += gp.tileSize;
        g2.drawString("Character Screen", textX,textY);
        textY += gp.tileSize;
        g2.drawString("Pause", textX,textY);
        textY += gp.tileSize;
        g2.drawString("Options", textX,textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize*6;
        textY = frameY + gp.tileSize*2;
        g2.drawString("WASD", textX,textY);
        textY += gp.tileSize;
        g2.drawString("ENTER", textX,textY);
        textY += gp.tileSize;
        g2.drawString("F", textX,textY);
        textY += gp.tileSize;
        g2.drawString("C", textX,textY);
        textY += gp.tileSize;
        g2.drawString("P", textX,textY);
        textY += gp.tileSize;
        g2.drawString("ESC", textX,textY);
        textY += gp.tileSize;

        //BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX,textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 3;
            }
        }

    }
    public void options_endGameConfirmation(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "Quit the game and \nreturn to the \ntitle screen?";
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line,textX,textY);
            textY += 40;
        }

        //YES
        String text = "Yes";
        textX = getXForCenteredText(text);
        textY += gp.tileSize*3;
        g2.drawString(text, textX,textY);
        if(commandNum == 0){
            g2.drawString(">", textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                gp.gameState = gp.titleState;
                gp.stopMusic();
                gp.resetGame(true);
            }
        }

        //NO
        text = "No";
        textX = getXForCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX,textY);
        if(commandNum == 1){
            g2.drawString(">", textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 4;

            }
        }
    }
    public void drawTransition(){
        counter++;
        g2.setColor(new Color(0,0,0,counter*5)); //in every loop the alpha value increases by 5
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        if(counter == 50){ //the transition is done
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            //reset player's position
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
            gp.changeArea();
        }
    }
    public void drawTradeScreen(){
        switch(subState){
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        gp.keyH.enterPressed = false;
    }
    public void trade_select(){
        npc.dialogueSet = 0;
        drawDialogueScreen();

        //draw window
        int x = gp.tileSize * 15;
        int y = gp.tileSize * 4;
        int width = gp.tileSize * 3;
        int height = (int)(gp.tileSize * 3.5);
        drawSubWindow(x,y,width,height);

        //draw texts
        x += gp.tileSize;
        y += gp.tileSize;

        g2.drawString("Buy", x, y);
        if(commandNum == 0){
            g2.drawString(">", x-24 ,y);
            if(gp.keyH.enterPressed == true){
                subState = 1;
            }
        }
        y += gp.tileSize;

        g2.drawString("Sell", x, y);
        if(commandNum == 1){
            g2.drawString(">", x-24 ,y);
            if(gp.keyH.enterPressed == true){
                subState = 2;
            }
        }
        y += gp.tileSize;

        g2.drawString("Leave", x, y);
        if(commandNum == 2){
            g2.drawString(">", x-24 ,y);
            if(gp.keyH.enterPressed == true){
                commandNum = 0;
                npc.startDialogue(npc,1);
            }
        }
    }
    public void trade_buy(){
        //draw player inventory
        drawInventory(gp.player, false);

        //draw npc inventory
        drawInventory(npc, true);

        //draw hint window
        int x = gp.tileSize*2;
        int y = gp.tileSize*9;
        int width = gp.tileSize *6;
        int height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("[ESC] Back", x+24, y+60);

        //draw player coin window
        x = gp.tileSize*12;
        y = gp.tileSize*9;
        width = gp.tileSize *6;
        height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Your coin: " + gp.player.coin, x+24, y+60);

        //draw price window
        int itemIndex = getItemIndexOnSlot(npcSlotCol,npcSlotRow);
        if(itemIndex < npc.inventory.size()){
            x = (int)(gp.tileSize*5.5);
            y = (int)(gp.tileSize*5.5);
            width = (gp.tileSize*3);
            height = gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.drawImage(coin, x+10, y+8,32,32, null);
            int price = npc.inventory.get(itemIndex).price;
            String text = ""+price;
            x = getXForAlignToRightText(text,gp.tileSize*8-20);
            g2.drawString(text, x, y+32);

            //buy an item
            if(gp.keyH.enterPressed == true){
                //check if player has enough coin
                if(npc.inventory.get(itemIndex).price > gp.player.coin){
                    subState = 0;
                    npc.startDialogue(npc,2);
                }
                else{
                    if(gp.player.canObtainItem(npc.inventory.get(itemIndex)) == true){
                        gp.player.coin -= npc.inventory.get(itemIndex).price; //subtract price -> reduce coin
                    }
                    else {
                        subState = 0;
                        npc.startDialogue(npc,3);
                    }
                }
            }
        }
    }
    public void trade_sell(){
        //draw player inventory
        drawInventory(gp.player, true);

        int x;
        int y;
        int width;
        int height;

        //draw hint window
        x = gp.tileSize*2;
        y = gp.tileSize*9;
        width = gp.tileSize *6;
        height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("[ESC] Back", x+24, y+60);

        //draw player coin window
        x = gp.tileSize*12;
        y = gp.tileSize*9;
        width = gp.tileSize *6;
        height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Your coin: " + gp.player.coin, x+24, y+60);

        //draw price window
        int itemIndex = getItemIndexOnSlot(playerSlotCol,playerSlotRow);
        if(itemIndex < gp.player.inventory.size()){
            x = (int)(gp.tileSize*15.5);
            y = (int)(gp.tileSize*5.5);
            width = (gp.tileSize*3);
            height = gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.drawImage(coin, x+10, y+8,32,32, null);
            int price = gp.player.inventory.get(itemIndex).price/2;
            String text = ""+price;
            x = getXForAlignToRightText(text,gp.tileSize*18-20);
            g2.drawString(text, x, y+32);

            //sell an item
            if(gp.keyH.enterPressed == true){
                //prevent sell equip items
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
                        gp.player.inventory.get(itemIndex) == gp.player.currentShield ){
                    commandNum = 0;
                    subState = 0;
                    npc.startDialogue(npc,4);
                } else {
                    if(gp.player.inventory.get(itemIndex).amount > 1){
                        gp.player.inventory.get(itemIndex).amount--; //reduce amount
                    }
                    else {
                        gp.player.inventory.remove(itemIndex);
                    }
                    gp.player.coin += price;
                }
            }
        }


    }
    public void drawSleepScreen(){
        counter++;
        if(counter < 120){//the screen gets darker for the next 2 seconds
            gp.eManager.lighting.filterAlpha += 0.01f;
            if(gp.eManager.lighting.filterAlpha > 1f){
                gp.eManager.lighting.filterAlpha = 1f;
            }
        }
        if(counter >= 120){//the screen gets lighter for the next 2 seconds
            gp.eManager.lighting.filterAlpha -= 0.01f;
            if(gp.eManager.lighting.filterAlpha <= 0f){
                gp.eManager.lighting.filterAlpha = 0f;
                counter = 0;
                gp.eManager.lighting.dayState = gp.eManager.lighting.day;
                gp.eManager.lighting.dayCounter = 0;
                gp.gameState = gp.playState;
                gp.player.getImage();
            }
        }
    }
    public int getItemIndexOnSlot(int slotCol, int slotRow){
        int itemIndex = slotCol + (slotRow*5); //return the item index of the inventory list... it finds the coordinates of the selected item
        return itemIndex;
    }
    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height ,35,35);

        c= new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5, width-10, height-10,25,25);
    }
    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
    public int getXForAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}
