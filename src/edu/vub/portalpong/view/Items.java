package edu.vub.portalpong.view;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import edu.vub.atportalpong.Constants;
import edu.vub.portalpong.objects.GameObject;
import edu.vub.portalpong.objects.GameWorld;
import edu.vub.portalpong.objects.Item;
import edu.vub.portalpong.objects.PickableGameObject;

public class Items extends GameObject implements PickableGameObject {
	
	public enum PowerUps {
		EXTRA_BALL,
		LONGER_PADDLE,
		MULTIBALL, 
		SUBSTRACT;
		
		public String toString() {
			switch (this) {			
			case EXTRA_BALL:
				return "Extra Ball";
			case MULTIBALL:
				return "Multiball";
			case SUBSTRACT:
				return "You got robbed";	
			case LONGER_PADDLE:
				return "Long Paddle";
			/*	case SPEEDUP:
				return "Speedup";
			case SWITCH_FIELD:
				return "Switch Fields";
			case MOVE_PORTALS:
				return "Move all portals";
			case MOVE_PORTALS_OTHERS:
				return "Move portals of opponents";
			case STEAL_PORTAL:
				return "Steal a portal";*/
			}
			return "Magic";
		}
		
		public static PowerUps randomPowerup()  {
			PowerUps[] x = values();
			return x [(GameWorld.random.nextInt(x.length-1))];
		}
	}
	
	public static int SIZE_X = 80;
	public static int SIZE_Y = 240;
	
	private ConcurrentLinkedQueue<Item> items;
	private Paint fillPaint;
	private Rect myRect;
	
	public Items(int x, int y) {
		super(x, y);
		this.items = new ConcurrentLinkedQueue<Item>();
		this.paint = new Paint();
		this.paint.setColor(0x40CCCCCC);
		
		this.fillPaint = new Paint();
		this.fillPaint.setColor(Color.RED);

		myRect = new Rect(0, 0, SIZE_X, SIZE_Y);
	}
	
	public void addItem(PowerUps p) {
		if (items.size() > Constants.MAX_ITEMS)
			return;
		items.add(new Item(40, 40 + 80 * items.size(), p, this));
	}
	
	public void removeItem(Item toRemove) {
		items.poll();
		for (Item i : items) {
			i.shiftUp();
		}
	}

	public void draw(Canvas c) {
		c.drawRect(myRect, paint);
		int index=0;
		for (Item i : items) {
			index++;
			if (index <= 3) {
				i.draw(c);
			}
		}
	}
	
	@Override
	public void update() {
		for (Item i : items) {
			i.update();
		}
	}
	
	public Rect getBoundingRect() {
		return myRect;
	}

	public PickableGameObject pick() {
		Item item = this.items.peek();
		if (item == null)
			return null;
		return item.pick();
	}
	
}
