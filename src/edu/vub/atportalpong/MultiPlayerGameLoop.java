package edu.vub.atportalpong;


import java.util.HashMap;
import java.util.Vector;

import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import edu.vub.portalpong.effects.PortalExcitedEffect;
import edu.vub.portalpong.objects.Ball;
import edu.vub.portalpong.objects.GameWorld;
import edu.vub.portalpong.objects.Portal;
import edu.vub.portalpong.objects.PortalListener;
import edu.vub.portalpong.objects.TennisBall;
import edu.vub.portalpong.sound.SoundEffects;
import edu.vub.portalpong.ui.PortalPongActivity;
import edu.vub.portalpong.view.GameView;
import edu.vub.portalpong.view.Notification;
import edu.vub.portalpong.view.Items.PowerUps;
import edu.vub.atportalpong.interfaces.*;

public class MultiPlayerGameLoop extends GameLoop implements JATPortalPong{
	private ATATPortalPong atpp;
	private String id;
	//Maps user id's to user names
	private HashMap<String,String> mapping;
	//Handler to the loop handling communication with AT
	private Handler mHandler;
	//Message code send to looper thread
	private final int _MSG_LOST_BALL_ = 0;
	private final int _MSG_WIN_ = 1;
	private final int _MSG_NEW_SCORE_ = 2;
	private final int _MSG_BALL_ENTER_ = 3;
	private final int _MSG_PROXIMITY_ = 4;
	private final int _MSG_APPLY_POWERUP_ = 5;
	private final int _MSG_SHUTDOWN_ = 6;


	public MultiPlayerGameLoop(String userName,String userId, PortalPongActivity c) {
		super(userName, c);
		id = userId;
		mapping = new HashMap<String,String>();
		LooperThread lt = new LooperThread();
		lt.start();
		mHandler = lt.mHandler;
	}
	
	public void updateDepencies(GameView view, SensorManager sm, SoundEffects s) {	
	    super.updateSomeDependencies(view, sm);
		if( world == null) {
			this.world = new GameWorld(1280, 720, user, s, this);
		} else {
			world.updateDepencies(1280, 720, user,s);
		}
	}
	
	//Methods called by other Java classes
	
	public void shutdown() {
		//Application is shutting down
	}
	
	public void sendLostBall() {
		atpp.sendLostBall(id);
	}
	
	public void sendWinMessage(){
		atpp.sendWin(id);
	}
	
	public void sendNewScoreMessage(long score){
		atpp.sendNewScore(id, score);
	}
	
	
	public void removePlayer(String ownerId) {
		world.removePortal(ownerId);
	}
	
	public void setATPP(ATATPortalPong atpp){
		atpp.setID(id);
		atpp.setName(Variables.user);
		this.atpp = atpp;
	}
	
	//Methods called by AmbientTalk
	
	public void newPlayer(String id, String userName){
		String txt = userName + " has joined the game";
		this.world.showNotification(new Notification(txt, Notification.SHORT_DURATION));
	}
	
	public void newLostBall(String id){
		String txt = id + " has lost the ball";
		this.world.showNotification(new Notification(txt, Notification.SHORT_DURATION));
	}
	
	public void spawnPortal(String ownerId) {
		PortalListener listener = new PortalListener() {
			@Override
			public void onEnter(String ownerId, Ball b) {
				atpp.sendBallMessage(ownerId, b, id);
				world.removeBall(b);
			}

			@Override
			public void onProximity(String ownerId) {
				atpp.sendPortalEntry(ownerId, id);
			};
			
			@Override
			public void onApplyPowerup(String ownerId, PowerUps powerup) {
				atpp.sendPowerUp(ownerId, powerup);
			}
		};
		world.spawnPortal(listener, ownerId);
	}
	
	public void newBall(Ball b,String fromId){
		Portal portal = world.getPortal(fromId);
		b.moveTo(portal.x, portal.y);
		while(portal.collidesWith(b)){
			b.moveTo(b.x + portal.radius, b.y + portal.radius);
		};
		world.newForeignBall(new TennisBall(b.x,b.y,b.owner));
		portal.setEntryStatus(false);
	}
	
	public void portalEntry(String fromId){
		Portal portal = world.getPortal(fromId);
		portal.setEntryStatus(true);
		world.spawn(new PortalExcitedEffect(portal,world.height,world.width));
	}
	
	public void powerUp(PowerUps powerup){
		this.applyPowerup(powerup);
	}
	
	public void win(String winnerId){
		String txt = String.format("%s: has won the game !", mapping.get(winnerId));
		this.world.showNotification(new Notification(txt, Notification.SHORT_DURATION));
	}
	
	public void died(String deceasedId){
		//A user has disconnected
		//Notify the world that the user has died, and show a notification
	}
	
	public void resurrected(String resurrectedId){
		//A disconnected user has reconnected
		//Notify the world that the user has resurrected, and show a notification
	}
	
	public void newScore(String fromId,long score){
		String txt = String.format("%s: %d !", fromId, score);
		this.world.showNotification(new Notification(txt, Notification.SHORT_DURATION));
	}
	
	public void lostBall(String fromId){
		//Another user has lost a ball, show a notification
	}
	
	// Call the AmbientTalk methods in a separate thread to avoid blocking the UI.
	private class LooperThread extends Thread {

		public Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (null == atpp)
					return;
				switch (msg.what) {
				case _MSG_LOST_BALL_: {
					//Call appropriate AT function
					break;
				}
				case _MSG_WIN_: {
					//Call appropriate AT function
					break;
				}
				case _MSG_NEW_SCORE_: {
					//Call appropriate AT function
					break;
				}
				case _MSG_BALL_ENTER_: {
					//Call appropriate AT function
					break;
				}
				case _MSG_PROXIMITY_: {
					//Call appropriate AT function
					break;
				}
				case _MSG_APPLY_POWERUP_: {
					//Call appropriate AT function
					break;
				}
				case _MSG_SHUTDOWN_:
					//Call appropriate AT function
					break;
				}
			}
		};

		public void run() {
			Looper.prepare();
			Looper.loop();
		}
	}
	
	//Needed to pass enter message arguments to looper thread
	private class EnterMessage{
		
	}
	
	//Need to pass power up arguments to looper thread
	private class PowerupMessage{
		
	}

	
}
