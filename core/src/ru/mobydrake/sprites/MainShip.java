package ru.mobydrake.sprites;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.Sprite;
import ru.mobydrake.math.Rect;

public class MainShip extends Sprite {


    public MainShip(TextureAtlas region) {
        super(region.findRegion("main_ship"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.12f);
        setBottom(worldBounds.getBottom() + 0.02f);
    }

    public void keyDown (int keycode, Rect worldBounds) {

        if(keycode == Input.Keys.RIGHT && getRight() < worldBounds.getRight()) {
            pos.add(0.04f, 0);
        }

        if(keycode == Input.Keys.LEFT && getLeft() > worldBounds.getLeft()) {
            pos.add(-0.04f, 0);
        }

    }

    public boolean touchDown(Vector2 touch, int pointer, int button, Rect worldBounds) {
        if(touch.x > 0 && getRight() < worldBounds.getRight()) {
            System.out.println(touch.x);
            pos.add(0.04f, 0);
        }

        if(touch.x < 0 && getLeft() > worldBounds.getLeft()) {
            System.out.println(touch.x);
            pos.add(-0.04f, 0);
        }
        return false;
    }
}
