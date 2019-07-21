package ru.mobydrake.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.mobydrake.base.ScaleTouchUpButton;
import ru.mobydrake.math.Rect;

public class ButtonExit extends ScaleTouchUpButton {


    public ButtonExit(TextureAtlas region) {
        super(region.findRegion("btExit"));
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
        setBottom(worldBounds.getBottom() + 0.04f);
        setRight(worldBounds.getRight() - 0.04f);
    }
}
