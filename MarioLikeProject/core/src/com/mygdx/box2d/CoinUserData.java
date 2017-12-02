package com.mygdx.box2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.enums.UserDataType;

public class CoinUserData extends UserData {

    private Sprite sprite;

    public CoinUserData() {
        super();
        userDataType = UserDataType.COIN;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}