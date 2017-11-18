package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.screens.GameScreen;

public class MyGdxGame extends Game {
	
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		setScreen(new GameScreen());
		
	}
	

}
