package data;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.io.*;

public class SaveLoad {
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void save(){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));

            DataStorage ds = new DataStorage();
            //pass game data to DataStorage class
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            //PLAYER INVENTORY
            for(int i = 0; i < gp.player.inventory.size();i++){
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }
            //PLAYER EQUIPMENT
            ds.currentWeaponSlot = gp.player.getCurrentWeaponSLot();
            ds.currentShieldSlot = gp.player.getCurrentShieldSLot();

            //OBJECTS ON MAP
            ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++){
                for(int i = 0; i < gp.obj[1].length; i++){
                    if(gp.obj[mapNum][i] == null){ //if it's empty
                        ds.mapObjectNames[mapNum][i] = "NA";
                    }
                    else { //if it's not empty
                        ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
                        if(gp.obj[mapNum][i].loot != null){ //check if there is a loot(only chests)
                            ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
                        }
                        ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
                    }
                }
            }

            //write the DataStorage object (ds)
            oos.writeObject(ds);

        } catch(Exception e){
            e.printStackTrace();
        }

    }
    public void load(){
        try{

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            //read the DataStorage object
            DataStorage ds = (DataStorage) ois.readObject();
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            //PLAYER INVENTORY
            gp.player.inventory.clear(); //remove all default items
            for(int i = 0; i < ds.itemNames.size();i++){
                gp.player.inventory.add(gp.eGenerator.getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }

            //PLAYER EQUIPMENT
            gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
            gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
            gp.player.getAttack();
            gp.player.getDefense();
            gp.player.getAttackImage();

            //OBJECTS ON MAP
            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++){
                for(int i = 0; i < gp.obj[1].length; i++){
                    if(ds.mapObjectNames[mapNum][i].equals("NA")){ //if it's empty
                        gp.obj[mapNum][i] = null;
                    }
                    else { //if it's not empty
                        gp.obj[mapNum][i] = gp.eGenerator.getObject(ds.mapObjectNames[mapNum][i]);
                        gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                        gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];

                        if (ds.mapObjectLootNames[mapNum][i] != null) { //check if there is a loot(only chests)
                            gp.obj[mapNum][i].setLoot(gp.eGenerator.getObject(ds.mapObjectLootNames[mapNum][i]));
                        }
                        gp.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
                        if(gp.obj[mapNum][i].opened == true){ //if it's open change image
                            gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2;
                        }
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
