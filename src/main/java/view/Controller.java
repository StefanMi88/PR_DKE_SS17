package view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
	
	public void okButtonClicked() throws IOException{
		if(TextInput.getText()!=null && !TextInput.getText().isEmpty()){
			TextOutput.getChildren().clear();
			text = TextInput.getText();
			setOutputText();
			TextInput.clear();
		}else{
			TextInput.setText(Files.lines(Paths.get("Resources/sample.txt")).collect(Collectors.joining("\n")));
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
		
		InfoOutput.getChildren().addAll(new Text(controller.Controller.getMeta(s)));
		}
		
		
		
	}
