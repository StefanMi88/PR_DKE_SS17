package view;



import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {
	@FXML TextFlow TextOutput;
	@FXML TextFlow InfoOutput;
	@FXML TextArea TextInput;
	private String text = new String();
	
	
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
		
	}
	
	private void setOutputText(){
		//TODO: parse links
		TextOutput.getChildren().addAll(new Text(text), new Hyperlink("test"));
		
	}
}
