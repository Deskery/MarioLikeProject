package com.mygdx.utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.box2d.*;
import com.mygdx.enums.EnemyType;

public class WorldUtils {

    public static Body createGround(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(Constants.GROUND_X, Constants.GROUND_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.GROUND_WIDTH, Constants.GROUND_HEIGHT / 2);
        body.createFixture(shape, Constants.GROUND_DENSITY);
        body.setUserData(new GroundUserData());
        shape.dispose();
        return body;
    }
    
    public static Body createRunner(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(Constants.RUNNER_GRAVITY_SCALE);
        body.createFixture(shape, Constants.RUNNER_DENSITY);
        body.resetMassData();
        MassData massData = new MassData();
        massData.mass = 50;
        body.setMassData(massData);
        body.setUserData(new RunnerUserData(Constants.RUNNER_WIDTH, Constants.RUNNER_HEIGHT));
        shape.dispose();
        return body;
    }

    public static Body createEnemy(World world, Vector2 poppingPosition, Vector2 minPosition, Vector2 maxPosition, EnemyType enemyType) {

        BodyDef bodyDefEnnemi = new BodyDef();
        bodyDefEnnemi.type = BodyDef.BodyType.KinematicBody;
        bodyDefEnnemi.position.set(new Vector2(poppingPosition.x, poppingPosition.y));
        PolygonShape shapeEnnemi = new PolygonShape();
        shapeEnnemi.setAsBox(enemyType.getWidth() / 2, enemyType.getHeight() / 2);
        Body bodyEnnemi = world.createBody(bodyDefEnnemi);
        bodyEnnemi.createFixture(shapeEnnemi, enemyType.getDensity());
        bodyEnnemi.resetMassData();
        EnemyUserData enemyUserData = new EnemyUserData(enemyType.getWidth(), enemyType.getHeight(), minPosition, maxPosition);
        bodyEnnemi.setUserData(enemyUserData);

        shapeEnnemi.dispose();

        return bodyEnnemi;
    }

    public static Body createPlatform(World world, float xMin, float xMax, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        float width = xMax - xMin;
        float height = 0.5f;

        bodyDef.position.set(new Vector2((xMax + xMin)/2, y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.GROUND_DENSITY);
        PlatformUserData userData = new PlatformUserData();
        body.setUserData(userData);
        shape.dispose();
        return body;
    }

    public static Body createCoin(World world, float x, float y) {
        float radius = 0.4f;;

        // body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(x, y));

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / 2);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 3f);

        CoinUserData userData = new CoinUserData();

        Texture texture = new Texture(Gdx.files.internal("data/coin.png"));
//        Sprite spriteCoin = new Sprite(texture);
        Sprite spriteCoin = new Sprite(texture,0,0,40,40);
//        spriteCoin.setSize(width, height);
        spriteCoin.setOrigin(x, y);
        spriteCoin.setPosition(body.getPosition().x, body.getPosition().y);
        userData.setSprite(spriteCoin);

        body.setUserData(userData);
        shape.dispose();
        return body;
    }
}