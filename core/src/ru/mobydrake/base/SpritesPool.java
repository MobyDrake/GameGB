package ru.mobydrake.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {

    protected final List<T> activeObjects = new ArrayList<T>();
    protected final List<T> freeObjects = new ArrayList<T>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        return object;
    }

    public void updateActiveSprites(float delta) {
        for(T sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        for(T sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    public void disposeAllSprites() {
        for(T sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.dispose();
            }
        }

        for(T sprite : freeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.dispose();
            }
        }
    }

    public void freeAllDestroyedActiveSprites() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                if (activeObjects.remove(sprite)) {
                    freeObjects.add(sprite);
                }
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose() {
        disposeAllSprites();
        activeObjects.clear();
        freeObjects.clear();
    }

    public void destroyAllSprites() {
        for(T sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.destroy();
            }
        }
    }

    public void freeAllActiveObject() {
        freeObjects.addAll(activeObjects);
        activeObjects.clear();
    }
}
