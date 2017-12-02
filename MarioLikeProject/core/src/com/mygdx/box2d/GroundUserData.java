package com.mygdx.box2d;

import com.mygdx.enums.UserDataType;

public class GroundUserData extends UserData {

    public GroundUserData() {
        super();
        userDataType = UserDataType.GROUND;
    }

}