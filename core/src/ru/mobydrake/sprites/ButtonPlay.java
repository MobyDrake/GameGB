package ru.mobydrake.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.mobydrake.base.ScaleTouchUpButton;
import ru.mobydrake.math.Rect;
import ru.mobydrake.screens.GameScreen;

public class ButtonPlay extends ScaleTouchUpButton {

    private Game game;

    public ButtonPlay(TextureAtlas region, Game game) {
        super(region.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.25f);
        setBottom(worldBounds.getBottom() + 0.04f);
        setLeft(worldBounds.getLeft() + 0.04f);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
