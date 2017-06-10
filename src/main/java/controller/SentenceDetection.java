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

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Stefan on 06.05.17.
 * Changed by Robert
 */
public class SentenceDetection {
    //TODO: also return space as token
    public ArrayList<String> getTokenizedTxt(String txt){
    	ArrayList<String> tokentxt = new ArrayList<>();
    	try {
    		//Input -> find Entities
            String sentence;
            if(txt.isEmpty()){
            	sentence = "Hey! This is a testing sentence from Linz! I'm curious to see if it works. Just run the file... It is 10:00am! "
                        + "Robert, Manuel and Steve are working on this project!";
            }else{
            	sentence = txt;
            }
            
            //Tokenize sentence
            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String tokens[] = tokenizer.tokenize(sentence);
            
            //Array of entities
            Span vocabSpans[] = null;
            
            //OpenNLP models
            ArrayList<InputStream> vocabs = new ArrayList<InputStream>();
            vocabs.add(new FileInputStream("Resources/en-ner-person.bin"));
            vocabs.add(new FileInputStream("Resources/en-ner-time.bin"));
            vocabs.add(new FileInputStream("Resources/en-ner-organization.bin"));
            vocabs.add(new FileInputStream("Resources/en-ner-money.bin"));
            vocabs.add(new FileInputStream("Resources/en-ner-location.bin"));
            vocabs.add(new FileInputStream("Resources/en-ner-date.bin"));
            
            for (InputStream iS: vocabs) {
            	TokenNameFinderModel vocabModel = new TokenNameFinderModel(iS);             
                vocabSpans = checkVocab(vocabSpans, tokens, vocabModel);
            }        
            
            boolean done=false;
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

	private Span[] checkVocab(Span[] vocabSpans, String[] tokens, TokenNameFinderModel vocabModel) {
		//Retrieve Entities in Sentence
        NameFinderME vocabChecker = new NameFinderME(vocabModel);
        Span found[] = vocabChecker.find(tokens);
        for (Span i: found) {
        	System.out.println(i.toString());
        }
        vocabSpans = ArrayUtils.addAll(vocabSpans, found); 
        return vocabSpans;
	}
}
