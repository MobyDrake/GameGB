package ru.mobydrake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture texture;

    private Vector2 imgPosition;
    private Vector2 speedV;
    private Vector2 touch;
    private Vector2 touchBuff;

    private final float V_LEN = 2f;


    @Override
    public void show() {
        super.show();
        texture = new Texture("463712.png");
        imgPosition = new Vector2(0, 0);
        touch = new Vector2();
        touchBuff = new Vector2();
        speedV = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        moveToPointMouse();
        moveToArrows();

        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, imgPosition.x, imgPosition.y);
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
        speedV.set(touch.cpy().sub(imgPosition)).setLength(V_LEN);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        if (character == 'q') {
            System.out.println("touch: " + touch.x + " " + touch.y);
            System.out.println("imgPosition position: " + imgPosition.x + " " + imgPosition.y);
            System.out.println("speed:" + speedV.x + " " + speedV.y);
        }

        return false;
    }

    private void moveToPointMouse() {

        touchBuff.set(touch);

        if(touchBuff.sub(imgPosition).len() > V_LEN) {
            imgPosition.add(speedV);
        } else {
            imgPosition.set(touch);
        }
    }

    private void moveToArrows() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) imgPosition.add(4, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) imgPosition.add(-4, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) imgPosition.add(0, 4);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) imgPosition.add(0, -4);
    }

}
