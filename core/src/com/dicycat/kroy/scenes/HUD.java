package com.dicycat.kroy.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dicycat.kroy.Kroy;
import com.dicycat.kroy.screens.GameScreen;

import static java.lang.Math.abs;

/**
 * HUD window
 *
 * @author Michele Imbriani
 *
 */
public class HUD {
	public Stage stage;
	private Viewport viewport;	//creating new port so that the HUD stays locked while map can move around independently
	private Integer trucks = 4;
	public static boolean refillVisible = true;

	// FORTRESS_IMPROVE_2 - START OF MODIFICATION - NP STUDIOS - CASSANDRA LILLYSTONE
	//  Deleted world timer attribute and changed to timer
	public static float timer = 0;
	// FORTRESS_IMPROVE_2 - END OF MODIFICATION - NP STUDIOS

	private Integer score;

	// SCREEN_COUNTDOWN_1 - START OF MODIFICATION - NP STUDIOS - CASSANDRA LILLYSTONE
	// Added attribute for the timer that shows on screen - set to 15 minutes
	private float screenTimer;
	// SCREEN_COUNTDOWN_1 - END OF MODIFICATION - NP STUDIOS

	private Label scoreLabel;
	private Label timeLabel;
	private Label fortressLabel;
	private Label timerLabel;
	private Label scoreCountLabel;
	private Label fortressCountLabel;
	private Label refillPrompt;
	//PowerUpAddition_HUD_1 - Start of Modification - DicyCat - Luke Taylor
	private Image currentPowerUpLabel;
	private Label powerUpLabel;
	//PowerUpAddition_HUD_1 - End of Modification - DicyCat - Luke Taylor



	/**
	 */
	public HUD(SpriteBatch sb, float timeLimit, int score) {
		this.score = score;
		screenTimer = timeLimit;
		viewport = new ScreenViewport(new OrthographicCamera());
		stage = new Stage(viewport, sb);	//Where we are going to put the HUD elements

		Table tableHUD = new Table();	//this allows to put widgets in the scene in a clean and ordered way
		tableHUD.top();	// puts widgets from the top instead of from the centre
		tableHUD.setFillParent(true);	//makes the table the same size of the stage

		// SCREEN_COUNTDOWN_2 - START OF MODIFICATION - NP STUDIOS - CASSANDRA LILLYSTONE
		// Changed attribute being displayed in time label and changed the label text
		timerLabel = new Label(String.format("%.0f", screenTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("TIME LEFT UNTIL FIRE STATION DESTROYED:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		// SCREEN_COUNTDOWN_2 - END OF MODIFICATION - NP STUDIOS
		scoreCountLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreLabel = new Label("SCORE:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		// FORTRESS_COUNT_1 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
		// Sets start value of fortress count to 6
		fortressLabel = new Label("FORTRESSES REMAINING:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		fortressCountLabel = new Label(String.format("%01d", 6), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		// FORTRESS_COUNT_1 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT

		//PowerUpAddition_HUD_2 - Start of Modification - DicyCat - Luke Taylor

		powerUpLabel = new Label("CURRENT POWER UP: ",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		currentPowerUpLabel = new Image(new TextureRegionDrawable(new Texture("Power1ActiveNot.png")));

		tableHUD.add(powerUpLabel).expandX().padTop(10);
		tableHUD.add(currentPowerUpLabel).padTop(10);

		//PowerUpAddition_HUD_2 - End of Modification - DicyCat - Luke Taylor
		refillPrompt = new Label("PRESS 'R' TO REFILL!", new Label.LabelStyle(new BitmapFont(), Color.RED));

		tableHUD.add(timeLabel).expandX().padTop(10);
		tableHUD.add(timerLabel).expandX().padTop(10);
		tableHUD.add(scoreLabel).expandX().padTop(10);			// expandX so that all elements take up the same amount of space
		tableHUD.add(scoreCountLabel).expandX().padTop(10);
		// FORTRESS_COUNT_2 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
		tableHUD.add(fortressLabel).expandX().padTop(10);
		tableHUD.add(fortressCountLabel).expandX().padTop(10);
		// FORTRESS_COUNT_2 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT
		tableHUD.add(refillPrompt).expandX().padTop(10);

		stage.addActor(tableHUD);

	}

	/**
	 * Increments the timer using delta time so that it will count up in seconds. Updates the labels in the HUD:
	 * the timer label, the score label and the fortress count label
	 * @param dt delta time
	 */
	public void update(float dt) {
		// SCREEN_COUNTDOWN_3 - START OF MODIFICATION - NP STUDIOS - CASSANDRA LILLYSTONE
		// Decrementing the timer shown on screen
		if (screenTimer > 0) {
		screenTimer -= dt; }
		timer += dt;
		timerLabel.setText(String.format("%.0f", abs(screenTimer)));
		// SCREEN_COUNTDOWN_3 - END OF MODIFICATION - NP STUDIOS

		scoreCountLabel.setText(String.format("%06d", score));

		// FORTRESS_COUNT_3 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
		// Updates the count depending on the amount of fortresses still alive
		fortressCountLabel.setText(String.format("%01d", Kroy.mainGameScreen.getFortressesCount()));
		// FORTRESS_COUNT_3 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT

		//PowerUpAddition_HUD_3 - Start of Modification - DicyCat - Luke Taylor
		currentPowerUpLabel.setDrawable(new TextureRegionDrawable(Kroy.mainGameScreen.getPlayer().getSelectedPowerUpTexture()));

		//PowerUpAddition_HUD_3 - End of Modification - DicyCat - Luke Taylor
		if(refillVisible){
			refillPrompt.setText(String.format("PRESS 'R' TO REFILL!"));
		}
		else{
			refillPrompt.setText(String.format("                                       "));
		}
	}

	public Integer getFinalScore() {
		return score;
	}

	// CODE_REFACTOR_5 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
	// Deleted unused methods
	// CODE_REFACTOR_5 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT

	/** Updates the score variable
	 * @param x	points to be added to the score
	 */
	public void updateScore(Integer x){ score += x; }

}
