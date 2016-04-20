package edu.vub.portalpong.effects;

import edu.vub.portalpong.objects.Paddle;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PaddleTrailEffect extends TrailEffect<Paddle> {

	public PaddleTrailEffect(Paddle t) {
		super(t);
	}

	@Override
	public void drawCopyAt(Canvas c, float x, float y, Paint p) {
		float halfwidth = trailed.width / 2;
		float halfheight = trailed.height / 2;
		c.drawRect((int)(x-halfwidth), (int)(y-halfheight), (int)(x+halfwidth), (int)(y+halfheight), p);
	}

}
