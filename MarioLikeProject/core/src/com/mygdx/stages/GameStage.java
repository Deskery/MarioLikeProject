package com.mygdx.stages;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.actors.*;
import com.mygdx.box2d.EnemyHitBoxUserData;
import com.mygdx.box2d.EnemyUserData;
import com.mygdx.enums.EnemyType;
import com.mygdx.actors.Background;
import com.mygdx.actors.Enemy;
import com.mygdx.actors.Ground;
import com.mygdx.actors.Runner;
import com.mygdx.utils.BodyUtils;
import com.mygdx.utils.Constants;
import com.mygdx.utils.RandomUtils;
import com.mygdx.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener, InputProcessor {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private World world;
    private Ground ground;
    private Runner runner;
    private ArrayList<EnemyHitBox> hitBoxes = new ArrayList<EnemyHitBox>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();


    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;
	private boolean rightKeyPressed;
	private boolean leftKeyPressed;
	private boolean jumpKeyPressed;
	private Background background;
	private float runnerInitialX;
	private float cameraInitialX;

    public GameStage() {
    	setUpWorld();
        setupCamera();
        renderer = new Box2DDebugRenderer();
        
//        setupCamera();
    }

    private void setUpWorld() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);
        setUpBackground();
        setUpGround();
        setUpEnemies();
        setUpPlatforms();
        setUpRunner();

    }

    private void setUpGround() {
        ground = new Ground(WorldUtils.createGround(world));
        addActor(ground);
    }

    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
        runnerInitialX = runner.getBody().getPosition().x;
    }

    private void setUpEnemies() {
        createEnemy(new Vector2(10f, 1.5f), new Vector2(5f, 1.5f), new Vector2(10f, 1.5f));
        createEnemy(new Vector2(18f, 1.5f), new Vector2(11f, 1.5f), new Vector2(18f, 1.5f));
    }

    private void setUpPlatforms() {
        //createPlatform(0, 5, 4);
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        cameraInitialX = camera.position.x;
        camera.update();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation

    }

    private void update(Body body) {
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !runner.isHit()) {

            }
            world.destroyBody(body);
        }
        if(rightKeyPressed && !jumpKeyPressed)
        {
        	runner.moveRight();
        	camera.translate(.01f, 0);
        	camera.update();
        	background.updateXBounds(-.01f);
        }
        else if(leftKeyPressed && !jumpKeyPressed)
        {
//        	System.err.println(runnerInitialX +":"+ runner.getBody().getWorldCenter().x);
//        	System.err.println("pppppp"+runnerInitialX +":"+ (runner.getBody().getWorldCenter().x + Constants.RUNNER_MOVE_LEFT_LINEAR_IMPULSE.x));

        	if(runner.getBody().getWorldCenter().x > runnerInitialX)
        	{
        		if(runner.getBody().getWorldCenter().x + Constants.RUNNER_MOVE_LEFT_LINEAR_IMPULSE.x < runnerInitialX)
        			if(runner.getBody().getWorldCenter().x-(runner.getBody().getWorldCenter().x-runnerInitialX) > 0){
        				runner.moveLeft(new Vector2(-(runner.getBody().getWorldCenter().x-runnerInitialX), 0));
//        				System.err.println(camera.position.x+":"+ cameraInitialX);
        				if(camera.position.x > cameraInitialX){
        					camera.translate(-.01f, 0);
        					camera.update();
        					background.updateXBounds(.01f);
        				}
        				
//			        	camera.lookAt(runner.getBody().getPosition().x, 0, camera.position.z);
			        		
        			}
        		else{
	        	runner.moveLeft();
	        	camera.translate(-.01f, 0);
//	        	camera.lookAt(runner.getBody().getPosition().x, 0, camera.position.z);
	        	camera.update();
	        	background.updateXBounds(.01f);
        		}
        	}
        	else
        	{
        		runner.setPosition(runnerInitialX, runner.getBody().getWorldCenter().y);
        		camera.translate(0f, 0);
//        		camera.lookAt(runnerInitialX, 0, camera.position.z);
        		camera.update();
	        	background.updateXBounds(0f);
        	}
        }
        else if(jumpKeyPressed)
    	{
    		runner.jump();

    	}else runner.stopMove();

       
        
    }

    private void createEnemy(Vector2 poppingPosition, Vector2 minPosition, Vector2 maxPosition) {
        EnemyType enemyType = RandomUtils.getRandomEnemyType();
        // On adapte la position Y de l'ennemi en fonction de son type
        poppingPosition.set(poppingPosition.x, enemyType.getY());
        minPosition.set(minPosition.x, enemyType.getY());
        maxPosition.set(maxPosition.x, enemyType.getY());

        ArrayList<Body> bodies = WorldUtils.createEnemy(world, poppingPosition, minPosition, maxPosition, enemyType);

        Enemy enemy = new Enemy(bodies.get(0));
        EnemyHitBox hitBox = new EnemyHitBox(bodies.get(1));

        addActor(enemy);
        addActor(hitBox);
        enemies.add(enemy);
        hitBoxes.add(hitBox);
    }

    private void createPlatform(float xMin, float xMax, float y) {
//        EnemyType enemyType = RandomUtils.getRandomEnemyType();

        Platform platform = new Platform(WorldUtils.createPlatform(world, xMin, xMax, y));
        addActor(platform);
    }


    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }
    
    @Override
    public boolean keyDown(int keyCode) {
    	
    	if(keyCode == Keys.SPACE || keyCode == Keys.UP)
    	{

//    		runner.jump();
    		jumpKeyPressed=true;
    	}
    	else 
    		if (keyCode == Keys.DOWN) {
    			runner.dodge();
			}
    		else
    			if (keyCode == Keys.RIGHT) {


    					rightKeyPressed = true;
				}
    			else
    				if (keyCode == Keys.LEFT) {
    					leftKeyPressed = true;
					}

    	return false;
    }

    @Override
    public boolean keyUp(int keyCode) {
    	// TODO Auto-generated method stub
    	if ( keyCode == Keys.RIGHT ) rightKeyPressed = false;
    	else if ( keyCode == Keys.LEFT ) leftKeyPressed = false;

    	if (runner.isDodging()) {
            runner.stopDodge();
        }
    	return super.keyUp(keyCode);
    }
    
    @Override
    public void beginContact(Contact contact) {

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
            runner.hit();
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
            jumpKeyPressed=false;
        } else if (BodyUtils.bodyIsHitBox(a) && BodyUtils.bodyIsRunner(b)) {

            EnemyHitBoxUserData hitBoxUserData = (EnemyHitBoxUserData) a.getUserData();

            world.destroyBody(hitBoxUserData.getBodyEnnemy());
            world.destroyBody(a);
        } else if (BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsHitBox(b)) {
            EnemyHitBoxUserData hitBoxUserData = (EnemyHitBoxUserData) b.getUserData();

            world.destroyBody(hitBoxUserData.getBodyEnnemy());
            world.destroyBody(b);
        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

	/**
	 * @return the runner
	 */
	public Runner getRunner() {
		return runner;
	}

	/**
	 * @param runner the runner to set
	 */
	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	private void setUpBackground() {
		background = new Background();
        addActor(background);
    }
}