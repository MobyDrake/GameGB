package ru.mobydrake.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.Ship;
import ru.mobydrake.math.Rect;
import ru.mobydrake.pools.BulletPool;
import ru.mobydrake.pools.ExplosionPool;

public class MainShip extends Ship {

    private static final int INVALID_POINTER = -1;
    private static final int HP = 100;

    private boolean pressedRight = false;
    private boolean pressedLeft = false;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;



    public MainShip(TextureAtlas region, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(region.findRegion("main_ship"), 1, 2, 2);

        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        bulletRegion = region.findRegion("bulletMainShip");
        reloadInterval = 0.2f;

        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));

        v = new Vector2();
        v0 = new Vector2(0.5f, 0);
        bulletV = new Vector2(0, 0.5f);
        bulletHeight = 0.01f;

        damage = 1;
        hp = HP;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.12f);
        setBottom(worldBounds.getBottom() + 0.02f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        //отвечает за авто стрельбу и боль в ушах
//        reloadTimer += delta;
//        if (reloadTimer >= reloadInterval) {
//            reloadTimer = 0f;
//            shoot();
//        }
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

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
                );
    }

    public void startNewGame() {
        stop();

        pressedLeft = false;
        pressedRight = false;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;

        hp = HP;
        pos.x = worldBounds.pos.x;

        flushDestroy();
    }
}
