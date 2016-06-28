package ORI_osmosmerka_web;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DatasetParser {
	
	public static ArrayList<String> parseDataSet(String path){
		ArrayList<String> words = new ArrayList<String>();
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
			System.err.println(e);
		}
        
		return words;
	}

//	public static void main(String[] args) {
//		String dataSet = "beach.csv";
//		ArrayList<String> words = parseDataSet(dataSet);
//	}

}
