package edu.vub.portalpong.objects;

import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Ball extends GameObject {
	public double dx;
	public double dy;
	public volatile transient boolean valid = true; 
	public float radius;
	public String owner;
	public int m = 1;

	public Ball() {
		//used for kryo
	}

	public Ball(float x, float y, float radius, String owner) {
		super(x, y);
		double angle = 0;
		do {
			angle = 2 * Math.PI * new Random().nextDouble();
			this.dx = Math.cos(angle);
			this.dy = Math.sin(angle);
		} while (! goodAngle(angle));
		this.paint = new Paint();
		this.paint.setColor(Color.WHITE);
		this.paint.setAntiAlias(true);
		this.radius = radius;
		this.owner = owner;
	}

	public Ball(float x, float y) {
		super(x,y);
		this.radius = 40/2;
		this.paint = new Paint();
		this.paint.setColor(Color.WHITE);
		this.paint.setAntiAlias(true);
	}

	public Ball(final Ball b) {
		super(b.x, b.y);
		this.dx = b.dx;
		this.dy = b.dy;
		this.paint = b.paint;
		this.radius = b.radius;
		this.owner = b.owner;
	}

	private boolean goodAngle(double angle) {
		return Math.abs(dx) < 5.90 && Math.abs(dx) > 0.10
				&& Math.abs(dy) < 5.90 && Math.abs(dy) > 0.10;
	}

	public void draw(Canvas c) {
		c.drawCircle(x, y, radius, paint);
	}

	public void setColor(int color) {
		this.paint.setColor(color);
	}

	public void setRadius(int size) {
		this.radius = size;
	}

	public Paint getPaint() {
		return paint;
	}

	public void invalidate() {
		valid = false;
	}

	@Override
	public Rect getBoundingRect() {
		return new Rect((int)(x-radius), (int)(y-radius), (int)(x+radius), (int)(y+radius));
	}

	public boolean inEuclideanDistance(Ball ball, float distance) {
		int dist2 = (int) Math.pow(ball.x - this.x, 2) + (int) Math.pow(ball.y - this.y, 2);
		return dist2 < distance;
	}
}
