package ORI_osmosmerka_web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
	public char fs = File.separatorChar;
	
	@POST
	@Path("/getGameTable")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Field> getGameTable(String category) throws ClassNotFoundException, IOException {
		category = category.replace("\"", "");
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
		
		generator.generate(); 		//ubaci reci
		//generator.randomLetters(); 	//popuni grid do kraja random slovima
		
		wordsToFind = new ArrayList<String>();
		wordsToFind.addAll(generator.getCurrentWordList());
		
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
		
		for (String s : wordsToFind)
			if (s.equalsIgnoreCase(wordToCheck))
				return "ok";
		
		return "no";
		
	}
}
