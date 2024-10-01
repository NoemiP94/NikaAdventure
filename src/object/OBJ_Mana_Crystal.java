package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Mana_Crystal extends Entity {
    GamePanel gp;
    public static final String objName ="Mana Crystal";
    public OBJ_Mana_Crystal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = objName;
        value = 1;
        down1 = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }

    public boolean use(Entity entity){
        gp.playSE(1);
        gp.ui.addMessage("Mana +" + value);
        entity.mana += value;
        return true;
    }
}
