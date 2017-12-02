package com.mygdx.actors;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.utils.Constants;

public class Background extends Actor {

    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds1;
    private Rectangle textureRegionBounds2;
    private int speed = 100;
	private Rectangle textureRegionBounds3;
	ArrayList<Rectangle> rectangles;

    public Background() {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/background.png")));
        textureRegionBounds1 = new Rectangle(0 - Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        textureRegionBounds2 = new Rectangle(Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
//        textureRegionBounds3 = new Rectangle(0 - Constants.APP_WIDTH , 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        rectangles = new ArrayList<Rectangle>();
        rectangles.add(new Rectangle(0 - Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT));
        rectangles.add(new Rectangle(Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT));

    }

    @Override
    public void act(float delta) {
        if (leftBoundsReached(delta)) {
            resetBounds();
        } else {
//            updateXBounds(-delta);
        }
    	
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.APP_WIDTH,
                Constants.APP_HEIGHT);
        batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, Constants.APP_WIDTH,
                Constants.APP_HEIGHT);
//        batch.draw(textureRegion, textureRegionBounds3.x, textureRegionBounds3.y, Constants.APP_WIDTH,
//                Constants.APP_HEIGHT);
        for (int i =0 ; i <rectangles.size(); i++)
        {
        	batch.draw(textureRegion, rectangles.get(i).x, rectangles.get(i).y, Constants.APP_WIDTH,
                    Constants.APP_HEIGHT);
        }
        
    }

    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) <= 0;
//    	return true;
    }

    public void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
        textureRegionBounds2.x += delta * speed;
    	for (int i =0 ; i <rectangles.size(); i++)
        {
    		rectangles.get(i).x += delta *speed;
        }
    }

    private void resetBounds() {
        textureRegionBounds1 = textureRegionBounds2;
        textureRegionBounds2 = new Rectangle(Constants.APP_WIDTH-10, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
//        textureRegionBounds3 = new Rectangle(-Constants.APP_WIDTH, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        rectangles.add(new Rectangle(Constants.APP_WIDTH-10, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT));

    }

}