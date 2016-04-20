package edu.vub.portalpong.objects;


import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import edu.vub.atportalpong.GameLoop;
import edu.vub.atportalpong.Variables;
import edu.vub.atportalpong.interfaces.JATPortalPong;
import edu.vub.portalpong.effects.BallDropEffect;
import edu.vub.portalpong.effects.BallTrailEffect;
import edu.vub.portalpong.effects.EdgeEffect;
import edu.vub.portalpong.effects.EdgeEffect.EdgeEffectOrientation;
import edu.vub.portalpong.effects.Effect;
import edu.vub.portalpong.effects.PaddleTrailEffect;
import edu.vub.portalpong.effects.PortalExcitedEffect;
import edu.vub.portalpong.sound.SoundEffects;
import edu.vub.portalpong.view.Items;
import edu.vub.portalpong.view.Items.PowerUps;
import edu.vub.portalpong.view.Notification;
import edu.vub.portalpong.view.NotificationBoard;

public class GameWorld {
	private static final int NUM_BALLS = 3;
	public static final int SCORE_LOSE_BALL    = -100;
	public static final int SCORE_HIT_PADDLE   = 50;
	public static final int SCORE_GRAB_POWERUP = 25;	
	public static final int UPDATE_TIME        = 1000;
	
	public int width, height;
	public String user;
	public long score;
	public long prev_score;
	public Paddle paddle;
	public ConcurrentLinkedQueue<Ball> balls;
	private ConcurrentLinkedQueue<PowerUp> powerups;
	public ConcurrentHashMap<String,Portal> portals;
	public ConcurrentHashMap<String,Portal> disconnectedPortals;
	private static Bitmap bitmap;
	private static Bitmap background;
	private Rect bitmapRect;
	public static Random random;
	private Sky sky;
	private Items items;
	private ConcurrentLinkedQueue<Effect> effects;
	private SelfPicker selfPicker;
	private ConcurrentLinkedQueue<GameObject> objects;
	private SoundEffects sounds;
	private NotificationBoard board;
	private long lastPulsingStar = 0;
	private JATPortalPong communicationLayer;

    public GameWorld( int width, int height, final String user, SoundEffects s, JATPortalPong gameLoop){
    	this(width, height, user, s);
    	this.communicationLayer = gameLoop;
    	this.board.addNotification(new Notification("Waiting for other Players", Notification.SHORT_DURATION));
    }
    
	public GameWorld(int width, int height, final String user, SoundEffects s) {		
		this.width = width;
		this.height = height;
		this.user = user;
		this.sounds = s;
		GameWorld.random = new Random();
		
		if(GameWorld.bitmap == null) {
			GameWorld.bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			GameWorld.background = Bitmap.createScaledBitmap(Bitmaps.court, width, height, true);
		}
		
		this.bitmapRect = new Rect(0,0,width,height);
		this.powerups = new ConcurrentLinkedQueue<PowerUp>();
		this.portals = new ConcurrentHashMap<String,Portal>();
		this.disconnectedPortals = new ConcurrentHashMap<String,Portal>();
		this.sky = Sky.generateSky(200, width, height);
		this.items = new Items(0, 0);
		this.effects = new ConcurrentLinkedQueue<Effect>();
		this.balls = new ConcurrentLinkedQueue<Ball>();
		this.user = user;
		this.score = 0;
		this.prev_score = 0;
		this.paddle = new Paddle(width / 2, (int)(height * 0.90));
		this.paddle.setColor(Color.GRAY);
		this.board = new NotificationBoard(0,0);
		this.board.addNotification(new Notification("Try to reach " + GameLoop.MAX_SCORE + " points !", Notification.SHORT_DURATION));


		//TODO extend tennisball with  Variables.user
		
		if(!Variables.multiplayer) {
			Ball ball = new TennisBall(width / 2, height / 2, Variables.user);
			ball.setColor(Color.WHITE);
			addBall(ball);
			this.effects.add(new BallTrailEffect(ball)); 
		}
		
		this.effects.add(new PaddleTrailEffect(this.paddle));
		this.objects = new ConcurrentLinkedQueue<GameObject>();
		this.selfPicker = new SelfPicker(new Rect(0, height / 3 * 2, width, height));
	}

