package com.huige.tzfe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HistoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		ListView historyListview = (ListView)findViewById(R.id.history_listview);

		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.history_item,
				new String[]{"date","day","score"},
				new int[]{R.id.date,R.id.day,R.id.score});
		historyListview.setAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "20日");
		map.put("img", R.drawable.point);
		map.put("score", "256");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("date", "5月");
		map.put("day", "12日");
		map.put("img", R.drawable.point);
		map.put("score", "16");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "4月");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("date", "4月");
		map.put("day", "30日");
		map.put("img", R.drawable.point);
		map.put("score", "234132");
		list.add(map);

		return list;
	}
}
