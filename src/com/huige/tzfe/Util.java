package com.huige.tzfe;


public class Util {

	static final int SWIPE_MIN_DISTANCE = 80;
	static final int SWIPE_MAX_OFF_PATH = 250;
	static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private static int []colors = {
		0xffaaaaaa,
		0xffffcccc,	//2
		0xffffcc99,	//4
		0xff99cccc,	//8
		0xff99cc99,	//16
		0xff66cccc,	//32
		0xffccccff,	//64
		0xffccff99,	//128
		0xffccffff,	//256
		0xffff9966,	//512
		0xffff6666,	//1024
		0xffcc3399,	//2048
		0xff9933cc,	//4096
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
