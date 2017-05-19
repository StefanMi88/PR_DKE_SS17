package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
 
@SuppressWarnings("restriction")
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Mainfxml.fxml"));
    	primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
    
    
}