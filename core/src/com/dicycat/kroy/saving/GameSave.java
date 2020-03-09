package com.dicycat.kroy.saving;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;

public class GameSave {
	
	private List<GameObject> gameObjects;
	private Vector2 spawnPos;

	public GameSave() {
		gameObjects = new ArrayList<GameObject>();
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setSpawnPos(Vector2 Pos){
		this.spawnPos = Pos;
	}

	public void addGameObjects(List<GameObject> newGameObjects) {
		this.gameObjects.addAll(newGameObjects);
	}
	
	public void addGameObject(GameObject newGameObject) {
		this.gameObjects.add(newGameObject);
	}

}
