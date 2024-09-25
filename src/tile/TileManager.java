package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][][];
    boolean drawPath = true;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    public TileManager(GamePanel gp){
        this.gp = gp;

        //read tile data file
        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //getting tile names and collision info form the file
        String line;

        try{
            while((line = br.readLine()) != null){
                fileNames.add(line); //for 1st line
                collisionStatus.add(br.readLine()); //for 2nd line
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }


        tile = new Tile[fileNames.size()]; //we know exactly how many tiles we are using
        getTileImage();

        //get the maxWorldCol & maxWorldRow
        is = getClass().getResourceAsStream("/maps/worldmap.txt");
        br = new BufferedReader(new InputStreamReader(is));

        try{
            String line2 = br.readLine();
            String[] maxTile = line2.split(" ");

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;

            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        loadMap("/maps/worldmap.txt", 0);
        loadMap("/maps/indoor01.txt", 1);

    }

    public void getTileImage(){
        for(int i = 0; i < fileNames.size();i++){
            String fileName;
            boolean collision;

            //get a file name
            fileName = fileNames.get(i);

            //get a collision status
            if(collisionStatus.get(i).equals("true")){
                collision = true;
            } else {
                collision = false;
            }
            setup(i, fileName, collision);
        }

    }


    public void setup(int index, String imageName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName));
            tile[index].image = uTool.scaledImage(tile[index].image, gp.tileSize,gp.tileSize);
            tile[index].collision = collision;
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //read .txt map
    public void loadMap(String filePath, int map){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col< gp.maxWorldCol && row <gp.maxWorldRow){
                String line = br.readLine(); //read a single line of text
                while(col< gp.maxWorldCol){
                    String numbers[] = line.split(" "); //split the space between numbers
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (Exception e){

        }
    }

    //draw background
    public void draw(Graphics2D g2){
        //g2.drawImage(tile[0].image, 0,0, gp.tileSize, gp.tileSize, null); //draw grass
        //g2.drawImage(tile[1].image, 48,0, gp.tileSize, gp.tileSize, null); //draw wall
        //g2.drawImage(tile[2].image, 96,0, gp.tileSize, gp.tileSize, null); //draw water
        int worldCol = 0;
        int worldRow = 0;
        //int x = 0;
        //int y = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            //implement camera

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize< gp.player.worldY + gp.player.screenY){
                    g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }


            worldCol++;
            //x += gp.tileSize;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                //x = 0;
                worldRow++;
                //y += gp.tileSize;
            }
        }
//        if(drawPath == true){
//            g2.setColor(new Color(255,0,0,70));
//
//            for(int i = 0; i < gp.pFinder.pathList.size(); i++){
//                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
//                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
//                int screenX = worldX - gp.player.worldX + gp.player.screenX;
//                int screenY = worldY - gp.player.worldY + gp.player.screenY;
//
//                g2.fillRect(screenX,screenY,gp.tileSize,gp.tileSize);
//            }
//        }

    }
}
