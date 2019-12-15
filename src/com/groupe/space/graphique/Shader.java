package com.groupe.space.graphique;

import com.groupe.space.math.Matrice;
import com.groupe.space.math.Vector;
import com.groupe.space.utils.ShaderUtils;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

public class Shader {
	
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	
	public static Shader BACK, FUSEE, OBSTACLE, FADE, BONUS; //Background
	
	private boolean enabled = false;
	
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public static void loadAll() {
		//chargement de tous les shaders
		BACK = new Shader("shaders/bg.vert", "shaders/bg.frag");
		FUSEE = new Shader("shaders/fusee.vert", "shaders/fusee.frag");
		OBSTACLE = new Shader("shaders/obstacle.vert", "shaders/obstacle.frag");
		FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
		BONUS = new Shader("shaders/bonus.vert", "shaders/bonus.frag");
	}
	
	public int getUniform(String name) { //retroune la valeur d'une variable uniform
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		int result = glGetUniformLocation(ID, name);
		if (result == -1) {
			System.err.println("Ne trouve pas la variable Uniform '" + name + "'!");
		}
		else 
			locationCache.put(name, result);
		return result;
	}
	
	public void setUniform1i(String name, int value) {
		if(!enabled) enable();
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		if(!enabled) enable();
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float a, float b) {
		if(!enabled) enable();
		glUniform2f(getUniform(name), a, b);
	}
	
	public void setUniform3f(String name, Vector vector) {
		if(!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniform4f(String name, Matrice matrice) {
		if(!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrice.toFloatBuffer());
	}
	
	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}
	
	public void disable() {
		glUseProgram(ID);
		enabled = false;
	}
	
}
