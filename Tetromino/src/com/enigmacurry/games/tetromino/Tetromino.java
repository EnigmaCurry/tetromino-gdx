package com.enigmacurry.games.tetromino;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Tetromino {
	
	public static enum shape {O,I,S,Z,L,J,T};
	public enum colors {RED, GREEN, BLUE};
	
	//Each shape has 4 blocks, in various configurations.
	//See http://www.colinfahey.com/tetris/ section 5.3 for rotation patterns:
	private static Vector2[][] shapes = {
			//O
			{new Vector2(-1,0), new Vector2(0,0), new Vector2(-1,-1), new Vector2(0,-1)},
			//I
			{new Vector2(-2,0), new Vector2(-1,0), new Vector2(0,0), new Vector2(1,0)},
			//S
			{new Vector2(-1,-1), new Vector2(0,-1), new Vector2(0,0), new Vector2(1,0)},
			//Z
			{new Vector2(-1,0), new Vector2(0,-1), new Vector2(0,0), new Vector2(1,0)},
			//L
			{new Vector2(-1,-1), new Vector2(-1,0), new Vector2(0,0), new Vector2(1,0)},
			//J
			{new Vector2(-1,0), new Vector2(0,0), new Vector2(1,0), new Vector2(1,-1)},
			//T
			{new Vector2(-1,0), new Vector2(0,0), new Vector2(1,0), new Vector2(0,-1)},
	};
	
	private Vector2[] currentShape;
	private shape shapeType;

	/**
	 * Get a random Tetromino shape
	 */
	public Tetromino() {
		this(shape.values()[(int)(Math.random()*(shape.values().length))]);
	}
	
	/**
	 * Get the specified Tetromino shape
	 * @param type
	 */
	public Tetromino(shape type){
		shapeType = type;
		currentShape = shapes[type.ordinal()];
	}
	
	public void rotateClockwise() {
		if (shapeType != shape.O) {
			for(int i=0 ; i<currentShape.length ; i++) {
				currentShape[i] = new Vector2(currentShape[i].y, -currentShape[i].x);
			}
		}
	}
	
	public void rotateCounterClockwise() {
		if (shapeType != shape.O){ 
			for(int i=0 ; i<currentShape.length ; i++) {
				currentShape[i] = new Vector2(-currentShape[i].y, currentShape[i].x);
			}
		}
	}

	public Array<Vector2> getCurrentShape() {
		return new Array<Vector2>(currentShape);
	}

}
