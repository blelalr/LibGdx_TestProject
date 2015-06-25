package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Skin buttonSkin;
	private Skin defaultSkin;
	private Stage stage;
	private TextButton startButton, quitButton;
	private Table table,table2;
	// For debug drawing
	private ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		defaultSkin  =  new Skin(Gdx.files.internal("uiskin.json"));
		buttonSkin	 =  new Skin(Gdx.files.internal("data/button.json"),new TextureAtlas("data/button.pack"));
		stage = new Stage();
		
		table = new Table();
		table.setPosition(0, 0);
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
		stage.addActor(table);
		
		table2 = new Table();
		table2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
		table2.setPosition(table.getX(), table.getY() + table.getHeight());
		
		stage.addActor(table2);
		
		
		startButton = new TextButton("START",buttonSkin,"orbitron");
		quitButton = new TextButton("QUIT",buttonSkin,"orbitron");
		
		Table horizTable = new Table();
		horizTable.defaults().expand().fill();
		
		
		for(int i = 0 ; i<100 ; i++ ){
			Image imageBadlogic = new Image(new Texture(Gdx.files.internal("data/badlogic.jpg")));
			horizTable.add(imageBadlogic).height(imageBadlogic.getImageHeight()).fill();
			
		}
		
		ScrollPane horizOnlyBottomScroll = new ScrollPane(horizTable, defaultSkin);
		horizOnlyBottomScroll.setScrollBarPositions(true, true);
		
		table.padTop(30);
		table.add(startButton);
		table.add(quitButton).row();
		
		table2.add(horizOnlyBottomScroll).expand().fill().colspan(4);
		
		Gdx.input.setInputProcessor(stage);
	
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		defaultSkin.dispose();
		buttonSkin.dispose();
		shapeRenderer.dispose();
	}
	
	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void render () {
	    Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	
	   
}