package edu.vub.portalpong.ui;

import edu.vub.atportalpong.R;
import edu.vub.atportalpong.Variables;
import edu.vub.atportalpong.R.id;
import edu.vub.atportalpong.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JoinGame  extends Activity {
	private Button joinButton;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	setContentView(R.layout.getname);
    	
    	
    	final SharedPreferences p = getSharedPreferences("NICK", MODE_PRIVATE);
    	String nick = p.getString("NICK", "");
    	
        ((TextView)findViewById(R.id.textView2)).setTypeface(SplashActivity.tf,Typeface.NORMAL);
        ((TextView)findViewById(R.id.textView2)).setTextSize(35);

        TextView textfield = (TextView) findViewById(R.id.title);
		textfield.setTypeface(SplashActivity.tf,Typeface.NORMAL);
		textfield.setTextSize(75);
		textfield.setTextColor(Color.rgb(89, 162,217));
		textfield.setTextColor(Color.WHITE);
		
		final EditText input = ((EditText)findViewById(R.id.nick));
		input.setTypeface(SplashActivity.tf,Typeface.NORMAL);
		input.setTextSize(35);
		input.setText(nick);
        
        Button b = (Button) findViewById(R.id.startGame);
        b.setTypeface(SplashActivity.tf);
        b.setTextColor(Color.WHITE);
        b.setTextSize(25);
        b.setOnClickListener(new OnClickListener() {			
        	@Override
        	public void onClick(View v) {
        		String nick = input.getText().toString();
        		if (nick != null) {
        			Editor pe = p.edit();
        			pe.putString("NICK", nick);
        			pe.commit();
        			Variables.user = nick;
        			Intent i = new Intent(JoinGame.this, PortalPongActivity.class);
        			startActivity(i);
        		}
        	}
        });
    	
    }

}
