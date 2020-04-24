package com.dicycat.kroy.scenes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dicycat.kroy.Kroy;


/**
 * Window for selecting FireTruck type
 * 
 * @author Luke Taylor
 *
 */
public class FireTruckSelectionScene {

	public Stage stage;
	public Table table = new Table();
	private SpriteBatch sb;
	private NinePatchDrawable background = new NinePatchDrawable(new NinePatch(new Texture("instructions.png"), 3, 3, 3, 3));
	
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	// DicyCat Assessment 4 - Split start button into 3 options to allow for difficulty selection
	public TextButton startGameButtonEasy = new TextButton("Start Game (Easy)", skin);
	public TextButton startGameButtonMedium = new TextButton("Start Game (Medium)", skin);
	public TextButton startGameButtonHard = new TextButton("Start Game (Hard)", skin);

    private float width = Gdx.graphics.getWidth();
    private float centre = width* 0.7f;

	public FireTruckSelectionScene(Kroy game) {
		sb = game.batch;
		Viewport viewport = new ScreenViewport(new OrthographicCamera());
		stage = new Stage(viewport, sb);

		table.setBackground(background);
		
		table.row();

	    table.row(); // Added a new row to the table

		// DicyCat Assessment 4 - Added the startGameButton options to the table and centered it in the table.
	    table.add(startGameButtonEasy).width(centre/2.0f).colspan(4);
		table.row();
		table.add(startGameButtonMedium).width(centre/2.0f).colspan(4);
		table.row();
		table.add(startGameButtonHard).width(centre/2.0f).colspan(4);
		table.row();
	    
		table.setFillParent(true);
	    stage.addActor(table);
	    
	    
	}
	
	/** Allows the window to be visible or hidden
	 * @param state true if visible, false if hidden
	 */
	public void visibility(boolean state){
		this.table.setVisible(state);
	}
}
