package com.huige.tzfe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TableView extends TextView {

	private String TAG = "TableView";
	private Tile[][] tiles;

	private Paint paint;			//方块画笔
	private RectF cellRect;			//小方块的矩形范围

	private int value;				//int型

	private Tile[] fromTiles;				
	private Tile[] toTiles;	
	private Tile curForm;
	private Tile curTo;

	int stepIntervalH;
	int stepIntervalW;

	private Tile randomTile = null;
	int []stepSign;

	private boolean mInitPosition = false;
	private boolean animation = false;
	private int count = 0;
	private int[] dirParam = { 0,					1,	0, 					1	};

	int []positionsH;
	int []positionsW;

	Interval intervalGrid[][];
	
	private Util util;

	private Handler handler = new Handler();

	private Runnable runnable= new Runnable() {
		public void run() {  
			handler.postDelayed(this, 5);
			Log.i("zhengwenhui", "count start: "+count);
			TableView.this.postInvalidate();
			count++;
			Log.i("zhengwenhui", "count: "+count);
			if(count>96){
				handler.removeCallbacks(runnable);
				animation = false;
			}
		}  
	};

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint=new Paint();
        paint.setAntiAlias(true); 
        paint.setFilterBitmap(true);
        paint.setDither(true);
        util = new Util(getResources());
	}

	private void initPositionlist(){
		int height = getHeight();	//view总的高度
		int width = getWidth(); 	//view总的宽度

		int cellHeight = height >> 2;//小方块的高度
		int cellWidth = width >> 2;  //小方块的宽度

		stepIntervalH = cellHeight >> 3;//小方块的高度
		stepIntervalW = cellWidth >> 3;  //小方块的宽度

		//float halfHeightText = paintText.getTextSize() /2 ;

		positionsH = new int[5];
		positionsW = new int[5];

		//positionsHText = new float[5];
		//positionsWText = new float[5];

		for(int h = 0; h < 5; h++){
			positionsH[h] = cellHeight * h;
			//positionsHText[h] = positionsH[h] + ( cellHeight >> 1 ) + halfHeightText;
			for(int w = 0; w < 5; w++){
				positionsW[w] = cellWidth * w;
				//positionsWText[w] = positionsW[w] + ( cellWidth >> 1 );
			}
		}

		//rect = new Rect(0, 0, width, height);
		cellRect = new RectF();

		intervalGrid = new Interval[4][4];
		for(int h = 0; h < 4; h++){
			for(int w = 0; w < 4; w++){
				intervalGrid[h][w] = new Interval();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("zhengwenhui", "onDraw:"+count);
		if( !mInitPosition ){
			initPositionlist();
			mInitPosition = true;
		}
		if (animation) {
			setInit(count);
		}

		Interval interval;

		for(int h = dirParam[0]; Grid.withinHeight(h); h+=dirParam[1]){
			for(int w = dirParam[2]; Grid.withinHeight(w); w+= dirParam[3]){
				//Log.v(TAG, "for for ["+h+","+w+"] previousValue:"+tiles[h][w].previousValue);
				interval = intervalGrid[h][w];
				if( animation ){
					value = tiles[h][w].previousValue;
				}
				else{
					value = tiles[h][w].value;
				}

				cellRect.set(positionsW[w]+interval.w, positionsH[h]+interval.h, positionsW[w+1]+interval.w, positionsH[h+1]+interval.h);
				if(value==0){
					continue;
				}

				Bitmap bitmap = util.getBitmap(value);
				canvas.drawBitmap(bitmap, null, cellRect, paint);
			}
		}
	}

	public void setDraw( Tile[][] tiles ) {
		this.tiles = tiles;
	}

	public void moveViewsStepAnimation(Object[] from, Object[] to, int[] directionParameter){
		Log.e(TAG, "[moveViewsStepAnimation]");
		count = 0;
		animation = true;

		int length = from.length;
		Log.e(TAG, "length:"+length);

		fromTiles = new Tile[length];
		toTiles = new Tile[length];

		Tile last = null;
		stepSign = new int[length];

		for( int i = 0, whichstep = 0; i < length; i++){
			curForm = (Tile) from[i];
			curTo = (Tile) to[i];

			fromTiles[i] = curForm;
			toTiles[i] = curTo;

			if( null != last && last.heigth==curForm.heigth && last.width==curForm.width){
				whichstep++;
			}
			else{
				whichstep = 0;
			}
			stepSign[i] = whichstep;
			Log.i(TAG, "["+ i +"] :"+stepSign[i]);
			last = toTiles[i];
		}

		handler.post(runnable);
		this.dirParam = directionParameter;
	}

	private void setInit(int timer){
		int index = timer >> 3;
		int step = timer % 8 + 1;

		boolean hasStep = false;
		
		Log.i(TAG, "[setInit] timer: "+timer);

		for (Interval[] intervalArray : intervalGrid) {
			for (Interval intervalElement : intervalArray) {
				intervalElement.h = 0;
				intervalElement.w = 0;
			}
		}

		for (int i = 0; i < fromTiles.length; i++) {
			if( index == stepSign[i] ){
				hasStep = true;
				curForm = fromTiles[i];
				curTo = toTiles[i];
				Interval intervalFrom = intervalGrid[curForm.heigth][curForm.width];

				if( step == 8 ){
					curTo.previousValue += curForm.previousValue;
					curForm.previousValue = 0;
				}else{
					intervalFrom.h = ( ( curTo.heigth - curForm.heigth ) * stepIntervalH * step );
					intervalFrom.w = ( ( curTo.width - curForm.width ) * stepIntervalW * step );
				}
			}
		}

		if(!hasStep){
			handler.removeCallbacks(runnable);
			animation = false;
		}
	}

	public void addRandomTile(Tile newTile) {
		// TODO Auto-generated method stub
		randomTile = newTile;
		//invalidate();
	}

	public boolean isAnimation(){
		return animation;
	}

	class Interval{
		int h = 0;
		int w = 0;
		int size = 0;
	}
}
