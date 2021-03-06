package practica.busqueda;

import java.util.ArrayList;

import practica.utilidades.Functions;

/**
 * Esta clase se encarga de realizar los arboles con cada algoritmo de busqueda.
 * Segun con cada algoritmo de busqueda, nuestra variable value se calculara de manera distinta.
 * Tiene la clase problema, la profundidad, estrategia, lista de visiados, y borde.
 * 
 * @author David Gonz�lez Berm�dez, Lucas Guti�rrez Dur�n, David Guti�rrez Mariblanca
 * Fecha: 14/11/2020
 */

public class SearchAlgorithm {
	/**
	 * Nos permite realizar el arbol correspondiente con su respectivo algoritmo de
	 * busqueda, estableciendo al nodo todos los atributos necesarios
	 * 
	 * @param problem
	 * @param depth
	 * @param strategy
	 * @param visited
	 * @param border
	 * 
	 * @return node
	 * 
	 */
	public static ArrayList<Node> search(Problem problem, int depth, int strategy, ArrayList<String>visited, Border border) {
	
		boolean isSolution = false;
		int id = 0;
		// Nodo inicial
		Node node = new Node();
		node.setId(id);
		node.setFather(null);
		node.setIdState(problem.getInitial());
		node.setCost(0);
		node.setDepth(0);
		node.setAction("None");
		node.setHeuristic(heuristic(problem, node.getIdState()));
		node.setValue(calculate(strategy, node));
		id++;
		
		border.push(node);
		while (!border.getFrontier().isEmpty() && !isSolution) {
			node = border.pop();
			
			if (problem.getObjective().equals(node.getIdState())) {
				isSolution = true;
			} else if (!visited.contains(node.getIdState()) && node.getDepth() < depth) {
				visited.add(node.getIdState());
				ArrayList<Node> expandNodes = new ArrayList<Node>();
				id = expandNode(problem, node, strategy, id, expandNodes);
				
				while (!expandNodes.isEmpty()) {
					border.push(expandNodes.remove(expandNodes.size() - 1));				
				}
			}
		}
		if (isSolution) {

			return road(node);
		} else {
			return null;
		}
	}
	
	/**
	 * Metodo que nos permite obtener los nodos hijos a la hora de 
	 * expandir un nodo de la frontera
	 * 
	 * @param problem, node, strategy, id, expandNodes
	 * @return id
	 */
	
	public static int expandNode(Problem problem, Node node, int strategy, int id, ArrayList<Node> expandNodes) {
		ArrayList<Sucesor> sucesors = problem.getSucesors(node.getIdState());
		while (!sucesors.isEmpty()) {
			Sucesor suc = new Sucesor();
			suc = sucesors.remove(sucesors.size() - 1);
			Node childNode = new Node();
			childNode.setId(id);
			childNode.setIdState(suc.getIdState());
			childNode.setFather(node);
			childNode.setAction(suc.getMov());
			childNode.setDepth(node.getDepth() + 1);
			childNode.setCost(node.getCost() + suc.getCost() + 1);
			childNode.setHeuristic(heuristic(problem, childNode.getIdState()));
			childNode.setValue(calculate(strategy, childNode));
			expandNodes.add(childNode);
			id++;
		}

		return id;
	}
	
	/**
	 * Nos permite obtener la heuristica
	 * 
	 * @param problem, idState
	 * @return heuristic
	 */
	
	public static double heuristic(Problem problem, String idState) {
		int finalRow = Functions.getRow(problem.getObjective());
		int finalCol = Functions.getCol(problem.getObjective());
		int row = Functions.getRow(idState);
		int col = Functions.getCol(idState);
		double heuristic = Math.abs((row - finalRow)) + Math.abs((col - finalCol));

		return heuristic;
	}
	
	/**
	 * Metodo que nos permite obtener el valor total de value segun la estrategia
	 * 
	 * @param strartegy, node
	 * @return value
	 */ 
	public static double calculate(int strategy, Node node) {
		double value = 0;
		switch (strategy) {
		case 1:
			value = node.getDepth();// Estrategia en anchura
			break;
		case 2:
			value = (1.0 / (node.getDepth() + 1.0));// Estrategia en profundidad acotada
			break;
		case 3:
			value = node.getCost(); // Estrategia costo uniforme
			break;
		case 4:
			value = node.getHeuristic();// Estrategia voraz
			break;
		case 5:
			value = node.getCost() + node.getHeuristic(); // Estrategia A*
			break;
		}
		

		return value;
	}

	/**
	 * Metodo que nos devuelve el camino solucion
	 * 
	 * @param node
	 * @return solution
	 */
	
	public static ArrayList<Node> road(Node node) {
		ArrayList<Node> solution = new ArrayList<Node>();
		while (node.getFather() != null) {
			solution.add(node);
			node = node.getFather();
		}
		solution.add(node);
		return solution;
	}

}
