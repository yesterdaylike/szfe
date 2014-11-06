package com.huige.tzfe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HistoryActivity extends Activity {
	private HistoryDB historyDB;
	String score;
	String step;
	String maxNumber;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTitle(getString(R.string.action_history));

		if( null == historyDB ){
			historyDB = new HistoryDB(this);
		}
		Cursor cursor = historyDB.query();

		if( null != cursor && cursor.getCount() > 0){
			setContentView(R.layout.activity_history);
			ListView historyListview = (ListView)findViewById(R.id.history_listview);

			score = getString(R.string.score);
			step = getString(R.string.step);
			maxNumber = getString(R.string.max_number);

			SimpleAdapter adapter = new SimpleAdapter(this,getData(cursor),R.layout.history_item,
					new String[]{"date","day","score"},
					new int[]{R.id.month, R.id.day,R.id.score});
			historyListview.setAdapter(adapter);
		}
		else {
			setContentView(R.layout.no_history);
		}

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		historyDB.close();
		super.onDestroy();
	}

	private List<Map<String, Object>> getData(Cursor cursor) {
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		int tomonth = calendar.get(Calendar.MONTH);

		int month, day, lastMonth = -1, lastDay = -1;
		String monthStr, dayStr; 
		StringBuilder builder = new StringBuilder();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();


		while (cursor.moveToNext()) {

			month = cursor.getInt(cursor
					.getColumnIndex(HistoryDB.month));
			day = cursor.getInt(cursor
					.getColumnIndex(HistoryDB.day));

			if( month == lastMonth ){
				monthStr = "";
				if( day == lastDay ){
					dayStr = "";
				}
				else{
					lastDay = day;
					dayStr = String.valueOf(day)+"��";
				}
			}
			else{
				lastMonth = month;
				lastDay = day;
				monthStr = String.valueOf(month+1)+"��";
				dayStr = String.valueOf(day)+"��";
			}

			if( dayStr != "" && month == tomonth ){
				if(day == today){
					monthStr = null;
					dayStr = "����";
				}
				else if(day == today-1){
					monthStr = null;
					dayStr = "����";
				}
			}

			builder.delete( 0, builder.length() );
			builder.append(score);
			builder.append(cursor.getString(cursor
					.getColumnIndex(HistoryDB.score)));
			builder.append("�� ");

			builder.append(step);
			builder.append(cursor.getString(cursor
					.getColumnIndex(HistoryDB.step)));
			/*builder.append(", ");

			builder.append(maxNumber);
			builder.append(cursor.getString(cursor
				.getColumnIndex(HistoryDB.maxnumber)));
			builder.append(", ");*/

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("month", monthStr);
			map.put("day", dayStr);
			//map.put("img", R.drawable.point);
			map.put("score", builder.toString());
			list.add(0, map);
		}

		return list;
	}
}
