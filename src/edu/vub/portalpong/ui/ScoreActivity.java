package edu.vub.portalpong.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.vub.atportalpong.GameLoop;
import edu.vub.atportalpong.R;



public class ScoreActivity extends Activity {
	Typeface tf;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.score);
		
		tf = Typeface.createFromAsset(getAssets(),"FREEDOM.ttf");
        TextView textfield = (TextView) findViewById(R.id.title);
    	textfield.setTypeface(tf,Typeface.NORMAL);
		textfield.setTextSize(75);
		textfield.setTextColor(Color.rgb(89, 162,217));
		textfield.setTextColor(Color.WHITE);
		ListView listView = (ListView) findViewById(R.id.listView1);

		Iterator<Map.Entry<String, Long>> it = GameLoop.scores.entrySet().iterator();
		List<String> list = new ArrayList<String>();
		while(it.hasNext()) {
			Entry<String, Long> pairs = it.next();
			list.add((String)pairs.getKey() +" : "+ (Long)pairs.getValue());
		}

		ArrayAdapter<String> simpleAdpt = new MyAdaptor(this, android.R.layout.simple_list_item_1,list);
		listView.setAdapter(simpleAdpt);
		simpleAdpt.notifyDataSetChanged();
	}
	
	class MyAdaptor extends ArrayAdapter<String> {

		public MyAdaptor(Context context,  int textViewResourceId,List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View view, ViewGroup viewGroup) {
			View v = super.getView(position, view, viewGroup);
			((TextView)v).setTypeface(tf);
			return v;
		}
		
	}

}
