package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.sql.rowset.RowSetWarning;

public class WordSearchGenerator {

	public static final int COLUMS = 12;
	public static final int ROWS = 12;

	private int maxLoops;

	private ArrayList<Word> availableWords;

	private ArrayList<String> currentWordList;

	private ArrayList<Word> words;

	private ArrayList<Field> grid;

	private static String difficult = "";

	public WordSearchGenerator() {

		this.availableWords = new ArrayList<Word>();
		this.currentWordList = new ArrayList<String>();
		this.words = new ArrayList<Word>();
		this.grid = new ArrayList<Field>();
		randomizeAndSort();

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

		Collections.shuffle(temp);

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

		while (!fit && (count < this.maxLoops)) {
			// slucaj kada ubacujemo prvu rec u tablu
			if (this.currentWordList.size() == 0) {
				// TODO stanja

				String direction = "";

				double random = Math.random();

				if (random >= 0 && random < 2.5)
					direction = "west";
				else if (random >= 2.5 && random < 5)
					direction = "south";
				else if (random >= 5 && random < 7.5)
					direction = "north";
				else
					direction = "east";

				direction = "east";

				int row = 5, col = 1;

				// if (this.checkValue(col, row, direction, word) != 0)
				if (this.checkValue(col - 1, row - 1, direction, word) != 0) {
					fit = true;
					setWord(col, row, direction, word);
				}

			}
			// slucaj kada ubacujemo dodatne reci u tablu (ne prvu)
			else {
				String coord = "";
				String tokens[];

				int col;
				int row;
				String direction;

				try {
					coord = coordList.get(count);

					tokens = coord.split(",");

					col = Integer.parseInt(tokens[0]);
					row = Integer.parseInt(tokens[1]);
					direction = tokens[2];

				} catch (Exception e) {
					return;
				}

				if (Integer.parseInt(tokens[3]) != 0) {
					fit = true;
					col++;
					row++;
					setWord(col, row, direction, word);
				}
			}
			count++;
		}
	}
	
	
	public void fitAndAdd2(Word word) {
		boolean fit = false;
		int count = 0; // koliko se puta preklapa

		ArrayList<String> coordList = new ArrayList<>();
		coordList.addAll(suggestCoordinates2(word));

		while (!fit && (count < this.maxLoops)) {
			// slucaj kada ubacujemo prvu rec u tablu
			if (this.currentWordList.size() == 0) {
				// TODO stanja

				String direction = "";

				double random = Math.random();

				if (random >= 0 && random < 2.5)
					direction = "west";
				else if (random >= 2.5 && random < 5)
					direction = "south";
				else if (random >= 5 && random < 7.5)
					direction = "north";
				else
					direction = "east";

				direction = "east";

				int row = 5, col = 1;

				// if (this.checkValue(col, row, direction, word) != 0)
				if (this.checkValue(col - 1, row - 1, direction, word) != 0) {
					fit = true;
					setWord(col, row, direction, word);
				}

			}
			// slucaj kada ubacujemo dodatne reci u tablu (ne prvu)
			else {
				String coord = "";
				String tokens[];

				int col;
				int row;
				String direction;

				try {
					coord = coordList.get(count);

					tokens = coord.split(",");

					col = Integer.parseInt(tokens[0]);
					row = Integer.parseInt(tokens[1]);
					direction = tokens[2];

				} catch (Exception e) {
					return;
				}

				if (Integer.parseInt(tokens[3]) != 0) {
					fit = true;
					col++;
					row++;
					setWord(col, row, direction, word);
				}
			}
			count++;
		}
	}


	private void setWord(int col, int row, String direction, Word word) {
		col--;
		row--;

		word.setDirection(direction);

		
		currentWordList.add(word.getWord());

		if (direction.contains("-")) {
			System.out.println("REC: " + word.getWord());
			System.out.println("PRAVAC " + direction);
		}

		for (char letter : word.getWord().toCharArray()) {
			int index = row * ROWS + col;

			setCell(col, row, letter);

			// TODO smerovi

			if (direction.contains("south")) // ide na dole
				row++;
			if (direction.contains("north")) // ide na gore
				row--;
			if (direction.contains("east")) // ide na desno
				col++;
			if (direction.contains("west")) // ide na levo
				col--;

			word.addFiled(grid.get(index));
		}

	}

