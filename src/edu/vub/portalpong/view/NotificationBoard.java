package edu.vub.portalpong.view;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import edu.vub.portalpong.objects.GameObject;
import edu.vub.portalpong.ui.SplashActivity;

public class NotificationBoard extends GameObject  {	
	private long score;
	private Paint fillPaint;
	private Rect myRect;
	private Paint textPaint;
	public Vector<Notification> notifications;
	
	public NotificationBoard(int x, int y) {
		super(x, y);
		this.paint = new Paint();
		this.paint.setColor(0x40CCCCCC);
		this.fillPaint = new Paint();
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(36);
		textPaint.setTypeface(SplashActivity.tf);
		this.fillPaint.setColor(Color.RED);
		myRect = new Rect(100, 0, 400, 80);
		notifications = new Vector<Notification>();
	}
	
	public void addNotification(Notification n) {
		this.notifications.add(n);
	}
	

	public void draw(Canvas c) {
		
		// DO not show score when playing alone
		//if ( Variables.multiplayer ) {
			c.drawRect(myRect, paint);
			c.drawText("Score: "+ score, 120, 40, textPaint);
		//}
		if( notifications.size()  > 0 ) {
			Notification n = notifications.firstElement();
			if(n.startTime == 0) {
				n.startTime = System.currentTimeMillis();
			}
			if( n.startTime+n.time >= System.currentTimeMillis() ) {
				Paint notifcationPaint = new Paint();
				notifcationPaint.setTextSize(1);
				notifcationPaint.setColor(0x40CCCCCC);
				notifcationPaint.setTypeface(SplashActivity.tf);
				
				while(notifcationPaint.measureText(n.message) < 800) {
					notifcationPaint.setTextSize(notifcationPaint.getTextSize()+1);
				}
				
				c.drawText(n.message, 200, 200, notifcationPaint);
				
			} else {
				notifications.remove(0);
			}
		}
	}
	
	public Rect getBoundingRect() {
		return myRect;
	}
	
	public void setScore(long score) {
		this.score =  score;
	}
	
}
