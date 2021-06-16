package de.grytsenko;

import java.util.HashMap;
import java.util.Map;

public class ProperNouns {
	
	ProperNouns() {
		properNounsList = new HashMap<Integer, String>();
		potentialNouns = new HashMap<Integer, String>();
	}
	
	Map<Integer, String> properNounsList;
	Map<Integer, String> potentialNouns; //Words that placed at first place in sentence

	private boolean checkHonorifics(Map<Integer, String> text, int index) {
		String str = text.get(index);
		return (str.equals("Mr") || str.equals("Ms") || str.equals("Mr") || str.equals("Mx") || 
				str.equals("Sir") || str.equals("Mrs") || str.equals("Dr") || str.equals("Prof"));
	}
		
	private boolean isFirstLatterCapital(Map<Integer, String> text, int index) {
		return Character.isUpperCase(text.get(index).charAt(0));
	}
	
	public Map<Integer, String> extract(Map<Integer, String> text) {
		
		//Make set of potential proper nouns
		if(!text.isEmpty() && isFirstLatterCapital(text, 0)) potentialNouns.put(0, text.get(0));
		
		//Make list of proper nouns (Words that are not first in sentence and have Capital first latter).
		for (int i = 1; i < text.size(); i++) {
			if (checkHonorifics(text, i)) {
				continue;
			}
			if (i >= 2 && checkHonorifics(text, i-2) && isFirstLatterCapital(text, i)) {
				properNounsList.put(i, text.get(i));
				continue;
			}
			if (isFirstLatterCapital(text, i) && text.get(i-1) != ".") {
				properNounsList.put(i, text.get(i));
				continue;
			}
			if (isFirstLatterCapital(text, i) && text.get(i-1) != "." && isFirstLatterCapital(text, i-1)) { //add both if two in a row words with first capital latter
				properNounsList.put(i-1, text.get(i-1));
				continue;
			}
			if (isFirstLatterCapital(text, i) && text.get(i-1) == ".") { //Make set of potential proper nouns (Words that are first in sentence)
				potentialNouns.put(i, text.get(i));
			}
		}
		
		//If potential proper noun present in proper nouns list then potential proper noun is real proper noun.
		for (Map.Entry<Integer, String> pair : potentialNouns.entrySet()) {
			if (properNounsList.containsValue(pair.getValue())) {
				properNounsList.put(pair.getKey(), pair.getValue());
			}
		}
		return properNounsList;
	}
	
}
