package edu.vub.portalpong.effects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.vub.portalpong.objects.Portal;

public class PortalExcitedEffect extends Effect {

	public Portal portal;
	
	protected Paint ringPaint;
	private float ringRadius;
	private int radiusDiff = -1;
	
	
	public PortalExcitedEffect(Portal portal, float width, float height){
		super(portal.x, portal.y, 60);
		this.portal = portal;
		
		this.ringRadius = portal.radius * 2;
		this.ringPaint = new Paint(portal.paint);
		this.ringPaint.setStyle(Style.STROKE);
		this.ringPaint.setStrokeWidth(5);
		this.ringPaint.setAlpha(Math.min(255, this.ringPaint.getAlpha()));
		this.ringPaint.setAntiAlias(true);
		
	}
//	@Override
//	public void update() {
//		// only decrement if entry is opened
//		if (portal.getEntryStatus()) { ttl--;};
//
//	}
	
	@Override
	public void draw(Canvas c) {
//		Paint p = new Paint(portal.paint);
//		//p.setAlpha(0);
//		p.setAlpha(Math.min(255, 30 + p.getAlpha()));
//		c.drawCircle(portal.x, portal.y, portal.radius + 2, p);
//		outter.draw(c);
//		
		c.drawCircle(x, y, portal.radius, ringPaint);
		if (ringRadius <= portal.radius * portal.ratio * portal.ratio) {
			this.radiusDiff = 1;
		}
		if (ringRadius >= portal.radius) {
			this.radiusDiff = -1;
		}
		ringRadius = ringRadius + radiusDiff;
		c.drawCircle(x, y, ringRadius, ringPaint);
		
		
		
	}
	
//	public void drawCopyAt(Canvas c, int x, int y, Paint p) {
//		c.drawCircle(x, y, DEFAULT_PORTAL_OPENING + 1, p);
//	}
	
	public boolean expired(){
		return (super.expired() || !portal.getEntryStatus());
	}
	
	public String toString(){
		
		return "PortalExcitedEffect on " + portal.getOwner();
	}
	
}
