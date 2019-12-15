package com.groupe.space;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.groupe.space.graphique.Shader;
import com.groupe.space.input.Input;
import com.groupe.space.math.Matrice;
import com.groupe.space.niveau.Niveau;

public class Main implements Runnable{
	
	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	
	private long window;
	private Niveau level;
	
	public void start() 
	{
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() 
	{
		//***! Creation de la fenetre de base et initialisation des images !***//
		if (!glfwInit()) 
		{
			System.err.println("Ne peut pas initialiser GLFW");
			return;
		}
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "SpaceRACLURE", NULL, NULL);
		
		if (window == NULL) 
		{
			System.err.println("Ne peut pas créer la fenêtre GLFW");
			return;
		}
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		glfwSetKeyCallback(window, new Input());
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();		
		
		//glClearColor(1.0f, 1.0f, 1.0f, 1.0f); //modifier la couleur de la window
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL : "+ glGetString(GL_VERSION));
		
		Shader.loadAll();
		
		//init du fond
		Matrice pr_matrix = Matrice.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BACK.setUniform4f("pr_matrix", pr_matrix);
		Shader.BACK.setUniform1i("tex", 1);
		
		//init de la fusée
		Shader.FUSEE.setUniform4f("pr_matrix", pr_matrix);
		Shader.FUSEE.setUniform1i("tex", 1);
		
		//init des obstacles
		Shader.OBSTACLE.setUniform4f("pr_matrix", pr_matrix);
		Shader.OBSTACLE.setUniform1i("tex", 1);
		
		//init des bonus
		Shader.BONUS.setUniform4f("pr_matrix", pr_matrix);
		Shader.BONUS.setUniform1i("tex", 1);
		level = new Niveau();
		
	}
	
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;//ns = nano secondes
		//double deltatime;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		while(running) 
		{
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) 
			{
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) 
			{
				timer += 1000;
				System.out.println(updates + "ups, "+ frames + " fps");//affichage dans la console des ups et des fps
				updates =0;
				frames = 0;
			}
			
			if (glfwWindowShouldClose(window) == true)//si ça marche pas, essayer : if (glfwWindowShouldClose(window))
				running = false;//fermer le jeu apres appui sur la croix	
		}	
		glfwDestroyWindow(window);
		glfwTerminate();
		
	}
	
	private void update() {
		glfwPollEvents();
		level.update();
		//***Phase de test pour chaque appui sur espace***//
		//if(Input.keys[GLFW_KEY_SPACE]) {
			//System.out.println("FLAP!"); //on affichait flap dans la console 
		//}
		if (level.isGameOver()) {
			level = new Niveau();
		}
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		
		int error = glGetError();
		if(error != GL_NO_ERROR) {
			System.out.println(error);
		}
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {
		new Main().start();
	}

}
