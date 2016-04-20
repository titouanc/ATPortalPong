package edu.vub.portalpong.ui;

import edu.vub.atportalpong.R;
import edu.vub.atportalpong.Variables;
import edu.vub.atportalpong.R.id;
import edu.vub.atportalpong.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SplashActivity extends Activity {

	//private static final int splashTime = 3000;
	private static final int splashTime = 10;

	private Context mContext; 
	private boolean launchedSplash;
	private Thread splashThread;
	public static Typeface tf;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_splash);
		mContext = this;
		
		String s = Build.BRAND;
		tf = Typeface.createFromAsset(getAssets(),"FREEDOM.ttf");
        TextView textfield = (TextView) findViewById(R.id.title);
        
        ((TextView)findViewById(R.id.textView1)).setTypeface(tf,Typeface.NORMAL);
        ((TextView)findViewById(R.id.textView2)).setTypeface(tf,Typeface.NORMAL);
        ((TextView)findViewById(R.id.textView3)).setTypeface(tf,Typeface.NORMAL);
        ((TextView)findViewById(R.id.textView4)).setTypeface(tf,Typeface.NORMAL);

        ((TextView)findViewById(R.id.textView1)).setTextSize(35);
        ((TextView)findViewById(R.id.textView2)).setTextSize(35);
        ((TextView)findViewById(R.id.textView3)).setTextSize(35);
        ((TextView)findViewById(R.id.textView4)).setTextSize(35);

       
		textfield.setTypeface(tf,Typeface.NORMAL);
		textfield.setTextSize(75);
		textfield.setTextColor(Color.rgb(89, 162,217));
		textfield.setTextColor(Color.WHITE);

		 Button b = (Button) findViewById(R.id.PlayAlone);
	        Button b2 = (Button) findViewById(R.id.MultiPlayer);

	        b.setTypeface(tf);
	        b.setTextColor(Color.WHITE);
	        b.setTextSize(20);
		//	b.setTextColor(Color.argb(255,89, 162,217));
			
	        b2.setTypeface(tf);
	        b2.setTextColor(Color.WHITE);
	        b2.setTextSize(20);
			//b2.setTextColor(Color.argb(125,89, 162,217));

			// Singleplayer launches directly PortalPongActivity
	        b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!launchedSplash) {
						launchedSplash = true;
						Variables.multiplayer = false;
			    		startActivity(new Intent(SplashActivity.this, PortalPongActivity.class));
			    	}
				}
			});
	        
	        // Multiplayer launches JoinGame
	        b2.setOnClickListener(new OnClickListener() {
	    			@Override
	    			public void onClick(View v) {
	        			Variables.multiplayer = true;
	    				startActivity(new Intent(SplashActivity.this,JoinGame.class));
	    			}
	    		});     
	}

	@Override 
	public void onResume() {
		super.onResume();
		launchedSplash = false;
		
		/*splashThread = new Thread(){
	        public void run() {
	            try {
	                synchronized (this) {
	                    wait(splashTime);
	                }
	            } catch(InterruptedException ex) {
	                ex.printStackTrace();
	            } finally {
	            	launchSplash();
	            }
	        }
	    };
	    splashThread.start();*/
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	   /* if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        synchronized (splashThread) {
	            splashThread.notifyAll();
	        }
	    }*/
	    return true;
	} 
}
