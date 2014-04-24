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

	private int height = 0;			//view总的高度
	private int width = 0;			//view总的宽度
	private Rect rect;				//view的矩形范围

	private int cellHeight = 0;		//小方块的高度
	private int cellWidth = 0;		//小方块的宽度
	private RectF cellRect;			//小方块的矩形范围

	private float halfHeightText;	//字符高度的一半
	private float halfWidthText;	//字符宽度的一半

	private int positionWText;		//字符的中心位置之w轴上
	private int positionHText;		//字符的中心位置之w轴上

	private int value;				//int型
	private String valueStr;		//String型

	private Object[] from;				
	private Object[] to;				

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Style.FILL);

		paintText = new Paint();
		paintText.setTextSize(24);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if( 0 == height ){
			height = getHeight();
			width = getWidth();

			cellHeight = height >> 2;
			cellWidth = width >> 2;

			rect = new Rect(0, 0, width, height);
			cellRect = new RectF();

			halfHeightText = paintText.getTextSize() /2 ;
		}
		paint.setColor(Util.getColor(0));
		canvas.drawRect(rect, paint);

		for (int h = 0, positionH = 0; h < 4; h++, positionH += cellHeight) {
			for (int w = 0, positionW = 0; w < 4; w++, positionW += cellWidth) {

				value = tiles[h][w].value;

				cellRect.set(positionW+1, positionH+1, positionW+cellWidth-1, positionH+cellHeight-1);
				paint.setColor( Util.getColor( value ) );
				canvas.drawRoundRect(cellRect, 5f, 5f, paint);

				if( value > 0 ){
					valueStr = String.valueOf(value);

					positionWText = positionW + ( cellWidth >> 1 );
					positionHText = positionH + ( cellHeight >> 1 );

					halfWidthText = paintText.measureText(valueStr) /2;

					canvas.drawText(valueStr, positionWText - halfWidthText, positionHText + halfHeightText, paintText);
				}
			}
		}
	}

	public void setDraw( Tile[][] tiles ) {
		this.tiles = tiles;
	}

	public void moveViewsStepAnimation(Object[] from, Object[] to){
		/*this.from = from;
		this.to = to;
		for (Object object : to) {

		}*/
		Log.e("zhengwenhui", "================================");
		handler.post(runnable);
	}

	private Handler handler = new Handler();  
	private int count = 0;
	private Runnable runnable= new Runnable() {    
		public void run() {  
			handler.postDelayed(this, 500);
			Log.i("zhengwenhui", "count: "+count);
			count++;
		}  
	}; 
}
