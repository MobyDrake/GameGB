package ru.mobydrake.base;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.math.Rect;
import ru.mobydrake.pools.BulletPool;
import ru.mobydrake.pools.ExplosionPool;
import ru.mobydrake.sprites.Bullet;
import ru.mobydrake.sprites.Explosion;

public abstract class Ship extends Sprite {

    protected TextureRegion bulletRegion;
    protected Rect worldBounds;

    protected Vector2 v;
    protected Vector2 v0;
    protected Vector2 bulletV;

    protected Sound shootSound;

    protected ExplosionPool explosionPool;

    protected BulletPool bulletPool;
    protected float reloadInterval;
    protected float reloadTimer;
    protected float bulletHeight;

    protected int hp;
    protected int damage;

    private final float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer = damageAnimateInterval;


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
    public void update(float delta) {
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= damageAnimateInterval) {
            frame = 0;
        }
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

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    public void damage(int damage) {
        frame = 1;
        damageAnimateTimer = 0f;
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    @Override
    public void destroy() {
        hp = 0;
        boom();
        super.destroy();
    }

    public int getHp() {
        return hp;
    }
}
