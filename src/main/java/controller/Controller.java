package controller;

import java.io.IOException;
import java.util.ArrayList;

import model.Model;

/*
 * Created by Robert
 */

public class Controller {
	
	/**
	 * Search for Metadata for a given token
	 * @param token e.g. (without"") "Donald E. Knuth", "Oxford"
	 * @return String list of metadata(already formated?)
	 * @throws IOException 
	 */
	public static String getMeta(String token){
		String res= model.Model.getMeta(token);
		if(res == null||res.isEmpty()){
			res=new Sparql().lookup(token);
		if (res != null && !res.toString().isEmpty())	model.Model.setmeta(token, res);
		}
			
        if (res.toString().isEmpty() || res == null) return "Sorry - No additional information was found " + token + "!";

		return res;
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
