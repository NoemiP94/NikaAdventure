package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC_OldMan extends Entity{
    public NPC_OldMan(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;
        getImage();
    }

    public void getImage(){
        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_1");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_1");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_1");
    }
}
