package edu.vub.portalpong.effects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class BallDropEffect extends Effect {
	

	public BallDropEffect(float x, float y, int ttl) {
		super(x, y, ttl);
		this.paint = new Paint();
		this.paint.setDither(true);
		RadialGradient gradient = new RadialGradient(x, y, 400, 0x80FF0000, 0x00000000, Shader.TileMode.CLAMP);
		this.paint.setShader(gradient);
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(x, y, ttl * 4, paint);
	}
}
