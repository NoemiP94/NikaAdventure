package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_BigRock extends Entity{
    public static final String npcName = "Big Rock";
    public NPC_BigRock(GamePanel gp) {
        super(gp);

        name = npcName;
        direction = "down";
        speed = 4;

        solidArea = new Rectangle();
        solidArea.x = 2;
        solidArea.y = 6;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 44;
        solidArea.height = 40;

        dialogueSet = -1;

        getImage();
        setDialogue();
    }

    public void getImage(){
        up1 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        up2 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        down1 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        down2 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        left1 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        left2 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        right1 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
        right2 = setup("/npc/bigrock", gp.tileSize,gp.tileSize);
    }

    public void setDialogue(){
        dialogues[0][0] = "It's a giant rock.";
    }

    //character behaviour
    public void setAction(){
    }

    public void speak(){
        facePlayer();
        startDialogue(this, dialogueSet);
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null){ //if there is no text
            dialogueSet--; //repeat the last dialogue set
        }
    }
}
