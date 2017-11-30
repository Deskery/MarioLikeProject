package com.mygdx.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.box2d.UserData;

public abstract class GameActor extends Actor {

	protected Body body;
    protected UserData userData;

    public GameActor(Body body) {
        this.body = body;
        this.userData = (UserData) body.getUserData();
    }

    public abstract UserData getUserData();

}