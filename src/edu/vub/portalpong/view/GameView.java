package edu.vub.portalpong.view;

import edu.vub.portalpong.objects.GameWorld;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView implements Callback {
	private SurfaceHolder holder;
	
	public GameView(SurfaceView sv) {
        sv.getHolder().addCallback(this);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		this.holder = holder;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) { }

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.holder = null;
	}

	public void draw(GameWorld world) {
		if (holder == null) return;
		Canvas c = holder.lockCanvas();
		world.draw(c);
		holder.unlockCanvasAndPost(c);
	}
}
