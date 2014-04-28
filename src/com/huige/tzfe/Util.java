package com.huige.tzfe;


public class Util {

	static final int SWIPE_MIN_DISTANCE = 80;
	static final int SWIPE_MAX_OFF_PATH = 250;
	static final int SWIPE_THRESHOLD_VELOCITY = 200;

	public static int []colors = {
		R.drawable.c2,
		R.drawable.c4,
		R.drawable.c8,
		R.drawable.c16,
		R.drawable.c32,
		R.drawable.c64,
		R.drawable.c128,
		R.drawable.c256,
		R.drawable.c512,
		R.drawable.c1024,
		R.drawable.c2048,
		R.drawable.c4096,
		R.drawable.c8192,
		R.drawable.c16384,
	};

	public static int getColor(int number){
		int color = colors[0];
		for (int i = 0; i < colors.length; i++) {
			if( number>>i <= 1 ){
				color = colors[i];
				break;
			}
		}
		return color;
	}
}
