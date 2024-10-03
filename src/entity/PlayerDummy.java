package entity;

import main.GamePanel;

public class PlayerDummy extends Entity{

    public static final String npcName = "Dummy";
    public PlayerDummy(GamePanel gp) {
        super(gp);
        name = npcName;
        getImage();
    }

    public void getImage(){
        up1 = setup("/player/nika_up1", gp.tileSize,gp.tileSize);
        up2 = setup("/player/nika_up2", gp.tileSize,gp.tileSize);
        down1 = setup("/player/nika_down-1", gp.tileSize,gp.tileSize);
        down2 = setup("/player/nika_down-2", gp.tileSize,gp.tileSize);
        left1 = setup("/player/nika_left_run1", gp.tileSize,gp.tileSize);
        left2 = setup("/player/nika_left_run2", gp.tileSize,gp.tileSize);
        right1 = setup("/player/nika_right_run1", gp.tileSize,gp.tileSize);
        right2 = setup("/player/nika_right_run2", gp.tileSize,gp.tileSize);
    }
}
