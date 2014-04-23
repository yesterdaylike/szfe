package com.huige.tzfe;

import com.huige.tzfe.GameManager.Point;

public class Tile {
	int heigth;
	int width;
	int value;
	Point mergedFrom;

	int previousValue;

	Tile(int heigth, int width, int value){
		this.heigth = heigth;
		this.width = width;
		this.value = value;
	}

	void saveValue() {
		previousValue = value;
	};

	void updatePosition(int heigth, int width) {
		this.heigth = heigth;
		this.width = width;
	}
}
