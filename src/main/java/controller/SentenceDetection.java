package controller;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import opennlp.tools.tokenize.TokenizerME;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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
            	sentence = Files.lines(Paths.get("Resources/sample.txt")).collect(Collectors.joining("\n"));
            }else{
            	sentence = txt;
            }
            
            //Tokenize sentence
            InputStream modelIn = new FileInputStream("Resources/en-token.bin");
            TokenizerModel model = new TokenizerModel(modelIn);
            TokenizerME tokenizer = new TokenizerME(model);
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
            
            /*boolean done=false;
            for (String s: tokens) {
            	 for(Span i: vocabSpans) {
            		 if(s.equals(tokens[i.getStart()])){
            			 tokentxt.add("<token>" + i.getStart());
            			 done=true;
            		 }
            	 }

            	 if(!done)tokentxt.add(s);
            	 else done=false;
            }
            
            for(Span i: vocabSpans) {
            	tokentxt.add("<token>" + tokens[i.getStart()] + " " + tokens[i.getEnd()-1]);
            }*/
            int i = 0;
            while (i < tokens.length-1) {
            	for (Span s: vocabSpans) {
            		if (i == s.getStart()) {
            			String toAdd = "";
            			for(int j = i; j < s.getEnd(); j++) {
            				if (toAdd != "") toAdd = toAdd + " " + tokens[j];
            				else toAdd = tokens[j];
            				i++;
            			}
            			tokentxt.add("<" + s.getType() + ">" + toAdd);
            			System.out.println("<" + s.getType() + ">" + toAdd);
            		}
            		else {
            			tokentxt.add(tokens[i]);
                    	i++;
            		}
            	}
            }

        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    	//remove double entries from tokentxt
    	tokentxt = removeDoubles(tokentxt); 	
		return tokentxt;
    }

	private ArrayList<String> removeDoubles(ArrayList<String> tokentxt) {
		ArrayList<String> newList = new ArrayList<String>();
		for (String s: tokentxt) {
			if (!newList.contains(s)) {
				newList.add(s);
			}
		}
		return newList;
	}

	private Span[] checkVocab(Span[] vocabSpans, String[] tokens, TokenNameFinderModel vocabModel) {
		//Retrieve Entities in Sentence
        NameFinderME vocabChecker = new NameFinderME(vocabModel);
        Span[] found = vocabChecker.find(tokens);       
        vocabSpans = ArrayUtils.addAll(vocabSpans, found); 
        return vocabSpans;
	}
}
