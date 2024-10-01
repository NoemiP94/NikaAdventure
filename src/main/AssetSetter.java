package main;

import entity.NPC_Merchant;
import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import monster.MON_Orc;
import monster.MON_RedSlime;
import object.*;
import tile_interactive.IT_DestructibleWall;
import tile_interactive.IT_DryTree;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    //create and place objects in the map
    public void setObject(){
        int mapNum = 0;
        int i = 0;
        gp.obj[mapNum][i] = new OBJ_Axe(gp);
        gp.obj[mapNum][i].worldX = 33 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 7 * gp.tileSize;
        i++;
//        gp.obj[mapNum][i] = new OBJ_Door(gp);
//        gp.obj[mapNum][i].worldX = 14 * gp.tileSize;
//        gp.obj[mapNum][i].worldY = 28 * gp.tileSize;
//        i++;
        gp.obj[mapNum][i] = new OBJ_Door(gp);
        gp.obj[mapNum][i].worldX = 12 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 12 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Key(gp));
        gp.obj[mapNum][i].worldX = 30 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 29 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Lantern(gp);
        gp.obj[mapNum][i].worldX = 18 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 20 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Tent(gp);
        gp.obj[mapNum][i].worldX = 19 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 20 * gp.tileSize;
        i++;

        mapNum = 2;
        i= 0;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Pickaxe(gp));
        gp.obj[mapNum][i].worldX = 40 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 41 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = 13 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 16 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = 26 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 34 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = 27 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 15 * gp.tileSize;
        i++;
    }

    public void setNPC(){
        int mapNum = 0;
        int i = 0;
        gp.npc[mapNum][0] = new NPC_OldMan(gp);
        gp.npc[mapNum][0].worldX = gp.tileSize*21;
        gp.npc[mapNum][0].worldY = gp.tileSize*21;
        i++;

        mapNum = 1;
        i = 0;
        gp.npc[mapNum][0] = new NPC_Merchant(gp);
        gp.npc[mapNum][0].worldX = gp.tileSize*12;
        gp.npc[mapNum][0].worldY = gp.tileSize*7;
        i++;


    }

    public void setMonster(){
        int mapNum = 0;
        int i = 0;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*23;
        gp.monster[mapNum][i].worldY = gp.tileSize*36;
        i++;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*23;
        gp.monster[mapNum][i].worldY = gp.tileSize*37;
        i++;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*24;
        gp.monster[mapNum][i].worldY = gp.tileSize*37;
        i++;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*34;
        gp.monster[mapNum][i].worldY = gp.tileSize*42;
        i++;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*38;
        gp.monster[mapNum][i].worldY = gp.tileSize*42;
        i++;
//        gp.monster[mapNum][i] = new MON_Orc(gp);
//        gp.monster[mapNum][i].worldX = gp.tileSize*12;
//        gp.monster[mapNum][i].worldY = gp.tileSize*33;
//        i++;
        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*35;
        gp.monster[mapNum][i].worldY = gp.tileSize*10;
        i++;
        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*37;
        gp.monster[mapNum][i].worldY = gp.tileSize*8;
        i++;
        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*39;
        gp.monster[mapNum][i].worldY = gp.tileSize*10;
        i++;

//        mapNum = 1;
//        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
//        gp.monster[mapNum][i].worldX = gp.tileSize*38;
//        gp.monster[mapNum][i].worldY = gp.tileSize*42;
//        i++;
    }

    public void setInteractiveTile(){
        int mapNum = 0;
        int i = 0;
        //go to healing pool
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 27,12);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,28,12);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,29,12);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,30,12);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,31,12);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,32,12);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,33,12);
        i++;
        //go to hut
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 18,40);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,17,40);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,16,40);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,15,40);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,14,40);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,13,40);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,13,41);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,12,41);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,11,41);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,10,41);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,10,40);
        i++;


        gp.iTile[mapNum][i] = new IT_DryTree(gp,31,21);
        i++;

        //to chest
        gp.iTile[mapNum][i] = new IT_DryTree(gp,25,27);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,26,27);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,27,28);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,27,29);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,27,30);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,27,31);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,28,31);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,29,31);
        i++;

        mapNum = 2;
        i = 0;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,30);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,17,31);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,17,32);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,17,34);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,34);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,33);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,10,22);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,10,24);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,18);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,19);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,20);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,21);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,13);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,14);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,22,28);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,30,28);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,32,28);i++;





    }
}
