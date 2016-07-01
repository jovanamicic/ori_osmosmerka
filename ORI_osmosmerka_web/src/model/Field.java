package model;

public class Field {
	
	private int x;
	private int y;
	private int value;
	private char letter;
	
	
	public Field() {
		this.value = -1;
	}
	
	
	public Field(int x, int y, int value, char letter) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
		this.letter = letter;
	}
	public char getLetter() {
		return letter;
	}
	public void setLetter(char letter) {
		this.letter = letter;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}


	@Override
	public boolean equals(Object obj) {
		
		if (! (obj instanceof Field) )
			return false;
		
		Field f = (Field)obj;
		return f.getX() == this.x && f.getY() == this.y;
		
		
	}


	@Override
	public String toString() {
		return "Field [x=" + x + ", y=" + y + ", value=" + value + ", letter=" + letter + "]";
	}
	
	
	
}
