package edu.vub.portalpong.objects;

import java.util.Random;

import edu.vub.portalpong.view.Items;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class PowerUp extends GameObject {
	
	private double dx;
	private double dy;
	private int size;
	private double angle;
	private int angle_delay;

	public PowerUp(int x, int y) {
		super(x,y);
		checkPosition();
		this.angle = 2 * Math.PI * new Random().nextDouble();
		this.angle_delay = 0;
		this.paint = new Paint();
		this.paint.setColor(Color.YELLOW);
		this.paint.setAntiAlias(true);
		this.size = 18;
	}

	public void draw(Canvas c) {
		drawTriangle(c, size/2);
		drawTriangle(c, size);
		drawTriangle(c, size+2);
		drawTriangle(c, size+3);
		drawTriangle(c, size+4);
		drawTriangle(c, size+5);
		drawTriangle(c, size+9);
		this.angle += 6 % 360;
	}
	
	public void drawTriangle(Canvas c, int size) {
		double angle = Math.toRadians(this.angle);
		int lx1 = (int) (size*Math.cos(angle+0) + x);
		int ly1 = (int) (size*Math.sin(angle+0) + y);
		int lx2 = (int) (size*Math.cos(angle+(1./3)*(2*Math.PI)) + x);
		int ly2 = (int) (size*Math.sin(angle+(1./3)*(2*Math.PI)) + y);
		int lx3 = (int) (size*Math.cos(angle+(2./3)*(2*Math.PI)) + x);
		int ly3 = (int) (size*Math.sin(angle+(2./3)*(2*Math.PI)) + y);
		c.drawLine(lx1, ly1, lx2, ly2, paint);
		c.drawLine(lx2, ly2, lx3, ly3, paint);
		c.drawLine(lx3, ly3, lx1, ly1, paint);
	}
	
	public void setColor(int color) {
		this.paint.setColor(color);
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public Paint getPaint() {
		return paint;
	}

	@Override
	public Rect getBoundingRect() {
		int size_ = size + 9;
		return new Rect((int)(x - size_), (int)(y - size_), (int)(x + size_), (int)(y + size_));
	}
	
	private void checkPosition() {
		if ((this.x < Items.SIZE_X) && (this.y < Items.SIZE_Y)) {
			this.x = Items.SIZE_X + 10;
		}
	}

	public static PowerUp randomPowerUp(GameWorld gameworld) {
		return new PowerUp(GameWorld.random.nextInt(gameworld.width), GameWorld.random.nextInt(gameworld.height));
	}
}
