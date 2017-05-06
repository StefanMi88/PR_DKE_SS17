package controller;

import opennlp.tools.namefind.DocumentNameFinder;
import opennlp.tools.util.Span;

//Stefan
public class Ner implements DocumentNameFinder{
    //testing
    String entity = "This is a simple test string. It is just for testing purposes.";

    public Span[][] find(String[][] strings) {
        return new Span[0][];
    }
}
