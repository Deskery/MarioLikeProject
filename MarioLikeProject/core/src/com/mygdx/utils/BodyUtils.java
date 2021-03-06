package com.mygdx.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.UserData;
import com.mygdx.enums.UserDataType;

public class BodyUtils {

    public static boolean bodyIsRunner(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.RUNNER;
    }

    public static boolean bodyIsGround(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.GROUND;
    }
    
    public static boolean bodyIsFinishLine(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.FINISH_LINE;
    }
    
    public static boolean bodyInBounds(Body body) {
        UserData userData = (UserData) body.getUserData();

        switch (userData.getUserDataType()) {
            case RUNNER:
                return body.getPosition().x + userData.getWidth() / 2 > 0;

            case ENEMY:
                return body.getPosition().x + userData.getWidth() / 2 > 0;
        }

        return true;
    }

    public static boolean bodyIsEnemy(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.ENEMY;
    }

    public static boolean bodyIsHitBox(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.HIT_BOX;
    }

    public static boolean bodyIsPlatform(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.PLATFORM;
    }

    public static boolean bodyIsCoin(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.COIN;
    }
}