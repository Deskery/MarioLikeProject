package com.mygdx.utils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.actors.EnemyHitBox;
import com.mygdx.box2d.*;
import com.mygdx.enums.EnemyType;

import java.util.ArrayList;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createGround(World world) {
        BodyDef bodyDef = new BodyDef();
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
        body.setUserData(new RunnerUserData(Constants.RUNNER_WIDTH, Constants.RUNNER_HEIGHT));
        shape.dispose();
        return body;
    }

    public static ArrayList<Body> createEnemy(World world, Vector2 poppingPosition, Vector2 minPosition, Vector2 maxPosition, EnemyType enemyType) {

        // Enemy
        BodyDef bodyDefEnnemi = new BodyDef();
        bodyDefEnnemi.type = BodyDef.BodyType.KinematicBody;
        bodyDefEnnemi.position.set(new Vector2(poppingPosition.x, poppingPosition.y)); // lmjlslks
        PolygonShape shapeEnnemi = new PolygonShape();
        shapeEnnemi.setAsBox(enemyType.getWidth() / 2, enemyType.getHeight() / 2);
        Body bodyEnnemi = world.createBody(bodyDefEnnemi);
        bodyEnnemi.createFixture(shapeEnnemi, enemyType.getDensity());
        bodyEnnemi.resetMassData();
        EnemyUserData enemyUserData = new EnemyUserData(enemyType.getWidth(), enemyType.getHeight(), minPosition, maxPosition);
        bodyEnnemi.setUserData(enemyUserData);

        // HitBox
        BodyDef bodyDefHitBox = new BodyDef();
        bodyDefHitBox.type = BodyDef.BodyType.KinematicBody;
        bodyDefHitBox.position.set(new Vector2(poppingPosition.x, poppingPosition.y + enemyType.getHeight() / 2)); // lmjlslks
        PolygonShape shapeHitBox = new PolygonShape();
        shapeHitBox.setAsBox(enemyType.getWidth() / 2, 0.2f);
        Body bodyHitBox = world.createBody(bodyDefHitBox);
        bodyHitBox.createFixture(shapeHitBox, enemyType.getDensity());
        bodyHitBox.resetMassData();
        EnemyHitBoxUserData hitBoxUserData = new EnemyHitBoxUserData(enemyType.getWidth(), 0.4f, minPosition, maxPosition, bodyEnnemi);
        bodyHitBox.setUserData(hitBoxUserData);

        // Dispose shapes
        shapeEnnemi.dispose();
        shapeHitBox.dispose();

        ArrayList<Body> result = new ArrayList<Body>();
        result.add(bodyEnnemi);
        result.add(bodyHitBox);

        return result;
    }

    public static Body createPlatform(World world, float xMin, float xMax, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        float width = xMax - xMin;
        float height = 0.5f;

        bodyDef.position.set(new Vector2((xMax + xMin)/2, y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 3f);
        PlatformUserData userData = new PlatformUserData();
        body.setUserData(userData);
        shape.dispose();
        return body;
    }
}