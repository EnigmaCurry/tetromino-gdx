package com.enigmacurry.games.tetromino;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Implements core game logic for the game.
 * Knows where all the blocks of the board are and the current falling block.
 * Receives input from the controller, determines if moves are valid, 
 * and updates the game board.
 * @param <BlockDrawable>
 */
public class TetrominoGrid {
	private Tetromino fallingBlock;
	private Vector2 fallingBlockLocation;
	private boolean[][] grid;

	private int gridHeight = 20;
	private int gridWidth = 10;
	private long autoDropRate = 500000000;
	private long lastAutoDrop;
	private long moveRate     = 100000000;
	private long lastMove;
	
	public TetrominoGrid() {
		grid = new boolean[gridHeight][gridWidth];
		
		newFallingBlock();
	}
	
	private void newFallingBlock() {
		fallingBlock = new Tetromino();
		fallingBlockLocation = new Vector2(gridWidth/2,gridHeight-1);
		//TODO: Check if brand new block collides with board, if so, game over.
	}
	
	/**
	 * Get the current block grid coordinates.
	 */
	public static Array<Vector2> translateBlockCoords(Tetromino block, Vector2 gridLocation) {
		Array<Vector2> gridCoords = new Array<Vector2>();
		Array<Vector2> shape = block.getCurrentShape();
		for (Vector2 shapeCoords : shape) {
			shapeCoords = shapeCoords.cpy();
			gridCoords.add(shapeCoords.add(gridLocation));
		}
		return gridCoords;
	}
	
	private boolean moveTooFast(long tooFast) {
		return TimeUtils.nanoTime() - lastMove < tooFast;
	}
	
	private boolean moveTooFast() {
		return moveTooFast(moveRate);
	}

	private boolean timeForAutoDrop() {
		return TimeUtils.nanoTime() - lastAutoDrop > autoDropRate;
	}

	/**
	 * Move the current falling block left (if possible)
	 */
	public void moveLeft() {		
		if (!moveTooFast() && canMoveLeft()){
			fallingBlockLocation.add(new Vector2(-1,0));
			lastMove = TimeUtils.nanoTime();
		}
	}

	/**
	 * Move the current falling block right (if possible)
	 */
	public void moveRight() {
		if (!moveTooFast() && canMoveRight()){
			fallingBlockLocation.add(new Vector2(1,0));
			lastMove = TimeUtils.nanoTime();
		}
	}
	
	/**
	 * Move the current falling block down (if possible)
	 */
	public void moveDown() {
		if (moveTooFast()) {
			return;
		}
		if (canFallMore()) {
			fallingBlockLocation.add(new Vector2(0,-1));
			lastMove = TimeUtils.nanoTime();
			if (timeForAutoDrop()) {
				lastAutoDrop = TimeUtils.nanoTime();
			}
		} else {
			//Can't move down any more:
			//1) Add the falling block to the grid.
			for (Vector2 coord : translateBlockCoords(fallingBlock, fallingBlockLocation)) {
				if(coord.y < gridHeight && coord.x < gridWidth)
					grid[(int) coord.y][(int) coord.x] = true;
			}
			//2) Check for completed lines
			removeCompletedLines();
			//3) Create new falling block
			newFallingBlock();
		}
	}

	/**
	 * Remove completed rows. Return how many were complete.
	 * @return
	 */
	private int removeCompletedLines() {
		int numCompleted = 0;
		for (int rowNum=0; rowNum<gridHeight; rowNum++) {
			boolean complete = true;
			for (int colNum=0; colNum<gridWidth; colNum++) {
				if(!grid[rowNum][colNum]) {
					complete = false;
					break;
				}
			}
			if (complete) {
				numCompleted++;
				//Shift rows above this one down one:
				for (int aboveRowNum=rowNum+1; aboveRowNum<gridHeight; aboveRowNum++) {
					for (int colNum=0; colNum<gridWidth; colNum++) {
						grid[aboveRowNum-1][colNum] = grid[aboveRowNum][colNum];
					}
				}
				//Check the current row again:
				rowNum--;
			}
		}
		return numCompleted;
	}

	/**
	 * Rotate the current falling block clockwise (if possible)
	 */
	public void rotateClockwise() {
		if (canRotateClockwise()) {
			fallingBlock.rotateClockwise();
			lastMove = TimeUtils.nanoTime();
		}
	}

		/**
	 * Rotate the current falling block counter-clockwise (if possible)
	 */
	public void rotateCounterClockwise() {
		if (canRotateCounterClockwise()) {
			fallingBlock.rotateCounterClockwise();
			lastMove = TimeUtils.nanoTime();
		}
	}

	/**
	 * Calculate if the current falling block would collide with existing blocks, 
	 * or would leave the edge of the board if it were moved in a direction (translation)
	 * 
	 * @param translation
	 * @return
	 */
	private boolean doesBlockCollide(Vector2 translation) {
		Array<Vector2> blockCoords = translateBlockCoords(fallingBlock, fallingBlockLocation);
		for (Vector2 coord : blockCoords) {
			coord = coord.cpy();
			coord.add(translation);
			//Check board constraints:
			if (coord.y < 0 || coord.x < 0 || coord.x > gridWidth-1) {
				return true;
			}
			//Check existing block collision:
			if (coord.y < gridHeight && grid[Math.round(coord.y)][Math.round(coord.x)]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Can the current falling block keep falling?
	 * @return
	 */
	private boolean canFallMore(){
		return !doesBlockCollide(new Vector2(0, -1));
	}
	
	/**
	 * Can the current falling block move left?
	 * @return
	 */
	private boolean canMoveLeft(){
		return !doesBlockCollide(new Vector2(-1,0));
	}
	
	/**
	 * Can the current falling block move right?
	 * @return
	 */
	private boolean canMoveRight(){
		return !doesBlockCollide(new Vector2(1,0));
	}

	/**
	 * Can the current falling block rotate clockwise?
	 * @return
	 */
	private boolean canRotateClockwise(){
		//Rotate the block, check if it collides, rotate it back:
		fallingBlock.rotateCounterClockwise();
		boolean check = doesBlockCollide(new Vector2(0,0));
		fallingBlock.rotateClockwise();
		return !check;
	}

	/**
	 * Can the current falling block rotate counter-clockwise?
	 * @return
	 */
	private boolean canRotateCounterClockwise(){
		//Rotate the block, check if it collides, rotate it back:
		fallingBlock.rotateClockwise();
		boolean check = doesBlockCollide(new Vector2(0,0));
		fallingBlock.rotateCounterClockwise();
		return !check;
		
	}

	public Array<BlockDrawable> getGridBlocksToDraw() {
		Array<BlockDrawable> blocks = new Array<BlockDrawable>();
		int rowNum = 0;
		for(boolean[] row : grid) {
			int colNum = 0;
			for(boolean block : row) {
				if (block) {
					blocks.add(new BlockDrawable(colNum, rowNum, Tetromino.colors.GREEN));
				}
				colNum++;
			}
			rowNum++;
		}
		if (fallingBlock != null) {
			for(Vector2 block : translateBlockCoords(fallingBlock, fallingBlockLocation)) {
				if (block.y <= 19 && block.x <= 9){
					blocks.add(new BlockDrawable(Math.round(block.x), Math.round(block.y), Tetromino.colors.BLUE));
				}
			}
		}
		return blocks;
	}

	/**
	 * Event loop callback for grid maintenance:
	 * @param delta 
	 */
	public void update(float delta) {
		if (timeForAutoDrop()) {
			lastMove = 0;
			moveDown();
		}
	}

	
	
}
