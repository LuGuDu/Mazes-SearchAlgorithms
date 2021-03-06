package practica.busqueda;

/**
 * Esta clase es un nodo que representa la celda que se va recorriendo y sus atributos en un arbol.
 * Tiene id Nodo, coste, id estado, id del padre (es decir del nodo primero), accion, profundidad del nodo,
 * heuristica, y el valor final
 * @author David Gonz�lez Berm�dez, Lucas Guti�rrez Dur�n, David Guti�rrez Mariblanca
 * Fecha: 28/10/2020
 * 
 */

public class Node {
	private int id;
	private double cost;
	private String idState;
	private Node father;
	private String action;
	private int depth;
	private double heuristic;
	private double value;
	
	public Node() {
		
	}
	
	public Node (int id, double cost, String idState, Node father, String action, 
			int depth, double heuristic, double value) {
		this.id = id;
		this.cost = cost;
		this.idState = idState;
		this.father = father;
		this.action = action;
		this.depth = depth;
		this.heuristic = heuristic;
		this.value = value;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getIdState() {
		return idState;
	}

	public void setIdState(String idState) {
		this.idState = idState;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	/*
	 * Tanto el metodo getRow() como el metodo getCol() se encargan de obtener las filas
	 * y las columnas de forma manual, dado el id estado
	 * 
	 */
	
	public int getRow() {
		int x = idState.indexOf(",");
		int row = Integer.parseInt(idState.substring(1, x));
		return row;
	}

	public int getCol() {
		int x = idState.indexOf(",");
		int col = Integer.parseInt(idState.substring(x + 2, idState.length() - 1));
		return col;
	}
	
	public String getFatherId(){
		String idFather = null;
		if(father != null) {
			idFather = Integer.toString(father.getId());
		} else idFather = "None";
		return idFather;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", cost=" + cost + ", idState=" + idState + ", action="
				+ action + ", depth=" + depth + ", heuristic=" + heuristic + ", value=" + value + "]";
	}
	
}
