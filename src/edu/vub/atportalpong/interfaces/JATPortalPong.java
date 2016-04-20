package edu.vub.atportalpong.interfaces;

import edu.vub.portalpong.objects.Ball;
import edu.vub.portalpong.view.Items.PowerUps;

public interface JATPortalPong {
	
	public void updateScore(long score);
	
	public void showScores(); 
	
	//Methods called by Java
		
	public void sendLostBall(); //You have lost a ball
	
	public void sendWinMessage(); //You have won the game
	
	public void sendNewScoreMessage(long score); //Your score has changed
	
	
	//Methods Called by AmbientTalk
	
	public void newPlayer(String id,String userName); //AT has discovered a new player
	
	public void spawnPortal(String ownerid); //Create a new portal for a given player id
		
	public void newBall(Ball b,String fromId); //A ball has been send to you
	
	public void portalEntry(String fromId); //A ball is about to pop out of your portal
	
	public void powerUp(PowerUps powerup); //Someone has send you a powerup
	
	public void win(String winnerId); //A player has won the game
	
	public void newScore(String fromId,long score); //Another player has send you his latest score
	
	public void lostBall(String fromId); //Another player has lost a ball
	
	public void died(String deceasedId); //Another player has disconnected
	
	public void resurrected(String resurrectedId); //Another player has reconnected
	

}
