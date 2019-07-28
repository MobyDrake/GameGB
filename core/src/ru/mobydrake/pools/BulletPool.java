package ru.mobydrake.pools;

import ru.mobydrake.base.SpritesPool;
import ru.mobydrake.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

}
