package com.mygdx.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.GroundUserData;
import com.mygdx.box2d.RunnerUserData;;

public class Ground extends GameActor {

	
    public Ground(Body body) {
        super(body);
    }
    
    @Override
    public GroundUserData getUserData() {
        return (GroundUserData) userData;
    }
    
    

}