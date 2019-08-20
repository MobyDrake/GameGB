package ru.mobydrake.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.mobydrake.base.ScaleTouchUpButton;
import ru.mobydrake.math.Rect;
import ru.mobydrake.screens.GameScreen;

public class ButtonNewGame extends ScaleTouchUpButton {

    private GameScreen gameScreen;

    public ButtonNewGame(TextureAtlas region, GameScreen gameScreen) {
        super(region.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(worldBounds.getTop() - 0.5f);
    }

    @Override
    public void action() {
        gameScreen.startNewGame();
    }
}
