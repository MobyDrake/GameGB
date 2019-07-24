package ru.mobydrake.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.Sprite;
import ru.mobydrake.math.Rect;
import ru.mobydrake.pools.BulettPool;

public class MainShip extends Sprite {

    private static final int INVALID_POINTER = -1;
    private static Sound soundShoot = Gdx.audio.newSound(Gdx.files.internal("music/bullet.wav"));

    private TextureRegion bulletRegion;
    private Rect worldBounds;
    private BulettPool bulettPool;

    private Vector2 v = new Vector2();
    private Vector2 v0 = new Vector2(0.5f, 0);
    private Vector2 bulletV = new Vector2(0, 0.5f);

    private boolean pressedRight = false;
    private boolean pressedLeft = false;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private float reloadInterval;
    private float reloadTimer;

    public MainShip(TextureAtlas region, BulettPool bulettPool) {
        super(region.findRegion("main_ship"), 1, 2, 2);
        this.bulettPool = bulettPool;
        bulletRegion = region.findRegion("bulletMainShip");
        reloadInterval = 0.2f;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.12f);
        setBottom(worldBounds.getBottom() + 0.02f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                moveLeft();
                pressedLeft = true;
            break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                moveRight();
                pressedRight = true;
            break;
            case Input.Keys.UP:
                shoot();
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                if (!pressedRight) {
                    stop();
                }
                pressedLeft = false;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                if (!pressedLeft) {
                    stop();
                }
                pressedRight = false;
                break;
        }

        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {

        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }

        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {

        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer){
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }

        return false;
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    private void shoot() {
        Bullet bullet = bulettPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, 0.01f, worldBounds, 1);
        soundShoot.setPitch(soundShoot.play(), 2);
        //soundShoot.play();
    }

    public void dispose() {
        soundShoot.dispose();
    }
}
