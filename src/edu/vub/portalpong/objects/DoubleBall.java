package edu.vub.portalpong.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class DoubleBall extends Ball {

	public final static int ALPHA = 170;	
	private Ball inner_; //final required??
	//private Ball inner_; //final required??
	public float ratio;
	Float mRotation = 5f;
	Bitmap b;

	public DoubleBall() {
		// for kryo
	}
	
	public DoubleBall(float f, float g, float radius, String owner) {
		super(f, g, radius, owner);
		b = Bitmaps.ball.createScaledBitmap(Bitmaps.ball, (int)(radius*3), (int)(radius*3), true);
		inner_ = new Ball(x, y);
		inner_.setRadius((int) (this.radius * ratio));
		this.paint.setColor(Color.WHITE);
		this.ratio = ratio;
	}

	@Override
	public void draw(Canvas c) {
		inner_.dx = this.dx;
		inner_.dy = this.dy;
		inner_.x = this.x;
		inner_.y = this.y;
		super.draw(c);
		inner_.draw(c);
		c.drawCircle(x, y, radius, paint);
	}

	@Override
	public void setColor(int color) {
		super.setColor(color);
		inner_.setColor(color);
		this.paint.setAlpha(ALPHA);
	}
	
	@Override
	public void setRadius(int size) {
		super.setRadius(size);
		inner_.setRadius((int) (this.radius * ratio));
	}

}
