package com.huige.tzfe;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

import com.huige.tzfe.GameManager.Point;

public class Grid {
	static int height_size = 4;
	static int width_size = 4;
	Tile [][] cells;
	String TAG = "tzfe Grid";

	/*Grid(int height_size, int width_size) {
		this.height_size = height_size;
		this.width_size = width_size;
		cells = new Tile[height_size][width_size];
		empty();
	}*/

	Grid() {
		cells = new Tile[height_size][width_size];
		empty();
	}

	// Inserts a tile at its position
	void insertTile(Tile tile) {
		cells[tile.heigth][tile.width] = tile;
	}

	void removeTile(Tile tile) {
		cells[tile.heigth][tile.width].value = 0;
	}

	// Find the first available random position
	Tile randomAvailableCell() {
		Log.i(TAG, "randomAvailableCell");
		ArrayList<Tile> availableCells = availableCells();
		Log.v(TAG, "randomAvailableCell size:"+ availableCells.size());
		if ( availableCells.size() > 0) {
			Random random = new Random();
			int randomInt = random.nextInt(availableCells.size());
			Log.i(TAG, "randomInt:"+ randomInt);
			return availableCells.get(randomInt);
		}
		return null;
	}

	void empty() {
		for(int height = 0; height < height_size; height++){
			for(int width = 0; width < width_size; width++){
				Tile tile = new Tile(height, width, 0);
				cells[height][width] = tile;
			}
		}
	}

	int cellsAvailable() {
		int size = availableCells().size();
		Log.i(TAG, "cellsAvailable:"+size);
		return size;
	}

	ArrayList<Tile> availableCells() {
		ArrayList<Tile> availableCells = new ArrayList<Tile>();

		for(int height = 0; height < height_size; height++){
			for(int width = 0; width < width_size; width++){
				if( cells[height][width].value == 0 ){
					availableCells.add(cells[height][width]);
				}
			}
		}
		return availableCells;
	}

	int maxValue() {
		int max = 0;

		for(int height = 0; height < height_size; height++){
			for(int width = 0; width < width_size; width++){
				if( cells[height][width].value > max ){
					max = cells[height][width].value;
				}
			}
		}
		return max;
	}

	static boolean withinBounds(int height, int width) {
		return height >= 0 && height < height_size &&
				width >= 0 && width < width_size;
	}

	static boolean withinBounds(Point position) {
		return position.heigth >= 0 && position.heigth < height_size &&
				position.width >= 0 && position.width < width_size;
	}

	static  boolean withinHeight(int height) {
		return height >= 0 && height < height_size;
	}

	//
	static  boolean withinWidth(int width) {
		return width >= 0 && width < width_size;
	}
}
