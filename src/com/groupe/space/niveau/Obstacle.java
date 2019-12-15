package com.groupe.space.niveau;

import com.groupe.space.graphique.Texture;
import com.groupe.space.graphique.VertexArray;
import com.groupe.space.math.Matrice;
import com.groupe.space.math.Vector;

// Création de la classe obstacle avec la definition des obstacles
// leurs fonctions, leur coordonnées, leur taille
//

public class Obstacle {
	
	private Vector position = new Vector();
	private Matrice ml_matrix;
	
	private static float width = 1.0f, height = 7.2f;//hauteur et largeur des obstacles
	private static Texture texture;
	private static VertexArray mesh;
	
	public static void create() {
		
	float[] vertices = new float[] 
	{	
		0.0f, 0.0f, 0.1f,
		0.0f, height, 0.1f,
		width, height, 0.1f,
		width, 0.0f, 0.1f
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
	texture = new Texture("ressources/obstacle.png");//mettre mon image de fusee qui est dans /ressources/
	}
				
		public Obstacle(float x, float y) 
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
	
	public static float getWidth() {
		return width;
	}
	
	public static float getHeight() {
		return height;
	}
	
}
