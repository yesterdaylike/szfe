package com.huige.tzfe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	int viewIds[] = {
			R.id.tile0,
			R.id.tile1,
			R.id.tile2,
			R.id.tile3,
			R.id.tile4,
			R.id.tile5,
			R.id.tile6,
			R.id.tile7,
			R.id.tile8,
			R.id.tile9,
			R.id.tile10,
			R.id.tile11,
			R.id.tile12,
			R.id.tile13,
			R.id.tile14,
			R.id.tile15,
	};

	private int direction;

	TextView[] tileView = new TextView[16];
	private GameManager game;

	private String TAG = "tzfe";
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		for (int i = 0; i < viewIds.length; i++) {
			tileView[i] = (TextView)findViewById(viewIds[i]);
		}
		game = new GameManager();
		print();

		Button upButton = (Button)findViewById(R.id.up);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		upButton.setOnTouchListener(gestureListener);
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.e(TAG, "onFling v  x:"+velocityX+",y:"+velocityY);
			Log.i(TAG, "e1 x:"+e1.getX()+",y:"+e1.getY());
			Log.i(TAG, "e2 x:"+e2.getX()+",y:"+e2.getY());

			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			
			float xInterval = e1.getX() - e2.getX();
			float yInterval = e1.getY() - e2.getY();
			
			float xIntervalAbs = Math.abs(xInterval);
			float yIntervalAbs = Math.abs(yInterval);
			
			if( xIntervalAbs > SWIPE_MIN_DISTANCE && xIntervalAbs > yIntervalAbs*2){
				
			}
			
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

			}
			else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Log.v(TAG, "onDown");
			return true;
		}
	}

	private void print(){
		Tile[][] tileValus = game.grid.cells;
		Tile tile;
		for(int i = 0; i < tileView.length; i++ ){
			tile = tileValus[i/4][i%4];
			tileView[i].setText(String.valueOf(tile.value));
		}
	}

	public void onClickDirection(View view){
		switch (view.getId()) {
		/*		case R.id.up:
			direction = 0;
			break;
		case R.id.down:
			direction = 2;
			break;
		case R.id.left:
			direction = 3;
			break;*/
		case R.id.right:
			direction = 1;
			break;

		default:
			direction = -1;
			break;
		}

		if( direction >= 0){
			game.Move(direction);
			print();
		}
	}

	public void onClickUndo(View view){
		if( game.undo ){
			game.undoMove(direction);
			print();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
