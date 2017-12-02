package com.mygdx.screens;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.mygdx.actors.Coin;
import com.mygdx.actors.Enemy;
import com.mygdx.actors.Runner;
import com.mygdx.box2d.CoinUserData;
import com.mygdx.box2d.EnemyUserData;
import com.mygdx.box2d.RunnerUserData;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.actors.Enemy;
import com.mygdx.actors.Runner;
import com.mygdx.game.MyGdxGame;
import com.mygdx.stages.GameStage;
import com.mygdx.utils.BodyUtils;
import com.mygdx.utils.Constants;
import com.mygdx.utils.WorldUtils;
import com.mygdx.utils.Constants;
import com.mygdx.utils.InputController;
import com.mygdx.utils.WorldUtils;
import com.sun.deploy.ref.AppModel;
import com.sun.glass.ui.View;

public class GameScreen implements Screen, ContactListener {

    private BitmapFont font = new BitmapFont();
//    private SpriteBatch batch = new SpriteBatch();
    private GameStage stage;
//    private Orthographicgamecam gamecam;
    private Box2DDebugRenderer debugRenderer;
    private int score = 0;
    private int nbCoins = 0;

    private ArrayList<Body> bodiesToBeDelete = new ArrayList<Body>();

    private boolean rightKeyPressed;
    private boolean leftKeyPressed;
    private boolean jumpKeyPressed;
    private boolean landKeyPressed;

	float origin = 0;
	private boolean jump = false;
	private MyGdxGame game;
//    private boolean gameFinish = false;

    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
//    private Box2DDebugRenderer b2dr;

    //sprites
    private Runner player;
	private Array<Body> bodies= new Array<Body>();

//    private Music music;

//    private Array<Item> items;
//    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public GameScreen(MyGdxGame myGdxGame) {
        stage = new GameStage();
        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(this);
        debugRenderer = new Box2DDebugRenderer();
        stage.createStage(world);
        player = stage.getRunner();

        game = myGdxGame;

//        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(Constants.APP_WIDTH / 100, Constants.APP_HEIGHT / 100, gamecam);

      //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("data/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / 100);


        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    }

    @Override
    public void render(float delta) {
    	update(delta);

        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);    

        debugRenderer.render(world, gamecam.combined);

        gamecam.position.set(stage.getRunner().getBody().getPosition().x, gamecam.viewportHeight / 2, gamecam.position.z);
        gamecam.update();

        renderer.render();
//        player.update(delta);
        //renderer our Box2DDebugLines
