package de.grytsenko;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Cell implements Comparator<Cell> {
	
	Cell() {
		preContext = new HashMap<String, Integer>();
		postContext = new HashMap<String, Integer>();
	}

	private String word;                        //Store proper Noun
	private int numberOfWords;                  //Number of occurrences
	private Map<String, Integer> preContext;    //Left context words
	private Map<String, Integer>  postContext;  //Right context words
	private int row;                            //Row in which cells should be placed
	private int column;							//Column in which cells should be placed

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public void incrementNumberOfWords() {
		numberOfWords++;
	}

	public int getNumberOfWords() {
		return numberOfWords;
	}

	public void setNumberOfWords(int numberOfWords) {
		this.numberOfWords = numberOfWords;
	}

	public Map<String, Integer> getPreContext() {
		return preContext;
	}

	public void setPreContext(Map<String, Integer> preContext) {
		this.preContext = preContext;
	}

	public Map<String, Integer> getPostContext() {
		return postContext;
	}

	public void setPostContext(Map<String, Integer> postContext) {
		this.postContext = postContext;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public String getContextString() {
		
		//Sort context words
		Object[] pre = preContext.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) 
        .limit(5).toArray();
		
		Object[] post = postContext.entrySet().stream()
		        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) 
		        .limit(5).toArray();
		
		//Prepare string to be printed
		String context = new String("<html> Before: &nbsp;&nbsp;");
		
		for (Object b : pre) {
			context += b.toString().replace("=", " (");
			context += ");&nbsp;&nbsp;";
		}
		
		context += "<br><br> After: &nbsp;&nbsp;";
		for (Object a : post) {
			context += a.toString().replace("=", " (");
			context += ");&nbsp;&nbsp;";
		}
		
		context += "</html>";
		return context;
	}

	//For sorting cells within columns
	@Override
	public int compare(Cell o1, Cell o2) {
		if (o1.getNumberOfWords() == o2.getNumberOfWords()) return 0;
		return o1.getNumberOfWords() > o2.getNumberOfWords() ? 1 : -1;
	}
}
