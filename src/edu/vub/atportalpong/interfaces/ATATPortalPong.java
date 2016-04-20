package edu.vub.atportalpong.interfaces;

import edu.vub.portalpong.objects.Ball;
import edu.vub.portalpong.view.Items.PowerUps;

public interface ATATPortalPong {
	
	public void setID(String id); //Java GUI is specifying your id
	
	public void setName(String userName); //Java GUI is specifying your user name
	
	public void stop(); //The application is stopping
	
	public void sendLostBall(String fromId); //You have lost a ball
	
	public void sendBallMessage(String portalOwnerId,Ball b,String fromId); //You are sending a ball to another player (via his/her portal)
	
	public void sendPortalEntry(String portalOwnerId,String fromId); //You are about to send a ball to another player (via his/her portal)
	
	public void sendPowerUp(String portalOwnerId, PowerUps powerup); //You have dragged a powerup to another player's portal
	
	public void sendWin(String fromId); // You have won the game
	
	public void sendNewScore(String fromId,long score); //Your score has changed
	
}