	public void update(float dx) {
		updatePaddle(dx);
		for (Iterator<Ball> iterator = balls.iterator(); iterator.hasNext();) {
			Ball ball = iterator.next();
			updateBall(ball);
			if (!ball.valid) {
				iterator.remove();
				sounds.playSound(SoundEffects.DEAD, SoundEffects.LONG_VIBRATE);
			}
		}

		for (Iterator<Effect> it = effects.iterator(); it.hasNext();) {
			Effect e = it.next();
			e.update();
			if (e.expired())
				it.remove();
		}

		for (Ball b1 : balls) {
			for (Iterator<PowerUp> sIt = powerups.iterator(); sIt.hasNext();) {
				PowerUp up = sIt.next();
				if (Physics.Collides(b1, up)) {
					sIt.remove();
					items.addItem(PowerUps.randomPowerup());
				}
			}
			for (Ball b2 : balls) {
				if (b1 == b2) { break; };
				Physics.Collide(b1, b2);
			}	
		}

		items.update();

		for (Iterator<GameObject> it = objects.iterator(); it.hasNext();) {
			GameObject go = it.next();
			go.update();
		}
		
		if (communicationLayer != null) {
			communicationLayer.updateScore(score);
		}
	}

	// @Safe(Concurrent)
	public void removeBall(Ball b) {
		b.invalidate();
	}

	// @Safe(Concurrent)
	private void spawnPortal(int width, int height, PortalListener l, String ownerId) { // should get the remote party as an argument
		
		if(portals.size() == 0) {
			Ball ball = new TennisBall(width / 2, height / 2, Variables.user);
			ball.setColor(Color.WHITE);
			addBall(ball);
			this.effects.add(new BallTrailEffect(ball)); 
		}
		
		int x_offset = 0;
		int y_offset = 300;
		int x = random.nextInt(width-x_offset) + x_offset;
		int y = random.nextInt(height-y_offset);
		Portal p = new Portal(x,y,l,ownerId);
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		p.setColor(Color.rgb(r,g,b));
		Log.d("wePortal", "adding Portal for " + ownerId);
		this.portals.put(ownerId, p);
	}

	private void updatePaddle(float dx) {
		paddle.x += dx * 75;
		float halfwidth = paddle.width /2;
		if (paddle.x - halfwidth < 0 + 10) {
			paddle.x = 10 + halfwidth;
		} 

		if (paddle.x + halfwidth > width - 10) {
			paddle.x = width - 10 - halfwidth;
		}
	}

	private void updateBall(Ball ball) {
		ball.x += ball.dx * 10;
		ball.y += ball.dy * 10;

		if (ball.x < ball.radius) {
			ball.x = ball.radius;
			ball.dx *= -1;
			spawn(new EdgeEffect(0, ball.y, EdgeEffectOrientation.Vertical));
			sounds.playSound(SoundEffects.WALL, SoundEffects.NO_VIBRATE);
		}

		if (ball.x > width - ball.radius) {
			ball.x = width - ball.radius;
			ball.dx *= -1;
			spawn(new EdgeEffect(width - 5, ball.y, EdgeEffectOrientation.Vertical));
			sounds.playSound(SoundEffects.WALL, SoundEffects.NO_VIBRATE);
		}

		if (ball.y < ball.radius) {
			ball.y = ball.radius;
			ball.dy *= -1;
			spawn(new EdgeEffect(ball.x, 0, EdgeEffectOrientation.Horizontal));
			sounds.playSound(SoundEffects.WALL, SoundEffects.NO_VIBRATE);
		}

		// bottom of the screen
		// DIE
		if (ball.y > height - ball.radius) {
			removeBall(ball);
			ball.y = height - ball.radius;
			ball.dy *= -1;
			score += SCORE_LOSE_BALL;
			if (communicationLayer != null) communicationLayer.sendLostBall();
			spawn(new BallDropEffect(ball.x, height, 20));
			return;
		}

		if (ball.x + ball.radius >= paddle.x - paddle.width / 2
				&& ball.x - ball.radius <= paddle.x + paddle.width / 2
				&& ball.y + ball.radius >= paddle.y - paddle.height / 2
				&& ball.dy > 0) {
			sounds.playSound(SoundEffects.PALLET, SoundEffects.SHORT_VIBRATE);
			ball.dy *= -1;
			//speedup the ball when it hits the paddle
			ball.dx = ball.dx * 1.1;
			ball.dy = ball.dy * 1.1;
			score += SCORE_HIT_PADDLE;
		}

		for (Portal p: portals.values()) {
			int distance =  (int) Math.pow(p.radius, 4);
			if (p.inEuclideanDistance(ball, (distance))) {
				if (p.collidesWith(ball)) {
					p.enter(ball);
					break;
				}else{
					p.nearby(ball);
				}
			}
		}
	}

