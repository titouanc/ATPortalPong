package edu.vub.portalpong.effects;

import edu.vub.portalpong.objects.GameObject;
import android.util.Log;

public class Effect extends GameObject {

	protected int ttl;
	
	public Effect(float x, float y, int ttl) {
		super(x, y);
		this.ttl = ttl;
	}

	public void update() {
		--ttl;
	}

	public boolean expired() {
		return ttl == 0;
	}

}