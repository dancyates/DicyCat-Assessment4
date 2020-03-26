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
		this.beenSaved = false;
	}

	/**
	 * If the index to save does not already have a GameSave that has been saved to, then this method saves the game and adds the save to savedGames
	 * @param indexToSaveTo The index of the save file to save to
	 * @param difficulty Difficulty
	 * @param spawnPos Spawn Position of the active truck
	 * @param gameTimer The time left in the game
	 * @param fortressCount The number of fortresses left
	 * @return Whether the game was able to save; whether that save file already had a game saved to it or not
	 */
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

	public Boolean hasBeenSaved(){
		return beenSaved;
	}

	//Setters:
	
	public void addGameObject(GameObject newGameObject) {
		this.gameObjects.add(newGameObject);
	}

	public void addPlayer(FireTruck newPlayer) {
		this.players.add(newPlayer);
	}


}
