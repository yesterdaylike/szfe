package com.huige.tzfe;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

public class GameManager {
	Grid grid = null;
	int startTiles;
	int score;
	int step;
	int maxNumber;
	boolean over;
	boolean won;
	boolean keepPlaying = true;
	String TAG = "tzfe";

	boolean undo = false;
	Tile random = null;
	private PrintInterface mPrintInterface; 
	
	private HistoryDB historyDB;
	private Context context;
	
	GameManager(PrintInterface mPI, Context context) {
		mPrintInterface = mPI;
		//this.size = size; // Size of the grid
		startTiles  = 2;
		setup();
		this.context = context;
	}
	void setPrintInterface(PrintInterface mPI){
		mPrintInterface = mPI;
	}

	// Restart the game
	void restart() {
		//clearGameState();
		//actuator.continueGame(); // Clear the game won/lost message
		saveHistory();
		setup();
	};
	
	public void saveHistory(){
		if( null == historyDB ){
			historyDB = new HistoryDB(context);
		}
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		long timeIiMillis = calendar.getTimeInMillis();
		Log.e("zhengwenhui", "SAVE HISTORY month:"+month+", day:"+day+", timeIiMillis:"+timeIiMillis+", step:"+step+", score:"+score+", maxNumber:"+maxNumber);
		historyDB.add(month, day, timeIiMillis, step, score, maxNumber);
	}
	
	public void closeHistoryDB(){
		if( null != historyDB ){
			historyDB.close();
		}
	}

	// Keep playing after winning (allows going over 2048)
	void keepPlaying() {
		this.keepPlaying = true;
		//this.actuator.continueGame(); // Clear the game won/lost message
	};

	// Return true if the game is lost, or has won and the user hasn't kept playing
	boolean isGameTerminated() {
		return over || (this.won && !this.keepPlaying);
	};


	// Get the vector representing the chosen direction
	Point getVector(int direction) {
		// Vectors representing tile movement
		int[][] map = {
				{ -1, 0 }, // Up
				{ 0, 1 },  // Right
				{ 1, 0 },  // Down
				{ 0, -1 }, // Left
		};

		return new Point(map[direction][0], map[direction][1]);
	}

	// Get the vector representing the chosen direction
	int[] getParameter(int direction) {
		// Vectors representing tile movement
		int[][] map = {
				{ 0,					1,	0, 					1	}, // Up
				{ 0,					1,	Grid.width_size-1,	-1	}, // Right
				{ Grid.height_size-1 ,	-1, 0, 					1	},  // Down
				{ 0, 					1,	0,					1	},  // Left
		};

		return map[direction];
	}

	int[] getUndoParameter(int direction) {
		// Vectors representing tile movement
		int[][] map = {
				{ Grid.height_size-1 ,  -1,	0, 					1	}, // Up
				{ 0,					1,	0,					1	}, // Right
				{ 0,					1,  0, 					1	},  // Down
				{ 0, 					1,	Grid.width_size-1,	-1	},  // Left
		};

		return map[direction];
	}

	// Save all tile positions and remove merger info
	void prepareTiles() {
		Tile tile;
		for(int height = 0; height < Grid.height_size; height++){
			for(int width = 0; width < Grid.width_size; width++){
				tile = grid.cells[height][width];
				tile.mergedFrom = null;
				tile.saveValue();
			}
		}
	}

	// Set up the game
	void setup() {
		if( null != grid ){
			//restart
			Tile tile;
			for(int height = 0; height < Grid.height_size; height++){
				for(int width = 0; width < Grid.width_size; width++){
					tile = grid.cells[height][width];
					tile.mergedFrom = null;
					tile.value = 0;
					tile.previousValue = 0;
				}
			}
		}else{
			grid        = new Grid();
		}
		
		score       = 0;
		step        = 0;
		maxNumber = 2;
		over        = false;
		won         = false;
		keepPlaying = false;

		// Add the initial tiles
		Log.i(TAG, "setup");
		mPrintInterface.printScore(score);
		mPrintInterface.printSteps(step);
		addStartTiles();
		// Update the actuator
		//actuate();
	};


	// Adds a tile in a random position
	void addRandomTile() {
		Log.i(TAG, "addRandomTile");

		random = grid.randomAvailableCell();

		if ( null != random ) {
			//int value = Math.random() < 0.9 ? 2 : 4;
			random.value = 2;
			mPrintInterface.addRandomTile(random);
			Log.e(TAG, "addRandomTile tile.value:"+random.value);
		}
	}

	// Set up the initial tiles to start the game with
	void addStartTiles() {
		Log.i(TAG, "addStartTiles");
		for (int i = 0; i < startTiles; i++) {
			addRandomTile();
		}
	}; 

	// Move a tile and its representation
	void moveTile(Tile from, Tile to) {
		mPrintInterface.moveView(from, to);
		to.value += from.value;
		from.value = 0;
		//cell.mergedFrom = tile;
		//tile = cell;
	}

