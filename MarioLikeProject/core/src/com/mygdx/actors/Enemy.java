package com.mygdx.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.EnemyUserData;
import com.mygdx.utils.Constants;

public class Enemy extends GameActor {

    public Enemy(Body body) {
        super(body);
    }

    @Override
    public EnemyUserData getUserData() {
        return (EnemyUserData) userData;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        EnemyUserData eud = (EnemyUserData) body.getUserData();

        if (body.getPosition().x < eud.getBeginPosition().x) {
            eud.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY_RIGHT);
        }

        if (body.getPosition().x > eud.getEndPosition().x) {
            eud.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY_LEFT);
        }

        body.setLinearVelocity(getUserData().getLinearVelocity());
    }

}