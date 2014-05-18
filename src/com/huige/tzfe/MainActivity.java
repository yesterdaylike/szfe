package com.huige.tzfe;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements PrintInterface{

	private int direction;

	private GameManager game;

	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private TextView stepTextView;
	private TextView scoreTextView;
	private TableView tableLayout;
	private View clingView;
	private View snowFallView;

	private Typeface mAndroidClockMonoThin, mAndroidClockMonoBold;

	static String FIRST_RUN_CLING_DISMISSED_KEY = "FIRST_RUN_CLING_DISMISSED_KEY";
	private SharedPreferences mSharedPrefs;
	boolean openCling = false;
	int clingStep = 0;
	int clingCount = 0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdManager.getInstance(this).init("03412307dfefb843", "80cf970036505cd6", false);

		setContentView(R.layout.activity_main);

		tableLayout = (TableView)findViewById(R.id.table);
		cling();

		stepTextView = (TextView)findViewById(R.id.step);
		scoreTextView = (TextView)findViewById(R.id.score);
		snowFallView = (SnowFallView)findViewById(R.id.snow);
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

		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		adLayout.addView(adView);
	}

	private void cling(){
		mSharedPrefs = getSharedPreferences(FIRST_RUN_CLING_DISMISSED_KEY,
				Context.MODE_PRIVATE);
		openCling = mSharedPrefs.getBoolean(FIRST_RUN_CLING_DISMISSED_KEY, true);
		if(openCling){
			clingView = findViewById(R.id.cling);
			clingView.setVisibility(View.VISIBLE);
			SharedPreferences.Editor editor = mSharedPrefs.edit();
			editor.putBoolean(FIRST_RUN_CLING_DISMISSED_KEY,false);
			editor.commit();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return game;
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			//Log.i(TAG, "onFling v  x:"+velocityX+",y:"+velocityY);
			//Log.i(TAG, "e1 x:"+e1.getX()+",y:"+e1.getY());
			//Log.i(TAG, "e2 x:"+e2.getX()+",y:"+e2.getY());

			float xInterval = e1.getX() - e2.getX();
			float yInterval = e1.getY() - e2.getY();

			float xIntervalAbs = Math.abs(xInterval);
			float yIntervalAbs = Math.abs(yInterval);

			direction = -1;
			if( xIntervalAbs > Util.SWIPE_MIN_DISTANCE && xIntervalAbs > yIntervalAbs*2){
				if( xInterval > 0 ){
					//Log.i(TAG, "left");
					direction = 3;
				}
				else{
					//Log.i(TAG, "right");
					direction = 1;
				}
			}

			if( yIntervalAbs > Util.SWIPE_MIN_DISTANCE && yIntervalAbs > xIntervalAbs*2){
				if( yInterval > 0 ){
					//Log.i(TAG, "up");
					direction = 0;
				}
				else{
					//Log.i(TAG, "down");
					direction = 2;
				}
			}
			//Log.i(TAG, tableLayout.isAnimation() ? "is Animation " : "not Animation");

			if( direction >= 0 && !tableLayout.isAnimation()){
				game.Move(direction);
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			//Log.v(TAG, "onDown");
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
		//Log.e(TAG, "Activity moveView");
		//Log.e(TAG, "from["+from.heigth+","+from.width+"]:"+from.value+" > to["+to.heigth+","+to.width+"]:"+to.value+"\n");
	}

	@Override
	public void moveViewsSetp(Object[] from, Object[] to, int direction) {
		// TODO Auto-generated method stub
		//Log.e(TAG, "Activity moveViewsSetp");
		tableLayout.moveViewsStepAnimation(from, to, game.getParameter(direction));
	}

	@Override
	public void addRandomTile(Tile newTile) {
		// TODO Auto-generated method stub
		int cling2 = 0;

		//StringBuffer sb = new StringBuffer("\n");
		if( null != game){
			Tile[][] cells = game.grid.cells;
			for (int h = 0; h < 4; h++) {
				/*for (int w = 0; w < 4; w++) {
					sb.append(String.format(" %2d ", cells[h][w].previousValue));
				}
				sb.append("  >>>>  ");*/
				for (int w = 0; w < 4; w++) {
					//sb.append(String.format(" %2d ", cells[h][w].value));
					if(openCling){
						if(clingStep == 1 && cells[h][w].value == 4){
							cling2 = 4;
						}
					}
				}
				//sb.append("\n");
				//Log.e(TAG, "sb.toString(): "+sb.toString());
			}
		}

		if(openCling){
			if( clingStep == 0){
				clingCount++;
				clingView.setBackgroundResource(R.drawable.cling1);
				if(clingCount == 2){
					clingStep = 1;
				}
			}
			else if( clingStep == 1){
				if(cling2 == 4){
					clingView.setBackgroundResource(R.drawable.cling2);
					clingStep = 2;
				}
				else{
					clingView.setBackgroundResource(0);
					clingStep = 1;
				}
			}
			else if( clingStep == 2){
				clingView.setBackgroundResource(R.drawable.cling3);
				clingStep = 3;
			}
			else if( clingStep == 3){
				clingView.setVisibility(View.GONE);
				clingStep = 0;
				openCling = false;
			}
			//tableLayout.invalidate();
		}

		tableLayout.addRandomTile(newTile);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		/*case R.id.action_settings:
			setTitle("action_settings");
			break;*/
		case R.id.action_restart:
			//setTitle("action_restart");
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

	@Override
	public void gameResult(int flag) {
		// TODO Auto-generated method stub
		String message = "";
		switch (flag) {
		case PrintInterface.GAME_OVER:
			message = "游戏结束\n";
			break;
		case PrintInterface.GAME_SUCCESS:
			message = "游戏成功\n";
			break;

		default:
			break;
		}

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(getString(R.string.score));
		strBuilder.append(game.score);
		strBuilder.append("， ");

		strBuilder.append(getString(R.string.step));
		strBuilder.append(game.step);

		SpotManager.getInstance(this).showSpotAds(this);

		snowFallView.setVisibility(View.VISIBLE);

		Dialog dialog = new Dialog(this, R.style.Theme_dialog);
		dialog.setContentView(R.layout.layout_dialog);
		dialog.show();
		TextView mMessage = (TextView) dialog.findViewById(R.id.message);
		mMessage.setText(message+strBuilder.toString());
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				snowFallView.setVisibility(View.INVISIBLE);
			}
		});
	}
}
