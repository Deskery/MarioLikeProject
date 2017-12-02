package com.mygdx.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.FinishLineUserData;

public class FinishLine extends GameActor {

	public FinishLine(Body body) {
		super(body);
		// TODO Auto-generated constructor stub
	}

	@Override
    public FinishLineUserData getUserData() {
        return (FinishLineUserData) userData;
    }
}
