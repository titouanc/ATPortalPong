package edu.vub.portalpong.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Paddle extends GameObject {

	public float width;
	public float height;
	public Paddle(float x, float y) {
		super(x,y);
		this.width = 200;
		this.height = 20;
		this.paint = new Paint();
		this.paint.setColor(Color.GRAY);
		this.paint.setAntiAlias(true);
		this.paint.setAlpha(25);

	}
	
	public void grow(int width) {
		this.width += width;
	}

	public void shrink(int width) {
		this.width -= width;
	}

	public void draw(Canvas c) {
		float halfwidth = width / 2;
		float halfheight = height / 2;
		c.drawRect(x-halfwidth, y-halfheight, x+halfwidth, y+halfheight, paint);
	}
	
	public void setColor(int color) {
		this.paint.setColor(color);
	}
	
	@Override
	public Rect getBoundingRect() {
		float halfwidth = width / 2;
		float halfheight = height / 2;
		return new Rect((int)(x-halfwidth), (int)(y-halfheight), (int)(x+halfwidth), (int)(y+halfheight));
	}

	public void grow() {
		width += 50;
	}
}
