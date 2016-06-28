package model;

import java.io.IOException;
import java.util.ArrayList;

import ORI_osmosmerka_web.DatasetParser;

public class SearchWord {
	
	
	
	public static void main (String[] args) throws ClassNotFoundException, IOException
	{
		ArrayList<String> availableWords = DatasetParser.parseDataSet("beach.csv");
		//ArrayList<String> availableWords = new ArrayList<>();
		availableWords.add("mina");
		availableWords.add("medic");
		availableWords.add("jovana");
		availableWords.add("micic");
		availableWords.add("katarina");
		availableWords.add("preradov");
		availableWords.add("kamila");
		availableWords.add("kornjaca");
		
		
		ArrayList<Word> allWords = new ArrayList<Word>();
		
		for (String s : availableWords)
		{
			Word word = new Word();
			word.setWord(s);
			allWords.add(word);
		}
		
		WordSearchGenerator generator = new WordSearchGenerator(5000, allWords);
		
		generator.generate();
		
		generator.randomLetters();
		generator.display();
		
		
	}

}
