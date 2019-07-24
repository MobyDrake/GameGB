package ru.mobydrake.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.Sprite;
import ru.mobydrake.math.Rect;

public class Bullet extends Sprite {

    private Rect worldBounds;
    private Vector2 speedV = new Vector2();
    private int damage;
    private Object owner;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public void set(Object owner, TextureRegion region, Vector2 pos0,
                    Vector2 v0, float height, Rect worldBounds, int damage) {

        this.owner = owner;
        regions[0] = region;
        this.pos.set(pos0);
        this.speedV.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speedV, delta);
        if (isOutside(worldBounds)) destroy();
    }
}
