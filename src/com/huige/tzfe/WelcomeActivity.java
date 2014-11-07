package com.huige.tzfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}

	public void onClickStartButton(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void onClickHistoryButton(View view){
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivity(intent);
	}
}
