package com.mygdx.screens;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class GameScreen implements Screen  {
	

	private GameStage stage;
	float origin = 0;
	private boolean jump = false;
	private MyGdxGame game;
	
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
//    private OrthogonalTiledMapRenderer renderer;

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
        game = myGdxGame;
        
//        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = myGdxGame;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(Constants.APP_WIDTH / 100, Constants.APP_HEIGHT / 100, gamecam);
        
      //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("data/level1.tmx");
//        renderer = new OrthogonalTiledMapRenderer(map, 1  / 100);
        

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
//        b2dr = new Box2DDebugRenderer();

//        creator = new B2WorldCreator(this);

        //create mario in our game world
        
        player = new Runner(this, WorldUtils.createRunner(world));
        player.setSize(Constants.RUNNER_WIDTH/2, Constants.RUNNER_HEIGHT/2);
        player.setOrigin(player.getWidth()/2, player.getHeight()/2);
        player.getBody().setUserData(player);

        world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beginContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}
		});

        
        Gdx.input.setInputProcessor(stage);        
           
    }

    @Override
    public void render(float delta) {
    	update(delta);
    	
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);    
        
//        renderer.render();
        player.update(delta);
        //renderer our Box2DDebugLines
//        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
//        player.draw(game.batch);
        world.getBodies(bodies);
        for(Body body:bodies)
        	if(body.getUserData() != null && body.getUserData() instanceof Sprite)
        	{
        		Sprite sprite = (Sprite) body.getUserData();
//        		player.update(delta);
                ((Runner) sprite).update(delta);

        		sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
//        		sprite.setRotation(0);
//        		sprite.draw(game.batch);
//        		game.batch.draw(sprite.getTexture(), ((Runner) sprite).getBound().getX(), ((Runner) sprite).getBound().getY());

        	}
       
        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        //Update the stage
        stage.draw();
        
        stage.act(delta);
        
        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }


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

//        player.act(dt);
//        for(Enemy enemy : creator.getEnemies()) {
//            enemy.update(dt);
//            if(enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
//                enemy.b2body.setActive(true);
//            }
//        }

//        for(Item item : items)
//            item.update(dt);
//
        stage.act(dt);
        
        player.update(dt);

        //attach our gamecam to our players.x coordinate
//        if(player.currentState != Mario.State.DEAD) {
            gamecam.position.x = player.getBody().getPosition().x;
//        }

        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
//        renderer.setView(gamecam);

    }

    

	public boolean gameOver(){
	    if(stage.isGameFinish()){
	        return true;
	    }
	    return false;
	}

    
	@Override
	public void show() {
		// TODO Auto-generated method stub
//		 Actor player = new Actor();
//		 player.setPosition(600, 600);
//		 stage.setKeyboardFocus(player);
//		 player.addListener(new InputListener(){
//		   @Override
//		   public boolean keyDown(InputEvent event, int keyCode) {
////			   if(event.getKeyCode() == Keys.SPACE || keyCode == Keys.UP)
////			    	{
//			    		stage.getRunner().jump();
////			    	}
//		    return true;
//		   }
//		   
//		   @Override
//			public boolean keyUp(InputEvent event, int keycode) {
//				// TODO Auto-generated method stub
//			   
////			   stage.getRunner().landed();
//				return super.keyUp(event, keycode);
//			}
//		   
//		   
//		 });
//		 stage.addActor(player);
		 
//		 Gdx.input.setInputProcessor(stage);
	}

		
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
        gamePort.update(width,height);

		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		map.dispose();
//        renderer.dispose();
        world.dispose();
//        b2dr.dispose();
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

	

}
