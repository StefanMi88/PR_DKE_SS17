package controller;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stefan on 06.05.17.
 * Changed by Robert
 */
public class SentenceDetection {
    //TODO: also return space as token
    public ArrayList<String> getTokenizedTxt(String txt){
    	ArrayList<String> tokentxt = new ArrayList<>();
    	try {
            InputStream inputStream = new FileInputStream("Resources/en-sent.bin");
            InputStream vocab = new FileInputStream("Resources/en-ner-person.bin");
            TokenNameFinderModel vocabModel = new TokenNameFinderModel(vocab);
            SentenceModel model = new SentenceModel(inputStream);
            String sentence;
            if(txt.isEmpty()){
            	sentence = "Hey! This is a testing sentence from Linz! I'm curious to see if it works. Just run the file... "
                        + "Robert, Manuel and Steve are working on this project!";
            }else{
            	sentence = txt;
            }
            

            //Needed for splitting sentence
            SentenceDetectorME detector = new SentenceDetectorME(model);

            //Detect single sentences in sentence String
            String[] splitString = detector.sentDetect(sentence);

            //Detect beginning and ending of sentences in sentence
            Span spans[] = detector.sentPosDetect(sentence);

            //Tokenize sentence
            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String tokens[] = tokenizer.tokenize(sentence);

            //Retrieve Entities in Sentence
            NameFinderME vocabChecker = new NameFinderME(vocabModel);
            Span vocabSpans[] = vocabChecker.find(tokens);
            
            

            //Combine splitString and spans to create output
            String[] combined = new String[spans.length];
            for (int i = 0; i < spans.length; i++) {
                combined[i] = splitString[i] + " from: " + spans[i];
            }
            for (String string: combined) {
                System.out.println(string);
            }
            
            
            boolean done=false;
            //System.out.println("\nFollowing tokens were found in the given sentence: ");
            for (String s: tokens) {
            	 for(Span i: vocabSpans) {
            		 if(s.equals(tokens[i.getStart()])){
            			 //System.out.println(i.toString());
            			 //System.out.println(s);
            			 tokentxt.add("<token>" + s);
            			 
            			 done=true;
            		 }
            	 }
            	 if(!done)tokentxt.add(s);
            	 else done=false;
            }
            


        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }
		
		return tokentxt;
    }
}
