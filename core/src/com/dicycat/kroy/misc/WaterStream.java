package com.dicycat.kroy.misc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.Kroy;

/**
* Sprite used to represent where the player is shooting
* @author IsaacAlbiston
*
*/
public class WaterStream extends GameObject{

	/**
	 * initialises the water stream at a given position
	 * @param spawnPos
	 */
	public WaterStream(Vector2 spawnPos) {
		super(spawnPos, new Texture("lightBlue.png"), new Vector2(1,1));
	}
	
	//PowerUpAddition_MultipleAttack_5 - Start of Modification - DicyCat - Luke Taylor
	/**
	 * Used to display the stream onto the screen
	 */
	public void displayStream() {
		setRemove(false); //Resets (or initially sets) whether it needs to be removed or not
		Kroy.mainGameScreen.addGameObject(this); // Adds it to GameScreen's list to be rendered
	}
	
	/**
	 * Used to remove the stream from being rendered without completely disposing it
	 */
	public void removeStream() {
		setRemove(true);
	}
	//PowerUpAddition_MultipleAttack_5 - End of Modification - DicyCat - Luke Taylor
	
	/**
	 * Changes the length of the sprite to x
	 * @param x
	 */
	public void setRange(float x){
		sprite.setScale(x,2);
	}

	// CODE_REFACTOR_4 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
	// Added override tag
	@Override
	public void update() {}
	// CODE_REFACTOR_4 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT
}