//        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);

        destroyBodiesToBeDeleted();

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }

        world.step(1/60f, 8, 5);


        game.batch.begin();
        font.draw(game.batch, "Coins : " + nbCoins, Constants.APP_WIDTH - 80, Constants.APP_HEIGHT - 20);
        font.draw(game.batch, "Score : " + score, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 20);
        game.batch.end();

        if(gameOver()){
            Gdx.input.setInputProcessor(new InputController());
            game.setScreen(new GameOverScreen(game, score));
        }
    }

    private void destroyBodiesToBeDeleted(){
        for (Body b : bodiesToBeDelete) {
            for (Enemy enemy : stage.getEnemies()) {
                if (enemy.getUserData().equals(b.getUserData())) {
                    enemy.remove();
                }
            }

            for(Coin coin : stage.getCoins()) {
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
    }

    private void update(Body body) {
        Runner runner = stage.getRunner();
        Body runnerBody = runner.getBody();

        if (BodyUtils.bodyIsEnemy(body)) {
            EnemyUserData eud = (EnemyUserData) body.getUserData();

            if (body.getPosition().x < eud.getBeginPosition().x) {
                eud.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY_RIGHT);
            }

            if (body.getPosition().x > eud.getEndPosition().x) {
                eud.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY_LEFT);
            }

            body.setLinearVelocity(eud.getLinearVelocity());
        }

        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !runner.isHit()) {

            }
            world.destroyBody(body);
        }

        if(rightKeyPressed && !jumpKeyPressed)
        {
            runner.moveRight();
//            background.updateXBounds(-.01f);
        }
        else if(leftKeyPressed && !jumpKeyPressed)
        {
            runner.moveLeft();
//            background.updateXBounds(.01f);


        }
        else if(jumpKeyPressed)
        {
            runner.jump();
//

        }else if (landKeyPressed && !rightKeyPressed) {

            runner.getBody().applyForce(Constants.WORLD_GRAVITY, runner.getBody().getWorldCenter(), true);


        }else runner.stopMove();

    }

    public void handleInput(float dt){
        //control our player using immediate impulses

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getBody().getLinearVelocity().x <= 2)
                player.getBody().applyLinearImpulse(new Vector2(0.1f, 0), player.getBody().getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getBody().getLinearVelocity().x >= -2)
                player.getBody().applyLinearImpulse(new Vector2(-0.1f, 0), player.getBody().getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                player.jump();
        }


    public void update(float dt){
        //handle user input first
        handleInput(dt);


        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

//        player.update(dt);

        //attach our gamecam to our players.x coordinate
//        if(player.currentState != Mario.State.DEAD) {
            gamecam.position.x = player.getBody().getPosition().x;
//        }

        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our gamecam can see in our game world.
        renderer.setView(gamecam);

    }



	public boolean gameOver(){
        return stage.isGameFinish();
    }


	@Override
	public void show() {
		// TODO Auto-generated method stub

        Gdx.input.setInputProcessor(new InputController() {

            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.UP :
                    case Keys.SPACE :
                        if (!stage.getRunner().isJumping()) {
                            stage.getRunner().jump();
                            jumpKeyPressed = true;
                        }
                        break;

                    case Keys.DOWN :
                        landKeyPressed = true;
                        stage.getRunner().dodge();
                        break;

                    case Keys.RIGHT :
                        rightKeyPressed = true;
//                        stage.getRunner().moveRight();
                    break;

                    case Keys.LEFT :
                        leftKeyPressed = true;
//                        stage.getRunner().moveLeft();
                    break;
                }

                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.UP:
                    case Keys.SPACE:
                        break;

                    case Keys.DOWN :
                        landKeyPressed = false;
                        if (stage.getRunner().isDodging()) {
                            stage.getRunner().stopDodge();
                        }
                        break;

                    case Keys.RIGHT :
                        rightKeyPressed = false;
                        stage.getRunner().stopMove();
                        break;

                    case Keys.LEFT :
                        leftKeyPressed = false;
                        stage.getRunner().stopMove();
                        break;
                }
                return true;
            }

        });
	}
		
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
//        gamePort.update(width,height);


		gamecam.viewportWidth = width /25;
		gamecam.viewportHeight = height /25;
		gamecam.update();
	}

	 public TiledMap getMap(){
	        return map;
	    }
    public World getWorld(){
	        return world;
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
		dispose();
	}

	@Override
	public void dispose() {
//		// TODO Auto-generated method stub
//        Array<Body> bodies = new Array<Body>(stage.getWorld().getBodyCount());
//        stage.getWorld().getBodies(bodies);
//
//        for (Body body : bodies) {
//            if(BodyUtils.bodyIsCoin(body)) {
//                CoinUserData userdata = (CoinUserData) body.getUserData();
//                userdata.getSprite().getTexture().dispose();
//            }
//        }

        world.dispose();
        debugRenderer.dispose();
//        game.batch.dispose();
//		map.dispose();
	}

	/**
	 * @return the player
	 */
	public Runner getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Runner player) {
		this.player = player;
	}


    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (BodyUtils.bodyIsRunner(b) && BodyUtils.bodyIsEnemy(a)) {

            EnemyUserData enemyUserData = (EnemyUserData) a.getUserData();
            RunnerUserData runnerUserData = (RunnerUserData) b.getUserData();

            if (b.getPosition().y >= a.getPosition().y + enemyUserData.getHeight()/2 + runnerUserData.getHeight()/2 -0.2f) {
                bodiesToBeDelete.add(a);
                score += Constants.SCORE_ENEMY;
                stage.getRunner().jump();
            }
            else {
                stage.setGameFinish(true);
                stage.getRunner().hit();
            }

        } else if (BodyUtils.bodyIsEnemy(b) && BodyUtils.bodyIsRunner(a)){
            EnemyUserData enemyUserData = (EnemyUserData) b.getUserData();
            RunnerUserData runnerUserData = (RunnerUserData) a.getUserData();

            if (a.getPosition().y >= b.getPosition().y + enemyUserData.getHeight()/2 + runnerUserData.getHeight()/2 -0.2f) {
                bodiesToBeDelete.add(b);
                stage.getRunner().jump();
                score += Constants.SCORE_ENEMY;
            }
            else {
                stage.setGameFinish(true);
                stage.getRunner().hit();
            }
        } else if ((BodyUtils.bodyIsRunner(a) && (BodyUtils.bodyIsGround(b) || BodyUtils.bodyIsPlatform(b))) ||
                ((BodyUtils.bodyIsGround(a) || BodyUtils.bodyIsPlatform(a)) && BodyUtils.bodyIsRunner(b))) {
            stage.getRunner().landed();
            jumpKeyPressed=false;
            landKeyPressed=false;

            if(rightKeyPressed)
                stage.getRunner().moveRight();

            if(leftKeyPressed)
                stage.getRunner().moveLeft();

            if ( (BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsPlatform(b) ) || (BodyUtils.bodyIsRunner(b) && BodyUtils.bodyIsPlatform(a) ) ) {
                landKeyPressed=true;
            }
        } else if (BodyUtils.bodyIsCoin(b) && BodyUtils.bodyIsRunner(a)) {
            bodiesToBeDelete.add(b);
            nbCoins++;
            score += Constants.SCORE_COIN;
        } else if (BodyUtils.bodyIsCoin(a) && BodyUtils.bodyIsRunner(b)) {
            bodiesToBeDelete.add(a);
            nbCoins++;
            score += Constants.SCORE_COIN;
        } else if ((BodyUtils.bodyIsFinishLine(a)) && (BodyUtils.bodyIsRunner(b)) ||
                (BodyUtils.bodyIsFinishLine(b)) && (BodyUtils.bodyIsRunner(a))) {

            stage.setGameFinish(true);

        }
    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) &&  BodyUtils.bodyIsPlatform(b)) || (BodyUtils.bodyIsPlatform(a) && BodyUtils.bodyIsRunner(b))) {
    	//TODO : Change y
//    	runner.getBody().applyLinearImpulse(new Vector2(0,-16f),new Vector2( runner.getBody().getWorldCenter().x+5,runner.getBody().getWorldCenter().y ), true );
//        	runner.getBody().setTransform(runner.getBody().getWorldCenter().x+5, runner.getBody().getWorldCenter().y, 0);
//        	runner.getBody().applyForce(Constants.WORLD_GRAVITY, runner.getBody().getWorldCenter(), true);
//        	jumpKeyPressed=true;
        	landKeyPressed=true;
        	rightKeyPressed=false;
    		leftKeyPressed=false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
