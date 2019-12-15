package com.groupe.space.niveau;

import java.util.Random;

import com.groupe.space.graphique.Shader;
import com.groupe.space.graphique.Texture;
import com.groupe.space.graphique.VertexArray;
 import com.groupe.space.input.Input;
import com.groupe.space.math.Matrice;
import com.groupe.space.math.Vector;
import static org.lwjgl.glfw.GLFW.*;

public class Niveau {
	
	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0;//variable qui va permettre de répeter le fond à l'infini
	//private double dxScroll = 0;
	private int map = 0;//variable du fond au début
	
	private fusee fusee;//integration de fusee
	private Obstacle[] obstacle = new Obstacle[5 * 2];//creation de l'obstacle
	private bonus[] Bonus = new bonus[5 * 2];
	private int index = 0;
	private float OFFSET = 5.0f;
	private boolean control = true, reset = false;
	
	private Random random = new Random();
	private float time = 0.0f;
	
	public Niveau() {
		float[] vertices = new float[] {
			-10.0f,	-10.0f * 9.0f / 16.0f, 0.0f,//on gere l'image de fond
			-10.0f,	10.0f * 9.0f / 16.0f, 0.0f,
			 0.0f,	10.0f * 9.0f / 16.0f, 0.0f,
			 0.0f,	-10.0f * 9.0f / 16.0f, 0.0f
		};
		
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		fade = new VertexArray(6);
		background = new VertexArray(vertices, indices, tcs);//tcs = Textures coordinates
		bgTexture = new Texture("ressources/font.jpg");//mettre en Background mon image qui est dans /ressources/
		fusee = new fusee();
		creationObstacle();
		//creationBonus();
	}
	
	
	private void creationObstacle() {
		Obstacle.create();//creation de l'obstacle
		com.groupe.space.niveau.bonus.create();//creation du bonus
		for(int i = 0; i < 5 * 2; i += 2) {
			obstacle[i] = new Obstacle(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);//obstacle du haut
			Bonus[i] = new bonus(OFFSET + index * 10.0f, random.nextFloat() * 4.0f);//bonus du haut

			obstacle[i + 1] = new Obstacle(obstacle[i].getX(), obstacle[i].getY() - 11.5f);//obstacle du bas
			Bonus[i + 1] = new bonus(obstacle[i].getX(), obstacle[i].getY() - 11.5f);//bonus du bas
			
			index += 2;
		}
	}
	
	/*private void creationBonus() {
		com.groupe.space.niveau.bonus.create();
		for(int i = 0; i < 5 * 2; i += 2) {
			Bonus[i] = new bonus(OFFSET + index * 10.0f, random.nextFloat() * 4.0f);//bonus du haut
			Bonus[i + 1] = new bonus(obstacle[i].getX(), obstacle[i].getY() - 11.5f);//bonus du bas
			index += 2;
		}
	}*/
	
	private void updateObstacle() {
		obstacle[index % 10] = new Obstacle(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);//repetition de l'obstacle du haut
		Bonus[index % 10] = new bonus(OFFSET + index * 4.0f, random.nextFloat() * 4.0f);//repetition du bonus du haut
		
		obstacle[(index  + 1) % 10] = new Obstacle(obstacle[index % 10].getX(), obstacle[index % 10].getY() - 11.5f); // repetition de l'obstcale du bas
		Bonus[(index  + 1) % 10] = new bonus(obstacle[index % 10].getX(), obstacle[index % 10].getY() - 11.5f); // repetition du bonus du bas
		index += 2;
	}
	
	/*private void updateBonus() {
		Bonus[index % 10] = new bonus(OFFSET + index * 10.0f, random.nextFloat() * 4.0f);//repetition du bonus du haut
		Bonus[(index  + 1) % 10] = new bonus(obstacle[index % 10].getX(), obstacle[index % 10].getY() - 11.5f); // repetition du bonus du bas
		index += 2;
	}*/
	
