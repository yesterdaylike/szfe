package com.huige.tzfe;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Util {

	static final int SWIPE_MIN_DISTANCE = 80;
	static final int SWIPE_MAX_OFF_PATH = 250;
	static final int SWIPE_THRESHOLD_VELOCITY = 200;
	public Bitmap bitmaps[];
	private Resources resources;

	public Util(Resources res) {
		// TODO Auto-generated constructor stub
		bitmaps = new Bitmap[colors.length];
		this.resources = res;
	}

	private static int []colors = {
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

	private static int getColorIndex(int number){
		int colorIndex = 0;
		switch (number) {
		case 2: colorIndex = 0;  break;
		case 4: colorIndex = 1;  break;
		case 8: colorIndex = 2;  break;
		case 16: colorIndex = 3;  break;
		case 32: colorIndex = 4;  break;
		case 64: colorIndex = 5;  break;
		case 128: colorIndex = 6;  break;
		case 256: colorIndex = 7;  break;
		case 512: colorIndex = 8;  break;
		case 1024: colorIndex = 9;  break;
		case 2048: colorIndex = 10;  break;
		case 4096: colorIndex = 11;  break;
		case 8192: colorIndex = 12;  break;
		case 16384: colorIndex = 13;  break;
		default: break;
		}
		return colorIndex;
	}

	public static int getColor(int value){
		int index = getColorIndex(value);
		return colors[index];
	}

	public Bitmap getBitmap(int value){
		int index = getColorIndex(value);

		if( null == bitmaps[index] ){
			bitmaps[index] = BitmapFactory.decodeResource( resources, colors[index] );
		}
		return bitmaps[index];
	}
}
