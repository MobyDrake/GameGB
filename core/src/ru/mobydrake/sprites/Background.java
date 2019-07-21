package ru.mobydrake.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.mobydrake.base.Sprite;
import ru.mobydrake.math.Rect;

public class Background extends Sprite {


    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}