	// Undo Move a tile and its representation
	void undoMoveTile(Tile tile, Tile cell) {
		tile.value = tile.previousValue;
		cell.value -= tile.value;
		//cell.mergedFrom = tile;
		//tile = cell;
	}

	// Move tiles on the grid in the specified direction
	void Move(int direction){
		// 0: up, 1: right, 2: down, 3: left
		Tile tile, other;
		int tempH, tempW;

		if (this.isGameTerminated()) return;

		// Save the current tile positions and remove merger information
		prepareTiles();

		Point point = getVector(direction);
		int []param = getParameter(direction);
		boolean moved = false;
		
		Log.e(TAG, "Move direction:"+direction);
		
		ArrayList<Tile> formTiles = new ArrayList<Tile>();
		ArrayList<Tile> toTiles = new ArrayList<Tile>();

		for(int height = param[0]; Grid.withinHeight(height); height+=param[1]){
			for(int width = param[2]; Grid.withinHeight(width); width+= param[3]){
				tile = grid.cells[height][width];
				Log.v(TAG, "height:"+height+",width:"+width);
				if( 0 == tile.value ){
					continue;
				}

				tempH = height + point.heigth;
				tempW = width + point.width;

				while(Grid.withinBounds(tempH,tempW)){
					other = grid.cells[tempH][tempW];
					Log.i(TAG, "tempH:"+tempH+",tempW:"+tempW);
					
					if( 0 == other.value ){
						Log.i(TAG, "0 == other.value");
						moveTile(tile, other);
						
						formTiles.add(tile);
						toTiles.add(other);
						
						moved = true;
						tile = other;
						tempH += point.heigth;
						tempW += point.width;
					}
					else if( tile.value == other.value && null == other.mergedFrom ){
						moveTile(tile, other);
						
						formTiles.add(tile);
						toTiles.add(other);
						
						moved = true;
						other.mergedFrom = new Point(tile.heigth, tile.width);
						if( maxNumber < other.value ){
							maxNumber = other.value;
						}
						score += other.value;
						mPrintInterface.printScore(score);
						if (other.value == 2048) won = true;
						break;
					}
					else{
						Log.i(TAG, "other");
						break;
					}
				}
			}
		}

		if (moved) {
			Log.i(TAG, "moved");
			addRandomTile();
			mPrintInterface.moveViewsSetp(formTiles.toArray(), toTiles.toArray(), direction);
			mPrintInterface.printSteps(++step);
			if (!movesAvailable()) {
				Log.i(TAG, "over");
				this.over = true; // Game over!
				saveHistory();
			}
			else{
				Log.i(TAG, "undo");
				undo = true;
			}
		}
	}

	// Move tiles on the grid in the specified direction
	void undoMove(int direction){
		// 0: up, 1: right, 2: down, 3: left
		Tile tile, other;
		int tempH, tempW;

		if (!undo) return; //不能undo

		random.value = 0; //新加的取消掉

		Point point = getVector(direction);
		int []param = getUndoParameter(direction);

		for(int height = param[0]; Grid.withinHeight(height); height+=param[1]){
			for(int width = param[2]; Grid.withinHeight(width); width+= param[3]){
				tile = grid.cells[height][width];

				if(tile.value == tile.previousValue ){
					continue;
				}

				tempH = height + point.heigth;
				tempW = width + point.width;

				while(Grid.withinBounds(tempH,tempW)){
					other = grid.cells[tempH][tempW];

					if( 0 == other.value ){
						tempH += point.heigth;
						tempW += point.width;
						continue;
					}
					else{
						undoMoveTile(tile, other);
						score -= ( other.value - tile.previousValue );
						mPrintInterface.printScore(score);
						break;
					}
				}
			}
		}

		if( grid.maxValue() < 2048 ){
			won = false;
		}
		
		mPrintInterface.printSteps(++step);
		undo = false;
	}


	boolean movesAvailable() {
		return grid.cellsAvailable() > 0 || this.tileMatchesAvailable();
	};

	// Check for available matches between tiles (more expensive check)
	boolean tileMatchesAvailable() {
		Tile tile, leftTile, topTile;

		for(int height = 0; height < Grid.height_size; height++){
			for(int width = 0; width < Grid.width_size; width++){
				tile = grid.cells[height][width];

				if( height >= 1 ){
					topTile = grid.cells[height-1][width];
					if( tile.value == topTile.value ){
						return true;
					}
				}

				if( width >= 1 ){
					leftTile = grid.cells[height][width-1];
					if( tile.value == leftTile.value ){
						return true;
					}
				}
			}
		}
		return false;
	};

	boolean positionsEqual(Tile first, Tile second){
		return first.heigth == second.heigth && first.width == second.width;
	}

	boolean positionsEqual(Point first, Point second){
		return first.heigth == second.heigth && first.width == second.width;
	}

	class Point{
		Point(int heigth, int width){
			this.heigth = heigth;
			this.width = width;
		}
		int heigth;
		int width;
	}
}
