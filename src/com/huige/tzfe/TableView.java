package com.huige.tzfe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TableView extends TextView {

	private String TAG = "TableView";
	private Tile[][] tiles;

	private Paint paint;			//方块画笔
	private Paint paintText;		//字符画笔

	private Rect rect;				//view的矩形范围
	private RectF cellRect;			//小方块的矩形范围

	private int value;				//int型
	private String valueStr;		//String型

	private Tile[] fromTiles;				
	private Tile[] toTiles;	
	private Tile curForm;
	private Tile curTo;

	//private int index;
	//private int step;

	int stepIntervalH;
	int stepIntervalW;
	private int IntervalH = 0;
	private int IntervalW = 0;

	private Tile randomTile = null;
	int []stepSign;

	private boolean mInitPosition = false;
	private boolean animation = false;
	private int count = 0;
	private int[] dirParam = { 0,					1,	0, 					1	};

	int []positionsH;
	int []positionsW;

	float []positionsHText;
	float []positionsWText;

	private Handler handler = new Handler();

	private Runnable runnable= new Runnable() {
		public void run() {  
			handler.postDelayed(this, 2);
			TableView.this.postInvalidate();
			count++;
			Log.i("zhengwenhui", "count: "+count);
		}  
	};

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Style.FILL);

		paintText = new Paint();
		paintText.setTextSize(24);
	}

	private void initPositionlist(){
		int height = getHeight();	//view总的高度
		int width = getWidth(); 	//view总的宽度

		int cellHeight = height >> 2;//小方块的高度
		int cellWidth = width >> 2;  //小方块的宽度

		stepIntervalH = cellHeight >> 3;//小方块的高度
		stepIntervalW = cellWidth >> 3;  //小方块的宽度

		float halfHeightText = paintText.getTextSize() /2 ;

		positionsH = new int[5];
		positionsW = new int[5];

		positionsHText = new float[5];
		positionsWText = new float[5];

		for(int h = 0; h < 5; h++){
			positionsH[h] = cellHeight * h;
			positionsHText[h] = positionsH[h] + ( cellHeight >> 1 ) + halfHeightText;
			for(int w = 0; w < 5; w++){
				positionsW[w] = cellWidth * w;
				positionsWText[w] = positionsW[w] + ( cellWidth >> 1 );
			}
		}

		rect = new Rect(0, 0, width, height);
		cellRect = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if( !mInitPosition ){
			initPositionlist();
			mInitPosition = true;
		}

		if(animation){
			index = count >> 3;
			step = count % 8 + 1;
			if (index < fromTiles.length) {
				curForm = fromTiles[index];
				curTo = toTiles[index];
			} else {
				handler.removeCallbacks(runnable);
				animation = false;
			}
		}

		paint.setColor(Util.getColor(0));
		canvas.drawRect(rect, paint);

		for(int h = dirParam[0]; Grid.withinHeight(h); h+=dirParam[1]){
			for(int w = dirParam[2]; Grid.withinHeight(w); w+= dirParam[3]){
				IntervalH = 0;
				IntervalW = 0;
				//Log.v(TAG, "for for ["+h+","+w+"] previousValue:"+tiles[h][w].previousValue);

				if( animation ){
					if ( h == curForm.heigth && w == curForm.width && ( curTo.previousValue == 0 || step <=4 ) ) {
						IntervalH = ( ( curTo.heigth - curForm.heigth ) * stepIntervalH * step );
						IntervalW = ( ( curTo.width - curForm.width ) * stepIntervalW * step );
					} else if( h == curTo.heigth && w == curTo.width ){
						if( curTo.previousValue == 0 && step == 8 || curTo.previousValue != 0 && step == 5 ){
							curTo.previousValue += curForm.previousValue;
							curForm.previousValue = 0;
						}
					}
					value = tiles[h][w].previousValue;
				}
				else{
					value = tiles[h][w].value;
				}

				cellRect.set(positionsW[w]+IntervalW, positionsH[h]+IntervalH, positionsW[w+1]+IntervalW, positionsH[h+1]+IntervalH);
				paint.setColor( Util.getColor( value ) );
				canvas.drawRoundRect(cellRect, 5f, 5f, paint);

				if( value > 0 ){
					valueStr = String.valueOf(value);
					float halfWidthText = paintText.measureText(valueStr) /2;
					canvas.drawText(valueStr, positionsWText[w] + IntervalW - halfWidthText, positionsHText[h]+IntervalH, paintText);
				}
			}
		}
	}

	public void setDraw( Tile[][] tiles ) {
		this.tiles = tiles;
	}

	public void moveViewsStepAnimation(Object[] from, Object[] to, int[] directionParameter){
		count = 0;
		animation = true;
		
		int length = from.length;
		
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
	
	private void setIn(int timer){
		int index = timer >> 3;
		int step = timer % 8;
		
		boolean hasStep = false;
		
		for (int i = 0; i < fromTiles.length; i++) {
			if( index == stepSign[i] ){
				hasStep = true;
				curForm = fromTiles[i];
				curTo = toTiles[i];
				IntervalH = ( ( curTo.heigth - curForm.heigth ) * stepIntervalH * step );
				IntervalW = ( ( curTo.width - curForm.width ) * stepIntervalW * step );
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
}
