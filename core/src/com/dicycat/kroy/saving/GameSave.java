package com.dicycat.kroy.saving;

import java.util.ArrayList;
import java.util.List;
import com.dicycat.kroy.GameObject;

public class GameSave {
	
	private List<GameObject> gameObjects;
	private static List<GameSave> currentSaves = new ArrayList<GameSave>();
	
	public GameSave() {
		gameObjects = new ArrayList<GameObject>();
		currentSaves.add(this);
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void addGameObjects(List<GameObject> newGameObjects) {
		this.gameObjects.addAll(newGameObjects);
	}
	
	public void addGameObject(GameObject newGameObject) {
		this.gameObjects.add(newGameObject);
	}
	
	public List<GameSave> getCurrentSaves(){
		return currentSaves;
	}

}