	public void spawn(Effect e) {
		effects.add(e);
	}

	public void draw(Canvas mainScreen) {
		Canvas c = new Canvas(bitmap);
		c.drawColor(Color.WHITE);
		c.drawBitmap(background,0,0,null);
		
		if(score > GameLoop.MAX_SCORE) {		
			balls.clear();
			board.notifications.clear();
			board.addNotification( new Notification("You won !", Notification.LONG_DURATION) );
			board.draw(c);
			if (communicationLayer != null){
				communicationLayer.sendWinMessage();
				communicationLayer.showScores();
			}
		}

		// Draw scaled canvas
		paddle.draw(c);
		for (Iterator<Ball> iterator = balls.iterator(); iterator.hasNext();) {
			Ball ball = (Ball) iterator.next();
			ball.draw(c);
		}

		//sky.draw(c);

		items.draw(c);
		for (Portal p: portals.values()) {
			p.draw(c);
		}
		for (Iterator<PowerUp> it = powerups.iterator(); it.hasNext();) {
			PowerUp p = it.next();
			p.draw(c);
		}
		for (Iterator<Effect> it = effects.iterator(); it.hasNext();) {
			Effect e = it.next();
			e.draw(c);
		}
		paddle.draw(c);

		for (Iterator<GameObject> it = objects.iterator(); it.hasNext();) {
			GameObject go = it.next();
			go.draw(c);
		}
		board.setScore(score);
		board.draw(c);
		

		informScore();
		updatePowerUps();
		
	
		
		// Draw on the screen
		float scale = Math.min((float)mainScreen.getWidth() / width, (float)mainScreen.getHeight() / height);
		Rect scaleRect = new Rect(0,0,(int)(width*scale),(int)(height*scale));
		mainScreen.drawBitmap(bitmap, bitmapRect, scaleRect, null);

	}

	public void informScore() {
		if (Math.abs( prev_score - score) > 200) {
			if (communicationLayer != null){
				communicationLayer.sendNewScoreMessage(score);
			}
			prev_score = score;
		}
	}
	
	public void updatePowerUps() {
		if ((System.currentTimeMillis() - lastPulsingStar) < 5000) return;
		if (powerups.size() >= 3) return;
		lastPulsingStar = System.currentTimeMillis();
		if( random.nextInt(2) > 0 ) {
			powerups.add(PowerUp.randomPowerUp(this));
		}
		else {
			items.addItem(PowerUps.randomPowerup());
		}
	}

	public void newForeignBall(Ball ball) {
		ball.setColor(Color.GREEN);
		addBall(ball);
	}

	public void addBall(Ball ball) {
		balls.add(ball);
		effects.add(new BallTrailEffect(ball));
	}

	// @Safe(Concurrent)
	public void spawnPortal(PortalListener listener, String ownerId) {	
		//400, 200
		spawnPortal(width, height, listener, ownerId);
	}

	// @Safe(Concurrent)
	public void removePortal(String ownerId) {	
		this.portals.remove(ownerId);
	}

