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
		switch (number) {
		case 2: color = colors[0];  break;
		case 4: color = colors[1];  break;
		case 8: color = colors[2];  break;
		case 16: color = colors[3];  break;
		case 32: color = colors[4];  break;
		case 64: color = colors[5];  break;
		case 128: color = colors[6];  break;
		case 256: color = colors[7];  break;
		case 512: color = colors[8];  break;
		case 1024: color = colors[9];  break;
		case 2048: color = colors[10];  break;
		case 4096: color = colors[11];  break;
		case 8192: color = colors[12];  break;
		case 16384: color = colors[13];  break;
		default: break;
		}
		return color;
	}
}
