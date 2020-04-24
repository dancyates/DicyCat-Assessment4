package com.dicycat.kroy.saving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.entities.FireTruck;

/**
 * Contains methods required to save an instance of the game for Assessment 4's Game Save requirement.
 *
 * @author Martha Cartwright
 */
public class GameSave {
	
	private List<GameObject> gameObjects;
	private List<FireTruck> players;
	private Vector2 spawnPos;
	private int difficulty;
	private float gameTimer;
	private int fortressCount;
	private int aliensKilled;
	private int score;
	private Vector2 lastAlienDeath;
	private static List<GameSave> savedGames = Arrays.asList(new GameSave(), new GameSave(), new GameSave()); //A static list that contains all three instances of GameSave for each of the three save files
	private Boolean beenSaved; //Variable that stores whether or not the saveGame() method has been called for that instance of GameSave


	/**
	 * Initialises as an empty save
	 */
	public GameSave() {
		gameObjects = new ArrayList<>();
		players = new ArrayList<>();

		this.spawnPos = null;
		this.difficulty = 0;
		this.gameTimer = 0;
		this.fortressCount = 0;
		this.aliensKilled = 0;
		this.score = 0;
		this.beenSaved = false;
	}

	/**
	 * This method saves the game and adds the save to savedGames
	 * @param indexToSaveTo The index of the save file to save to
	 * @param difficulty Difficulty
	 * @param spawnPos Spawn Position of the active truck
	 * @param gameTimer The time left in the game
	 * @param fortressCount The number of fortresses left
	 */
	public void saveGame(int indexToSaveTo, int difficulty, Vector2 spawnPos, float gameTimer, int fortressCount, int aliensKilled, Vector2 lastAlienDeath, int score) {
		this.beenSaved = true;
		this.spawnPos = spawnPos;
		this.difficulty = difficulty;
		this.gameTimer = gameTimer;
		this.fortressCount = fortressCount;
		this.aliensKilled = aliensKilled;
		this.score = score;
		this.lastAlienDeath = lastAlienDeath;
		savedGames.set(indexToSaveTo,this);
	}

	//Getters:

	public static List<GameSave> getSavedGames(){
		return savedGames;
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

	public int getAliensKilled() {
		return aliensKilled;
	}

	public Vector2 getLastAlienDeath() {
		return lastAlienDeath;
	}

	public Boolean hasBeenSaved(){
		return beenSaved;
	}

	public int getScore() {
		return score;
	}

	//Setters:
	
	public void addGameObject(GameObject newGameObject) {
		this.gameObjects.add(newGameObject);
	}

	public void addPlayer(FireTruck newPlayer) {
		this.players.add(newPlayer);
	}

}
