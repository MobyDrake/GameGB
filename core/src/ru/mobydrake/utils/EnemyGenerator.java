package ru.mobydrake.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.mobydrake.math.Rect;
import ru.mobydrake.math.Rnd;
import ru.mobydrake.pools.EnemyPool;
import ru.mobydrake.sprites.Enemy;

public class EnemyGenerator {

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_SMALL_HP = 1;
    private static final int ENEMY_SMALL_SCORE = 5;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.12f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_MEDIUM_BULLET_VY = -0.25f;
    private static final int ENEMY_MEDIUM_DAMAGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 4f;
    private static final int ENEMY_MEDIUM_HP = 5;
    private static final int ENEMY_MEDIUM_SCORE = 15;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final float ENEMY_BIG_BULLET_VY = -0.3f;
    private static final int ENEMY_BIG_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 1f;
    private static final int ENEMY_BIG_HP = 10;
    private static final int ENEMY_BIG_SCORE = 100;

    private float generateInterval = 4f;
    private float generateTimer;

    private TextureRegion[] enemySmallRegion;
    private TextureRegion[] enemyMediumRegion;
    private TextureRegion[] enemyBigRegion;
    private TextureRegion bulletRegion;

    private Vector2 enemySmallV = new Vector2(0, -0.2f);
    private Vector2 enemyMediumV = new Vector2(0, -0.03f);
    private Vector2 enemyBigV = new Vector2(0, -0.005f);

    private EnemyPool enemyPool;

    private Rect worldBounds;

    private int level;

    public EnemyGenerator(EnemyPool enemyPool, TextureAtlas atlas, Rect worldBounds) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;

        TextureRegion region0 = atlas.findRegion("enemy0");
        enemySmallRegion = Regions.split(region0, 1, 2, 2);

        TextureRegion region1 = atlas.findRegion("enemy1");
        enemyMediumRegion = Regions.split(region1, 1, 2, 2);

        TextureRegion region2 = atlas.findRegion("enemy2");
        enemyBigRegion = Regions.split(region2, 1, 2, 2);

        bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta, int score) {
        level = score / 1000 + 1;
        generateInterval = generateInterval / level;
        generateTimer+= delta;

        if (generateTimer > generateInterval) {
           generateTimer = 0f;
           Enemy enemy = enemyPool.obtain();

           float type = (float) Math.random();

            if (type < 0.6f) {
                enemy.set(enemySmallRegion, enemySmallV, bulletRegion, ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY, ENEMY_SMALL_DAMAGE * level, ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT, ENEMY_SMALL_HP, ENEMY_SMALL_SCORE * level);
            }else if (type < 0.85f) {
                enemy.set(enemyMediumRegion, enemyMediumV, bulletRegion, ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY, ENEMY_MEDIUM_DAMAGE * level, ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HEIGHT, ENEMY_MEDIUM_HP, ENEMY_MEDIUM_SCORE * level);
            } else {
                enemy.set(enemyBigRegion, enemyBigV, bulletRegion, ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY, ENEMY_BIG_DAMAGE * level, ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT, ENEMY_BIG_HP, ENEMY_BIG_SCORE * level);
            }

            enemy.pos.x = Rnd.nextFloat(
                    worldBounds.getLeft() + enemy.getHalfWidth(),
                    worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }
}
