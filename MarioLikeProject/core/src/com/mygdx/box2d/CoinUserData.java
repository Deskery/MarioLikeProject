package com.mygdx.box2d;

import com.mygdx.enums.UserDataType;

public class CoinUserData extends UserData {

    public CoinUserData() {
        super();
        userDataType = UserDataType.COIN;
    }
}