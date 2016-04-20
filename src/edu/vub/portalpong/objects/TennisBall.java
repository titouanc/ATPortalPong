package edu.vub.portalpong.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class TennisBall extends Ball {
	private float mRotation = 0f;
	Bitmap b;
	
	public TennisBall(float x, float y, String owner) {
		super(x,y,20, owner);
		b = Bitmaps.ball.createScaledBitmap(Bitmaps.ball, (int)radius*3, (int)radius*3, true);
	}

	@Override
	public void draw(Canvas c) {
		Matrix matrix = new Matrix();
		mRotation  += 5f % 360;
		matrix.setRotate(mRotation, b.getWidth()/2,b.getHeight()/2);
		matrix.setTranslate(x-radius, y-radius);
		c.drawBitmap(b, matrix, null);
	}
}
