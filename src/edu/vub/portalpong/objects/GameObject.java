package edu.vub.portalpong.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameObject {
	public float x;
	public float y;
	public Paint paint;
	
	public GameObject(){
		// only for kryo
	}
	
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveTo(float newX, float newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void update() {
		// do nothing
	}

	public void draw(Canvas c) {
		// do nothing
	}
	
	public Rect getBoundingRect() {
		return new Rect();
	}
}
