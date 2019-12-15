package com.groupe.space.niveau;

import com.groupe.space.graphique.Texture;
import com.groupe.space.graphique.VertexArray;
import com.groupe.space.math.Matrice;
import com.groupe.space.math.Vector;

// Création de la classe obstacle avec la definition du bonus
// ses fonctions, ses coordonnées, sa taille
//

public class bonus {
	
	private Vector position = new Vector();
	private Matrice ml_matrix;
	
	private static float SIZE = 0.5f;//taille du bonus
	private static Texture texture;
	private static VertexArray mesh;
	
	public static void create() {
		
	float[] vertices = new float[] 
	{	
			-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
			-SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f, -SIZE / 2.0f, 0.2f
	};
	
	byte[] indices = new byte[] 
	{
		0, 1, 2,
		2, 3, 0
	};
	
	float[] tcs = new float[] 
	{
		0, 1,
		0, 0,
		1, 0,
		1, 1
	};
	mesh = new VertexArray(vertices, indices, tcs);//tcs = Textures coordinates
	texture = new Texture("ressources/bonus.png");//mettre mon image de fusee qui est dans /ressources/
	}
				
		public bonus(float x, float y) 
		{
			position.x = x;
			position.y = y;
			ml_matrix = Matrice.translate(position);
		}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public Matrice getModelMatrice() {
		return ml_matrix;
	}
	
	public static VertexArray getMesh() {
		return mesh;
	}
	
	public static Texture getTexture() {
		return texture;
	}
	
	public float getSIZE() {
		return SIZE;
	}
	
}
