package de.grytsenko;

public class Main {

    public static void main(String[] args) {

        TextReader text = new TextReader();
        TextReader stopWords = new TextReader();

        stopWords.read("stopWords.txt"); //Read stop words
        text.read("text.txt");			 //Read text

        TextAnalyser analyser = new TextAnalyser();
        analyser.start(text, stopWords);  //Analyze text
        
        Gui gui = new Gui();
        gui.fillTable(analyser.getTableData()); //Fill GUI with data
        gui.setVisible(true);
    }
}
