import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.Map.Entry;

public class Functions {

	static Scanner sc = new Scanner(System.in);

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

	public static void genLab() {
		boolean seguir = false;
		int row = 0;
		int col = 0;
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

		int[][] mov = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
		String[] id_mov = { "N", "E", "S", "O" };

		Stack<String> stackAuxVisited = new Stack<String>();
		Stack<Cell> stackAuxVisitedC = new Stack<Cell>();
		String aux = "";
		Cell auxC = null;
		ArrayList<String> noVisited = new ArrayList<String>();
		LinkedHashMap<String, Cell> cells = new LinkedHashMap<String, Cell>();
		LinkedHashMap<String, Cell> cellVisited = new LinkedHashMap<String, Cell>();

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				boolean[] neighbours = { false, false, false, false };
				Cell cell = new Cell(0, neighbours, false);
				cells.put("(" + i + "," + j + ")", cell);
				noVisited.add("(" + i + "," + j + ")");
			}
		}

		Labyrinth lab = new Labyrinth(row, col, 4, mov, id_mov, cells);

		int position2 = (int) (Math.random() * (noVisited.size()));
		String key2 = noVisited.get(position2);

		lab.getCells().get(key2).setVisited(true);

		while (cellVisited.size() < cells.size()) {
			int position = (int) (Math.random() * (noVisited.size()));
			String key1 = noVisited.get(position);
			int x = key1.indexOf(",");

			int ranRow = Integer.parseInt(key1.substring(1, x));
			int ranCol = Integer.parseInt(key1.substring(x + 1, key1.length() - 1));

			Functions.wilsonAlg(col, row, ranRow, ranCol, lab, stackAuxVisited, stackAuxVisitedC);
			while (!stackAuxVisited.empty()) {
				aux = stackAuxVisited.pop();
				auxC = stackAuxVisitedC.pop();
				auxC.setVisited(true);
				noVisited.remove(aux);
				if (cells.containsKey(aux)) {
					cells.get(aux).setVisited(true);
					cells.put(aux, auxC);
					cellVisited.put(aux, auxC);
				}
			}
		}
		saveLab(lab);
	}

	public static void saveLab(Labyrinth lab) {
		String name = null;
		char a = (char) 92; // character "
		char b = (char) 34; // character \
		boolean seguir = false;
		System.out.println("�Con qu� nombre quiere guardar los archivos?");		
		do {	
			name = sc.next();
			if (name.contains("<") || name.contains(">") || name.contains(":") || name.contains("*")
					|| name.contains("?") || name.contains("|") || name.contains("/") || name.contains(a + "")
					|| name.contains(b + "")) {
				System.out.println("Error: Caracter no valido\n"
						+ "Introduzca un nombre sin los caracteres < > : * / ? | " + a + " " + b);
			} else {
				seguir = true;
			}
		}while(!seguir);
		WriteJson.writeJson(lab, name);
		DrawLab.drawLab(lab, name);
		System.out.println("\nLos archivos se han guardado en su escritorio!");
	}

	public static void wilsonAlg(int col, int row, int ranRow, int ranCol, Labyrinth lab, Stack<String> stackAuxVisited,
			Stack<Cell> stackAuxVisitedC) {
		Map<String, Cell> cells = lab.getCells();
		Cell cell = null;
		String bucle = "";
		boolean done = false;
		int ranNei = 0;
		String last = "";
		while (!cells.get("(" + ranRow + "," + ranCol + ")").isVisited()) {
			cell = cells.get("(" + ranRow + "," + ranCol + ")");

			boolean[] neighbours = Arrays.copyOf(cell.getNeighbors(), 4);
			if (!stackAuxVisited.contains("(" + ranRow + "," + ranCol + ")")) {

				if (!stackAuxVisited.empty()) {
					neighbours[0] = false;
					neighbours[1] = false;
					neighbours[2] = false;
					neighbours[3] = false;
					switch (ranNei) {
					case 0:
						neighbours[2] = true;
						break;
					case 1:
						neighbours[3] = true;
						break;
					case 2:
						neighbours[0] = true;
						break;
					case 3:
						neighbours[1] = true;
						break;
					}
				}

				// Order - N,E,S,W
				do {
					ranNei = (int) (Math.random() * 4);
					if (ranNei == 0 && neighbours[0] == false) {
						if (ranRow > 0) {
							neighbours[0] = true;
							stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
							stackAuxVisitedC.push(new Cell(0, neighbours, false));
							ranRow = ranRow - 1;
							done = true;
						}
					}
					if (ranNei == 1 && neighbours[1] == false) {
						if (ranCol < col - 1) {
							neighbours[1] = true;
							stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
							stackAuxVisitedC.push(new Cell(0, neighbours, false));
							ranCol = ranCol + 1;
							done = true;
						}
					}
					if (ranNei == 2 && neighbours[2] == false) {
						if (ranRow < row - 1) {
							neighbours[2] = true;
							stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
							stackAuxVisitedC.push(new Cell(0, neighbours, false));
							ranRow = ranRow + 1;
							done = true;
						}
					}
					if (ranNei == 3 && neighbours[3] == false) {
						if (ranCol > 0) {
							neighbours[3] = true;
							stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
							stackAuxVisitedC.push(new Cell(0, neighbours, false));
							ranCol = ranCol - 1;
							done = true;
						}
					}
				} while (!done);
				done = false;
			} else {
				bucle = "(" + ranRow + "," + ranCol + ")";
				while (stackAuxVisited.contains(bucle)) {
					last = stackAuxVisited.pop();
					stackAuxVisitedC.pop();
				}
				if (!stackAuxVisited.empty()) {
					String key1 = stackAuxVisited.peek();
					int x = key1.indexOf(",");

					int ranRow3 = Integer.parseInt(key1.substring(1, x));
					int ranCol3 = Integer.parseInt(key1.substring(x + 1, key1.length() - 1));

					int y = last.indexOf(",");

					int ranRow4 = Integer.parseInt(last.substring(1, y));
					int ranCol4 = Integer.parseInt(last.substring(y + 1, last.length() - 1));

					int r = ranRow3 - ranRow4;
					int c = ranCol3 - ranCol4;

					if (c == -1) {
						ranNei = 1;
					}
					if (c == 1) {
						ranNei = 3;
					}
					if (r == -1) {
						ranNei = 2;
					}
					if (r == 1) {
						ranNei = 0;
					}
				}
				bucle = "";

			}
		}
		cell = cells.get("(" + ranRow + "," + ranCol + ")");

		boolean[] neighbours = Arrays.copyOf(cell.getNeighbors(), 4);
		if (!stackAuxVisited.empty()) {
			switch (ranNei) {
			case 0:
				neighbours[2] = true;
				stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
				stackAuxVisitedC.push(new Cell(0, neighbours, false));
				break;
			case 1:
				neighbours[3] = true;
				stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
				stackAuxVisitedC.push(new Cell(0, neighbours, false));
				break;
			case 2:
				neighbours[0] = true;
				stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
				stackAuxVisitedC.push(new Cell(0, neighbours, false));
				break;
			case 3:
				neighbours[1] = true;
				stackAuxVisited.push("(" + ranRow + "," + ranCol + ")");
				stackAuxVisitedC.push(new Cell(0, neighbours, false));
				break;
			}
		}
	}

	public static void readLab() {
		Labyrinth lab;
		boolean checkGood;
		System.out.println("\nEscriba la ruta completa de su archivo .json:");
		lab = ReadJson.readJsons();
		checkGood = checkSemantic(lab);

		if (checkGood) {
			saveLab(lab);
		} else {
			System.out.println("\nEL ARCHIVO JSON ES INCONSISTENTE");
		}

	}

	public static boolean checkSemantic(Labyrinth lab) {
		Map<String, Cell> cells = lab.getCells();
		Cell cellCheck = null;
		Cell cellCheckCol = null;
		Cell cellCheckRow = null;
		boolean semanticGood = true;
		// boolean[][] semanticGood = new boolean[lab.getCols()][lab.getRows()];

		for (int i = 0; i < lab.getRows(); i++) {
			for (int j = 0; j < lab.getCols(); j++) {

				cellCheck = cells.get("(" + i + ", " + j + ")");
				boolean[] neighbours = Arrays.copyOf(cellCheck.getNeighbors(), 4);

				if (j < (lab.getCols() - 1) && i < (lab.getRows() - 1)) {
					cellCheckCol = cells.get("(" + i + ", " + (j + 1) + ")");
					boolean[] neighboursCol = Arrays.copyOf(cellCheckCol.getNeighbors(), 4);
					cellCheckRow = cells.get("(" + (i + 1) + ", " + j + ")");
					boolean[] neighboursRow = Arrays.copyOf(cellCheckRow.getNeighbors(), 4);

					// En cada celda, va a ir comprobando el vecino este y sur, comprobar los 4 a la
					// vez son innecesarios
					if (neighbours[1] == neighboursCol[3] && neighbours[2] == neighboursRow[0]) {
						// semanticGood[i][j] = true;
					} else {
						semanticGood = false;

						if (neighbours[1] == neighboursCol[3]) {

						} else {
							System.out.println("\nFallo de inconsistencia entre las celdas (" + i + "," + j + ") y ("
									+ i + ", " + (j + 1) + ")");
						}
						if (neighbours[2] == neighboursRow[0]) {

						} else {
							System.out.println("\nFallo de inconsistencia entre las celdas (" + i + "," + j + ") y ("
									+ (i + 1) + ", " + j + ")");
						}
					}

				}

				if (j == (lab.getCols() - 1) && i == (lab.getRows() - 1)) {
					// semanticGood[i][j]=true;
					// Esta celda cuando sea llegada no va a necesitar comprobacion debido a que ha
					// sido comprobada
				} else {
					// Si llega a la ultima columna, que compruebe su vecino del sur
					if (j == (lab.getCols() - 1)) {
						cellCheckRow = cells.get("(" + (i + 1) + ", " + j + ")");
						boolean[] neighboursRow = Arrays.copyOf(cellCheckRow.getNeighbors(), 4);

						if (neighbours[2] == neighboursRow[0]) {
							// semanticGood[i][j] = true;
						} else {
							semanticGood = false;
							System.out.println("\nFallo de inconsistencia entre las celdas (" + i + "," + j + ") y ("
									+ (i + 1) + ", " + j + ")");
						}

					}
					// Si llega a la ultima fila, que compruebe su vecino del este
					if (i == (lab.getRows() - 1)) {
						cellCheckCol = cells.get("(" + i + ", " + (j + 1) + ")");
						boolean[] neighboursCol = Arrays.copyOf(cellCheckCol.getNeighbors(), 4);

						if (neighbours[1] == neighboursCol[3]) {
							// semanticGood[i][j] = true;
						} else {
							semanticGood = false;
							System.out.println("\nFallo de inconsistencia entre las celdas (" + i + "," + j + ") y ("
									+ i + ", " + (j + 1) + ")");
						}

					}
				}

			}

		}

		return semanticGood;
	}
}