package com.huige.tzfe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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
	
	TextView[] tileView = new TextView[16];
	private GameManager game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		for (int i = 0; i < viewIds.length; i++) {
			tileView[i] = (TextView)findViewById(viewIds[i]);
		}
		game = new GameManager();
		print();
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
		int direction = -1;
		switch (view.getId()) {
		case R.id.up:
			direction = 0;
			break;
		case R.id.down:
			direction = 2;
			break;
		case R.id.left:
			direction = 3;
			break;
		case R.id.right:
			direction = 1;
			break;

		default:
			break;
		}
		
		if( direction >= 0){
			game.Move(direction);
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
