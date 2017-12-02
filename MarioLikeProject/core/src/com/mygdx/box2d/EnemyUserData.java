package com.mygdx.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.enums.UserDataType;
import com.mygdx.utils.Constants;

public class EnemyUserData extends UserData  {

    private Vector2 linearVelocity;
    private Vector2 beginPosition;
    private Vector2 endPosition;

    public EnemyUserData(float width, float height, Vector2 beginPosition, Vector2 endPosition) {
        super(width, height);
        userDataType = UserDataType.ENEMY;
        linearVelocity = Constants.ENEMY_LINEAR_VELOCITY_LEFT;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public Vector2 getBeginPosition() {
        return beginPosition;
    }

    public void setBeginPosition(Vector2 beginPosition) {
        this.beginPosition = beginPosition;
    }

    public Vector2 getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Vector2 endPosition) {
        this.endPosition = endPosition;
    }
}