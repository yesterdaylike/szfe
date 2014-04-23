package com.huige.tzfe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements PrintInterface{

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
	TextView messageTextView;
	TextView stepTextView;
	TextView scoreTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		for (int i = 0; i < viewIds.length; i++) {
			tileView[i] = (TextView)findViewById(viewIds[i]);
		}
		
		stepTextView = (TextView)findViewById(R.id.step);
		scoreTextView = (TextView)findViewById(R.id.score);
		
		game = new GameManager(MainActivity.this);
		print();

		messageTextView = (TextView)findViewById(R.id.message);
		View tableLayout = findViewById(R.id.table);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		tableLayout.setOnTouchListener(gestureListener);
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			messageTextView.setText("onFling v  x:"+velocityX+",y:"+velocityY);
			messageTextView.append("\n");
			messageTextView.append("e1 x:"+e1.getX()+",y:"+e1.getY());
			messageTextView.append("\n");
			messageTextView.append("e2 x:"+e2.getX()+",y:"+e2.getY());
			messageTextView.append("\n");

			float xInterval = e1.getX() - e2.getX();
			float yInterval = e1.getY() - e2.getY();

			float xIntervalAbs = Math.abs(xInterval);
			float yIntervalAbs = Math.abs(yInterval);

			String str = "null";

			if( xIntervalAbs > SWIPE_MIN_DISTANCE && xIntervalAbs > yIntervalAbs*2){
				if( xInterval > 0 ){
					Log.i(TAG, "left");
					str = "left";
					direction = 3;
				}
				else{
					Log.i(TAG, "right");
					str = "right";
					direction = 1;
				}
			}

			if( yIntervalAbs > SWIPE_MIN_DISTANCE && yIntervalAbs > xIntervalAbs*2){
				if( yInterval > 0 ){
					Log.i(TAG, "up");
					str = "up";
					direction = 0;
				}
				else{
					Log.i(TAG, "down");
					str = "down";
					direction = 2;
				}
			}

			if( direction >= 0){
				game.Move(direction);
				print();
			}

			messageTextView.append(str);
			messageTextView.append("\n");
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
			
			if( 0 == tile.value ){
				tileView[i].setText("");
			}
			else{
				tileView[i].setText(String.valueOf(tile.value));
			}
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

	@Override
	public void printSteps(int step) {
		// TODO Auto-generated method stub
		stepTextView.setText(String.valueOf(step));
	}

	@Override
	public void printScore(int score) {
		// TODO Auto-generated method stub
		scoreTextView.setText(String.valueOf(score));
		
	}
}
