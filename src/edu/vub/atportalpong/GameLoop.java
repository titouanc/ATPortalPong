package edu.vub.atportalpong;

import java.util.concurrent.ConcurrentHashMap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import edu.vub.portalpong.objects.GameWorld;
import edu.vub.portalpong.sound.SoundEffects;
import edu.vub.portalpong.ui.PortalPongActivity;
import edu.vub.portalpong.view.GameView;
import edu.vub.portalpong.view.Items.PowerUps;

public class GameLoop extends Thread implements SensorEventListener {

	public static final long MAX_SCORE = 1000;
	public String user;
	protected GameWorld world;
	protected GameView view;
	protected SensorManager sm;

	protected Sensor rv;
	private boolean running;
	private float dx;
	protected PortalPongActivity context;	
	public static ConcurrentHashMap<String,Long> scores = new ConcurrentHashMap<String, Long>();
	private final float[] mRotationMatrix = new float[16];
	// true when the Win Notification is received.
	protected boolean gameOver;

	public GameLoop(String user, PortalPongActivity c) {
		this.user = user;
		this.context = c;

		this.world = null;
		this.view = null;
		this.sm = null;
		this.rv = null;

		this.running = false;
		this.dx = 0;
		this.gameOver = false;
		this.start();
	}

	public void updateDepencies(GameView view, SensorManager sm, SoundEffects s) {	
		this.updateSomeDependencies(view, sm);
		if( world == null) {
			this.world = new GameWorld(1280, 720, user, s);
		} else {
			world.updateDepencies(1280, 720, user,s);
		}
	}

	protected void updateSomeDependencies(GameView view, SensorManager sm) {
		this.view = view;
		this.sm = sm;
		rv = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		sm.registerListener(this, rv, 100000, new Handler());
		// initialize the rotation matrix to identity
		mRotationMatrix[ 0] = 1;
		mRotationMatrix[ 4] = 1;
		mRotationMatrix[ 8] = 1;
		mRotationMatrix[12] = 1;
	}

	public GameWorld getWorld(){
		return world;
	}

	@Override
	public void run() {
		long lastTick = 0;
		while (true) {
			while (!running) {
				synchronized(this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						Log.d("wePong - GameLoop", "Thread interrupted while waiting...");
					}
				}
			}
			long now = System.currentTimeMillis();
			if (now - lastTick >= 20) {
				lastTick = now;
				world.update(dx);
				view.draw(world);
			}
			if (world.score > MAX_SCORE) {
				running = false;
			}

			if (gameOver){	
				running = false;
			}
		}
	}

	public synchronized void startRunning() {
		running = true;
		this.notify();
	}

	public synchronized void stopRunning() {
		running = false;
		sm.unregisterListener(this, rv);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent ev) {	
		if(ev.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR)
		{
			SensorManager.getRotationMatrixFromVector(mRotationMatrix,ev.values);
			SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, mRotationMatrix);
			float[] orientationVals = new float[3];
			SensorManager.getOrientation(mRotationMatrix, orientationVals );
			float c =(float) Math.toDegrees(orientationVals[2]);
			dx = (c/90);
		}
	}

	public void applyPowerup(PowerUps powerup) {
		world.applyPowerup(powerup);
	}


	public void updateScore(long score) {
		scores.put(user, score);
	}

	public void showScores() {
		this.context.showScores();
	}
}
