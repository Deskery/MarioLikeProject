package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.screens.GameScreen;
import com.mygdx.utils.Constants;

public class MyGdxGame extends Game  {
	
	
	public SpriteBatch batch ;
	private AssetManager manager;
	private GameScreen screen;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		this.batch = new SpriteBatch();
		
//		game.batch.begin();
//        player.draw(game.batch);
//       
//        game.batch.end();
		
		manager = new AssetManager();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
//		manager.load("data/level1.tmx", TiledMap.class);
		manager.load("data/background.png", Texture.class);
//		manager.load("level1.tsx", TiledMapTileSet.class);
		manager.finishLoading();
		screen = new GameScreen(this);
		setScreen(screen);
		
	}
	
//	public void render(){
//		batch.begin();
////        screen.getPlayer().draw(batch);
////        batch.draw(screen.getPlayer().getTexture(), Constants.RUNNER_X, Constants.RUNNER_Y);
//       
//        batch.end();
//		
//	}
//	

}
