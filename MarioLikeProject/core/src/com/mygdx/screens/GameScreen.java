package com.mygdx.screens;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.stages.GameStage;
import com.mygdx.utils.BodyUtils;
import com.mygdx.utils.Constants;
import com.mygdx.utils.InputController;
import com.mygdx.utils.WorldUtils;
import com.sun.deploy.ref.AppModel;
import com.sun.glass.ui.View;

public class GameScreen implements Screen, ContactListener {

    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();
    private GameStage stage;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private World world;
    private int score = 0;
    private int nbCoins = 0;

    private ArrayList<Body> bodiesToBeDelete = new ArrayList<Body>();

    private boolean rightKeyPressed;
    private boolean leftKeyPressed;
    private boolean jumpKeyPressed;
    private boolean landKeyPressed;

	float origin = 0;

    public GameScreen() {
        stage = new GameStage();
        
        
//        Gdx.input.setInputProcessor(stage);        
           
    }

    @Override
    public void render(float delta) {
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);    

        debugRenderer.render(world, camera.combined);

        camera.position.set(stage.getRunner().getBody().getPosition().x, camera.viewportHeight / 2, camera.position.z);
        camera.update();

        destroyBodiesToBeDeleted();

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }

        world.step(1/60f, 8, 5);

        batch.begin();
        font.draw(batch, "Coins : " + nbCoins, Constants.APP_WIDTH - 80, Constants.APP_HEIGHT - 20);
        font.draw(batch, "Score : " + score, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 20);

        for (Body body : bodies) {
            if(BodyUtils.bodyIsCoin(body)) {
                CoinUserData userdata = (CoinUserData) body.getUserData();

                Sprite Sprite = (Sprite) userdata.getSprite();
                Sprite.setPosition(body.getPosition().x - Sprite.getWidth() / 2, body.getPosition().y - Sprite.getHeight() / 2);
                Sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                Sprite.draw(batch);

//                Table t = new Table();
//
//                userdata.getSprite().setX(body.getWorldCenter().x * Constants.APP_WIDTH/20 + stage.getCamera().viewportWidth/2);
//                userdata.getSprite().setY(body.getWorldCenter().y * Constants.APP_HEIGHT/13);
//                userdata.getSprite().draw(batch);
            }
        }

        batch.end();

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
//            if(rightKeyPressed) {
//                background.updateXBounds(-.0150f);
//            }
//
//            if(leftKeyPressed) {
//                background.updateXBounds(.0150f);
//            }

        }else if (landKeyPressed && !rightKeyPressed) {

            runner.getBody().applyForce(Constants.WORLD_GRAVITY, runner.getBody().getWorldCenter(), true);


        }else runner.stopMove();

    }
    
	@Override
	public void show() {
		// TODO Auto-generated method stub
        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(this);
        debugRenderer = new Box2DDebugRenderer();
        stage.createStage(world);

        camera = new OrthographicCamera();

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
		camera.viewportWidth = width /25;
		camera.viewportHeight = height /25;
		camera.update();
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
        batch.dispose();
	}


    @Override
    public void beginContact(Contact contact) {
        System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeey");
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
                stage.getRunner().hit();
            }
        } else if ((BodyUtils.bodyIsRunner(a) && (BodyUtils.bodyIsGround(b) || BodyUtils.bodyIsPlatform(b))) ||
                ((BodyUtils.bodyIsGround(a) || BodyUtils.bodyIsPlatform(a)) && BodyUtils.bodyIsRunner(b))) {
            stage.getRunner().landed();
            jumpKeyPressed=false;
            landKeyPressed=false;

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
