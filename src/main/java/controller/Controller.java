package controller;

import java.util.ArrayList;

/*
 * Created by Robert
 */

public class Controller {
	
	/**
	 * Search for Metadata for a given token
	 * @param token e.g. (without"") "Donald E. Knuth", "Oxford"
	 * @return String of metadata(already formated?)
	 */
	public static String getMeta(String token){
		//TODO: implement me!
		return new Sparql().lookup(token);
	}
	
	/**
	 * Generates tokens out of a text
	 * special tokens are marked e.g. <token>steve
	 * @param txt
	 * @return Arraylist of tokens
	 */
	public static ArrayList<String> getTokenizedTxt(String txt){
		return new SentenceDetection().getTokenizedTxt(txt);
	}
}
