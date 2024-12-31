import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * This file displays the game over screen after the user's pet dies.
 * The game over screen has three buttons: new game button, home button, and a
 * view stats button.
 * 
 * 
 * @author Dania Khan
 * @author Bilguun Purevjav
 * @author William Du
 * @author Rahul Iyer
 * 
 * @version 1.2
 * @since 1.0
 */
public class GameOverScreen extends Application {

    /**
     * The main entry point for the JavaFX application.
     * 
     * @param stage The primary stage for this application, on which the GUI
     *              components are displayed.
     */
    @Override
    public void start(Stage stage) {
        // Set up the stage and show it.
        Scene scene = createScene(stage);
        stage.setScene(scene);
        stage.show();
    }

    
    /** 
     * @param stage
     * @return Scene
     */
    private Scene createScene(Stage stage) {

        // Root layout
        VBox root = new VBox(50); // 50px vertical spacing
        root.setAlignment(Pos.CENTER); // Center everything
        root.setPadding(new Insets(50, 0, 0, 0)); // Add padding to move buttons down slightly
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 30);

        Font customFont1 = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 65);

        // Load the image for the buttons
        Image buttonBackground = new Image("Assets/Buttons/rectanglebtn.png");
        BorderPane borderPane = new BorderPane();

        Text title = new Text("Game Over");
        title.setFill(Color.RED);
        title.setFont(customFont1);
        borderPane.setTop(title);
        BorderPane.setAlignment(title, javafx.geometry.Pos.CENTER);

        // Buttons
        Button homeButton = createButtonWithImage("Home", buttonBackground, customFont);
       

        homeButton.setOnAction(e -> {
            // Close the current stage
            stage.close();
        
            // Open the main menu in a new stage
            MainMenuScreen mainMenu = new MainMenuScreen();
            Stage mainMenuStage = new Stage();
            try {
                mainMenu.start(mainMenuStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(title, homeButton);
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("Styles/GameOverScreen.css").toExternalForm());

        stage.setTitle("Game Over Screen");
        return scene;

    }

    
    /** 
     * @param stage
     * @return Scene
     */
    public Scene getScene(Stage stage) {
        return createScene(stage);
    }

    /**
     * This method creates a button with an image as the background and text on top
     * of it.
     * 
     * 
     * 
     * @param buttonText
     * @param backgroundImage
     * @param font
     * @return
     */
    private Button createButtonWithImage(String buttonText, Image backgroundImage, Font font) {
        // Create an ImageView for the button background
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(300); // Set button width
        imageView.setFitHeight(100); // Set button height

        // Create the button text
        Text text = new Text(buttonText);
        text.setFont(font);

        // Stack the text over the image
        StackPane stack = new StackPane();
        stack.getChildren().addAll(imageView, text);

        // Create the button and set its graphic
        Button button = new Button();
        button.setGraphic(stack);

        return button;
    }

    
    /** 
     * @param args[]
     */
    public static void main(String args[]) {
        launch(args);

    }

}