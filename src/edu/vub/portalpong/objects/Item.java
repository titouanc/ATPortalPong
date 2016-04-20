package edu.vub.portalpong.objects;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import edu.vub.portalpong.ui.PortalPongActivity;
import edu.vub.portalpong.view.Items;
import edu.vub.portalpong.view.Items.PowerUps;

public class Item extends GameObject implements PickableMovableGameObject {
	
	private float targetX, targetY;
	private boolean picked;
	private WeakReference<Items> container;
	private PowerUps power;
	private Bitmap picture;
	int halfwidth = 32;
	int halfheight = 32;

	public Item(float x, float y, PowerUps powerUps, Items container) {
		super(x,y);
		targetX = x;
		targetY = y;
		this.power = powerUps;
		this.paint = new Paint();
		this.paint.setColor(Color.RED);
		this.container = new WeakReference<Items>(container);
		picture = Bitmaps.ball.createScaledBitmap(Bitmaps.getPowerBitmap(powerUps), halfwidth*2, halfheight*2, true);
	}
	
	@Override
	public void update() {
		if (! picked && (x != targetX || y != targetY)) {
			final float distX = x-targetX;
			final float distY = y-targetY;
			float distance2 = distX * distX + distY * distY;
			if (distance2 < 2000) {
				x = targetX;
				y = targetY;
			} else {
				float distance = (float) Math.sqrt(distance2);
				x -= 100 * distX / distance;
				y -= 100 * distY / distance;
				if (   Math.signum(distX) != Math.signum(x-targetX)
				    || Math.signum(distY) != Math.signum(y-targetY)) {
					x = targetX;
					y = targetY;
				}
			}
		}
	}
	
	public void shiftUp() {
		targetY = targetY - 80;
	}

	@Override
	public PickableGameObject pick() {
		picked = true;
		return this;
	}

	@Override
	public void dropOnto(PickableGameObject pgo) {
		picked = false;
		if (pgo instanceof SelfPicker) {
			Log.d("PortalPong", "Dropping onto own field!");
			container.get().removeItem(this);
			PortalPongActivity.gameloopInstance().applyPowerup(power);
		} else if (pgo instanceof Portal) {
			Log.d("PortalPong", "Dropping onto portal!");
			container.get().removeItem(this);
			((Portal) pgo).applyPowerup(power);
		}
	}

	@Override
	public void draw(Canvas c) {
		//c.drawRect(x - halfwidth, y - halfheight, x + halfwidth, y + halfheight, paint);
		c.drawBitmap(picture, x - halfwidth, y - halfheight, null);
	}
}
