package view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
	private ArrayList<Hyperlink> infos = new ArrayList<>();
	
	@FXML
	void initialize(){
		InfoOutput.getChildren().add(new Text("InfoOutput: Links from the Textoutput."));
		TextOutput.getChildren().add(new Text("TextOutput: Displays tokenized text."));
	}
	
	public void okButtonClicked(){
		if(TextInput.getText()!=null && !TextInput.getText().isEmpty()){
			TextOutput.getChildren().clear();
			text = TextInput.getText();
			setOutputText();
			TextInput.clear();
		}else{
			TextInput.setText("Hey! This is a testing sentence from Linz! I'm curious to see if it works. Just run the file...\n Robert, Manuel and Steve are working on this project!");
		}
	}
	public void cancleButtonClicked(){
		TextOutput.getChildren().clear();
		TextInput.clear();
		InfoOutput.getChildren().clear();
	}
	
	private void setOutputText(){
		ArrayList<String> tokentxt = controller.Controller.getTokenizedTxt(text);
		for(String s : tokentxt){
						
			if(s.startsWith("<token>")){//token start found
				final String str=s.replace("<token>", "");
				Hyperlink link = new Hyperlink(str);
		    	link.setOnAction(new EventHandler<ActionEvent>() {

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
		ArrayList<String> meta = controller.Controller.getMeta(s);
		for(String ms:meta){
			Hyperlink hyperlink = new Hyperlink(ms);
			infos.add(hyperlink);
			hyperlink.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if(Desktop.isDesktopSupported()){
						try {
							Desktop.getDesktop().browse(new URI(hyperlink.getText()));
						} catch (IOException | URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}}
					
				}
			});
			InfoOutput.getChildren().add(hyperlink);
		}
		
		
		
	}
	
	}
