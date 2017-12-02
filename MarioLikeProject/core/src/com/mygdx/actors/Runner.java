package com.mygdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.EnemyUserData;
import com.mygdx.box2d.RunnerUserData;
import com.mygdx.screens.GameScreen;
import com.mygdx.utils.Constants;

public class Runner extends GameActor {

	private boolean jumping;
	private boolean dodging;
    private boolean hit;
	private boolean moving;
	private Body body;
	private GameScreen gameScreen;
//	private Rectangle bound;
//    private TextureRegion texture;
//    private float weight = 10000;
//private TextureRegion marioStand;
//private Texture texture;


    public Runner(Body body) {
        super(body);
    	this.body=body;
    }
    
//    public void update(float delta){
//        getBound().y -= delta/weight;
////    	setRegion(getFrame(delta));
////        this.setPosition(this.bound.getX(), this.bound.getY());
////    	setTexture(texture);
//    }
    
//    public Texture getFrame(float dt){
//        
//
//        Texture region;
//
////        //depending on the state, get corresponding animation keyFrame.
////        switch(currentState){
////            case DEAD:
////                region = marioDead;
////                break;
////            case GROWING:
////                region = growMario.getKeyFrame(stateTimer);
////                if(growMario.isAnimationFinished(stateTimer)) {
////                    runGrowAnimation = false;
////                }
////                break;
////            case JUMPING:
////                region = marioIsBig ? bigMarioJump : marioJump;
////                break;
////            case RUNNING:
////                region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
////                break;
////            case FALLING:
////            case STANDING:
////            default:
////                region = new Texture(Gdx.files.internal("data/background.png"));;
////                break;
////        }
//
//        //if mario is running left and the texture isnt facing left... flip it.
//        
//        return region;
//
//    }

    public Runner(GameScreen gameScreen,Body body) {
		// TODO Auto-generated constructor stub
    	super(body);
    	this.body=body;
    	this.gameScreen = gameScreen;
//    	texture = new Texture(Gdx.files.internal("data/background.png"));
//    	this.setTexture(texture);
//    	this.setPosition(body.getWorldCenter().x+Constants.RUNNER_WIDTH/2, body.getWorldCenter().y+Constants.RUNNER_HEIGHT);
//    	this.setSize(Constants.RUNNER_WIDTH/2, Constants.RUNNER_HEIGHT/2);
//    	setBound(new Rectangle(this.body.getPosition().x-Constants.RUNNER_WIDTH/2, this.body.getPosition().y-Constants.RUNNER_HEIGHT,Constants.RUNNER_WIDTH, Constants.RUNNER_HEIGHT));
//        this.setPosition(this.getBound().getX(), this.getBound().getY());
//    	this.setSize(Constants.RUNNER_WIDTH/2, Constants.RUNNER_HEIGHT/2);

	}

	public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }

//	@Override
//	public void draw(Batch batch) {
//		// TODO Auto-generated method stub
//		super.draw(batch);
////        batch.draw(this.getTexture(), this.bound.getX(), this.bound.getY());
//
//	}
    public void jump() {

    	if (!(jumping || dodging|| hit)) {
//            body.applyLinearImpulse(13f, 13f, body.getPosition().x, body.getPosition().y, true);
//    		body.applyLinearImpulse(0, 1.0f, body.getPosition().x, body.getPosition().y, true);
    		 body.applyLinearImpulse(new Vector2(0,800f), body.getWorldCenter(), true);
//    		 this.update(50*30f);
//    		 body.setGravityScale(5f);
             jumping = true;

        }

    }

    public boolean isJumping() {
        return jumping;
    }

    public void landed() {
    	
        jumping = false;
    }

    public void dodge() {
        if (!(jumping || hit)) {
            body.setTransform(new Vector2(body.getPosition().x,body.getPosition().y-.5f), 90);
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
    
    public void moveLeft(Vector2 vector){
    	//TODO : Supr if? -> Test
    	if (!(dodging || hit || jumping)) {
    		body.setLinearVelocity(vector);
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

	/**
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Body body) {
		this.body = body;
	}

//	public Rectangle getBound() {
//		return bound;
//	}
//
//	public void setBound(Rectangle bound) {
//		this.bound = bound;
//	}
}