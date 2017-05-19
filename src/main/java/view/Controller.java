package view;



import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {
	@FXML TextFlow TextOutput;
	@FXML TextFlow InfoOutput;
	@FXML TextArea TextInput;
	private String text = new String();
	
	
	public void okButtonClicked(){
		System.out.println("TEst");
		if(TextInput.getText()!=null && !TextInput.getText().isEmpty()){
			TextOutput.getChildren().clear();
			text = TextInput.getText();
			TextOutput.getChildren().add(getOutputText());
			TextInput.clear();
		}else{
			System.out.println("no text in area");
		}
	}
	
	private Text getOutputText(){
		//TODO: parse links
		return new Text(text);
		
	}
}
