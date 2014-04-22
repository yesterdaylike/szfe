package com.huige.tzfe;

import com.huige.tzfe.GameManager.Point;

public class Tile {
	int heigth;
	int width;
	int value;
	Point mergedFrom;

	int previousH;
	int previousW;

	Tile(int heigth, int width, int value){
		this.heigth = heigth;
		this.width = width;
		this.value = value;
	}

	void savePosition() {
		previousH = heigth;
		previousW = width;
	};

	void updatePosition(int heigth, int width) {
		this.heigth = heigth;
		this.width = width;
	}
}
