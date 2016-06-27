package model;

import java.util.ArrayList;

public class Word {
	
	private int number;
	
	private String word;
	private String direction;
	
	ArrayList<Field> letters;
	
	public Word() {
	}

	public Word(int number, String word, String direction) {
		super();
		this.number = number;
		this.word = word;
		this.direction = direction;
		this.letters = new ArrayList<Field>();
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	

	public ArrayList<Field> getLetters() {
		return letters;
	}

	public void setLetters(ArrayList<Field> letters) {
		this.letters = letters;
	}

	public int getNumber() {
		return number;
	}



	public void setNumber(int number) {
		this.number = number;
	}



	public String getDirection() {
		return direction;
	}



	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public void addFiled(Field field)
	{
		if ( letters == null)
			letters = new ArrayList<Field>();
		letters.add(field); 
	}

}
