package edu.vub.portalpong.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import edu.vub.portalpong.objects.Ball;
import edu.vub.portalpong.objects.Bitmaps;

public class BallTrailEffect extends TrailEffect<Ball> {
	
	Bitmap b;
	float radius;
	
	public BallTrailEffect(final Ball ball) {
		super(ball);
		b = Bitmaps.ball.createScaledBitmap(Bitmaps.ball, (int)ball.radius*3, (int)ball.radius*3, true);
		this.radius = ball.radius;
	}
	
	@Override
	public void drawCopyAt(Canvas c, float x, float y, Paint p) {
		c.drawBitmap(b, x-radius , y-radius, p);
	}
	
	@Override
	public boolean expired() {
		return ! trailed.valid;
	}
}
