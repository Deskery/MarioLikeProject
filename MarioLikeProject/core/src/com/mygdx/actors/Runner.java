package com.mygdx.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.RunnerUserData;
import com.mygdx.utils.Constants;

public class Runner extends GameActor {

	private boolean jumping;
	private boolean dodging;
    private boolean hit;
	private boolean moving;


    public Runner(Body body) {
        super(body);
    }

    @Override
    public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }

    public void jump() {

    	if (!(jumping || dodging|| hit)) {
//            body.applyLinearImpulse(13f, 13f, body.getPosition().x, body.getPosition().y, true);
//    		body.applyLinearImpulse(0, 1.0f, body.getPosition().x, body.getPosition().y, true);
    		 float posx = body.getPosition().x;
    		 body.applyLinearImpulse(new Vector2(0,13f), body.getPosition(), true);
//    		 body.setGravityScale(5f);
             jumping = true;

        }

    }

    public void landed() {
    	
        jumping = false;
    }

    public void dodge() {
        if (!(jumping || hit)) {
            body.setTransform(new Vector2(body.getPosition().x,body.getPosition().y-.5f), getUserData().getDodgeAngle());
            dodging = true;
        }
    }

    public void stopDodge() {
        dodging = false;
        if (!hit) {
        	body.setTransform(new Vector2(body.getPosition().x,body.getPosition().y+.5f), 0f);   
        }
    }
    
    public void moveRight(){
    	//TODO : Supr if? -> Test
    	if (!(dodging || hit || jumping)) {
    		//body.applyLinearImpulse(getUserData().getMoveRightLinearImpulse(), body.getWorldCenter(), true);
    		body.setLinearVelocity(Constants.RUNNER_MOVE_RIGHT_LINEAR_IMPULSE);
    		setMoving(true);
    	}
    	
    }
    
    public void moveLeft(){
    	//TODO : Supr if? -> Test
    	if (!(dodging || hit || jumping)) {
    		body.setLinearVelocity(Constants.RUNNER_MOVE_LEFT_LINEAR_IMPULSE);
    		setMoving(true);
    	}
    	
    }
    
    public void stopMove(){
    	setMoving(false);
    	body.setLinearVelocity(new Vector2(0,0));
    	//body.setTransform(getUserData().getRunningPosition(), 0f); 
    }

    public boolean isDodging() {
        return dodging;
    }
    
    public void hit() {
        body.applyAngularImpulse(getUserData().getHitAngularImpulse(), true);
        hit = true;
    }

    public boolean isHit() {
        return hit;
    }

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
}