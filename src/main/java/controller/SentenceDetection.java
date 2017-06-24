package controller;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import opennlp.tools.tokenize.TokenizerME;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            //Loop over all tokens
            //tokens contains all tokens of sentence
            //vocabSpans contains all entities
            
            //if text contains no known entities
            if (vocabSpans.length == 0) {
        		for (String s: tokens) {
        			tokentxt.add(s);
        		}
        	}
            //enter else branch if entities were found
            else {
                int i = 0;
            	while (i < tokens.length) {
            		boolean span = false;
            		for (Span s: vocabSpans) {
            			if (s.getStart() == i) {
            				span = true;
            				//wordCnt > 1
            				if (s.getEnd() - s.getStart() != 1) {
            					String toAdd = "";
            					for (int j = i; j < s.getEnd(); j++) {
            						if (toAdd=="") toAdd = tokens[i];
            						else toAdd = toAdd + " " + tokens[i];
            						i++;
            					}
            					tokentxt.add("<token>" + toAdd);
            				}
            				else {
            					tokentxt.add("<token>" + tokens[i]);
            					i++;
            				}
            			}
            		}
            		if (!span) {
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
    	//tokentxt = removeDoubles(tokentxt); 	
    	for (String s: tokentxt) {
    		System.out.println(s);
    	}
		return tokentxt;
    }

	/*private ArrayList<String> removeDoubles(ArrayList<String> tokentxt) {
		ArrayList<String> newList = new ArrayList<String>();
		for (String s: tokentxt) {
			if (!newList.contains(s)) {
				newList.add(s);
			}
		}
		return newList;
	}*/

	private Span[] checkVocab(Span[] vocabSpans, String[] tokens, TokenNameFinderModel vocabModel) {
		//Retrieve Entities in Sentence
        NameFinderME vocabChecker = new NameFinderME(vocabModel);
        Span[] found = vocabChecker.find(tokens);       
        vocabSpans = ArrayUtils.addAll(vocabSpans, found);
        //clear Data to improve performance
        vocabChecker.clearAdaptiveData();
        return vocabSpans;
	}
}
