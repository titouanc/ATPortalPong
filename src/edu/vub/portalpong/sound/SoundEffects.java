package edu.vub.portalpong.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import edu.vub.atportalpong.R;

public class SoundEffects {
	public static final int LONG_VIBRATE = 900;
	public static final int SHORT_VIBRATE = 200;
	public static final int NO_VIBRATE = 0;


	public static int PALLET = 0;
	public static int WALL = 1;
	public static int DEAD = 2;
	public static int TRANSPORT = 3;
	
	int[] sounds = { R.raw.player, R.raw.computer, R.raw.dead, R.raw.transport};
	MediaPlayer[] mPlayers = new MediaPlayer[sounds.length];
	Vibrator v;
	Context c;
	
	public SoundEffects(Context c) {
		this.c = c;
		for (int i = 0; i < sounds.length; i++) {
			mPlayers[i] = MediaPlayer.create(c, R.raw.player);
		}
		v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public void playSound(int i, int vibrate) {
		mPlayers[i].start();
		v.vibrate(vibrate);
	}
	
}
