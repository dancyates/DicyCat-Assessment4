package com.dicycat.kroy.misc;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.Kroy;

public class PowerUp extends GameObject {
    private Integer powerUpType;
    private Rectangle hitbox;

    public PowerUp(Vector2 spawnPos,Texture image,int type) {
        super(spawnPos, image, new Vector2(40,40));    //Constructor; takes the screen to be put on, spawn position vector, image and a vector for its size
        powerUpType = type;
        setPosition(spawnPos);
        hitbox = new Rectangle(spawnPos.x,spawnPos.y,40,40);
    }

    public static int generatePowerUpType(){
        Random r = new Random();
        return r.nextInt(7);
    }

    public void update() {
        if (Intersector.overlaps(hitbox, Kroy.mainGameScreen.getPlayer().getHitbox())){
            Kroy.mainGameScreen.gainPowerUp(powerUpType);
            die();
        }
    }
    
    public Integer getPowerUpType() {
		return powerUpType;
	}
}
