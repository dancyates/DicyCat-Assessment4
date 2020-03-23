package com.dicycat.kroy.saving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.entities.Entity;
import com.dicycat.kroy.entities.FireStation;
import com.dicycat.kroy.entities.FireTruck;

public class GameSave {
	
	private List<GameObject> gameObjects;
	private List<FireTruck> players;
	private Vector2 spawnPos;
	private int difficulty;
	private float gameTimer;
	private int fortressCount;
	private static List<GameSave> savedGames = Arrays.asList(new GameSave(), new GameSave(), new GameSave());
	private Boolean beenSaved;


	public GameSave() {
		gameObjects = new ArrayList<GameObject>();
		players = new ArrayList<FireTruck>();

		this.spawnPos = null;
		this.difficulty = 0;
		this.gameTimer = 0;
		this.fortressCount = 0;
		this.beenSaved = false;
	}

	public Boolean saveGame(int indexToSaveTo, int difficulty, Vector2 spawnPos, float gameTimer, int fortressCount) {

		if (!savedGames.get(indexToSaveTo).hasBeenSaved()){
			this.beenSaved = true;
			this.spawnPos = spawnPos;
			this.difficulty = difficulty;
			this.gameTimer = gameTimer;
			this.fortressCount = fortressCount;
			savedGames.set(indexToSaveTo,this);
			return true;
		}
		return false;
	}

	public List<GameObject> getGameObjects(){ return gameObjects; }

	public int getDifficulty(){
		return difficulty;
	}

	public List<FireTruck> getPlayers() {
		return players;
	}

	public Vector2 getSpawnPos() {
		return spawnPos;
	}

	public float getGameTimer() {
		return gameTimer;
	}

	public int getFortressCount() {
		return fortressCount;
	}

	public Boolean hasBeenSaved(){
		return beenSaved;
	}

	public static List<GameSave> getSavedGames(){
		return savedGames;
	}

	public void setSpawnPos(Vector2 Pos) {
		this.spawnPos = Pos;
	}

	public void addGameObjects(List<GameObject> newGameObjects) {
		this.gameObjects.addAll(newGameObjects);
	}
	
	public void addGameObject(GameObject newGameObject) {
		this.gameObjects.add(newGameObject);
	}

	public void addPlayer(FireTruck newPlayer) {
		this.players.add(newPlayer);
	}


}
