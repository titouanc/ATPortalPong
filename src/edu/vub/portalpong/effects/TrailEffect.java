package edu.vub.portalpong.effects;

import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Paint;

import edu.vub.portalpong.objects.GameObject;

public abstract class TrailEffect<Trailed extends GameObject> extends Effect {

	protected final LinkedList<Float> positions;
	final Trailed trailed;

	public TrailEffect(Trailed t) {
		super(t.x, t.y, 10);
		this.trailed = t;
		positions = new LinkedList<Float>();
	}

	@Override
	public boolean expired() {
		return false;
	}

	@Override
	public void update() {
		positions.add(trailed.x);
		positions.add(trailed.y);
		if (positions.size() > 10) {
			positions.removeFirst();
			positions.removeFirst();
		}
	}
	
	public abstract void drawCopyAt(Canvas c, float x, float y, Paint p);

	public void draw(Canvas c) {
		float x, y;
		Iterator<Float> it = positions.iterator();
		Paint p = new Paint(trailed.paint);
		p.setAlpha(0);
		while (it.hasNext()) {
			p.setAlpha(Math.min(255, 30 + p.getAlpha()));
			x = it.next();
			y = it.next();
			drawCopyAt(c, x, y, p);
		}	
	}
	
}