package view;


import controller.SentenceDetection;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
/**
 * Created by Robert
 */
public class Controller{
	@FXML TextFlow TextOutput;
	@FXML TextFlow InfoOutput;
	@FXML TextArea TextInput;
	private String text = new String();
	private ArrayList<Hyperlink> links = new ArrayList<>();
	
	
	public void okButtonClicked(){
		if(TextInput.getText()!=null && !TextInput.getText().isEmpty()){
			TextOutput.getChildren().clear();
			text = TextInput.getText();
			setOutputText();
			TextInput.clear();
		}else{
			System.out.println("no text in area");
		}
	}
	public void cancleButtonClicked(){
		TextOutput.getChildren().clear();
		TextInput.clear();
		InfoOutput.getChildren().clear();
	}
	
	private void setOutputText(){
		ArrayList<String> tokentxt = SentenceDetection.getTokenizedTxt(text);
		for(String s : tokentxt){
						
			if(s.startsWith("<token>")){//token start found
				final String str=s.replace("<token>", "");
				Hyperlink link = new Hyperlink(str);
		    	link.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						System.out.println("Link with name: [" +str+"] was clicked");
						linkClicked(str);
					}
				});
		    	links.add(link);
				TextOutput.getChildren().add(link);
				TextOutput.getChildren().add(new Text(" "));
				}else{
				TextOutput.getChildren().add(new Text(s+" "));
			}
		}
	}

	
	public void linkClicked(String s) {
		InfoOutput.getChildren().clear();
		InfoOutput.getChildren().addAll(new Text("Link: ["+s+"] clicked"));
	}
	
	}
