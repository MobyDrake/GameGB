package ru.mobydrake;


import com.badlogic.gdx.Game;

import ru.mobydrake.screens.MenuScreen;

public class GameStar extends Game  {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
