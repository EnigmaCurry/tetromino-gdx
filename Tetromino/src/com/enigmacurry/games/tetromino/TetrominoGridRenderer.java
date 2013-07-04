package com.enigmacurry.games.tetromino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.enigmacurry.games.tetromino.Tetromino.colors;

public class TetrominoGridRenderer {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle[][] gridRectangles;
	private ArrayMap<colors, Texture> textures;
	private TetrominoGrid grid;
	
	public TetrominoGridRenderer(TetrominoGrid grid) {
		camera = new OrthographicCamera();
		//The base unit for screen dimensions is equal to one tetromino unit
		//The playfield is 10 wide x 20 high.
		//The playfield is centered on the screen with 1 unit blank all around.
		//So, final camera perspective is is 12 wide x 20 high
		camera.setToOrtho(false, 12, 22);
		
		batch = new SpriteBatch();
		this.grid = grid;
		gridRectangles = createGridRectangles();
		
		textures = new ArrayMap<colors, Texture>();
		textures.put(colors.RED, new Texture(Gdx.files.internal("data/tetrominos/red.png")));
		textures.put(colors.GREEN, new Texture(Gdx.files.internal("data/tetrominos/green.png")));
		textures.put(colors.BLUE, new Texture(Gdx.files.internal("data/tetrominos/blue.png")));		
	}

	public void render(float delta) {
		camera.update();
      
		//render by ortho camera coordinates:
		batch.setProjectionMatrix(camera.combined);
		
		//Draw current grid:
		batch.begin();
		for (BlockDrawable block : grid.getGridBlocksToDraw()) {
			Rectangle rect = gridRectangles[(int) block.y][(int) block.x];
			rect.width = 1;
			rect.height = 1;
			rect.x = block.x+1;
			rect.y = block.y+1;
			batch.draw(textures.get(block.color), rect.x, rect.y, rect.width, rect.height);
		}
		batch.end();
		
	}
	
	private Rectangle[][] createGridRectangles() {
		Rectangle[][] rects = new Rectangle[20][10];
		//Create 20 rows of 10 rectangles:
		for (int row=0; row<20; row++) {
			for (int col=0;col<10;col++) {
				Rectangle rect = new Rectangle();
				rect.width = 1;
				rect.height = 1;
				rect.x = col+1;
				rect.y = row+1;
				rects[row][col] = rect;
			}
		}
		return rects;
	}
	
}
