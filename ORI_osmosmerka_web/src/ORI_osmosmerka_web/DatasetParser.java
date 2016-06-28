package ORI_osmosmerka_web;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DatasetParser {
	
	public static ArrayList<String> parseDataSet(String dataSet){
		ArrayList<String> words = new ArrayList<String>();
		String path = ".\\WebContent\\dataset\\" + dataSet;
		Scanner scanner;
		try {
			scanner = new Scanner(new File(path));

			while (scanner.hasNext()) {
				String word = scanner.next().trim();
				if (word.length() > 1)
					words.add(word.toLowerCase());
			}
			scanner.close();
		         
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't exist.");
		}
        
		return words;
	}

	public static void main(String[] args) {
		String dataSet = "beach.csv";
		ArrayList<String> words = parseDataSet(dataSet);
	}

}
