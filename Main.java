import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class Main extends Application {

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    
    /** 
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        // Start from the main menu
        MainMenuScreen mainScreen = new MainMenuScreen();

        // Set up the scene with the main menu screen
        Scene scene = mainScreen.getScene(primaryStage);

        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }
}
