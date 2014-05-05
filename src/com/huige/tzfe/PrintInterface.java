package com.huige.tzfe;


public interface PrintInterface {

	static int GAME_SUCCESS = 0x80;
	static int GAME_OVER = 0x81;

	void printSteps(int step);
	void printScore(int score);
	void moveView(Tile from, Tile to);
	void addRandomTile(Tile newTile);
	void moveViewsSetp(Object[] from, Object[] to, int direction);
	void gameResult(int flag);
}
