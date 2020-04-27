package com.dicycat.kroy.entities;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.Kroy;
import com.dicycat.kroy.misc.StatBar;
import com.dicycat.kroy.misc.WaterStream;
import com.dicycat.kroy.screens.GameScreen;

/**
 * GameObject controlled controlled by the player which automatically fires
 * at hostile enemies when they're within range.
 *
 * @author Riju De
 * @author Luke Taylor
 */
public class FireTruck extends Entity{
	private float speed;	//How fast the truck can move
	private float flowRate;	//How fast the truck can dispense water
	private float maxWaterLevel; //How much water the truck can hold
	private float currentWaterLevel; //Current amount of water
	// TRUCK_SELECT_CHANGE_5- START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	private boolean selected; // Added boolean to say whether or not the truck is selected
	// TRUCK_SELECT_CHANGE_5- END OF MODIFICATION - NP STUDIOS - LUCY IVATT----

	private Rectangle hitbox = new Rectangle(20, 45, 20, 20);

	//PowerUpAddition_Teleportation_1 - Start of modification - DicyCat - Luke Taylor
	private Vector2 spawnPoint;
	//PowerUpAddition_Teleportation_1 - End of Modification - DicyCat - Luke Taylor

	//PowerUpAddition_General_1 - Start of Modification - DicyCat - Luke Taylor
	private int selectedPowerUp = 0;

	enum PowerUp{
		OBTAINED, ACTIVE, NULL;

		private float activeLength = 10f, currentTime = 0f;


		public void updateTimer() {
			currentTime += Gdx.graphics.getDeltaTime();
		}

		public boolean hasEnded() {
			return (currentTime >= activeLength);
		}

		public void resetTimer() {
			currentTime = 0f;
		}

	}

	private PowerUp[] powerUps = {
			PowerUp.NULL,				//1 powerUps[0] - invincibility - Done tested informally
			PowerUp.NULL,				//2 powerUps[1] - Multiple attacks - Done tested informally
			PowerUp.NULL,				//3 powerUps[2] - Damage increase	 - Done	tested informally
			PowerUp.NULL,				//4 powerUps[3] - Smaller Truck - Done tested informally
			PowerUp.NULL,				//5 powerUps[4] - Health/water regen - Done tested informally
			PowerUp.NULL,				//6 powerUps[5] - invisibility from patrols but not fortresses
			PowerUp.NULL,				//7 powerUps[6] - Teleportation (return to station instantly) - Done and tested informally
	};

	//PowerUpAddition_General_1 - End of Modification - Luke Taylor


	protected final HashMap<String,Integer> DIRECTIONS = new HashMap<String,Integer>(); // Dictionary to store the possible directions the truck can face based on a key code created later
	protected final int[] ARROWKEYS = {Keys.UP, Keys.DOWN, Keys.RIGHT, Keys.LEFT}; // List of the arrow keys to be able to iterate through them later on
	// Alternative Movement Controls 2.4 - START OF MODIFICATION - DicyCat Assessment 4 - Riju De----
	protected final int[] LETTERKEYS = {Keys.W, Keys.S, Keys.D, Keys.A}; // List of the letter keys for alternative movement
	// End of modification - Alternative Movement Controls 2.4
	protected Integer direction = 0; // Direction the truck is facing

	private WaterStream water, secondStream;
	private StatBar tank;
	private StatBar healthBar;
	private boolean secondStreamFiring;
	private float range;

