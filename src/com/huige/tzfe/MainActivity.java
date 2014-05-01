package com.huige.tzfe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements PrintInterface{

	private int direction;

	private GameManager game;

	private String TAG = "tzfe";
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private TextView stepTextView;
	private TextView scoreTextView;
	private TableView tableLayout;

	private Typeface mAndroidClockMonoThin, mAndroidClockMonoBold;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tableLayout = (TableView)findViewById(R.id.table);
		stepTextView = (TextView)findViewById(R.id.step);
		scoreTextView = (TextView)findViewById(R.id.score);
		TypefaceSet();                                     
		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		tableLayout.setOnTouchListener(gestureListener);

		game = (GameManager)getLastNonConfigurationInstance();
		if( null == game ){  
			game = new GameManager(this,this);
		}
		else{
			game.setPrintInterface(MainActivity.this);
		}
		tableLayout.setDraw(game.grid.cells);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return game;
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.i(TAG, "onFling v  x:"+velocityX+",y:"+velocityY);
			Log.i(TAG, "e1 x:"+e1.getX()+",y:"+e1.getY());
			Log.i(TAG, "e2 x:"+e2.getX()+",y:"+e2.getY());

			float xInterval = e1.getX() - e2.getX();
			float yInterval = e1.getY() - e2.getY();

			float xIntervalAbs = Math.abs(xInterval);
			float yIntervalAbs = Math.abs(yInterval);

			direction = -1;
			if( xIntervalAbs > Util.SWIPE_MIN_DISTANCE && xIntervalAbs > yIntervalAbs*2){
				if( xInterval > 0 ){
					Log.i(TAG, "left");
					direction = 3;
				}
				else{
					Log.i(TAG, "right");
					direction = 1;
				}
			}

			if( yIntervalAbs > Util.SWIPE_MIN_DISTANCE && yIntervalAbs > xIntervalAbs*2){
				if( yInterval > 0 ){
					Log.i(TAG, "up");
					direction = 0;
				}
				else{
					Log.i(TAG, "down");
					direction = 2;
				}
			}
			Log.i(TAG, tableLayout.isAnimation() ? "is Animation " : "not Animation");

			if( direction >= 0 && !tableLayout.isAnimation()){
				game.Move(direction);
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Log.v(TAG, "onDown");
			return true;
		}
	}

	/*private void print(){
		tableLayout.invalidate();
	}*/

	public void onClickUndo(View view){
		if( game.undo ){
			game.undoMove(direction);
			//print();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void TypefaceSet(){
		mAndroidClockMonoThin = Typeface.createFromAsset(
				getAssets(), "fonts/AndroidClockMono-Thin.ttf");
		mAndroidClockMonoBold = Typeface.createFromAsset(
				getAssets(), "fonts/AndroidClockMono-Bold.ttf");
		/*mAndroidClockMonoLight = Typeface.createFromAsset(
				getAssets(), "fonts/AndroidClockMono-Light.ttf");
		mRobotoLabel= Typeface.create("sans-serif-condensed", Typeface.BOLD);*/

		stepTextView.setTypeface(mAndroidClockMonoThin);
		scoreTextView.setTypeface(mAndroidClockMonoBold);
	}

	@Override
	public void printSteps(int step) {
		// TODO Auto-generated method stub
		//getString(R.string.step);
		stepTextView.setText(getString(R.string.step)+step);
	}

	@Override
	public void printScore(int score) {
		// TODO Auto-generated method stub
		//getString(R.string.score);
		scoreTextView.setText(getString(R.string.score)+score);
	}

	@Override
	public void moveView(final Tile from, final Tile to) {
		Log.e(TAG, "Activity moveView");
		Log.e(TAG, "from["+from.heigth+","+from.width+"]:"+from.value+" > to["+to.heigth+","+to.width+"]:"+to.value+"\n");
	}

	@Override
	public void moveViewsSetp(Object[] from, Object[] to, int direction) {
		// TODO Auto-generated method stub
		Log.e(TAG, "Activity moveViewsSetp");
		tableLayout.moveViewsStepAnimation(from, to, game.getParameter(direction));
	}

	@Override
	public void addRandomTile(Tile newTile) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("\n");
		if( null != game){
			Tile[][] cells = game.grid.cells;

			for (int h = 0; h < 4; h++) {
				for (int w = 0; w < 4; w++) {
					sb.append(String.format(" %2d ", cells[h][w].previousValue));
				}
				sb.append("  >>>>  ");
				for (int w = 0; w < 4; w++) {
					sb.append(String.format(" %2d ", cells[h][w].value));
				}
				sb.append("\n");
			}
			Log.e(TAG, "sb.toString(): "+sb.toString());
		}
		tableLayout.addRandomTile(newTile);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			setTitle("action_settings");
			break;
		case R.id.action_restart:
			setTitle("action_restart");
			game.restart();
			tableLayout.invalidate();
			break;
		case R.id.action_history:
			Intent intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		game.saveHistory();
		game.closeHistoryDB();
		super.onDestroy();
	}
}
