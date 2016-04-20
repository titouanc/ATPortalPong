package edu.vub.portalpong.objects;

import android.graphics.Color;

class PVector {
	double x;
	double y;

	PVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	static PVector normal(Ball b1, Ball b2) {
		return new PVector(b2.x-b1.x, b2.y - b1.y);
	}

	double length() {
		return Math.sqrt( Math.pow(x,2) + Math.pow(y,2) );
	}

	PVector divide(double m) {
		return new PVector(x/m , y/m);
	}

	PVector mul(double m) {
		return new PVector(x*m, y*m);
	}

	PVector tangent() {
		return new PVector(-y,x);
	}

	public double dotProduct(PVector v1) {
		return x*v1.x + y*v1.y ;
	}

	public PVector add(PVector a) {
		return new PVector(x+a.x,y+a.y);
	}

}

public class Physics {

	static boolean Collides(Ball o1, PowerUp o2) {
		return o2.getBoundingRect().contains((int)o1.x, (int)o1.y);
	}
	
	static boolean Collides(Ball b1, Ball b2) {
		if ((b1.x != b2.x) && (b2.y != b1.y)) {
			double distance = Math.sqrt( Math.pow(b1.x - b2.x , 2) + Math.pow(b1.y - b2.y,2) );
			return distance < b2.radius + b1.radius;
		}
		return false;
	}
	
	static void Collide(Ball b1, Ball b2) {
		if (Collides(b1, b2)) {
			PVector normal = PVector.normal(b1, b2);
			PVector uNormal = normal.divide(normal.length());
			PVector utNormal = uNormal.tangent();
			PVector v1 = new PVector(b1.dx, b1.dy);
			PVector v2 = new PVector(b2.dx, b2.dy);
	
			double v1n = uNormal.dotProduct(v1);		
			double v1t = utNormal.dotProduct(v1);
			double v2n = uNormal.dotProduct(v2);
			double v2t = utNormal.dotProduct(v2);
	
			double v1tFinal = v1t;
			double v2tFinal = v2t;
	
			double v1nFinal = ((v1n * (b1.m-b2.m)) + (v2n*2*b2.m))  /  (b1.m + b2.m);
			double v2nFinal = ((v2n * (b2.m-b1.m)) + (v1n*2*b1.m))  /  (b1.m + b2.m);
	
			PVector vector1n = uNormal.mul(v1nFinal);
			PVector vector2n = uNormal.mul(v2nFinal);
	
			PVector vector1t = utNormal.mul(v1tFinal);
			PVector vector2t = utNormal.mul(v2tFinal);
	
			PVector vectorFinal1 = vector1n.add( vector1t );
			PVector vectorFinal2 = vector2n.add( vector2t );
	
			b1.dx = vectorFinal2.x;
			b1.dy = vectorFinal2.y;
	
			b2.dx = vectorFinal1.x;
			b2.dy = vectorFinal1.y;
			
		}
	}
}
