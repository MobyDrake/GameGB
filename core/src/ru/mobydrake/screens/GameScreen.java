package ru.mobydrake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.mobydrake.base.BaseScreen;
import ru.mobydrake.math.Rect;
import ru.mobydrake.sprites.Background;
import ru.mobydrake.sprites.Star;

public class GameScreen extends BaseScreen {

    private final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;
    private Star[] starArray;

    @Override
    public void show() {
        super.show();

        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));

        atlas = new TextureAtlas("textures/menuAtlas.tpack");

        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        background.resize(worldBounds);
        for(Star star : starArray) {
            star.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }

    private void update(float delta) {
        for(Star star : starArray) {
            star.update(delta);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        background.draw(batch);
        for(Star star : starArray) {
            star.draw(batch);
        }

        batch.end();
    }
}
