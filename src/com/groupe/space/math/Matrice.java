package com.groupe.space.math;

import java.nio.FloatBuffer;

import com.groupe.space.utils.BufferUtils;

//import org.lwjgl.BufferUtils;

public class Matrice {

	public static final int TAILLE = 4 * 4;
	public float[] elements = new float[TAILLE];
	public Matrice() {
		
	}
	
	public static Matrice identity() {
		Matrice result = new Matrice();
		for (int i =0; i < TAILLE; i++) {
			result.elements[i] = 0.0f;
		}
		result.elements[0 + 0 *4] = 1.0f;
		result.elements[1 + 1 *4] = 1.0f;
		result.elements[2 + 2 *4] = 1.0f;
		result.elements[3 + 3 *4] = 1.0f;
		return result; 
	}
	public static Matrice orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrice result = identity();
		//ligne + colonnes * 4 
		result.elements[0 + 0 * 4] = 2.0f / (right - left);
		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		result.elements[2 + 2 * 4] = 2.0f / (near - far);
		
		result.elements[0 + 3 * 4] = (left + right) / (left - right);
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		result.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		return result;
	}
	
	public static Matrice translate(Vector vector) {
		Matrice result = identity();
		result.elements[0 + 3 * 4] = vector.x;
		result.elements[1 + 3 * 4] = vector.y;
		result.elements[2 + 3 * 4] = vector.z;
		return result;
	}
	
	public static Matrice rotate(float angle) {
		//**!Gère la rotation de l'image!***//
		Matrice result = identity();
		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		
		result.elements[0 + 0 * 4] =cos;
		result.elements[1 + 0 * 4] =sin;
		
		result.elements[0 + 1 * 4] =-sin;
		result.elements[1 + 1 * 4] =cos;
		
		return result;
	}
	

	public Matrice multiply(Matrice matrice) {
		Matrice result = new Matrice();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				float somme = 0.0f;
				for (int b = 0; b < 4; b++) {
					somme += this.elements[x + b * 4] * matrice.elements[b + y * 4];
				}
				result.elements[x + y * 4] = somme;
			}
		}
		return result;
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}
}
