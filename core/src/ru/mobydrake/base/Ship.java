package ru.mobydrake.base;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.math.Rect;
import ru.mobydrake.pools.BulletPool;
import ru.mobydrake.sprites.Bullet;

public abstract class Ship extends Sprite {

    protected TextureRegion bulletRegion;
    protected Rect worldBounds;

    protected Vector2 v;
    protected Vector2 v0;
    protected Vector2 bulletV;

    protected Sound shootSound;

    protected BulletPool bulletPool;
    protected float reloadInterval;
    protected float reloadTimer;
    protected float bulletHeight;

    protected int hp;
    protected int damage;


    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void dispose() {
        shootSound.dispose();
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
        //shootSound.setPitch(shootSound.play(), 2);
        shootSound.play();
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }
}
