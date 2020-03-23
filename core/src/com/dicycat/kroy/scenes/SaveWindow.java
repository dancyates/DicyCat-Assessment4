package com.dicycat.kroy.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dicycat.kroy.Kroy;
import com.dicycat.kroy.saving.GameSave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveWindow {
    /**
     * Save window
     *
     * @author Martha Cartwright
     *
     */

        public Stage stage;
        public Table table = new Table();
        private SpriteBatch sb;
        private NinePatch patch = new NinePatch(new Texture("loool.jpg"), 3, 3, 3, 3);
        private NinePatchDrawable background = new NinePatchDrawable(patch);
        private static Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        public static List<TextButton> saveButtons = new ArrayList<>(Arrays.asList(new TextButton("EMPTY SLOT", skin),
                new TextButton("EMPTY SLOT", skin),
                new TextButton("EMPTY SLOT", skin)));


        public SaveWindow(Kroy game) {

            sb = game.batch;
            Viewport viewport = new ScreenViewport(new OrthographicCamera());
            stage = new Stage(viewport, sb);

            table.setBackground(background);

        }

        public void update(){
            table.clear();
            table.setBackground(background);
            for (int i = 0; i <= 2; i++){
                if (GameSave.getSavedGames().get(i).hasBeenSaved()) saveButtons.set(i,new TextButton("SAVE SLOT " + i, skin));
                else saveButtons.set(i,new TextButton("EMPTY SLOT", skin));
                table.row();
                table.add(saveButtons.get(i)).width(Kroy.CentreWidth());
            }

            table.setFillParent(true);
            stage.addActor(table);
        }



        /** Allows the window to be visible or hidden
         * @param state	true means visible, false means hidden
         */
        public void visibility(boolean state){
            this.table.setVisible(state);
        }


}
