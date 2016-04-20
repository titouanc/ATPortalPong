package edu.vub.portalpong.objects;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import edu.vub.atportalpong.R;
import edu.vub.portalpong.view.Items.PowerUps;


public class Bitmaps {
	public static Bitmap ball;
	public static Bitmap court;
	
	public static Bitmap boom;
	public static Bitmap death;
	public static Bitmap live;
	public static Bitmap shield;

	

	
	public static void LoadBitmaps(Activity a ) {
		if( Bitmaps.ball == null) {
			Bitmaps.ball = BitmapFactory.decodeResource(a.getResources(), R.drawable.ball);
			Bitmaps.court = BitmapFactory.decodeResource(a.getResources(), R.drawable.tennis_background);
			Bitmaps.boom = BitmapFactory.decodeResource(a.getResources(), R.drawable.boom);
			Bitmaps.death = BitmapFactory.decodeResource(a.getResources(), R.drawable.death);
			Bitmaps.live = BitmapFactory.decodeResource(a.getResources(), R.drawable.live);
			Bitmaps.shield = BitmapFactory.decodeResource(a.getResources(), R.drawable.shield);		
		}
	}
	
	
	public static Bitmap getPowerBitmap(PowerUps p) {
		switch (p) {
		case EXTRA_BALL:
			return live;
		case LONGER_PADDLE:
			return shield;
		case MULTIBALL:
			return boom;
		case SUBSTRACT:
			return death;
		default: 
			return boom;
		}
	}
		
}
