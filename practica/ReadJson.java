import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;

class InvalidFileException extends Exception {
	public InvalidFileException(){
		super("Error: Archivo Json no valido");
	}
}

public class ReadJson {

	static Scanner sc = new Scanner(System.in);

	public static Labyrinth readJsons() {

		String json = "";
		String path;
		boolean seguir = false;
		do {
			try {
				path = sc.next();
				String fileExtension=path.substring(path.lastIndexOf(".")+1);
				if(fileExtension.equals("json")) throw new InvalidFileException();
				BufferedReader br = new BufferedReader(new FileReader(path)); 
				seguir = true;
				String line = "";
				while ((line = br.readLine()) != null) {
					json += line;
				}

				br.close();

			} catch(InvalidFileException e){
				System.out.println(e.getMessage());
				System.out.println("Vuelva a intentarlo con otro archivo:");
			}
			catch (FileNotFoundException FNFE) {
				System.out.println("Error: archivo no encontrado");
				System.out.println("Vuelva a intentar introducir la ruta: ");
			} catch (IOException IOE) {
				System.out.println(IOE.toString());
			}
		} while (!seguir);

		Gson gson = new Gson();

		Labyrinth lab = gson.fromJson(json, Labyrinth.class);
		return lab;
	}
}
