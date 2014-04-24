package com.huige.tzfe;


public class Util {

	static final int SWIPE_MIN_DISTANCE = 80;
	static final int SWIPE_MAX_OFF_PATH = 250;
	static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private static int []colors = {
		0xfffff6df,
		0xffffecc0,
		0xffffe3a0,
		0xffffd980,
		0xffffd060,
		0xffffc641,
		0xffffbd21,
		0xffffb61c,
		0xffffae18,
		0xffffa713,
		0xffffa00e,
		0xffff9909,
		0xffff9105,
		0xffff8a00,
	};


	public static int getColor(int number){
		int color = colors[0];
		for (int i = 1; i < colors.length; i++) {
			if( number>>i == 1 ){
				color = colors[i];
				break;
			}
		}
		return color;
	}
}
