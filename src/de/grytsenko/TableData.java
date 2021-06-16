package de.grytsenko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TableData {
	
	TableData(int numberOfWords) {
		cells = new ArrayList<Cell>();
		this.numberOfWords = numberOfWords;
		cellTable = new ArrayList<List<Cell>>();
		topFive = new HashMap<String, Integer>();
		topFiveSorted = new String[5];
	}
	
	private List<Cell> cells;
	private List<List<Cell>> cellTable;
	private int numberOfRows;
	private int partSize;                    //Number of words in one part (column) if text have more then 20 000 words
	private final int defaulPartSize = 2000; //Number of words in one part (column) if text have less then 20 000 words
	private final int contextLookup = 4;     //Number of analyzed words left and right from noun 
	final int numberOfWords;
	private int cellIndex;
	private int numberOfMoustRepeatedNoun = 0; //Most frequent noun
	private Map<String, Integer> topFive;
	private String[] topFiveSorted;         //Five most frequent words (sorted)
	final private float maxColumns = 10;    //Maximal number of columns
	
	//Fill cells with data.
	public void initCells(Map<Integer, String> text, Map<Integer, String> nouns) {
		calculatePartSize(numberOfWords);
    	for (Map.Entry<Integer, String> pair : nouns.entrySet()) {
    		cellIndex = getIndexOfCell(pair);
    		if (cells.isEmpty() || cellIndex == -1) {
    			cells.add(new Cell());
    			cellIndex = cells.size()-1;
    			cells.get(cellIndex).setWord(pair.getValue());
    			cells.get(cellIndex).incrementNumberOfWords();
    			cells.get(cellIndex).setColumn(calculateCellColumn(pair.getKey()));
    			
    			addCellContext(text, pair.getKey(), cellIndex, pair.getValue());
    		}
    		else {
    			cells.get(cellIndex).incrementNumberOfWords();
    			addCellContext(text, pair.getKey(), cellIndex, pair.getValue());
    		}
    	}
		
    	makeTable();
		sortInColumns();
		calculateTopFive(text);
		sortTopFive();
	}
	
	//Adds word to cell context (left or right). If word is already present then increments count.
	private void addContextWord(Map<Integer, String> text, int key, int cellIndex, String noun, String s) {
		
		if (s.toLowerCase().equals("r")) {
			if (cells.get(cellIndex).getPostContext().containsKey(text.get(key))) {
				cells.get(cellIndex).getPostContext().put(text.get(key), cells.get(cellIndex).getPostContext().get(text.get(key)) + 1);
			}
			else {
				cells.get(cellIndex).getPostContext().put(text.get(key), 1);
			}
		}
		if (s.toLowerCase().equals("l")) {
			if (cells.get(cellIndex).getPreContext().containsKey(text.get(key))) {
				cells.get(cellIndex).getPreContext().put(text.get(key), cells.get(cellIndex).getPreContext().get(text.get(key)) + 1);
			}
			else {
				cells.get(cellIndex).getPreContext().put(text.get(key), 1);
			}
		}
	}
	
	//Find and add left context words.
	void addPreContext(Map<Integer, String> text, int nounIndex, int cellIndex, String noun) {
		int count = 1;
		int key = nounIndex-1;
		while (count < contextLookup) {
			while (key > 0 && key > (cells.get(cellIndex).getColumn()-1) * partSize && !text.containsKey(key)) { //context only from corresponding parts	
				key--;
			}
			if (text.containsKey(key)) {
				addContextWord(text, key, cellIndex, noun, "l");
				count++;
			}
			key--;
			if (key <= 0 && key <= (cells.get(cellIndex).getColumn()-1) * partSize) return;
		}
	}
	
	//Find and add right context words.
	void addPostContext(Map<Integer, String> text, int nounIndex, int cellIndex, String noun) {
		int count = 1;
		int key = nounIndex+1;
		while (count < contextLookup) {
			while (key < numberOfWords && key < cells.get(cellIndex).getColumn() * partSize && !text.containsKey(key)) { //context only from corresponding parts	
				key++;
			}
			if (text.containsKey(key)) {
				addContextWord(text, key, cellIndex, noun, "r");
				count++;
			}
			key++;
			if (key >= numberOfWords || key >= cells.get(cellIndex).getColumn() * partSize) return;
		}
	}
	
	private void addCellContext(Map<Integer, String> text, int nounIndex, int cellIndex, String noun) {
		addPreContext(text, nounIndex, cellIndex, noun);
		addPostContext(text, nounIndex, cellIndex, noun);
	}
	
	
	//Count words occurrences and get five most frequent words
	private void calculateTopFive(Map<Integer, String> text) {
		Map<String, Integer> words = new HashMap<String, Integer>();

		for (Entry<Integer, String> pair : text.entrySet()) {
			
			if (words.containsKey(pair.getValue())) {
				words.put(pair.getValue(), words.get(pair.getValue()).intValue() + 1);
			}
			else {
				words.put(pair.getValue(), 1);
			}
		}
		
		topFive.put("", 0);
		Entry<String, Integer> min = findTopFiveMin();
		
		for (Entry<String, Integer> pair : words.entrySet()) {
			
			if (topFive.size() < 5) {
		    	topFive.put(pair.getKey(), pair.getValue());
		    }
			else if (pair.getValue() > min.getValue()) {
		        topFive.remove(min.getKey());
		        topFive.put(pair.getKey(), pair.getValue());
		    }
		    min = findTopFiveMin();
		}
	}

	//Finds minimal value.
	private Entry<String, Integer> findTopFiveMin() {
		Entry<String, Integer> m = null;
	    for (Entry<String, Integer> entry : topFive.entrySet()) {
	        if (m == null || m.getValue() > entry.getValue()) {
	            m = entry;
	        }
	    }
		return m;
	}
	
	private void sortTopFive() {
		int size = Math.min(topFiveSorted.length, topFive.size());
		for (int i = 0; i < size; i++) {
			topFiveSorted[i] = Collections.max(topFive.entrySet(), Map.Entry.comparingByValue()).getKey() + " (" + topFive.get(Collections.max(topFive.entrySet(), Map.Entry.comparingByValue()).getKey()) + ")\t\t";
			topFive.remove(Collections.max(topFive.entrySet(), Map.Entry.comparingByValue()).getKey());
		}
	}

	//Sort cells within columns
	private void sortInColumns() {
		for (List<Cell> tableColumn : cellTable) {
			Collections.sort(tableColumn, new Cell().reversed());
		}
		for (List<Cell> tableColumn : cellTable) {
			for (Cell cell : tableColumn) {
				cell.setRow(tableColumn.indexOf(cell)+1);
				if (tableColumn.indexOf(cell)+1 > numberOfRows) {
					numberOfRows = tableColumn.indexOf(cell)+1;
				}
			}
		}
	}
	
	//Fill columns with cells
	private void makeTable() {
		for (int i = 0; i < maxColumns; i++) {
			cellTable.add(new ArrayList<Cell>());
		}
		
		for (Cell cell : cells) {
			cellTable.get(cell.getColumn()-1).add(cell);
			if (cell.getNumberOfWords() > numberOfMoustRepeatedNoun) {
				numberOfMoustRepeatedNoun = cell.getNumberOfWords();
			}
		}
	}

	
	private int calculateCellColumn(Integer key) {
		if (key == 0) return 1;
		return (int)Math.ceil(key / (float)partSize);
	}


	//Returns index of a cell in Cells List
	private int getIndexOfCell(Entry<Integer, String> pair) {
		int res = -1;
		for (Cell cell : cells) {
			if (cell.getWord().equals(pair.getValue()) && cell.getColumn() == calculateCellColumn(pair.getKey())) {
				res = cells.indexOf(cell);
			}
 		}
		return res;
	}

	//PartSize (number of analyzed words per column). PartSize is always > 2000. PartSize will increase if maxNumberOfcolumns (10) will be filled.
	private void calculatePartSize(int fullTextSize) {
		if (fullTextSize > defaulPartSize * maxColumns) {
			partSize = (int)Math.ceil(fullTextSize / maxColumns);
		}
		else {
			partSize = defaulPartSize;
		}
	}

	public List<List<Cell>> getCellTable() {
		return cellTable;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public int getDefaulPartSize() {
		return defaulPartSize;
	}

	public int getNumberOfContextWords() {
		return contextLookup;
	}

	public int getPartSize() {
		return partSize;
	}

	public void setPartSize(int partSize) {
		this.partSize = partSize;
	}
	
	
	public int getNumberOfMoustRepeatedNoun() {
		return numberOfMoustRepeatedNoun;
	}

	public String[] getTopFiveSorted() {
		return topFiveSorted;
	}

	public Map<String, Integer> getTopFive() {
		return topFive;
	}
	
}
