package edu.vub.portalpong.objects;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Canvas;

public class Sky {
	
	public ConcurrentLinkedQueue<Ball> stars;
	
	public static Sky generateSky(int numberOfStars, int width, int height) {
		Sky sky = new Sky();
		for (int i = 0 ; i<numberOfStars ; i++) {
			int x = GameWorld.random.nextInt(width);
			int y = GameWorld.random.nextInt(height);
			sky.stars.add(new PulsingStar(x,y));
		}
		return sky;
	}
	
	public Sky() {
		this.stars = new ConcurrentLinkedQueue<Ball>();
	}
	
	public void draw(Canvas c) {
		for (GameObject o : stars) {
			o.draw(c);
		}
	}

}
