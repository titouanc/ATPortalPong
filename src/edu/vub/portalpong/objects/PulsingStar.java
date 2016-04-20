package edu.vub.portalpong.objects;

import edu.vub.portalpong.view.Items;
import android.graphics.Canvas;
import android.graphics.Color;

public class PulsingStar extends Ball {
	
	public static int UPPER_ALPHA = 170;
	public static int LOWER_ALPHA = 10;
	public static int MAX_STEP = 3;

	public boolean fadeOut;
	public int step;
	
	public PulsingStar(float x, float y) {
		super(x, y);
		super.setRadius(GameWorld.random.nextInt(15));
		this.paint.setAlpha(GameWorld.random.nextInt(255));
		this.fadeOut = GameWorld.random.nextBoolean();
		this.step = GameWorld.random.nextInt(MAX_STEP-1) + 1;
		int r = GameWorld.random.nextInt(255);
		int g = GameWorld.random.nextInt(255);
		int b = GameWorld.random.nextInt(255);
		this.setColor(Color.rgb(r,g,b));
		this.paint.setAntiAlias(true);
	}

	@Override
	public void draw(Canvas c) {
		if (this.fadeOut) {
			this.paint.setAlpha(this.paint.getAlpha() - this.step);
			if (this.paint.getAlpha() < LOWER_ALPHA) {
				this.paint.setAlpha(LOWER_ALPHA);
				this.fadeOut = false;
			}
		} else {
			this.paint.setAlpha(this.paint.getAlpha() + this.step);
			if (this.paint.getAlpha() > UPPER_ALPHA) {
				this.paint.setAlpha(UPPER_ALPHA);
				this.fadeOut = true;
			}
		}
		super.draw(c);
	}
	
}
