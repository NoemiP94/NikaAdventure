package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    //BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00"); //to format time

    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);//text font
        arial_80B = new Font("Arial", Font.BOLD, 80);//text font
        //OBJ_Key key = new OBJ_Key(gp);
        //keyImage = key.image;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        if(gp.gameState == gp.playState){

        }
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
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

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);

        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}