	public ArrayList<String> suggestCoordinates(Word word) {
		ArrayList<String> coordList = new ArrayList<String>();
		ArrayList<String> newCoordList = new ArrayList<String>();

		for (int i = 0; i < word.getWord().length(); i++) {

			char letter = word.getWord().charAt(i);

			for (int row = 0; row < ROWS; row++) {
				for (int cell = 0; cell < COLUMS; cell++) {
					if ((getCell(cell, row)).getLetter() == letter) 
					{
						// TODO: ostala stanja

						// proba south, na dole
						if (row - i >= 0) // da ne izadjemo van mreze
						{
							if (row - i + word.getWord().length() < ROWS) { // da
																			// li
																			// moze
																			// da
																			// stane
																			// vertikalno
								String coords = cell + "," + (row - i) + ",south,0";
								coordList.add(coords);
							}
						}

						// proba north, na gore
						if (row + i < ROWS && !difficult.equalsIgnoreCase("Easy")) // da
																					// ne
																					// izadjemo
																					// van
																					// mreze
						{
							if (row + i - word.getWord().length() >= 0) { // da
																			// li
																			// moze
																			// da
																			// stane
																			// vertikalno
								String coords = cell + "," + (row + i) + ",north,0";
								coordList.add(coords);
							}
						}

						// proba east, na desno
						if (cell - i >= 0) // da nije izasao na levu stranu
							if (cell - i + word.getWord().length() < COLUMS) { // da
																				// nije
																				// izasao
																				// na
																				// desnu
								String coords = (cell - i) + "," + (row) + ",east,0";
								coordList.add(coords);
							}

						// proba west, na levo
						if (cell + i < COLUMS && !difficult.equalsIgnoreCase("Easy")) {
							if (cell + i - word.getWord().length() >= 0) {
								String coords = (cell + i) + "," + (row) + ",west,0";
								coordList.add(coords);
							}
						}

						// proba north-east, gore desno
						if (row + i < ROWS && cell - i >= 0 && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row + i - word.getWord().length() > 0 && cell - i + word.getWord().length() <= COLUMS) { // da
																															// li
																															// moze
																															// da
																															// stane
																															// vertikalno
								String coords = (cell - i) + "," + (row + i) + ",north-east,0";
								coordList.add(coords);
							}
						}

						// proba north-west, na gore levo
						if (row + i < ROWS && cell + i < COLUMS && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row + i - word.getWord().length() > 0 && cell + i - word.getWord().length() > 0) { // da
																													// li
																													// moze
																													// da
																													// stane
																													// vertikalno
								String coords = (cell + i) + "," + (row + i) + ",north-west,0";
								coordList.add(coords);
							}
						}

						// proba south-east, dole desno
						if (row - i > 0 && cell - i > 0 && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row - i + word.getWord().length() < ROWS
									&& cell - i + word.getWord().length() < COLUMS) { // da
																						// li
																						// moze
																						// da
																						// stane
																						// vertikalno
								String coords = (cell - i) + "," + (row - i) + ",south-east,0";
								coordList.add(coords);
							}
						}

						// proba south-west, dole levo
						if (row - i > 0 && cell + i < COLUMS && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row - i + word.getWord().length() < ROWS && cell + i - word.getWord().length() > 0) { // da
																														// li
																														// moze
																														// da
																														// stane
																														// vertikalno
								String coords = (cell + i) + "," + (row - i) + ",south-west,0";
								coordList.add(coords);
							}
						}

					}
				}

			}

		}

		newCoordList.addAll(coordList);

		newCoordList = this.sortCoordList(newCoordList, word);

		return newCoordList;
	}
	
	public ArrayList<String> suggestCoordinates2(Word word) {
		ArrayList<String> coordList = new ArrayList<String>();
		ArrayList<String> newCoordList = new ArrayList<String>();

		for (int i = 0; i < word.getWord().length(); i++) {

			char letter = word.getWord().charAt(i);

			for (int row = 0; row < ROWS; row++) {
				for (int cell = 0; cell < COLUMS; cell++) {
					//if ((getCell(cell, row)).getLetter() == letter) 
					{
						// TODO: ostala stanja

						// proba south, na dole
						if (row - i >= 0) // da ne izadjemo van mreze
						{
							if (row - i + word.getWord().length() < ROWS) { // da
																			// li
																			// moze
																			// da
																			// stane
																			// vertikalno
								String coords = cell + "," + (row - i) + ",south,0";
								coordList.add(coords);
							}
						}

						// proba north, na gore
						if (row + i < ROWS && !difficult.equalsIgnoreCase("Easy")) // da
																					// ne
																					// izadjemo
																					// van
																					// mreze
						{
							if (row + i - word.getWord().length() >= 0) { // da
																			// li
																			// moze
																			// da
																			// stane
																			// vertikalno
								String coords = cell + "," + (row + i) + ",north,0";
								coordList.add(coords);
							}
						}

						// proba east, na desno
						if (cell - i >= 0) // da nije izasao na levu stranu
							if (cell - i + word.getWord().length() < COLUMS) { // da
																				// nije
																				// izasao
																				// na
																				// desnu
								String coords = (cell - i) + "," + (row) + ",east,0";
								coordList.add(coords);
							}

						// proba west, na levo
						if (cell + i < COLUMS && !difficult.equalsIgnoreCase("Easy")) {
							if (cell + i - word.getWord().length() >= 0) {
								String coords = (cell + i) + "," + (row) + ",west,0";
								coordList.add(coords);
							}
						}

						// proba north-east, gore desno
						if (row + i < ROWS && cell - i >= 0 && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row + i - word.getWord().length() > 0 && cell - i + word.getWord().length() <= COLUMS) { // da
																															// li
																															// moze
																															// da
																															// stane
																															// vertikalno
								String coords = (cell - i) + "," + (row + i) + ",north-east,0";
								coordList.add(coords);
							}
						}

						// proba north-west, na gore levo
						if (row + i < ROWS && cell + i < COLUMS && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row + i - word.getWord().length() > 0 && cell + i - word.getWord().length() > 0) { // da
																													// li
																													// moze
																													// da
																													// stane
																													// vertikalno
								String coords = (cell + i) + "," + (row + i) + ",north-west,0";
								coordList.add(coords);
							}
						}

						// proba south-east, dole desno
						if (row - i > 0 && cell - i > 0 && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row - i + word.getWord().length() < ROWS
									&& cell - i + word.getWord().length() < COLUMS) { // da
																						// li
																						// moze
																						// da
																						// stane
																						// vertikalno
								String coords = (cell - i) + "," + (row - i) + ",south-east,0";
								coordList.add(coords);
							}
						}

						// proba south-west, dole levo
						if (row - i > 0 && cell + i < COLUMS && !difficult.equalsIgnoreCase("Easy")
								&& !difficult.equalsIgnoreCase("Medium")) // da
																			// ne
																			// izadjemo
																			// van
																			// mreze
						{
							if (row - i + word.getWord().length() < ROWS && cell + i - word.getWord().length() > 0) { // da
																														// li
																														// moze
																														// da
																														// stane
																														// vertikalno
								String coords = (cell + i) + "," + (row - i) + ",south-west,0";
								coordList.add(coords);
							}
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
			coord += "," + value;

			newCoordList.add(coord);

		}

		Collections.shuffle(newCoordList);

		// sortiraj po value
		Collections.sort(newCoordList, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				// Integer val1 = new Integer(s1.split(",")[3]);
				// Integer val2 = new Integer(s2.split(",")[3]);
				Integer val1 = (s1.split(",")[2]).length();
				Integer val2 = (s2.split(",")[2]).length();
				return val2.compareTo(val1);
			}
		});

		for (String s : newCoordList)
			if (s.contains("north-east")) {
				newCoordList.set(0, s);
				break;
			}

		return newCoordList;
	}

	// heuristicka funkcija, racuna nam vrednost polja
	// vratice 0 ako ne moze da se uklopi, 1 je ako se uklapa, a 2+ ako se i
	// preklapa
	private int checkValue(int col, int row, String direction, Word word) {

		int count = 1; // koje je slovo trenutno
		int value = 1; // vrednost polja

		for (char letter : word.getWord().toCharArray()) {
			Field activeCell;
			try {
				activeCell = this.getCell(col, row);
			} catch (Exception e) {
				return 0;

			}

			if (activeCell.getLetter() != '-' && activeCell.getLetter() != letter)
				return 0;

			if (activeCell.getLetter() == letter)
				value++;

			// TODO: stanja
			// proverava da li postoji vec upisana neka rec
			// ako postoji ne moze da je upise

			if (direction.equals("north")) // ka gore
			{
				if ((activeCell.getLetter() != (letter))) // proveri da li se
															// ukrstaju
				{
					if (col < (COLUMS - 1) && !checkIfFieldClear(col + 1, row)) // proveri
																				// desno
																				// polje
					{
						return 0;
					}
					if ((col > 0) && !checkIfFieldClear(col - 1, row)) // proveri
																		// levo
																		// polje
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// ispod
				{
					if (row < (ROWS - 1) && !checkIfFieldClear(col, row + 1)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec iznad
				{
					if (row > 0 && !checkIfFieldClear(col, row - 1)) {
						return 0;
					}
				}

				row--;
			}

			else if (direction.equals("south")) {
				if ((activeCell.getLetter() != (letter))) // proveri da li se
															// ukrstaju
				{
					if (col < (COLUMS - 1) && !checkIfFieldClear(col + 1, row)) // proveri
																				// desno
																				// polje
					{
						return 0;
					}
					if ((col > 0) && !checkIfFieldClear(col - 1, row)) // proveri
																		// levo
																		// polje
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// iznad
				{
					if (row > 0 && !checkIfFieldClear(col, row - 1)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec ispod
				{
					if (row < (ROWS - 1) && !checkIfFieldClear(col, row + 1)) {
						return 0;
					}
				}

				row++;
			}

			else if (direction.equals("east")) {
				if (activeCell.getLetter() != (letter)) // proveri da li se
														// ukrstaju
				{
					if (row > 0 && !checkIfFieldClear(col, row - 1)) // proveri
																		// polje
																		// iznad
					{
						return 0;
					}
					if (row < (ROWS - 1) && !checkIfFieldClear(col, row + 1)) // proveri
																				// polje
																				// ispod
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// levo
				{
					if (col > 0 && !checkIfFieldClear(col - 1, row)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec desno
				{
					if (col < (COLUMS - 1) && !checkIfFieldClear(col + 1, row)) {
						return 0;
					}
				}

				col++;
			}

			else if (direction.equals("west")) {
				if (activeCell.getLetter() != (letter)) // proveri da li se
														// ukrstaju
				{
					if (row > 0 && !checkIfFieldClear(col, row - 1)) // proveri
																		// polje
																		// iznad
					{
						return 0;
					}
					if (row < (ROWS - 1) && !checkIfFieldClear(col, row + 1)) // proveri
																				// polje
																				// ispod
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// desno
				{
					if (col < (COLUMS - 1) && !checkIfFieldClear(col + 1, row)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec levo
				{
					if (col > 0 && !checkIfFieldClear(col - 1, row)) {
						return 0;
					}
				}

				col--;
			}

			else if (direction.equals("north-east")) // ka gore
			{
				if ((activeCell.getLetter() != (letter))) // proveri da li se
															// ukrstaju
				{
					if (col < (COLUMS - 1) && row > 0 && !checkIfFieldClear(col + 1, row - 1)) // proveri
																								// desno
																								// gore
					{
						return 0;
					}
					if ((col > 0) && row < (ROWS - 1) && !checkIfFieldClear(col - 1, row + 1)) // proveri
																								// levo
																								// dole
					{
						return 0;
					}
				}

				if (count == 1) {
					if (row < (ROWS - 1) && col > 0 && !checkIfFieldClear(col - 1, row + 1)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) {
					if (row > 0 && col < (COLUMS - 1) && !checkIfFieldClear(col + 1, row - 1)) {
						return 0;
					}
				}

				row--;
				col++;
			}

			else if (direction.equals("north-west")) // gore levo
			{
				if ((activeCell.getLetter() != (letter))) // proveri da li se
															// ukrstaju
				{
					if (col < (COLUMS - 1) && row > 0 && !checkIfFieldClear(col + 1, row - 1)) // proveri
																								// desno
																								// polje
					{
						return 0;
					}
					if ((col > 0) && row < ROWS - 1 && !checkIfFieldClear(col - 1, row + 1)) // proveri
																								// levo
																								// polje
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// ispod
				{
					if (row < (ROWS - 1) && col < COLUMS - 1 && !checkIfFieldClear(col + 1, row + 1)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec iznad
				{
					if (row > 0 && col > 0 && !checkIfFieldClear(col - 1, row - 1)) {
						return 0;
					}
				}

				row--;
				col--;
			}

			else if (direction.equals("south-east")) {
				if ((activeCell.getLetter() != (letter))) // proveri da li se
															// ukrstaju
				{
					if (col < (COLUMS - 1) && row > 0 && !checkIfFieldClear(col + 1, row - 1)) // proveri
																								// desno
																								// polje
					{
						return 0;
					}
					if ((col > 0) && row < ROWS - 1 && !checkIfFieldClear(col - 1, row + 1)) // proveri
																								// levo
																								// polje
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// iznad
				{
					if (row > 0 && col > 0 && !checkIfFieldClear(col - 1, row - 1)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec ispod
				{
					if (row < (ROWS - 1) && col < COLUMS - 1 && !checkIfFieldClear(col + 1, row + 1)) {
						return 0;
					}
				}

				row++;
				col++;
			}

			else if (direction.equals("south-west")) {
				if ((activeCell.getLetter() != (letter))) // proveri da li se
															// ukrstaju
				{
					if (col < (COLUMS - 1) && row > 0 && !checkIfFieldClear(col + 1, row - 1)) // proveri
																								// desno
																								// polje
					{
						return 0;
					}
					if ((col > 0) && row < ROWS - 1 && !checkIfFieldClear(col - 1, row + 1)) // proveri
																								// levo
																								// polje
					{
						return 0;
					}
				}

				if (count == 1) // ako je prvo slovo, proveri da li ima neka rec
								// iznad
				{
					if (row > 0 && col < COLUMS - 1 && !checkIfFieldClear(col + 1, row - 1)) {
						return 0;
					}
				}

				if (count == word.getWord().length()) // ako je poslednje slovo,
														// proveri da li ima
														// neka rec ispod
				{
					if (row < (ROWS - 1) && col > 0 && !checkIfFieldClear(col - 1, row + 1)) {
						return 0;
					}
				}

				row++;
				col--;
			}

			count++;
		}
		return value;

	}

	public boolean checkIfFieldClear(int col, int row) {
		Field f = this.getCell(col, row);

		if (f.getLetter() == '-')
			return true;

		return false;
	}

	public Field getCell(int col, int row) {
		int index = row * ROWS + col;

		return grid.get(index);
	}

	public void setCell(int col, int row, char c) {
		int index = row * ROWS + col;

		Field field = grid.get(index);
		field.setLetter(c);
		grid.set(index, field);
	}

	public void generate(String difficultStr) throws ClassNotFoundException, IOException {
		difficult = difficultStr;

		WordSearchGenerator copy = new WordSearchGenerator();
		copy.setAvailableWords(this.getAvailableWords());
		copy.setCurrentWordList(this.getCurrentWordList());
		copy.setMaxLoops(this.getMaxLoops());
		copy.setGrid(this.getGrid());
		copy.setWords(this.getWords());
		copy.setCurrentWordList(this.getCurrentWordList());

		copy.clearGrid();
		copy.randomizeAndSort();

		// provera da li se u tabli nalazi 12 reci
		// ako da, prekini dalju pretragu
		for (Word w : copy.availableWords) {
			if (copy.currentWordList.size() > 11) {
				break;
			} else {
				if (!copy.currentWordList.contains(w.getWord())) {
					copy.fitAndAdd(w);
				}
			}
		}
		
		int iter = 0;
		int prevListSize = -1;
		while (copy.currentWordList.size() < 12 && iter < 5) {
			if (copy.currentWordList.size() == prevListSize)
				break;
			for (Word w : copy.availableWords) {
				if (copy.currentWordList.size() > 11) {
					break;
				} else {
					if (!copy.currentWordList.contains(w.getWord())) {
						copy.fitAndAdd2(w);
					}
				}
			}
			iter++;
			prevListSize = copy.currentWordList.size();
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

	public void display() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMS; col++) {
				int index = row * ROWS + col;

				System.out.print(grid.get(index).getLetter());
			}
			System.out.println();
		}

	}

	public void randomLetters() {
		for (int i = 0; i < grid.size(); i++) {
			if (grid.get(i).getLetter() == '-') {
				Random r = new Random();
				char c = (char) (r.nextInt(26) + 'A');
				grid.get(i).setLetter(c);
			}

		}
	}

}
