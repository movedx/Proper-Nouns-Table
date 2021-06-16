package de.grytsenko;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextReader {
	
	TextReader() {
		wordsList = new HashMap<Integer, String>();
		sKey = 0;
		numberOfWords = 0;
	}
	
	private Map<Integer, String> wordsList; //Indexed words list
	private Integer sKey;      //Index for words (with special symbols)
	private int numberOfWords; //Total number of words (without special symbols)
	
	
	//Add word without special symbols
	private void addClearWordIfNotEmpty(String str) {
		String tempWord = str.replaceAll("[^A-Za-z0-9'-]","");
		if (!tempWord.replaceAll("('s)|(')", "").isEmpty()) {
			wordsList.put(sKey++, tempWord.replaceAll("('s)|(')", ""));
			numberOfWords++;
		}
	}
	
	//Reads file and puts words and indicator for sentence end (".") in array.
    public void read(String s) {
    	BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(s));

				String line;
			
				while ((line = in.readLine()) != null) {
					String[] words = line.trim().split("\\s+");
					for (String str : words) {
						String st = str.replaceAll("[\"]", "");
						if (st.isEmpty()) continue;
						char last = st.charAt(st.length()-1);
						if (last == '.' || last == '?' || last == '!') { //check for sentence end
							addClearWordIfNotEmpty(str);
							wordsList.put(sKey++, ".");
							continue;
						}
						addClearWordIfNotEmpty(str);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    public Map<Integer, String>  getWordsList() {
        return wordsList;
    }

	public int getNumberOfWords() {
		return numberOfWords;
	}

	public int getBiggestKey() {
		return sKey;
	}
    
}
