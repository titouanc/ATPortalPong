package edu.vub.portalpong.ui;

import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import edu.vub.at.IAT;
import edu.vub.at.android.util.IATAndroid;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.atportalpong.ATPortalPongAssetInstaller;
import edu.vub.atportalpong.GameLoop;
import edu.vub.atportalpong.MultiPlayerGameLoop;
import edu.vub.atportalpong.R;
import edu.vub.atportalpong.Variables;
import edu.vub.atportalpong.interfaces.ATATPortalPong;
import edu.vub.atportalpong.interfaces.JATPortalPong;
import edu.vub.atportalpong.interfaces.JATPortalPongRegisterer;
import edu.vub.portalpong.effects.PortalExcitedEffect;
import edu.vub.portalpong.objects.Bitmaps;
import edu.vub.portalpong.objects.GameObject;
import edu.vub.portalpong.objects.PickableGameObject;
import edu.vub.portalpong.objects.PickableMovableGameObject;
import edu.vub.portalpong.sound.SoundEffects;
import edu.vub.portalpong.view.GameView;

public class PortalPongActivity extends Activity implements JATPortalPongRegisterer {
	private static GameLoop gameloop;
	public static IAT iat;
	boolean end = false;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Bitmaps.LoadBitmaps(this);
        
        if (Variables.user == null) {
        	throw new IllegalStateException("Variables.user should not be null");
        }
        if(iat == null && Variables.multiplayer){ //No need to start asset installer for single player
        	Intent i = new Intent(this, ATPortalPongAssetInstaller.class);
        	startActivityForResult(i,0);
        }
    }
    
    public JATPortalPong registerATApp(ATATPortalPong atpp){
    	if(Variables.multiplayer){
    		MultiPlayerGameLoop mpg = (MultiPlayerGameLoop) gameloop;
    		mpg.setATPP(atpp);
    		return mpg;
    	}
    	else{
    		//AT is only set-up in multi-player mode, registering will not happen when playing alone
    		return null;
    	}
    }
    
    public void onBackPressed() {
    	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	    builder.setMessage("Are you sure you want to leave?")
    	           .setCancelable(false)
    	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	        	   public void onClick(DialogInterface dialog, int id) {
    	        		   PortalPongActivity.this.end = true;
    	        		   PortalPongActivity.this.finish();
    	               }
    	           })
    	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	               public void onClick(DialogInterface dialog, int id) {
    	                    dialog.cancel();
    	               }
    	           });
    	    AlertDialog alert = builder.create();
    	    alert.show();
    }
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("WeScribble", "Return of Asset Installer activity");
		switch (requestCode) {
		case (0):
			if (resultCode == Activity.RESULT_OK) {
		    	SurfaceView sv = (SurfaceView) findViewById(R.id.field);
		    	SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
		    	sv.setOnTouchListener(new SurfaceTouchListener());
				// Create a new game
		        if (gameloop == null) {
		        	if (Variables.multiplayer) {
		        		//Get the device's unique identifier (hardware)
		        		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		        		String id  = telephonyManager.getDeviceId();
		        		MultiPlayerGameLoop multiplayerloop = new MultiPlayerGameLoop(Variables.user,id, this);
		        		gameloop = multiplayerloop;
		        		//Create AT here
		        		new StartIATTask().execute((Void)null);
		        	} else{
		        		// playing alone
		        		gameloop = new GameLoop(Variables.user, this);
		        	}  	
		        }  else {
		        	//this is needed for when the game is restarted after a shutdown.
		        	if (Variables.multiplayer) {
		        		//TODO starts two instances of at, bug 
		        	}
		        }
		    	gameloop.updateDepencies(new GameView(sv), sm, new SoundEffects(this)); 
		    	gameloop.startRunning();
			}
			break;
		}
	}

 

	/*
	@Override
	public void onPause() {
		super.onPause();
		if (end){
			gameloop.stopRunning();
			if (Variables.multiplayer) {
				((MultiPlayerGameLoop) gameloop).shutdown();
	    	}
			gameloop = null;
		} else {
			gameloop.stopRunning();
		}
	}
	
    
    @Override
    public void onStop() {
    	super.onStop();
    	if(end){
    		if(gameloop != null) {
    			gameloop.stopRunning();
    			if (Variables.multiplayer) {
    				((MultiPlayerGameLoop) gameloop).shutdown();
    			}
    			gameloop = null;
    		}
		} else {
			gameloop.stopRunning();
		}
    }
    */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// option menu should not stop the game.
		//finish();
		//gameloop.stopRunning();
		return super.onCreateOptionsMenu(menu);
	}
	
    private final class SurfaceTouchListener implements OnTouchListener {
    	private PickableMovableGameObject pickedObject;
    	private PortalExcitedEffect portalEffect;
    	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// Note: always return true in fullscreen mode
			final int scaledX = (int)(event.getX() / v.getWidth() * 1280);
			final int scaledY = (int)(event.getY() / v.getHeight() * 720);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				PickableGameObject pgo = gameloop.getWorld().pickGameObject(scaledX, scaledY);
				if (pgo == null) return true;
				if (pgo instanceof PickableMovableGameObject) {
					pickedObject = (PickableMovableGameObject) pgo;
					gameloop.getWorld().addGameObject((GameObject) pickedObject);
				} else if (pickedObject instanceof PortalExcitedEffect) {
					portalEffect = (PortalExcitedEffect) pickedObject;
					pickedObject = portalEffect.portal;
				}
				return true;
			case MotionEvent.ACTION_UP:
				PickableGameObject target = gameloop.getWorld().pickGameObject(scaledX, scaledY);
				if (pickedObject == null) return true;
				gameloop.getWorld().removeGameObject((GameObject) pickedObject);
				pickedObject.dropOnto(target);
				pickedObject = null;
				portalEffect = null;
				return true;
			case MotionEvent.ACTION_MOVE:
				if (pickedObject == null) return true;
				pickedObject.moveTo(scaledX, scaledY);
				if (portalEffect != null) {
					portalEffect.moveTo(scaledX, scaledY);
				}
				return true;
			}
			return true;
		}
	}
    
    public static GameLoop gameloopInstance() {
		return gameloop;
	}

    public void showScores() {
    	Intent intent = new Intent(this, ScoreActivity.class);
    	end = true;
    	finish();
    	startActivity(intent);
    }
 
    
    public class StartIATTask extends AsyncTask<Void,String,Void> {

    	@Override
    	protected Void doInBackground(Void... params) {
    		try {
    			iat = IATAndroid.create(PortalPongActivity.this);
    			iat.evalAndPrint("import /.ATPortalPong.ATPortalPong.makeATPortalPong()", System.err);
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (InterpreterException e) {
    			Log.e("AmbientTalk","Could not start IAT",e);
    		}
    		return null;
    	}

    }

}
