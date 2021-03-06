package practica.busqueda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import practica.utilidades.*;
import practica.creacion.Cell;
import practica.creacion.Labyrinth;

/**
 * Esta clase define el problema en el que queremos ir desde una celda inicial a una objetivo.
 * Tiene como atributos el inicio, el final, y el laberinto a usar
 * @author David González Bermúdez, Lucas Gutiérrez Durán, David Gutiérrez Mariblanca
 * Fecha: 28/10/2020
 * 
 */

public class Problem {
	@SerializedName("INITIAL")
	private String initial;
	@SerializedName("OBJETIVE")
	private String objective;
	@SerializedName("MAZE")
	private String maze;
	private Labyrinth lab;
	private String path;
	
	public Problem () {
		this.lab = new Labyrinth();
	}
	
	public Problem (String initial, String objective, String maze, Labyrinth lab) {
		this.initial = initial;
		this.objective = objective;
		this.maze = maze;
		this.lab = lab;
	}
	
	public Labyrinth getLab() {
		return lab;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path=path;
	}

	public void setLab(Labyrinth lab) {
		this.lab = lab;
	}
	

	/**
	 * Metodo que nos permite obtener los sucesores de nuestro nodo dado
	 * el id estado, comprobando sus vecinos
	 * 
	 * @param idEstado
	 * @return sucesors
	 */
	
	public ArrayList<Sucesor> getSucesors(String idState) {

		ArrayList<Sucesor> sucesors = new ArrayList<Sucesor>();
		Map<String, Cell> cells = lab.getCells();
		Cell cellCheck = null;
		int row = Functions.getRow(idState);
		int col = Functions.getCol(idState);
		int rowN = row - 1;
		int colN = col;
		int rowE = row;
		int colE = col + 1;
		int rowS = row + 1;
		int colS = col;
		int rowO = row;
		int colO = col - 1;

		cellCheck = cells.get("(" + row + ", " + col + ")");
		boolean[] neighbours = Arrays.copyOf(cellCheck.getNeighbors(), 4);

		if (neighbours[3]) {
			sucesors.add(new Sucesor("O", "(" + rowO + ", " + colO + ")", cells.get("(" + rowO + ", " + colO + ")").getValue()));
		}
		if (neighbours[2]) {
			sucesors.add(new Sucesor("S", "(" + rowS + ", " + colS + ")", cells.get("(" + rowS + ", " + colS + ")").getValue()));
		}
		if (neighbours[1]) {
			sucesors.add(new Sucesor("E", "(" + rowE + ", " + colE + ")", cells.get("(" + rowE + ", " + colE + ")").getValue()));
		}
		if (neighbours[0]) {
			sucesors.add(new Sucesor("N", "(" + rowN + ", " + colN + ")", cells.get("(" + rowN + ", " + colN + ")").getValue()));
		} 
		
		return sucesors;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getMaze() {
		return maze;
	}

	public void setMaze(String maze) {
		this.maze = maze;
	}

	@Override
	public String toString() {
		return "Problema [initial=" + initial + ", objective=" + objective + ", maze=" + maze + "]";
	}

}
