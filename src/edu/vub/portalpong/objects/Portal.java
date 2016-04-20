package edu.vub.portalpong.objects;

import edu.vub.portalpong.view.Items;
import edu.vub.portalpong.view.Items.PowerUps;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Portal extends DoubleBall implements PickableMovableGameObject {

	protected Paint ringPaint;
	private float ringRadius;
	private float radiusDiff = -1;
	private PortalListener l;
	
	public static final int DEFAULT_PORTAL_RADIUS = 40;
	// Keep track who is the owner of the portal
	private String owner;
	// a portal is opened when the NotifyPortalEntry is received and closes when
	// the BallMessage is received.
	public transient boolean entry_opened = false; 
	// a portal exit is opened when the ball is nearby, and closes when the ball enters.
	public transient boolean exit_opened = false; 


	public Portal(float x, float y, PortalListener l, String owner) {
		this(x, y, 0.6f, owner);
		this.l = l;
		this.owner = owner;
	}
	
	public Portal(float x, float y, float ratio, String owner) {
		super(x, y, ratio, owner);
		super.setRadius(DEFAULT_PORTAL_RADIUS);
		checkPosition();
		this.ringRadius = this.radius;
		this.ringPaint = new Paint();
		this.ringPaint.setStyle(Style.STROKE);
		this.ringPaint.setStrokeWidth(5);
		this.setColor(Color.GREEN);
		this.ringPaint.setAntiAlias(true);
	}

	public boolean collidesWith(Ball ball) {
		return super.inEuclideanDistance(ball, this.radius * this.radius);
	}

	@Override
	public void draw(Canvas c) {
		super.draw(c);
		c.drawCircle(x, y, this.radius, ringPaint);
		if (ringRadius <= this.radius * super.ratio * super.ratio) {
			this.radiusDiff = 1;
		}
		if (ringRadius >= this.radius) {
			this.radiusDiff = -1;
		}
		ringRadius = ringRadius + radiusDiff;
		c.drawCircle(x, y, ringRadius, ringPaint);
	}

	public void enter(Ball ball) {
		l.onEnter(owner,ball);
		exit_opened = false;
	}
	
	public void applyPowerup(PowerUps p) {
		l.onApplyPowerup(owner, p);
	}
	
	public void nearby(Ball ball){
		if (!exit_opened){
			exit_opened = true;
			l.onProximity(owner);
		}
	}
	
	public void setColor(int color) {
		super.setColor(color);
		this.ringPaint.setColor(color);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Override
	public PickableGameObject pick() {
		return this;
	}
	
	public boolean getEntryStatus(){
		return entry_opened;
	}
	public void setEntryStatus(boolean opened){
		this.entry_opened = opened;
	}
	
	private void checkPosition() {
		if ((this.x < Items.SIZE_X) && (this.y < Items.SIZE_Y)) {
			this.x = Items.SIZE_X + DEFAULT_PORTAL_RADIUS;
		}
	}

	@Override
	public void dropOnto(PickableGameObject pgo) {
		checkPosition();
	}

}
