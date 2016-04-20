package edu.vub.portalpong.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class EdgeEffect extends Effect {
	
	public enum EdgeEffectOrientation { Horizontal, Vertical }

	private final EdgeEffectOrientation orientation;
	private final int thickness;

	public EdgeEffect(float x, float y, final EdgeEffectOrientation orientation) {
		this(x, y, 10, orientation);
	}
	
	public EdgeEffect(float x, float y, int ttl, final EdgeEffectOrientation orientation) {
		super(x, y, ttl);
		this.orientation = orientation;
		this.paint = new Paint();
		this.paint.setColor(Color.WHITE);
		this.thickness = 5;
	}

	@Override
	public void draw(Canvas c) {
		paint.setAlpha(25 * ttl);
		if (this.orientation == EdgeEffectOrientation.Horizontal) {
			c.drawRect(this.x - (float) 100, this.y, this.x + (float) 100, this.y + this.thickness, this.paint);
		} else {
			c.drawRect(this.x, this.y - (float) 100, this.x + this.thickness, this.y + (float) 100, this.paint);
		}
	}
}
