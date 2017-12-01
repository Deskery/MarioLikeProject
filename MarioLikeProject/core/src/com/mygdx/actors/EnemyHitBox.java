package com.mygdx.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.EnemyHitBoxUserData;
import com.mygdx.box2d.EnemyUserData;
import com.mygdx.utils.Constants;

public class EnemyHitBox extends GameActor {

    public EnemyHitBox(Body body) {
        super(body);
    }

    @Override
    public EnemyHitBoxUserData getUserData() {
        return (EnemyHitBoxUserData) userData;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        EnemyHitBoxUserData ehbud = (EnemyHitBoxUserData) body.getUserData();

        if (body.getPosition().x < ehbud.getBeginPosition().x) {
            ehbud.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY_RIGHT);
        }

        if (body.getPosition().x > ehbud.getEndPosition().x) {
            ehbud.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY_LEFT);
        }

        body.setLinearVelocity(getUserData().getLinearVelocity());
    }
}