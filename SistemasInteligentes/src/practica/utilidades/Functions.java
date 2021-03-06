package practica.utilidades;

import java.io.File;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import practica.busqueda.Problem;
import practica.creacion.Cell;
import practica.creacion.Labyrinth;
import practica.creacion.WilsonAlgorithm;

/**
 * Esta clase tiene funciones auxiliares
 * 
 * @author David Gonz�lez Berm�dez, Lucas Guti�rrez Dur�n, David Guti�rrez
 *         Mariblanca Fecha: 16/10/2020
 */
public class Functions {

	static Scanner sc = new Scanner(System.in);

	/**
	 * Pasando un laberinto devuelves sus celdas en forma de array
	 * 
	 * @param map
	 * @return
	 */
	public static Cell[] getCellsFromMap(Map<String, Cell> map) {
		int counter = 0;
		Cell[] cellsArray = new Cell[map.size()];

		for (Iterator<Entry<String, Cell>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Cell> pairEntry = iterator.next();
			cellsArray[counter] = (Cell) pairEntry.getValue();
			counter++;
		}

		return cellsArray;
	}

	/**
	 * Genera un laberinto desde 0
	 */
	public static void genLab() {
		int[][] mov = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
		String[] id_mov = { "N", "E", "S", "O" };
		LinkedHashMap<String, Cell> cells = new LinkedHashMap<String, Cell>();
		int row = 0;
		int col = 0;
		boolean seguir = false;

		do {
			try {
				System.out.println("Indique las filas: ");
				row = sc.nextInt();
				if (row <= 0)
					throw new NegativeIntegerException();
				System.out.println("Indique las columnas: ");
				col = sc.nextInt();
				if (col <= 0)
					throw new NegativeIntegerException();
				seguir = true;
			} catch (NegativeIntegerException e) {
				System.out.println(e.getMessage());
				System.out.println("Introduzca un n�mero entero POSITIVO: ");
			} catch (InputMismatchException e) {
				System.out.println("Error: No ha introducido un car�cter num�rico");
			}
			sc.nextLine();
		} while (!seguir);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				boolean[] neighbours = { false, false, false, false };
				Cell cell = new Cell(0, neighbours, false); 
				cells.put("(" + i + ", " + j + ")", cell);
			}
		}

		Labyrinth lab = new Labyrinth(row, col, 4, mov, id_mov, cells);

		WilsonAlgorithm.wilson(lab);
		makeValue(lab);
		saveLab(lab, true);
		removeRandomWalls(lab);
		saveLab(lab,true); //Para ver si guarda nuevo laberinto con paredes quitadas
	}

	/**
	 * Se encarga de guardar el laberinto en forma de imagen y documento de texto
	 * 
	 * @param lab
	 */
	public static void saveLab(Labyrinth lab, boolean write) {
		String name = null;
		char a = (char) 92; // character "
		char b = (char) 34; // character \
		
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Seleccione donde quiere guardar su laberinto");
		int valorDevuelto = fc.showSaveDialog(null);
		
		File fileToSave;

		if (valorDevuelto == JFileChooser.APPROVE_OPTION) {
			fileToSave = fc.getSelectedFile();
			name = fileToSave.getName();
			if (name.contains("<") || name.contains(">") || name.contains(":") || name.contains("*")
					|| name.contains("?") || name.contains("|") || name.contains("/") || name.contains(a + "")
					|| name.contains(b + "")) {
				System.out.println("Error: Caracter no valido\n"
						+ "Introduzca un nombre sin los caracteres < > : * / ? | " + a + " " + b);
			} else {
				if(write) {
					WriteJson.writeJsonLab(lab, fileToSave);
				}
				DrawLab.drawLab(lab, fileToSave.getName());
				System.out.println("\nLos archivos se han guardado correctamente!");
			}
		} else {
			System.out.println("El usuario ha cancelado el guardado");
		}
	}
	
	/**
	 * Se encarga de generar un problema eligiendo el laberinto deseado, para despues
	 *  elegir la casilla inicial y la final para poder llevarlo a cabo.
	 *  
	 */

	public static void genProblem() {
		
		char a = (char) 92; // character "
		char b = (char) 34; // character \
		Labyrinth lab = new Labyrinth();
		String maze = null;
		String initial = null;
		String objetive = null;
		File fileToOpen = null;
		
		System.out.println("Seleccione el archivo del laberinto");
		JFileChooser fcOpen = new JFileChooser();
		fcOpen.setFileFilter(new ImageFilter());
		fcOpen.setDialogTitle("Seleccione un laberinto para el problema");
		int returnValue = fcOpen.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			fileToOpen = fcOpen.getSelectedFile();
			String name = fileToOpen.getName();
			maze = name;
			if (name.contains("<") || name.contains(">") || name.contains(":") || name.contains("*")
					|| name.contains("?") || name.contains("|") || name.contains("/") || name.contains(a + "")
					|| name.contains(b + "")) {
				System.out.println("Error: Caracter no valido\n"
						+ "Introduzca un nombre sin los caracteres < > : * / ? | " + a + " " + b);
			} else {
				System.out.println("\nEl archivo se seleccionado correctamente!");
				
				System.out.println("Casilla inicial:");
				System.out.println("-> Introduzca la fila:");
				int fi = sc.nextInt();
				System.out.println("-> Introduzca la columna: ");
				int ci = sc.nextInt();
				
				System.out.println("Casilla objetivo:");
				System.out.println("-> Introduzca la fila:");
				int fo = sc.nextInt();
				System.out.println("-> Introduzca la columna: ");
				int co = sc.nextInt();

				initial = "("+fi+", "+ci+")";
				objetive = "("+fo+", "+co+")";
				
				System.out.println(""+initial+" "+objetive+" "+ maze);
				Problem prob = new Problem(initial, objetive,maze, lab);
				
				saveProblem(prob);
				
			}
		} else {
			System.out.println("El usuario ha cancelado la operaci�n");
		}		
	}
	
	/**
	 * Se encarga de guardar el problema en un json
	 * 
	 * @param prob
	 */
	
	public static void saveProblem(Problem prob) {
		String name = null;
		char a = (char) 92; // character "
		char b = (char) 34; // character \
		JFileChooser fc = new JFileChooser();
		int valorDevuelto = fc.showSaveDialog(null);
		File fileToSave;

		if (valorDevuelto == JFileChooser.APPROVE_OPTION) {
			fileToSave = fc.getSelectedFile();
			name = fileToSave.getName();
			if (name.contains("<") || name.contains(">") || name.contains(":") || name.contains("*")
					|| name.contains("?") || name.contains("|") || name.contains("/") || name.contains(a + "")
					|| name.contains(b + "")) {
				System.out.println("Error: Caracter no valido\n"
						+ "Introduzca un nombre sin los caracteres < > : * / ? | " + a + " " + b);
			} else {
				WriteJson.writeJsonProblem(prob, fileToSave);
				System.out.println("\nLos archivos se han guardado correctamente!");
			}
		} else {
			System.out.println("El usuario ha cancelado el guardado");
		}
	}
	
	/**
	 * Se encarga de recorrer las celdas para asignar un valor aleatorio a cada una
	 * de ellas
	 * 
	 * @param lab
	 */ 
	
	public static void makeValue(Labyrinth lab) {
		Map<String, Cell> map = lab.getCells();
		for (int i = 0; i < lab.getRows(); i++) {
			for (int j = 0; j < lab.getCols(); j++) {
				Cell cell = map.get("(" + i + ", " + j + ")");
				cell.setValue((int)(Math.random()*4));
			}
		}
	}
	
	/**
	 * Se encarga de eliminar paredes aleatorias de nuestro laberinto indicando 
	 * el porcentaje de paredes a eliminar 
	 * 
	 * @param lab
	 */
	
	public static void removeRandomWalls(Labyrinth lab) {
		Map<String, Cell> map = lab.getCells();
		Cell cellCheck, cellCheckN, cellCheckE, cellCheckS, cellCheckW;
		int row = lab.getRows();
		int col = lab.getCols();
		int nwalls, wallremoved, ranRow, ranCol, ranNei;
		float nw, percent;
		boolean[] neighbours, neighboursN, neighboursE, neighboursS, neighboursW;
		

		System.out.println("Indique el porcentaje de paredes que quiere quitar: ");
		nw = sc.nextFloat();

		nwalls=((row+1)*col + (col+1)*row) - (row*2 + col*2) - (row*col-1);
		
		System.out.println("Numero de paredes en el laberinto: " + nwalls);
		percent=(nw/100);
		wallremoved=(int)(percent * nwalls);
		System.out.println("Se van a quitar un "+nw+"% de paredes. Total a remover: " + wallremoved+ " paredes");
		
		while (wallremoved > 0) {
			
			ranRow=(int)(Math.random() * ( lab.getRows()));
			ranCol=(int)(Math.random() * ( lab.getCols()));
			ranNei=(int)(Math.random() * 4 );
			
			cellCheck = map.get("(" + ranRow + ", " + ranCol + ")");
			neighbours = Arrays.copyOf(cellCheck.getNeighbors(), 4);
			
			if(ranNei == 0) {
				if((ranRow > 0) && !(neighbours[ranNei])) {
					neighbours[0] = true;
					cellCheck.setNeighbors(neighbours);
					
					cellCheckN = map.get("(" + (ranRow - 1) + ", " + ranCol + ")");
					neighboursN = cellCheckN.getNeighbors();
					neighboursN[2] = true;
					cellCheckN.setNeighbors(neighboursN);
					
					wallremoved--;
				}
				
			}
			
			if(ranNei == 1) {
				if((ranCol < col-1) && !(neighbours[ranNei])) {
					neighbours[1] = true;
					cellCheck.setNeighbors(neighbours);
					
					cellCheckE = map.get("(" + ranRow + ", " + (ranCol+1) + ")");
					neighboursE = cellCheckE.getNeighbors();
					neighboursE[3] = true;
					cellCheckE.setNeighbors(neighboursE);
					
					wallremoved--;
				}
			}
			
			if(ranNei == 2) {
				if((ranRow < row-1) && !(neighbours[ranNei])) {
					neighbours[2] = true;
					cellCheck.setNeighbors(neighbours);
					
					cellCheckS = map.get("(" + (ranRow + 1) + ", " + ranCol + ")");
					neighboursS = cellCheckS.getNeighbors();
					neighboursS[0] = true;
					cellCheckS.setNeighbors(neighboursS);
					
					wallremoved--;
				}
			}
			
			if(ranNei == 3) {
				if((ranCol < 0) && !(neighbours[ranNei]) ) {
					neighbours[3] = true;
					cellCheck.setNeighbors(neighbours);
					
					cellCheckW = map.get("(" + ranRow + ", " + (ranCol - 1) + ")");
					neighboursW = cellCheckW.getNeighbors();
					neighboursW[1] = true;
					cellCheckW.setNeighbors(neighboursW);
					
					wallremoved--;
				}
			}
									
		}

	}
	
	/**
	 * Se encarga de obtener la fila dado el id del estado
	 * 
	 * @param idState
	 */
	
	public static int getRow(String idState) {
		int x = idState.indexOf(",");
		int row = Integer.parseInt(idState.substring(1, x));
		return row;
	}

	/**
	 * Se encarga de obtener la columna dado el id del estado 
	 * 
	 * @param idState
	 */ 
	
	public static int getCol(String idState) {
		int x = idState.indexOf(",");
		int col = Integer.parseInt(idState.substring(x + 2, idState.length() - 1));
		return col;
	}
}