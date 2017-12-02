package com.mygdx.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.PlatformUserData;

public class Platform extends GameActor {

    public Platform(Body body) {

        super(body);
    }

    @Override
    public PlatformUserData getUserData() {
        return (PlatformUserData) userData;
    }

}