	public FireTruck(Vector2 spawnPos, Float[] truckStats, int truckNum) {
		super(spawnPos, Kroy.mainGameScreen.textures.getTruck(truckNum), new Vector2(25,50), 100, 500);

		//PowerUpAddition_Teleportation_2 - Start of modification - DicyCat - Luke Taylor
		spawnPoint = spawnPos;
		//PowerUpAddition_Teleportation_2 - End of modification - DicyCat - Luke Taylor

		DIRECTIONS.put("n",0);			//North Facing Direction (up arrow)
		DIRECTIONS.put("w",90);			//West Facing Direction (left arrow)
		DIRECTIONS.put("s",180);		//South Facing Direction (down arrow)
		DIRECTIONS.put("e",270);		//East Facing Direction (right arrow)

		DIRECTIONS.put("nw",45);		//up and left arrows
		DIRECTIONS.put("sw",135);		//down and left arrows
		DIRECTIONS.put("se",225);		//down and right arrows
		DIRECTIONS.put("ne",315);		//up and right arrows
		DIRECTIONS.put("",0); 			// included so that if multiple keys in the opposite direction are pressed, the truck faces north

		speed = truckStats[0]; 			// Speed value of the truck
		flowRate = truckStats[1];		// Flow rate of the truck (referred to as the damage of the truck in game)
		maxWaterLevel = truckStats[2];		// Capacity of the truck
		currentWaterLevel = truckStats[2];	// amount of water left, initialised as full in the beginning
		range = truckStats[3];			// Range of the truck

		water = new WaterStream(Vector2.Zero);
		secondStream = new WaterStream(Vector2.Zero);

		tank = new StatBar(Vector2.Zero, "Blue.png", 3);
		Kroy.mainGameScreen.addGameObject(tank);

		healthBar= new StatBar(Vector2.Zero, "Green.png", 3);
		Kroy.mainGameScreen.addGameObject(healthBar);

		// TRUCK_SELECT_CHANGE_6 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		selected = false; // initially sets the truck to false
		// TRUCK_SELECT_CHANGE_6 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	}

	/**
	 * This method moves the truck in the direction calculated in updateDirection()
	 */
	public void moveInDirection() {

		Vector2 movement = new Vector2(1,0); // movement represents where the truck is moving to. Initially set to (1,0) as this represents a unit vector

		movement.setAngle(direction+90); // rotates the vector to whatever angle it needs to face. 90 is added in order to get the keys matching up to movement in the right direction

		float posChange = speed * Gdx.graphics.getDeltaTime();	//Sets how far the truck can move this frame in the x and y direction
		Matrix3 distance = new Matrix3().setToScaling(posChange,posChange); // Matrix to scale the final normalised vector to the correct distance

		movement.nor(); // Normalises the vector to be a unit vector
		movement.mul(distance); // Multiplies the directional vector by the correct amount to make sure the truck moves the right amount

		//PowerUpAddition_Teleportation_3 - Start of Modification - DicyCat - Luke Taylor
		switch(powerUps[6]) {
		case ACTIVE:
			setPosition(spawnPoint); // Teleports back to home
			powerUps[6] = PowerUp.NULL;
			break;
		default:
			Vector2 newPos = new Vector2(getPosition());
			if (!isOnCollidableTile(newPos.add(movement.x,0))) { // Checks whether changing updating x direction puts truck on a collidable tile
					setPosition(newPos); // updates x direction
			}
			newPos = new Vector2(getPosition());
			if (!isOnCollidableTile(newPos.add(0,movement.y))) { // Checks whether changing updating y direction puts truck on a collidable tile
				setPosition(newPos); // updates y direction
			}
		}
		//PowerUpAddition_Teleportation_3 - End of Modification - DicyCat - Luke Taylor


		setRotation(direction);// updates truck direction
	}

	/**
	 * Method checks if any arrow keys currently pressed and then converts them into a integer direction
	 * @return Direction to follow
	 */
	public Integer updateDirection() {
			String directionKey = "";
			String[] directionKeys = {"n", "s", "e", "w"}; // alphabet of directionKey

			// Alternative Movement Controls 2.4 - START OF MODIFICATION - DicyCat Assessment 4 - Riju De----
			for (int i = 0; i <= 3; i++) {// loops through the 4 arrow keys (Stored as KEYS above)
				if (Gdx.input.isKeyPressed(ARROWKEYS[i]) || Gdx.input.isKeyPressed(LETTERKEYS[i])) {
					directionKey+=directionKeys[i];
				}
			}
			// End of modification - Alternative Movement Controls 2.4

			if (directionKey.contains("ns")) {// makes sure direction doesn't change if both up and down are pressed
				directionKey = directionKey.substring(2);
			}
			if (directionKey.contains("ew")) {// makes sure direction doesn't change if both left and right are pressed
				directionKey = directionKey.substring(0, directionKey.length()-2);
			}

			return DIRECTIONS.get(directionKey);
	}

	/**
	 * Updates the direction in which the firetruck is moving in as well as rendering it, moves it and
	 * its hitbox and checks if any entity is inside its range.
	 */
	public void update(){

		// TRUCK_SELECT_CHANGE_7 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		// Only allows the truck to move, control the camera and attack if selected
		if (selected) {
			// Alternative Movement Controls 2.4 - START OF MODIFICATION - DicyCat Assessment 4 - Riju De----
			if (Gdx.input.isKeyPressed(ARROWKEYS[0]) ||
					Gdx.input.isKeyPressed(ARROWKEYS[1]) ||
					Gdx.input.isKeyPressed(ARROWKEYS[2]) ||
					Gdx.input.isKeyPressed(ARROWKEYS[3]) ||
					Gdx.input.isKeyPressed(LETTERKEYS[0]) ||
					Gdx.input.isKeyPressed(LETTERKEYS[1]) ||
					Gdx.input.isKeyPressed(LETTERKEYS[2]) ||
					Gdx.input.isKeyPressed(LETTERKEYS[3]) ||// Runs movement code if any arrow key pressed or if the teleporation powerup has been activated

					//PowerUpAddition_Teleportation_4 - Start of Modification - DicyCat - Luke Taylor
					powerUps[6] == PowerUp.ACTIVE) {
				//PowerUpAddition_Teleportation_4 - End of Modification - DicyCat - Luke Taylor) { // Runs movement code if any arrow key pressed
				// End of modification - Alternative Movement Controls 2.4

				direction = updateDirection(); // updates direction based on current keyboard input
				moveInDirection(); // moves in the direction previously specified
				Kroy.mainGameScreen.updateCamera(); // Updates the screen position to always have the truck roughly centre
			}
			Kroy.mainGameScreen.updateCamera(); // Updates the screen position to always have the truck roughly centre
		}

		//player firing
		ArrayList<GameObject> inRange = entitiesInRange();		//find list of enemies in range

		//FiringSystemRefactor - Start of Modification - DicyCat - Luke Taylor
		if(inRange.isEmpty() || (currentWaterLevel<=0)){				//Removes the water stream if nothing is in range or no water
			water.removeStream();
		}else {													//Adds the water stream if something comes into range
			//PowerUpAddition_MultipleAttacks_6 - Start of Modification - DicyCat - Luke Taylor
			playerFire(inRange);
			water.displayStream();
			//PowerUpAddition_MultipleAttacks_6 - End of Modification - DicyCat - Luke Taylor
		}
		//FiringSystemRefactor - End of Modification - DicyCat - Luke Taylor


		//PowerUpAddition_MultipleAttacks_4 - Start of Modification - DicyCat - Luke Taylor

		switch(powerUps[1]) {
			case ACTIVE:
				if (secondStreamFiring) {
					secondStream.displayStream(); // Display stream if it can fire and something is nearby //
					}else{
					secondStream.removeStream(); // Remove stream if nothing is nearby
				}
				break;
			default:
				secondStream.removeStream(); // Dont display second stream when powerup not active
				break;
			}

			//PowerUpAddition_MultipleAttacks_4 - End of Modification - DicyCat - Luke Taylor


		// TRUCK_SELECT_CHANGE_7 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----

		//PowerUpAddition_Regen_1 - Start of Modification - DicyCat - Luke Taylor

		switch(powerUps[4]) {
		case ACTIVE:
			currentHealthPoints = maxHealthPoints;
			currentWaterLevel = maxWaterLevel;
			powerUps[4] = PowerUp.NULL;
			break;
		default:
			break;
		}

		//PowerUpAddition_Regen_1 - End of Modification - DicyCat - Luke Taylor


		//PowerUpAddition_SmallerTruck_1 - Start of modification - DicyCat - Luke Taylor
        switch(powerUps[3]) {
        case ACTIVE:
        	hitbox.set(hitbox.x, hitbox.y, 10, 10);
        	sprite.setBounds(getX(), getY(), 12.5f, 25f);

			//water bar update
			tank.setPosition(getCentre().add(-5,20));
			tank.setBarDisplay((currentWaterLevel/maxWaterLevel)*50);

			//Health bar update
			healthBar.setPosition(getCentre().add(-5,25));
			healthBar.setBarDisplay((currentHealthPoints*50)/maxHealthPoints);
        	break;
		default:
    		hitbox.set(hitbox.x,hitbox.y,20,20);
    		sprite.setBounds(getX(), getY(), 25, 50);

			//water bar update
			tank.setPosition(getCentre().add(0,20));
			tank.setBarDisplay((currentWaterLevel/maxWaterLevel)*50);

			//Health bar update
			healthBar.setPosition(getCentre().add(0,25));
			healthBar.setBarDisplay((currentHealthPoints*50)/maxHealthPoints);
			break;
        }
        //PowerUpAddition_SmallerTruck_1 - End of modification - DicyCat - Luke Taylor

		//Move the hit box to it's new centred position according to the sprite's position.
        hitbox.setCenter(getCentre().x, getCentre().y);


        //PowerUpAddition_Invisibility_3 - Start of Modification - DicyCat - Luke Taylor
        if (selected) {
			switch(powerUps[5]) {
			case ACTIVE:
				Kroy.mainGameScreen.setPlayerVisible(false);
				break;
			default:
				Kroy.mainGameScreen.setPlayerVisible(true);
				break;
			}
        }
        //PowerUpAddition_Invisibility_3 - End of Modification - DicyCat - Luke Taylor


		//PowerUpAddition_General_3 - Start of Modification - DicyCat - Luke Taylor

		if (Gdx.input.isKeyJustPressed(Keys.TAB)) { // Checks if Tab is pressed to toggle to next power up
			selectNextPowerUpType();
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) { // Checks if space is pressed to activate a power up
			usePowerUp();
		}

		for (int i: new int[] {0,1,2,3,4,5,6}) { // This loop checks the state of all the power up every loop. It makes sure their timers are always counting down if they are active
			switch (powerUps[i]){
				case ACTIVE:
					powerUps[i].updateTimer();
					if (powerUps[i].hasEnded()) {
						powerUps[i].resetTimer();
						powerUps[i] = PowerUp.NULL;
					}
				break;
			default:
				break;
			}
		}

		//PowerUpAddition_General_3 - End of Modificiation - DicyCat - Luke Taylor

	}


	//PowerUpAddition_MultipleAttack_1 - Start of Modification - DicyCat - Luke Taylor
	public Entity getNearestEnemy(ArrayList<GameObject> targets) {
		GameObject currentGameObject;
		GameObject nearestEnemy=targets.get(0);				//set nearest enemy to the first gameobject

		for (int i=1;i<targets.size();i=i+1) {									//iterates through inRange to find the closest gameobject
			currentGameObject=targets.get(i);
			if(Vector2.dst(nearestEnemy.getCentre().x, nearestEnemy.getCentre().y, getCentre().x, getCentre().y)>Vector2.dst(currentGameObject.getCentre().x,currentGameObject.getCentre().y,getCentre().x,getCentre().y)) {	//checks if the current enemy is the new nearest enemy
				nearestEnemy=targets.get(i);
			}
		}

		return (Entity) nearestEnemy;
	}
	//PowerUpAddition_MulipleAttack_1 - End of Modification - DicyCat - Luke Taylor


	/**
	 * Find and aim at the nearest target from an ArrayList of GameObjects
	 * @param targets the list of targets within the firetrucks ranged
	 */
	private void playerFire(ArrayList<GameObject> targets) {		//Method to find and aim at the nearest target from an ArrayList of Gameobjects

		//PowerUpAddition_MultipleAttack_2 - Start of Modification - DicyCat - Luke Taylor
		Entity nearestEnemy = getNearestEnemy(targets);
		//PowerUpAddition_MultipleAttack_2 - End of Modification - DicyCat - Luke Taylor

		Vector2 direction = new Vector2();
		direction.set(new Vector2(nearestEnemy.getCentre().x,nearestEnemy.getCentre().y).sub(getCentre()));		//creates a vector2 distance of the line between the firetruck and the nearest enemy
		float angle = direction.angle();												//works out the angle of the water stream

		water.setRotation(angle);									//adjusts the water sprite to the correct length, position and angle
		water.setRange(direction.len());
		water.setPosition(getCentre().add(direction.scl(0.5f)));

		//PowerUpAddition_DamageIncrease_1 - Start of Modification - DicyCat - Luke Taylor
		float finalFlowRate = flowRate;

		switch(powerUps[2]) {
		case ACTIVE:
			finalFlowRate *= 2; // Doubles the damage taken by an enemy if the power up is active
			break;
		default:
			break;
		}
		//PowerUpAddition_DamageIncrease_1 - End of Modification - DicyCat - Luke Taylor

		//PowerUpAddition_MultipleAttack_3 - Start of Modification - DicyCat - Luke Taylor
		switch(powerUps[1]) {
		case ACTIVE:
			targets.remove(nearestEnemy);

			secondStreamFiring = !targets.isEmpty();

			if (secondStreamFiring) {
				Entity secondNearest = getNearestEnemy(targets);

				Vector2 secondDirection = new Vector2(secondNearest.getCentre().x,secondNearest.getCentre().y).sub(getCentre());
				float secondAngle = secondDirection.angle();

				secondStream.setRotation(secondAngle);
				secondStream.setRange(secondDirection.len());
				secondStream.setPosition(getCentre().add(secondDirection.scl(0.5f)));

				secondNearest.applyDamage(finalFlowRate);
			}
			break;
		default:
			break;
		}
		//PowerUpAddition_MultipleAttack_3 - End of Modification - DicyCat - Luke Taylor


		//PowerUpAddition_DamageIncrease_2 - Start of Modification - DicyCat - Luke Taylor
		nearestEnemy.applyDamage(finalFlowRate);			//Applies damage to the nearest enemy
		currentWaterLevel -= flowRate;					//reduces the tank by amount of water used
		//PowerUpAddition_DamageIncrease_2 - End of modification - DicyCat - Luke Taylor


	}

	/**
	 * Returns an array of all enemy GameObjects in range
	 * @return
	 */
	private ArrayList<GameObject> entitiesInRange(){
		ArrayList<GameObject> outputArray = new ArrayList<GameObject>();	//create array list to output enemies in range

		for (GameObject currentObject : Kroy.mainGameScreen.getGameObjects()) {		//iterates through all game objects
			// PATROLS_2 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT ------------
			// Added a check for Aliens so that the player can also attack them.
			if ((currentObject instanceof Fortress) && (objectInRange(currentObject))
			|| (currentObject instanceof Alien) && (objectInRange(currentObject))){  	//checks if entity is in range and is an enemy
				outputArray.add(currentObject);												//adds the current entity to the output array list
			}
			// PATROLS_2 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT ------------
		}

		return (outputArray);
	}

	//PowerUpAddition_Invincibility_1 - Start of modification - DicyCat - Luke Taylor
	@Override
	public void applyDamage(float damage) {
		switch(powerUps[0]) {
		case ACTIVE:			//If invincibility power up is active no damage is taken so nothing happens
			break;
		default:				//If invincibility power up is not active, damage is taken as usual
			super.applyDamage(damage);
			break;
		}
	}
	//PowerUpAddition_Invincibility_1 - End of Modification - DicyCat - Luke Taylor

	/**
	 * Checks if the firetrucks tank is full of water.
	 * @return true if full, false if not
	 */
	public boolean isFull(){
		if (this.maxWaterLevel == this.currentWaterLevel){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Check if a game object is in range of the fire truck
	 * @param object Object to check
	 * @return Is the object within range?
	 */
	public boolean objectInRange(GameObject object) {
		return (Vector2.dst(object.getCentre().x, object.getCentre().y, getCentre().x, getCentre().y)<range);
	}

	/**
	 * Remove the FireTruck and stat bars when they are destroyed
	 *
	 * Edited by Lucy Ivatt - NP STUDIOS
	 */
	@Override
	public void die() {
		super.die();
		water.setRemove(true);
		tank.setRemove(true);
		healthBar.setRemove(true);
	}

	public Rectangle getHitbox(){
		return this.hitbox;
	}

	/**
	 * Sets the currentWater to maxWater (the maximum tank value)
	 *
	 * Added by Lucy Ivatt - NP STUDIOS
	 */
	// REPLENISH_1: OVER TIME -> INSTANT  - START OF MODIFICATION - NP STUDIOS - BETHANY GILMORE -----------------------------------------
	public void refillWater(){
		this.currentWaterLevel = this.maxWaterLevel;
	}
	// END OF MODIFICATION  - NP STUDIOS -----------------------------------------

	// REPLENISH_2: OVER TIME -> INSTANT  - START OF MODIFICATION - NP STUDIOS - LUCY IVATT -----------------------------------------

	/**
	 * Repairs the truck overtime by adding 2 to the healthPoints each game tick until it has reached maxHealth
	 *
	 * Added by Lucy Ivatt - NP STUDIOS
	 */
	// Separated refilling water and fixing truck into 2 seperate methods as refilling the truck is now linked to the minigame
	public void repairTruck() {
		if(!(currentHealthPoints >= maxHealthPoints)){
			currentHealthPoints += 2;
		}
	}
	// REPLENISH_2: OVER TIME -> INSTANT  - END OF MODIFICATION - NP STUDIOS - LUCY IVATT -----------------------------------------

	/**
	 * Checks finds the tile that the coordinate is a part of and checks if that tile is solid
	 * @param pos the coordinate on the game map
	 * @return true if solid tile, otherwise false
	 *
	 * Added by Lucy Ivatt - NP STUDIOS
	 */
	public boolean isOnCollidableTile(Vector2 pos) {
		if(GameScreen.gameMap.getTileTypeByLocation(0, pos.x, pos.y).isCollidable()
				||GameScreen.gameMap.getTileTypeByLocation(0, pos.x + this.getWidth(), pos.y).isCollidable()
				||GameScreen.gameMap.getTileTypeByLocation(0, pos.x, pos.y+this.getHeight()).isCollidable()
				||GameScreen.gameMap.getTileTypeByLocation(0, pos.x+this.getWidth(), pos.y+this.getHeight()).isCollidable()) {
			return true;
		}
		return false;
	}

	// TRUCK_SELECT_CHANGE_8 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	// Added a setter for the selected boolean
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	// TRUCK_SELECT_CHANGE_8 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----


	//PowerUpAddition_General_2 - Start of modification - DicyCat - Luke Taylor

	/**
	 * Lets the fireTruck know it can use a certain powerup
	 * @param type int between 0-6 (inclusive) that relates to a different possible power
	 */
	public void obtainPowerUp(int type) {
		powerUps[type] = PowerUp.OBTAINED;
	}

	/**
	 * Changes the current power up selected
	 */
	public void selectNextPowerUpType() {
		selectedPowerUp += 1; //increments marker to next selected
		if (selectedPowerUp > 6) {//Checks if the marker has reached the end and loops back to 0 if so
			selectedPowerUp = 0;
		}
	}

	/**
	 *  Activates the currently selected specific powerup
	 */
	public void usePowerUp() {
		if (powerUps[selectedPowerUp] == PowerUp.OBTAINED) { //Checks power up has been obtained
			powerUps[selectedPowerUp] = PowerUp.ACTIVE; // Activates the power up
		}
	}

	/**
	 * Used for HUD to get the texture of the currently selected power Up
	 * @return Texture either active or inactive version of currently selected powerup
	 */
	public Texture getSelectedPowerUpTexture() {
		if (powerUps[selectedPowerUp] == PowerUp.NULL) {
			return Kroy.mainGameScreen.textures.getInactivePowerUp(selectedPowerUp);
		}else {
			return Kroy.mainGameScreen.textures.getActivePowerUp(selectedPowerUp);
		}
	}

	//PowerUpAddition_General_2 - End of modification - DicyCat - Luke Taylor
}
