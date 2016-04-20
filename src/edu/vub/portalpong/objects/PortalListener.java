package edu.vub.portalpong.objects;

import edu.vub.portalpong.view.Items;

public interface PortalListener {
	public void onEnter(String owner, Ball l);
	public void onApplyPowerup(String owner, Items.PowerUps powerup);
	public void onProximity(String owner);
}
