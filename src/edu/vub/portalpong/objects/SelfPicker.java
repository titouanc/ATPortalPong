package edu.vub.portalpong.objects;

import android.graphics.Rect;

public class SelfPicker extends GameObject implements PickableGameObject {
	private Rect storedRect;

	public SelfPicker(Rect rect) {
		storedRect = rect;
	}
	
	@Override
	public Rect getBoundingRect() {
		return storedRect;
	}

	@Override
	public PickableGameObject pick() {
		return this;
	}
	
}
