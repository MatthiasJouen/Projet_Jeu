package com.groupe.space.math;

public class Vector {
	
	public float x, y,z;
	public Vector() {
		//set les variables à zero
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vector(float x, float y, float z) {
		//constructeur
		this.x = x;
		this.y  = y;
		this.z = z;
	}
}
