package ru.mobydrake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.mobydrake.base.BaseScreen;
import ru.mobydrake.math.Rect;
import ru.mobydrake.pools.BulletPool;
import ru.mobydrake.pools.EnemyPool;
import ru.mobydrake.pools.ExplosionPool;
import ru.mobydrake.sprites.Background;
import ru.mobydrake.sprites.Bullet;
import ru.mobydrake.sprites.ButtonNewGame;
import ru.mobydrake.sprites.Enemy;
import ru.mobydrake.sprites.MainShip;
import ru.mobydrake.sprites.GameOver;
import ru.mobydrake.sprites.Star;
import ru.mobydrake.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private  enum State {PLAYING, PAUSE, GAME_OVER}

    private final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;
    private Star[] starArray;
    private Music music;
    private Sound explosionSound;

    private BulletPool bulletPool;

    private MainShip player;

    private EnemyPool enemyPool;
    private EnemyGenerator enemyGenerator;
    private ExplosionPool explosionPool;

    private State state;
    private State stateBuff;

    private GameOver gameOverSprite;
    private ButtonNewGame buttonNewGame;

    @Override
    public void show() {
        super.show();

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        music.play();
        music.setLooping(true);

        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));

        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }


        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        player = new MainShip(atlas, bulletPool, explosionPool);

        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyGenerator = new EnemyGenerator(enemyPool, atlas, worldBounds);

        state = State.PLAYING;
        stateBuff = State.PLAYING;

        gameOverSprite = new GameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas, this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
        freeAllDestroyedActiveSprites();
        draw();
    }


    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        background.resize(worldBounds);
        for(Star star : starArray) {
            star.resize(worldBounds);
        }

        player.resize(worldBounds);
        gameOverSprite.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void pause() {
        super.pause();
        pauseOn();
    }

    @Override
    public void resume() {
        super.resume();
        pauseOff();
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        music.dispose();
        explosionSound.dispose();
        player.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        super.dispose();
    }

    private void update(float delta) {
        if (state != State.PAUSE) {
            for(Star star : starArray) {
                star.update(delta);
            }
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            bulletPool.updateActiveSprites(delta);
            player.update(delta);
            enemyPool.updateActiveSprites(delta);
            enemyGenerator.generate(delta);
        }
    }

    private void checkCollision() {
        if (state != State.PLAYING) return;

        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) continue;
            float minDist = enemy.getHalfWidth() + player.getHalfWidth();
            if (enemy.pos.dst(player.pos) < minDist) {
                enemy.destroy();
                player.destroy();
                state = State.GAME_OVER;
            }
        }

        List<Bullet> bulletList = bulletPool.getActiveObjects();

        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed()) continue;

            if (bullet.getOwner() != player) {
                if (player.isBulletCollision(bullet)) {
                    player.damage(bullet.getDamage());
                    if (player.isDestroyed()) {
                        state = State.GAME_OVER;
                    }
                    bullet.destroy();
                }
            } else {
                for (Enemy enemy : enemyList) {
                    if (enemy.isDestroyed() || bullet.getOwner() != player) continue;

                    if (enemy.isBulletCollision(bullet)) {
                        enemy.damage(bullet.getDamage());
                        bullet.destroy();
                    }
                }
            }
        }
    }

    private void freeAllDestroyedActiveSprites() {
        explosionPool.freeAllDestroyedActiveSprites();
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        background.draw(batch);
        for(Star star : starArray) {
            star.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        if (state == State.PLAYING || state == State.PAUSE && stateBuff != State.GAME_OVER) {
            player.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }
        if (state == State.GAME_OVER) {
            gameOverSprite.draw(batch);
            buttonNewGame.draw(batch);
        }
        batch.end();
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) {
            if (state == State.PAUSE) {
                pauseOff();
            } else {
                pauseOn();
            }
        }

        if (state == State.PLAYING) {
            player.keyDown(keycode);
        }

        if (keycode == Input.Keys.G) {
            state = State.GAME_OVER;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            player.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            player.touchDown(touch, pointer, button);
        }
        if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            player.touchUp(touch, pointer, button);
        }
        if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return super.touchUp(touch, pointer, button);
    }


    private void pauseOn() {
        stateBuff = state;
        state = State.PAUSE;
        music.pause();
    }

    private void pauseOff() {
        state = stateBuff;
        music.play();
    }

    public void startNewGame() {
        state = stateBuff;

        player.startNewGame();

        enemyPool.freeAllActiveObject();
        bulletPool.freeAllActiveObject();
        explosionPool.freeAllActiveObject();
    }
}
