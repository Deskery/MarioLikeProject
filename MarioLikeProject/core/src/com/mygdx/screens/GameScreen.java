package com.mygdx.screens;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.stages.GameStage;
import com.mygdx.utils.BodyUtils;

public class GameScreen implements Screen ,ContactListener {

	private GameStage stage;
	float origin = 0;
	private boolean jump = false;

    public GameScreen() {
        stage = new GameStage();
        
        
//        Gdx.input.setInputProcessor(stage);        
           
    }

    @Override
    public void render(float delta) {
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);    
       
        //Update the stage
        stage.draw();
        
        stage.act(delta);
        
        
    }
    
	@Override
	public void show() {
		// TODO Auto-generated method stub
		 Actor player = new Actor();
		 player.setPosition(600, 600);
		 stage.setKeyboardFocus(player);
		 player.addListener(new InputListener(){
		   @Override
		   public boolean keyDown(InputEvent event, int keyCode) {
//			   if(event.getKeyCode() == Keys.SPACE || keyCode == Keys.UP)
//			    	{
			    		stage.getRunner().jump();
//			    	}
		    return true;
		   }
		   
		   @Override
			public boolean keyUp(InputEvent event, int keycode) {
				// TODO Auto-generated method stub
			   
//			   stage.getRunner().landed();
				return super.keyUp(event, keycode);
			}
		   
		   
		 });
		 stage.addActor(player);
		 
		 Gdx.input.setInputProcessor(stage);
	}

		
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            stage.getRunner().landed();
        }
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
