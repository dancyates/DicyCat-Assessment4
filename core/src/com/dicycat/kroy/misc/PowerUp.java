package com.dicycat.kroy.misc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;

public class PowerUp extends GameObject {
    private String powerUpType;

    public PowerUp(Vector2 spawnPos, Texture image, Vector2 imSize, String powerUpType) {
        super(spawnPos, image, imSize);    //Constructor; takes the screen to be put on, spawn position vector, image and a vector for its size
        this.powerUpType = powerUpType;

    }

    public void update() {

    }
    
    public String getPowerUpType() {
		return powerUpType;
	}
}
