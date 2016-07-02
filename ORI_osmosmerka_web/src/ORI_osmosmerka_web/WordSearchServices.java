package ORI_osmosmerka_web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;

import model.Field;
import model.Word;
import model.WordSearchGenerator;

@Path("/game")
public class WordSearchServices {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	private static ArrayList<String> wordsToFind;
	private static ArrayList<Word> wordsInGrid;
	public char fs = File.separatorChar;
	
	@POST
	@Path("/getGameTable")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Field> getGameTable(String input) throws ClassNotFoundException, IOException {
		
		input = input.replace("\"", "");
		input = input.replace("}", "");
		input = input.replace("{", "");
		
		String category = (input.split(",")[0]).split(":")[1];
		String difficult = (input.split(",")[1]).split(":")[1];
		
		String path = ctx.getRealPath("")+ fs + "dataset" + fs + category.toLowerCase()+".csv";
		ArrayList<String> availableWords = DatasetParser.parseDataSet(path);
		ArrayList<Word> allWords = new ArrayList<Word>();
		
		for (String s : availableWords)
		{
			Word word = new Word();
			word.setWord(s);
			allWords.add(word);
		}
		
		WordSearchGenerator generator = new WordSearchGenerator(5000, allWords);
		
		generator.generate(difficult); 		//ubaci reci
		generator.randomLetters(); 	//popuni grid do kraja random slovima
		
		wordsToFind = new ArrayList<String>();
		wordsInGrid = new ArrayList<Word>();
		
		wordsInGrid.addAll(generator.getAvailableWords());
		
		for (String s : generator.getCurrentWordList())
			wordsToFind.add(s.toLowerCase());
		
		return generator.getGrid();
		
	}

	@POST
	@Path("/getWords")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getWords() throws ClassNotFoundException, IOException {
		return wordsToFind;
	}
	
	
	
	@POST
	@Path("/checkAnswer")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public String checkAnswer(String wordToCheck) throws ClassNotFoundException, IOException {
		wordToCheck = wordToCheck.replace("\"", "");
		wordToCheck = wordToCheck.toLowerCase();
		
		for (String s : wordsToFind)
		{
			if (s.equalsIgnoreCase(wordToCheck))
				return "ok";
		}
		
		return "no";
		
	}
	
	@POST
	@Path("/findLetterForHintEasy")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public String findLetterForHintEasy(String word) throws ClassNotFoundException, IOException {
		word = word.replace("\"", "");
		
		for (Word w : wordsInGrid)
		{
			if (w.getWord().equalsIgnoreCase(word)){
				int row = w.getLetters().get(0).getX();
				int col = w.getLetters().get(0).getY();
				int index = row * 12 + col;
				return index+"";
			}
		}
		return "error";
	}
	
	@POST
	@Path("/findLetterForHintHard")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public String findLetterForHintHard(String word) throws ClassNotFoundException, IOException {
		word = word.replace("\"", "");
		
		for (Word w : wordsInGrid)
		{
			if (w.getWord().equalsIgnoreCase(word)){
				int n = getRandomNumberInRange(0, w.getLetters().size());
				int row = w.getLetters().get(n).getX();
				int col = w.getLetters().get(n).getY();
				int index = row * 12 + col;
				return index+"";
			}
		}
		return "error";
	}

	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	@GET
	@Path("/solve")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> solve() throws ClassNotFoundException, IOException {
		ArrayList<String> retVal = new ArrayList<String>();
		for (Word w : wordsInGrid)
		{
			if(wordsToFind.contains(w.getWord().toLowerCase())) {
				if (w != null && w.getLetters() != null) {
					for (Field letter : w.getLetters()){
						int row =letter.getX();
						int col = letter.getY();
						int index = row * 12 + col;
						retVal.add(index+"");
					}
				}
			}
		}
		return retVal;
	}
	
	@GET
	@Path("/solvedWords")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> solvedWords() throws ClassNotFoundException, IOException {
		ArrayList<String> retVal = new ArrayList<String>();
		for (Word w : wordsInGrid)
		{
			if(wordsToFind.contains(w.getWord().toLowerCase())) {  
				retVal.add(w.getWord().toLowerCase());
			}
		}
		return retVal;
	}
}
