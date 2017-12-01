package com.mygdx.box2d;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.enums.UserDataType;
import com.mygdx.utils.Constants;

public class RunnerUserData extends UserData {

	private final Vector2 runningPosition = new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y);
    private final Vector2 dodgePosition = new Vector2(Constants.RUNNER_DODGE_X, Constants.RUNNER_DODGE_Y);
    private Vector2 jumpingLinearImpulse;
    private Vector2 moveRightLinearImpulse;
    private Vector2 moveLeftLinearImpulse;


    public RunnerUserData() {
        super();
        jumpingLinearImpulse = Constants.RUNNER_JUMPING_LINEAR_IMPULSE;
        moveRightLinearImpulse = Constants.RUNNER_MOVE_RIGHT_LINEAR_IMPULSE;
        moveLeftLinearImpulse = Constants.RUNNER_MOVE_LEFT_LINEAR_IMPULSE;
        userDataType = UserDataType.RUNNER;
    }
    
    public RunnerUserData(float width, float height) {
        super(width, height);
        jumpingLinearImpulse = Constants.RUNNER_JUMPING_LINEAR_IMPULSE;
        moveRightLinearImpulse = Constants.RUNNER_MOVE_RIGHT_LINEAR_IMPULSE;
        moveLeftLinearImpulse = Constants.RUNNER_MOVE_LEFT_LINEAR_IMPULSE;
        userDataType = UserDataType.RUNNER;
    }
    
    public float getHitAngularImpulse() {
        return Constants.RUNNER_HIT_ANGULAR_IMPULSE;
    }

    public Vector2 getJumpingLinearImpulse() {
        return jumpingLinearImpulse;
    }

    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse) {
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }

    public float getDodgeAngle() {
        // In radians
        return (float) (-90f * (Math.PI / 180f));
    }

    public Vector2 getRunningPosition() {
        return runningPosition;
    }

    public Vector2 getDodgePosition() {
        return dodgePosition;
    }

	public Vector2 getMoveRightLinearImpulse() {
		return moveRightLinearImpulse;
	}

	public void setMoveRightLinearImpulse(Vector2 moveLinearImpulse) {
		this.moveRightLinearImpulse = moveLinearImpulse;
	}

	public Vector2 getMoveLeftLinearImpulse() {
		return moveLeftLinearImpulse;
	}

	public void setMoveLeftLinearImpulse(Vector2 moveLeftLinearImpulse) {
		this.moveLeftLinearImpulse = moveLeftLinearImpulse;
	}

}