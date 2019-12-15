package com.groupe.space.niveau;

import static org.lwjgl.glfw.GLFW.*;

import com.groupe.space.graphique.Shader;
import com.groupe.space.graphique.Texture;
import com.groupe.space.graphique.VertexArray;
import com.groupe.space.input.Input;
import com.groupe.space.math.Matrice;
import com.groupe.space.math.Vector;

public class fusee {

	private float SIZE = 1.2f;
	private VertexArray mesh;
	private Texture texture;
	
	private Vector position = new Vector();
	private float rotation;
	private float delta = 0.0f;
	
	public fusee() {
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
		texture = new Texture("ressources/fusee.png");//mettre mon image de fusee qui est dans /ressources/
	}
	
	public void update(/*double deltatime*/) {
		//***! TEST POUR LE FAIRE BOUGER DANS TOUT LES SENS!***///
		//if(Input.keys[GLFW.GLFW_KEY_UP]) 
			//position.y += 0.1f;
		//if(Input.keys[GLFW.GLFW_KEY_DOWN]) 
			//position.y -= 0.1f;
		//if(Input.keys[GLFW.GLFW_KEY_LEFT]) 
			//position.x -= 0.1f;
		//if(Input.keys[GLFW.GLFW_KEY_RIGHT]) 
			//position.x += 0.1f;
		
		position.y -= delta;
		if(Input.isKeyDown(GLFW_KEY_SPACE))//si on appui sur espace il monte
			delta = -0.15f;
		else  // sinon il retombe
			delta += 0.01f;
		
		rotation = -delta * 70.0f;//permet la rotation si il monte ou descend
	}
	
	public void fall() {
		//Fonctions de la gravité 
		delta = -0.15f;
	}
	
	public void render() {
		Shader.FUSEE.enable();
		//on applique la fonction de rotation faite dans Matrice
		Shader.FUSEE.setUniform4f("ml_matrix", Matrice.translate(position).multiply(Matrice.rotate(rotation)));
		texture.bind();
		mesh.render();
		Shader.FUSEE.disable();
	}

	public float getY() {
		return position.y;
	}

	public float getSize() {
		return SIZE;
	}
}
