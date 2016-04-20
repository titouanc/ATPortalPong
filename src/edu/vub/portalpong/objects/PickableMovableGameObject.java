package edu.vub.portalpong.objects;

public interface PickableMovableGameObject extends PickableGameObject {
	public void moveTo(float newX, float newY);
	public void dropOnto(PickableGameObject pgo);
}
