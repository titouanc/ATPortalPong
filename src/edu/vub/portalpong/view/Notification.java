package edu.vub.portalpong.view;

public class Notification {
	public static long SHORT_DURATION = 2000;
	public static long MEDIUM_DURATION = 3500;
	public static long LONG_DURATION = 5000;

	
	String message;
	long time;
	public long startTime = 0;
	
	
	
	public Notification(String message, long t) {
		this.message = message;
		this.time = t;
	}
	
}
