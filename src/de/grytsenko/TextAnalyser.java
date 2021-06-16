package de.grytsenko;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TextAnalyser {
	
	private ProperNouns properNouns;
	private TableData tableData;
	private Map<Integer, String> properNounsList;
	private Map<Integer, String> text;
	private Map<Integer, String> stopWords;
	private int numberOfWords;
	
	private void removeStopWords(Map<Integer, String> map) {	
    	Iterator<Entry<Integer,String>> iter = map.entrySet().iterator();
    		while (iter.hasNext()) {
    			Entry<Integer,String> entry = iter.next();
    			if(stopWords.containsValue(entry.getValue().toLowerCase())) {
    				iter.remove();
    			}
    		}
	}
	
    public void start(TextReader text, TextReader stopWords) {
    	initialize(text, stopWords);
    	properNounsList = properNouns.extract(this.text);         //Find proper nouns
    	removeStopWords(properNounsList);                         
    	removeStopWords(this.text);								  //Remove stop words from text
    	this.text.values().removeAll(Collections.singleton(".")); //Remove all entries with "." value.
    	tableData.initCells(this.text, properNounsList);          //Create cells
    }
    
    private void initialize(TextReader text, TextReader stopWords) {
    	numberOfWords = text.getBiggestKey();
    	this.text = text.getWordsList();
    	this.stopWords = stopWords.getWordsList();
    	properNouns = new ProperNouns();
    	tableData = new TableData(numberOfWords);
    }

    public TableData getTableData() {
        return tableData;
    }
}
