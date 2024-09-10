package main;

import entity.Entity;
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
    Font pixelify;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    //BufferedImage keyImage;
    public boolean messageOn = false;
    //public String message = "";
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    //int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;


    //double playTime;
    //DecimalFormat dFormat = new DecimalFormat("#0.00"); //to format time

    public UI(GamePanel gp){
        this.gp = gp;
        //arial_40 = new Font("Arial", Font.PLAIN, 40);//text font
        //arial_80B = new Font("Arial", Font.BOLD, 80);//text font

        //Custom Font -> import from file

        try {
            InputStream is = getClass().getResourceAsStream("/font/PixelifySans-VariableFont_wght.ttf");
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
            drawPlayerLife();

        }

        //PAUSE STATE
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
            drawPlayerLife();
        }

        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
            drawPlayerLife();
        }

        //CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }

        /*if(gameFinished == true){
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            String text;
            int textLength;
            int x;
            int y;

            text = "You found the treasure!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); //return the length of the text
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - gp.tileSize*3;
            g2.drawString(text,x,y);

            text = "Your time is: !" + dFormat.format(playTime) + "!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); //return the length of the text
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + gp.tileSize*4;
            g2.drawString(text,x,y);


            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); //return the length of the text
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + gp.tileSize*2;
            g2.drawString(text,x,y);

            //stop the game
            gp.gameThread = null; //stop the thread

        } else {
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize,gp.tileSize , null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            //TIME
            playTime += (double)1/60;
            g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize*11, 65);

            //MESSAGE
            if(messageOn == true){
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gp.tileSize/2, gp.tileSize*5);

                messageCounter++;

                if(messageCounter > 120){ //120 fps -> 2 seconds
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }*/

    }

    public void drawPlayerLife(){

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        //DRAW MAX LIFE
        while(i < gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x += gp.tileSize;
        }

        //RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //DRAW CURRENT LIFE
        while(i < gp.player.life){
            g2.drawImage(heart_half,x,y,null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full,x,y, null);
            }
            i++;
            x += gp.tileSize;
        }

        //DRAW MAX MANA
        x = (gp.tileSize/2)-5;
        y = (int)(gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.maxMana){
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        //DRAW MANA
        x = (gp.tileSize/2)-5;
        y = (int)(gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.mana){
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
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
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,72F));
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
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;
        drawSubWindow(x,y,width,height);

        //TEXT
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }
    }

    public void drawCharacterScreen(){
        //create a frame
        final int frameX = gp.tileSize;
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

    public void drawInventory(){
        //frame
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubWindow(frameX, frameY, frameWidth,frameHeight);

        //slot -> 5 x 4
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize+3;

        //draw player's items
        for(int i = 0; i < gp.player.inventory.size(); i++){
            //equip cursor
            if(gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield){
                g2.setColor(new Color(240,190,90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize,gp.tileSize, 10 ,10);
            }
            g2.drawImage(gp.player.inventory.get(i).down1, slotX,slotY,null);
            slotX += slotSize; //go to next slot
            if(i == 4 || i == 9 || i == 14){ // if i is 4 go to next col
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        //cursor
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

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()){
            drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);
            for(String line: gp.player.inventory.get(itemIndex).description.split("\n")){
                g2.drawString(line,textX,textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot(){
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
