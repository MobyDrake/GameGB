package ru.mobydrake.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.mobydrake.base.Sprite;
import ru.mobydrake.math.Rect;

public class GameOver extends Sprite {

    public GameOver(TextureAtlas region) {
        super(region.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.08f);
        setTop(worldBounds.getTop() - 0.25f);
    }
}