	public void update() {
		if (control)
		{	
				xScroll--; //mise à jour du fond
				if (-xScroll % 335 == 0) map++;
				if (-xScroll > 250 && -xScroll % 120 == 0)
				updateObstacle() ;	
		}
		
		fusee.update();//appel de update pour la position de la fusee
		
		if (control && collision())//on verifie si il y a collision
		{
			fusee.fall();//si oui alors il tombe
			control = false;
		}
		
		if (!control && Input.isKeyDown(GLFW_KEY_SPACE))//si on appuie sur espace
		{
			reset = true;//le niveau reccommence
		}
		time += 0.01f;
			//System.out.println("Collision !");
	}
	
	public void renderObstacle() 
	{
		Shader.OBSTACLE.enable();
		Shader.BONUS.enable();

		Shader.OBSTACLE.setUniform2f("fusee", 0, fusee.getY());
		Shader.BONUS.setUniform2f("fusee", 0, fusee.getY());

		
		Shader.OBSTACLE.setUniform4f("vw_matrix", Matrice.translate(new Vector(xScroll * 0.05f, 0.0f, 0.0f)));//xScroll * 0.05f = vitesse de defilement
		Shader.BONUS.setUniform4f("vw_matrix", Matrice.translate(new Vector(xScroll * 0.05f, 0.0f, 0.0f)));//xScroll * 0.05f = vitesse de defilement 

		Obstacle.getTexture().bind();
		//bonus.getTexture().bind();

		
		Obstacle.getMesh().bind();
		//bonus.getMesh().bind();

		for(int i = 0; i < 5 * 2; i++) {
			Shader.OBSTACLE.setUniform4f("ml_matrix", obstacle[i].getModelMatrice());
			//Shader.BONUS.setUniform4f("ml_matrix", obstacle[i].getModelMatrice());

			Shader.OBSTACLE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			//Shader.BONUS.setUniform1i("top", i % 2 == 0 ? 1 : 0);

			Obstacle.getMesh().draw();
			//bonus.getMesh().draw();

		}
		Obstacle.getMesh().unbind();
		//bonus.getMesh().unbind();

		Obstacle.getTexture().unbind();
		//bonus.getTexture().unbind();

	}
	
	/*public void renderBonus() 
	{
		Shader.BONUS.enable();
		Shader.BONUS.setUniform2f("fusee", 0, fusee.getY());
		Shader.BONUS.setUniform4f("vw_matrix", Matrice.translate(new Vector(xScroll * 0.05f, 0.0f, 0.0f)));//xScroll * 0.05f = vitesse de defilement 
		bonus.getTexture().bind();
		bonus.getMesh().bind();
		
		for(int i = 0; i < 5 * 2; i++) {
			Shader.BONUS.setUniform4f("ml_matrix", obstacle[i].getModelMatrice());
			Shader.BONUS.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			bonus.getMesh().draw();
		}
		bonus.getMesh().unbind();
		bonus.getTexture().unbind();
	}*/
	
	private boolean collision() 
	{
		for (int i = 0; i < 5 * 2; i++) 
		{
		float bx = -xScroll * 0.05f;
		float by = fusee.getY();
		float px = obstacle[i].getX();
		float py = obstacle[i].getY();
		
		float bx0 = bx - fusee.getSize() / 2.0f;
		float bx1 = bx + fusee.getSize() / 2.0f;
		float by0 = by - fusee.getSize() / 2.0f;
		float by1 = by + fusee.getSize() / 2.0f;
		
		float px0 = px;
		float px1 = px + Obstacle.getWidth();
		float py0 = py;
		float py1 = py + Obstacle.getHeight();
		
			if (bx1 > px0 && bx0 < px1) 
			{
				if (by1 > py0 && by0 < py1) 
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isGameOver() {
		return reset;
	}
	
	public void render() {
		bgTexture.bind(); //bind the background texture
		Shader.BACK.enable();//enable the shader
		Shader.BACK.setUniform2f("fusee", 0, fusee.getY());
		background.bind();
		for(int i = map; i < map + 4; i++) {
			Shader.BACK.setUniform4f("vw_matrix", Matrice.translate(new Vector(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			background.draw();
		}
		Shader.BACK.disable();
		bgTexture.unbind();
		
		renderObstacle();
		//renderBonus();

		fusee.render();
		
		Shader.FADE.enable();
		Shader.FADE.setUniform1f("time", time);
		fade.render();
		Shader.FADE.disable();
	}

}