	public PickableGameObject pickGameObject(int pickX, int pickY) {
		// TODO synchronize.
		for (Portal p: portals.values()) {
			if (p.getBoundingRect().contains(pickX, pickY))
				return p;
		}		
		if (items.getBoundingRect().contains(pickX, pickY))
			return items.pick();

		if (selfPicker.getBoundingRect().contains(pickX, pickY))
			return selfPicker.pick();
		return null;
	}

	// since moving portals may be kicked out
	// adapted this method to take the effect (if a portal has an effect) and move it as well.
	// the effect has the portal so, if there is an effect we return the effect
	// otherwise the portal
	public GameObject pickPortal(int pickX, int pickY) {
		// TODO synchronize.
		for (Portal p: portals.values()) {
			if (Math.abs(p.x - pickX) < (p.radius * 2) && Math.abs(p.y - pickY) < (p.radius * 2)) {
				if (p.getEntryStatus()) {
					// check if there is an effect to also move it
					for (Iterator<Effect> it = effects.iterator(); it.hasNext();) {
						Effect effect = it.next();
						if (effect instanceof PortalExcitedEffect) {
							PortalExcitedEffect pe = (PortalExcitedEffect) effect;
							if ((pe.portal).equals(p)) return pe;
						}
					}
				}
				// if we are here is because either p does not have an effect active
				return p;
			}
		}
		return null;
	}

	public Portal getPortal(String ownerId) {
		return portals.get(ownerId);
	}
	
	public boolean hasPortal(String ownerId){
		return portals.contains(ownerId);
	}

	// Safe(concurrent)
	public void addGameObject(GameObject pickedObject) {
		// TODO synchronize
		objects.add(pickedObject);
	}

	// Safe(concurrent)
	public void removeGameObject(GameObject pickedObject) {
		objects.remove(pickedObject);
	}

	public void applyPowerup(PowerUps powerup) {
		Ball ball;
		switch (powerup) {
		case EXTRA_BALL:
			//TODO  Variables.user
			 ball = new TennisBall(width / 2, height / 2, Variables.user);
			addBall(ball);
			board.addNotification(new Notification(powerup.toString(), Notification.SHORT_DURATION));
			break;
		case LONGER_PADDLE:
			

			int paddlegrowth = 50;
			paddle.grow(paddlegrowth);
			board.addNotification(new Notification(powerup.toString(), Notification.SHORT_DURATION));
			Rect r = paddle.getBoundingRect();
			if (r.left <= 0)
				paddle.moveTo(paddle.width / 2 + 1, paddle.y);
			if (r.right >= width)
				paddle.moveTo(width - paddle.width / 2 - 1, paddle.y);

			//shrink paddle after 10 seconds
			final Handler shrinkPaddleHandler = new Handler();
			Runnable shrinkPaddleRunnable = new Runnable() {
				public void run() {
					paddle.shrink(1);
				}
			};
			int wait = 1000;
			for(int i = 0; i < paddlegrowth; i++){
				shrinkPaddleHandler.postDelayed(shrinkPaddleRunnable, wait + i * paddlegrowth);
			}
			break;

			
			
		case MULTIBALL:
			board.addNotification(new Notification(powerup.toString(), Notification.SHORT_DURATION));
			for (int i = 0; i < 10; i++) {
				ball = new TennisBall(width / 2, height / 2, Variables.user);
				addBall(ball);	
			}
			break;
		case SUBSTRACT:
			board.addNotification(new Notification(powerup.toString(), Notification.SHORT_DURATION));
			score -= 250;
			break;
		default:
			break;
		}
	}   

	public void died(String userId) {
		Portal deceasedPortal = portals.get(userId);
		portals.remove(userId);
		disconnectedPortals.put(userId, deceasedPortal);
	}
	
	public void resurrected(String userId){
		Portal resurrecting = disconnectedPortals.get(userId);
		disconnectedPortals.remove(userId);
		portals.put(userId, resurrecting);
	}

	public void showNotification(Notification n) {
		board.addNotification(n);
	}

	public void updateDepencies(int width, int height, String user, SoundEffects s) {
		this.width = width;
		this.height = height;
		this.user = user;
		this.sounds = s;
	}

}
