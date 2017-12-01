package com.mygdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.box2d.CoinUserData;
import com.mygdx.utils.Constants;

public class Coin extends GameActor {
    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds1;

    public Coin(Body body) {
        super(body);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/coin.png")));
        textureRegionBounds1 = new Rectangle(body.getPosition().x, body.getPosition().y, Constants.COIN_WIDTH, Constants.COIN_WIDTH);
    }

    @Override
    public CoinUserData getUserData() {
        return (CoinUserData) userData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.COIN_WIDTH,
                Constants.COIN_WIDTH);
    }
}
