package com.enigmacurry.games.tetromino;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class TetrominoGame extends Game {

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new GameScreen());
	}

}
