package com.huige.tzfe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TableView extends TextView {

	Paint paint;
	private int height = 0;
	private int width = 0;

	private int cellHeight = 0;
	private int cellWidth = 0;

	Rect rect;
	RectF cellRect;
	String TAG = "TableView";

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Util.getColor(0));
		// ÉèÖÃÑùÊ½-Ìî³ä
		paint.setStyle(Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if( 0 == height ){
			height = getHeight();
			width = getWidth();
			
			cellHeight = height >> 2;
			cellWidth = width >> 2;
			
			rect = new Rect(0, 0, width, height);
			cellRect = new RectF(cellWidth+1, cellHeight+1, (cellWidth<<1)-1, (cellHeight<<1)-1);
		}
		paint.setColor(Util.getColor(128));
		//canvas.drawRect(rect, paint);
		canvas.drawRoundRect(cellRect, 20f, 20f, paint);
	}
}
