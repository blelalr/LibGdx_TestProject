package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private TextButton startButton, quitButton;
	private Table table;
	
	@Override
	public void create () {
		
		batch = new SpriteBatch();
		skin  = new Skin(Gdx.files.internal("data/button.json"),new TextureAtlas("data/button.pack"));
		stage = new Stage();
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.center | Align.top);
		
		table.setPosition(0, Gdx.graphics.getHeight());
		
		startButton = new TextButton("START",skin,"orbitron");
		quitButton = new TextButton("QUIT",skin,"orbitron");
		
		startButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y ){
				startButton.setText("START");
			}
			
		});
		
		table.padTop(30);
		table.add(startButton);
		table.row();
		table.add(quitButton);
		
		
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
		
		
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
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}

	
	   
}