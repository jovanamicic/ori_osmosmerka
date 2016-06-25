package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class WordSearchGenerator {

	public static final int COLUMS = 12;
	public static final int ROWS = 12;

	private int maxLoops;

	private ArrayList<Word> availableWords;
	
	private ArrayList<String> currentWordList;

	private ArrayList<Word> words;

	private ArrayList<Field> grid;

	public WordSearchGenerator() {
		
//		this.availableWords = new ArrayList<Word>();
//		this.currentWordList = new ArrayList<String>();
//		this.words = new ArrayList<Word>();
//		this.grid = new ArrayList<Field>();
//		randomizeAndSort();

	}

	public WordSearchGenerator(int maxLoops, ArrayList<Word> availableWords) {
		super();
		this.maxLoops = maxLoops;
		this.availableWords = availableWords;

		this.currentWordList = new ArrayList<String>();
		this.words = new ArrayList<Word>();
		this.grid = new ArrayList<Field>();
		
		randomizeAndSort();
	}

	public int getMaxLoops() {
		return maxLoops;
	}

	public void setMaxLoops(int maxLoops) {
		this.maxLoops = maxLoops;
	}


	public ArrayList<Word> getAvailableWords() {
		return availableWords;
	}

	public void setAvailableWords(ArrayList<Word> availableWords) {
		this.availableWords = availableWords;
	}

	public ArrayList<String> getCurrentWordList() {
		return currentWordList;
	}

	public void setCurrentWordList(ArrayList<String> currentWordList) {
		this.currentWordList = currentWordList;
	}

	public static int getColums() {
		return COLUMS;
	}

	public static int getRows() {
		return ROWS;
	}
	
	

	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}

	public ArrayList<Field> getGrid() {
		return grid;
	}

	public void setGrid(ArrayList<Field> grid) {
		this.grid = grid;
	}

	public void randomizeAndSort() {

		ArrayList<Word> temp = new ArrayList<Word>();

		for (Word word : availableWords) {
			temp.add(word);
		}

		//Collections.shuffle(temp);

		Collections.sort(temp, new Comparator<Word>() {
			@Override
			public int compare(Word w1, Word w2) {
				Integer int1 = new Integer(w1.getWord().length());
				Integer int2 = new Integer(w2.getWord().length());
				return int2.compareTo(int1);
			}
		});
		
		availableWords.clear();
		availableWords.addAll(temp);
		
	}

	public void clearGrid() {
		grid.clear();
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMS; col++) {
				grid.add(new Field(row, col, 0, '-'));
			}
		}

	}

	public void fitAndAdd(Word word) {
		boolean fit = false;
		int count = 0; // koliko se puta preklapa
		
		ArrayList<String> coordList = new ArrayList<>();
		coordList.addAll(suggestCoordinates(word));
		
		
		while (!fit && (count < this.maxLoops))
		{
			if (this.currentWordList.size() == 0)
			{
				//prva rec
				Random rn = new Random();
				//TODO ovo n se treba povecati za 1 kad se doda dijagonalno
				int n = 2;
				int d = rn.nextInt() % n;
				
				String direction = "";
				
				if (d == 0)
					direction = "vertical";
				else if (d == 1)
					direction = "horizontal";
				else
					direction = "diagonal";
				
				int row = 1, col = 1;
				
				if (this.checkValue(col, row, direction, word) != 0) 
				{
					fit = true;
					setWord(col, row, direction, word);
				}
				
			}
			else 
			{
				String coord = "";
				String tokens[];
				
				int col;
				int row;
				String direction;
				
				try {
					coord = coordList.get(count);
					
					System.out.println("COORD " + coord);
					tokens = coord.split(",");
					
					col = Integer.parseInt(tokens[0]);
					row = Integer.parseInt(tokens[1]);
					direction = tokens[2];
					
				} catch (Exception e) {
					System.out.println("No more coords! Stop trying to fit!");
					return;
				}
				
				if (Integer.parseInt(tokens[3]) != 0) 
				{
					fit = true;
					setWord(col, row, direction, word);
				}
			}
			count++;
		}
	}
	
	

	private void setWord(int col, int row, String direction, Word word) {
		
		int index = row * ROWS + col; //TODO index
		
		word.setDirection(direction);
		word.addFiled(grid.get(index));
		System.out.println("DUZINA : " + word.getLetters().size());
		
		currentWordList.add(word.getWord());
		
		for (char letter : word.getWord().toCharArray()) 
		{
			setCell(col-1, row-1, letter);
			if (direction.equals("vertical"))
				row++;
			else if (direction.equals("horizontal"))
				col++;
			//TODO za diagonal
			
		}
		
		
	}

	public ArrayList<String> suggestCoordinates(Word word) {
		ArrayList<String> coordList = new ArrayList<String>();
		ArrayList<String> newCoordList = new ArrayList<String>();

		int count = 0;
		int ind = -1; // indikator
		

		for (int i = 0; i < word.getWord().length(); i++) {
			char letter = word.getWord().charAt(i);
			
			ind++;
			int rowC = 0; //indeks kolone u redu
			
			for (int row = 0; row < ROWS; row++)
			{
				if (rowC == ROWS)
					rowC = 0;
				
				rowC++;
				
				int colC = 0; //
				
				for (int cell = 0; cell < COLUMS; cell++)
				{
					if (colC == COLUMS)
						colC = 0;
					
					colC++;
					
					if ( (getCell(cell, row)).getLetter() == letter )
					{
						// coord = col, row, direction, col + row, value

						// proba vertikalno
						if (rowC - ind > 0)
						
							if (rowC - ind + word.getWord().length() <= ROWS) {
								String coords = colC + "," + (rowC - ind) + ",vertical,"
										 + "0";
								coordList.add(coords);
							}

						// proba horizontalno
						if (colC - ind > 0)
							if (colC - ind + word.getWord().length() <= COLUMS) {
								String coords = (colC - ind) + "," + (rowC) + ",horizontal,"
										 + "0";
								coordList.add(coords);
							}
					}
				}
				
			}
			
		}
		
		newCoordList.addAll(coordList);
		
		newCoordList = this.sortCoordList(newCoordList, word);
		
		
		return newCoordList;
	}

	// dodaje value svakom polju i onda ga sortira
	private ArrayList<String> sortCoordList(ArrayList<String> coordList, Word word) {
		ArrayList<String> newCoordList = new ArrayList<String>();

		for (String coord : coordList) {
			// coord = col, row, direction, col + row, value
			String tokens[] = coord.split(",");
			int col = Integer.parseInt(tokens[0]);
			int row = Integer.parseInt(tokens[1]);
			String direction = tokens[2];
			int value = Integer.parseInt(tokens[3]);
			
			coord = coord.substring(0, (coord.lastIndexOf(',')));

			value = this.checkValue(col, row, direction, word);
			coord += "," +  value;
			
			newCoordList.add(coord);
			
		}
		
		//Collections.shuffle(newCoordList);
		
		//sortiraj po value
		Collections.sort(newCoordList, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				Integer val1 = new Integer(s1.split(",")[3]);
				Integer val2 = new Integer(s2.split(",")[3]);
				return val2.compareTo(val1);
			}
		});
		
		for (String s : newCoordList)
			System.out.println(s);

		return newCoordList;
	}

	//heuristicka funkcija, racuna nam vrednost polja
	//vratice 0 ako ne moze da se uklopi, 1 je ako se uklapa, a 2+ ako se i preklapa
	private int checkValue(int col, int row, String direction, Word word) {
		

		if (col < 1 || row < 1)
			return 0;
		

		int count = 1; //koje je slovo trenutno
		int value = 1; //vrednost polja
		
		Field activeCell;
		try {
			activeCell = this.getCell(col, row);
		} catch (Exception e) {
			return 0;
			
		}
		
		
		System.out.println("REC: " + word.getWord());
		for (char letter : word.getWord().toCharArray())
		{
			if (activeCell.getLetter() != '-' || activeCell.getLetter() != letter)
				return 0;
			
			if (activeCell.getLetter() == letter)
				value++;
			
			if (direction.equals("vertical"))
			{
				if (!activeCell.equals(letter)) //proveri da li se ukrstaju
				{
					if (!checkIfFieldClear(col+1, row)) //proveri desno polje
					{
						return 0;
					}
					if (!checkIfFieldClear(col-1, row)) //proveri levo polje
					{
						return 0;
					}
				}
				
				if (count == 1) //ako je prvo slovo, proveri da li ima neka rec iznad
				{
					if (!checkIfFieldClear(col, row-1))
					{
						return 0;
					}
				}
				
				if (count == word.getWord().length()) //ako je poslednje slovo, proveri da li ima neka rec ispod
				{
					if (!checkIfFieldClear(col, row+1))
					{
						return 0;
					}
				}
				
				row++;
			}
			if (direction.equals("horizontal"))
			{
				if (!activeCell.equals(letter)) //proveri da li se ukrstaju
				{
					if (!checkIfFieldClear(col, row-1)) //proveri polje iznad
					{
						return 0;
					}
					if (!checkIfFieldClear(col, row+1)) //proveri polje ispod
					{
						return 0;
					}
				}
				
				if (count == 1) //ako je prvo slovo, proveri da li ima neka rec levo
				{
					if (!checkIfFieldClear(col-1, row))
					{
						return 0;
					}
				}
				
				if (count == word.getWord().length()) //ako je poslednje slovo, proveri da li ima neka rec desno
				{
					if (!checkIfFieldClear(col+1, row))
					{
						return 0;
					}
				}
				
				col++;
			}
			count++;
		}
		return value;
		

	}
	
	public boolean checkIfFieldClear(int col, int row)
	{
		System.out.println(row + "  jhjsgdjsgj " + col);
		Field f = this.getCell(col, row);
		
		if (f.getLetter() == '-')
			return true;
		
		return false;
	}

	// TODO proveri indekse!!
	public Field getCell(int col, int row) {
		int index = row * ROWS + col; //TODO index
		
		return grid.get(index);
	}

	// TODO proveri indekse!!
	public void setCell(int col, int row, char c) {
		int index = row * ROWS + col; //TODO index
		
		Field field = grid.get(index);
		field.setLetter(c);
		grid.set(index, field);
	}

	public void generate() throws ClassNotFoundException, IOException {

		WordSearchGenerator copy = new WordSearchGenerator();
		copy.setAvailableWords(this.getAvailableWords());
		copy.setCurrentWordList(this.getCurrentWordList());
		copy.setMaxLoops(this.getMaxLoops());
		copy.setGrid(this.getGrid());
		copy.setWords(this.getWords());
		copy.setCurrentWordList(this.getCurrentWordList());
		
		copy.clearGrid();
		copy.randomizeAndSort();
		
		boolean exist = false;
		for (Word w : copy.availableWords) {
			for (String tmp : copy.getCurrentWordList()) {
				if (tmp.equals(w.getWord())) 
				{
					exist = true;
					break;
				}
			}
			if (!exist)
			{
				System.out.println("xxxxxxxxxxxxxxxxxxxx");
				copy.fitAndAdd(w);
			}
		}
		
		

		if (copy.currentWordList.size() > this.currentWordList.size()) {
			this.currentWordList.clear();
			this.currentWordList.addAll(copy.currentWordList);
			this.words.clear();
			this.words.addAll(copy.getWords());
			this.grid.clear();
			this.grid.addAll(copy.getGrid());
		}
	}

	
	public void display()
	{
		String solution = "";
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLUMS; col++)
			{
				int index = row * ROWS + col; //TODO index
				
				System.out.print(grid.get(index).getLetter());
			}
			System.out.println();
		}
	}

}
