package ru.mobydrake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture texture;
    private Vector2 img;
    private Vector2 speedV;
    private Vector2 touch;
    private boolean moveByMouse = true;
    private int speedMouse = 2;


    @Override
    public void show() {
        super.show();
        texture = new Texture("463712.png");
        img = new Vector2(0, 0);
        touch = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        moveToPointMouse();
        moveToArrows();

        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, img.x, img.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 'q') {
            System.out.println("touch: " + touch.x + " " + touch.y);
            System.out.println("img: " + img.x + " " + img.y);
            System.out.println("speed:" + speedV.x + " " + speedV.y);
        }

        //переключение режима движуния
        if (character == 'n') {
            moveByMouse = !moveByMouse;
        }

        return false;
    }

    private void moveToPointMouse() {
        if (moveByMouse) {
            if (img.x != touch.x || img.y != touch.y) {
                if (img.x < touch.x) {
                    img.add(speedMouse, 0);
                }
                if (img.x > touch.x) {
                    img.add(-speedMouse, 0);
                }
                if (img.y < touch.y) {
                    img.add(0, speedMouse);
                }
                if (img.y > touch.y) {
                    img.add(0, -speedMouse);
                }
            }
        }
    }

    private void moveToArrows() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) img.add(4, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) img.add(-4, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) img.add(0, 4);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) img.add(0, -4);
    }

}
