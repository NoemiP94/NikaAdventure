package entity;

import main.GamePanel;

public class Projectile extends Entity{
    Entity user;
    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user){
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife; //reset the life to the max value every time you shoot it

    }
    public void update(){
        if(user == gp.player){
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if(monsterIndex != 999){
                gp.player.damageMonster(monsterIndex, attack); //fireball attack
                alive = false; //if the projectile hits a monster, it disappears
            }
        }
        if(user != gp.player){
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if(gp.player.invincible == false && contactPlayer == true){
                damagePlayer(attack);
                alive = false;
            }
        }
        switch (direction){
            case "up":
                worldY -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "left":
                worldX -= speed;
                break;
            case "right":
                worldX += speed;
                break;
        }
        //when shoot a projectile, it gradually loses his life
        //when life is 0, alive is false, and you can shoot another projectile
        life--;
        if(life <= 0){
            alive = false;
        }

        spriteCounter++;
        if(spriteCounter > 12){
            if (spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    public boolean haveResource(Entity user){
        boolean haveResource = false;
        return haveResource;
    }

    public void subtractResource(Entity user){


    }
}
