package com.huige.tzfe;


public interface PrintInterface {
	void printSteps(int step);
	void printScore(int score);
	void moveView(Tile from, Tile to);
	void addRandomTile(Tile newTile);
	void moveViewsSetp(Object[] from, Object[] to, int direction);
}
