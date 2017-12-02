package com.mygdx.stages;

import java.util.ArrayList;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.mygdx.actors.*;
import com.mygdx.box2d.CoinUserData;
import com.mygdx.box2d.EnemyUserData;
import com.mygdx.box2d.RunnerUserData;
import com.mygdx.enums.EnemyType;
import com.mygdx.actors.Background;
import com.mygdx.actors.Enemy;
import com.mygdx.actors.Ground;
import com.mygdx.actors.Runner;
import com.mygdx.utils.BodyUtils;
import com.mygdx.utils.Constants;
import com.mygdx.utils.RandomUtils;
import com.mygdx.utils.WorldUtils;

public class GameStage extends Stage implements InputProcessor {

    // This will be our viewport measurements while working with the debug renderer
    public static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private World world;
    private Ground ground;
    private Runner runner;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Coin> coins = new ArrayList<Coin>();
    private int nbPieces = 0;
    private int score = 0;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private boolean rightKeyPressed;
	private boolean leftKeyPressed;
	private boolean jumpKeyPressed;
    private boolean landKeyPressed;
	private Background background;
	private ArrayList<Body> bodiesToBeDelete = new ArrayList<Body>();
	private float runnerInitialX;

    public GameStage() {
//    	setUpWorld();
//        setupCamera();
//        renderer = new Box2DDebugRenderer();
    }

    private void setUpWorld() {
//        world.setContactListener(this);
        setUpBackground();
    }

    public void createStage(World world) {
        setUpRunner(world);
        setUpGround(world);
        setUpEnemies(world);
        setUpPlatforms(world);
        setUpCoins(world);
    }

    private void setUpGround(World world) {
        ground = new Ground(WorldUtils.createGround(world));
//        addActor(ground);
    }

    private void setUpRunner(World world) {
        runner = new Runner(WorldUtils.createRunner(world));
//        addActor(runner);
//        runnerInitialX = runner.getBody().getPosition().x;
    }

    private void setUpEnemies(World world) {
        createEnemy(world, new Vector2(10f, 1.5f), new Vector2(5f, 1.5f), new Vector2(10f, 1.5f));
        createEnemy(world,new Vector2(18f, 1.5f), new Vector2(11f, 1.5f), new Vector2(18f, 1.5f));
    }

    private void setUpPlatforms(World world) {
        createPlatform(world, 5, 8, 4);
    }

    private void setUpCoins(World world) {
        createCoin(world, 6, 5);
        createCoin(world, 7.5f, 5);
    }

    @Override
    public void act(float delta) {

        for (Body b : bodiesToBeDelete) {
            for (Enemy enemy : enemies) {
                if (enemy.getUserData().equals(b.getUserData())) {
                    enemy.remove();
                }
            }

            for(Coin coin : coins) {
                if (coin.getUserData().equals(b.getUserData())) {
                    coin.remove();
                }
            }

            final Array<JointEdge> list = b.getJointList();

            while (list.size > 0) {
                world.destroyJoint(list.get(0).joint);
            }
            world.destroyBody(b);
            b.setUserData(null);
            b = null;


        }
        bodiesToBeDelete.clear();

        super.act(delta);

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
//            update(body);
        }

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

    }

    private void createEnemy(World world, Vector2 poppingPosition, Vector2 minPosition, Vector2 maxPosition) {
        EnemyType enemyType = RandomUtils.getRandomEnemyType();
        // On adapte la position Y de l'ennemi en fonction de son type
        poppingPosition.set(poppingPosition.x, enemyType.getY());
        minPosition.set(minPosition.x, enemyType.getY());
        maxPosition.set(maxPosition.x, enemyType.getY());

        Enemy enemy = new Enemy(WorldUtils.createEnemy(world, poppingPosition, minPosition, maxPosition, enemyType));

        addActor(enemy);
        enemies.add(enemy);
    }

    private void createPlatform(World world, float xMin, float xMax, float y) {
//        PlatformType platformType = RandomUtils.getRandomPlatformType();
        Platform platform = new Platform(WorldUtils.createPlatform(world, xMin, xMax, y));
        addActor(platform);
    }

    private void createCoin(World world, float x, float y) {
        Coin coin = new Coin(WorldUtils.createCoin(world, x, y));
        addActor(coin);
        coins.add(coin);
    }


    @Override
    public void draw() {
        super.draw();
        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            if(BodyUtils.bodyIsCoin(body)) {
                CoinUserData userdata = (CoinUserData) body.getUserData();

//                userdata.getSprite().setX(body.getWorldCenter().x * Constants.APP_WIDTH/20 + getCamera().viewportWidth/2);
//                userdata.getSprite().setY(body.getWorldCenter().y * Constants.APP_HEIGHT/13);
                userdata.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
            }
        }

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

	/**
	 * @return the runner
	 */
	public Runner getRunner() {
		return runner;
	}

	private void setUpBackground() {
		background = new Background();
        addActor(background);
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }


}