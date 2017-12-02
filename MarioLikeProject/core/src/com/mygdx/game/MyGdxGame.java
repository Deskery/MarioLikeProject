package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.screens.GameScreen;

public class MyGdxGame extends Game {
	
	
	public SpriteBatch batch ;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		this.batch = new SpriteBatch();
		
		setScreen(new GameScreen(this));
		
	}
	

}
