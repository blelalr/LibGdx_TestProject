package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private TextButton button;
//	private BitmapFont font1,font2;
   
	@Override
	public void create () {
		
		batch = new SpriteBatch();
		
		skin  = new Skin(Gdx.files.internal("data/button.json"),new TextureAtlas("data/button.pack"));
		
		stage = new Stage();
		
		button = new TextButton("Start",skin,"orbitron");
		button.setPosition(Gdx.graphics.getWidth()/2 - 100f, Gdx.graphics.getHeight()/2 - 10f);
		
		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y ){
				button.setText("Start");
			}
			
		});
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
		
		
//		font1 = new BitmapFont();
//		font1.setColor(Color.BLACK);
//		font2 = new BitmapFont(Gdx.files.internal("fonts/orbitron.fnt"),
//				Gdx.files.internal("fonts/orbitron.png"),false);
		
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
	}
	
	@Override
	public void resize (int width, int height) {
		
	}
	
	@Override
	public void render () {
	    Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		stage.draw();
		batch.end();
		
	}

	
	   
}