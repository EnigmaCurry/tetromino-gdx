package com.enigmacurry.games.tetromino;

import com.enigmacurry.games.tetromino.Tetromino.colors;

public class BlockDrawable {

	public int x;
	public int y;
	public colors color;
	
	public BlockDrawable(int x, int y, colors color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
}
