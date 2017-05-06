package controller;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

import java.io.*;

/**
 * Created by Stefan on 06.05.17.
 */
public class SentenceDetection {
    public static void main(String args[]) {
        try {
            InputStream inputStream = new FileInputStream("Ressources/en-sent.bin");
            SentenceModel model = new SentenceModel(inputStream);

            String sentence = "Hey! This is a testing sentence! I'm curious to see if it works. Just run the file...";

            //Needed for splitting sentence
            SentenceDetectorME detector = new SentenceDetectorME(model);

            //Detect single sentences in sentence String
            String[] splitString = detector.sentDetect(sentence);

            //Detect beginning and ending of sentences in sentence
            Span spans[] = detector.sentPosDetect(sentence);

            //Combine splitString and spans to create output
            String[] combined = new String[spans.length];
            for (int i = 0; i < spans.length; i++) {
                combined[i] = splitString[i] + " from: " + spans[i];
            }
            for (String string: combined) {
                System.out.println(string);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
