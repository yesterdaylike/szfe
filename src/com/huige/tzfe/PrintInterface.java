package com.huige.tzfe;


public interface PrintInterface {
	public void printSteps(int step);
	public void printScore(int score);
	public void moveView(Tile from, Tile to);
	public void moveViewsSetp(Object[] from, Object[] to);
	public void addRandomTile(Tile newTile);
}
