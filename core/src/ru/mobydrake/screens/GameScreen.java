package ru.mobydrake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.BaseScreen;
import ru.mobydrake.math.Rect;
import ru.mobydrake.pools.BulletPool;
import ru.mobydrake.sprites.Background;
import ru.mobydrake.sprites.MainShip;
import ru.mobydrake.sprites.Star;

public class GameScreen extends BaseScreen {

    private final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;
    private Star[] starArray;
    private Music music;

    private BulletPool bulletPool;

    private MainShip player;

    @Override
    public void show() {
        super.show();

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/sounds.mp3"));
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
        player = new MainShip(atlas, bulletPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
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
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        music.dispose();
        player.dispose();
        super.dispose();
    }

    private void update(float delta) {
        for(Star star : starArray) {
            star.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        player.update(delta);

    }

    private void freeAllDestroyedActiveSprites() {
        bulletPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        background.draw(batch);
        for(Star star : starArray) {
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        player.draw(batch);
        batch.end();
    }


    @Override
    public boolean keyDown(int keycode) {
        player.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        player.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        player.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        player.touchUp(touch, pointer, button);
        return super.touchUp(touch, pointer, button);
    }
}
