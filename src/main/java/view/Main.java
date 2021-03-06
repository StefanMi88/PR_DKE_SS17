package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Created by Robert
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Mainfxml.fxml"));
    	primaryStage.setTitle("Semantic Tokenizer");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
        
        
    }
    

    
    
}