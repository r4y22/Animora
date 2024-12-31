import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.geometry.*;

/**
 * This class represents the "Game Over Tutorial" screen of the Animora
 * game.
 * It provides a tutorial screen displayed when the player fails to take care of
 * their pet.
 * The screen includes a message informing the player of the game over condition
 * and encourages them to try again.
 * 
 * @author Dania Khan
 * @version 1.0
 * @since 1.0
 */
public class GameOverTutorialScreen extends Application {
    private static Scene previousScene;

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
     * Creates the "Game Over Tutorial" screen.
     * 
     * @param stage The primary stage for this application.
     * @return The scene to be displayed on the stage.
     */
    public Scene createScene(Stage stage) {

        // Create and configure labels for page title, subtitle, welcome message, and
        // instructions.
        Label page_title = new Label("Tutorial \n ");
        Label sub_title = new Label("Game Over!");
        Label welcome_msg = new Label("Welcome to the world of Animora!");
        Label howTo_msg = new Label("If you fail to take care of your pet, it dies.");
        Label msg = new Label("Don't worry! Try again to keep your pet happy");
        page_title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Load custom fonts for the labels.
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 65);
        Font customFont2 = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 45);
        Font customFont3 = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 30);

        // Set fonts and configure page title label.
        page_title.setFont(customFont);
        page_title.setTranslateX(0);
        page_title.setTranslateY(-190);

        // Set fonts and configure welcome message label.
        welcome_msg.setFont(customFont2);
        welcome_msg.setTranslateX(0);
        welcome_msg.setTranslateY(-170);

        // Set fonts and configure subtitle label.
        sub_title.setFont(customFont2);
        sub_title.setUnderline(true);
        sub_title.setTranslateX(0);
        sub_title.setTranslateY(-70);

        // Set fonts and configure motivational message label.
        msg.setFont(customFont3);
        msg.setTranslateX(0);
        msg.setTranslateY(200);

        // Set fonts and configure how-to message label.
        howTo_msg.setFont(customFont3);
        howTo_msg.setTranslateX(0);
        howTo_msg.setTranslateY(-30);

        // Create and configure a cancel button to close the stage.
        Image cancelButtonImage = new Image(getClass().getResource("Assets/Buttons/closebtn.png").toExternalForm());
        ImageView cancelButtonView = new ImageView(cancelButtonImage);
        cancelButtonView.setFitWidth(50);
        cancelButtonView.setFitHeight(50);
        Button cancelButton = new Button("");
        cancelButton.setGraphic(cancelButtonView);
        cancelButton.setOnAction(e -> {
            stage.setScene(previousScene);
        });

        // Align the button in the top-right corner
        StackPane.setMargin(cancelButton, new Insets(20, 20, 0, 0));
        StackPane.setAlignment(cancelButton, Pos.TOP_RIGHT);

        // Create and configure an ImageView for the "pet dying" animation.
        ImageView petDying = new ImageView(
                getClass().getResource("Assets/Sprites/dragon_dying.gif").toExternalForm());
        petDying.setPreserveRatio(true);
        petDying.setFitWidth(300);
        petDying.setTranslateX(0);
        petDying.setTranslateY(50);

        // Create and configure an ImageView for the background
        Image background = new Image(getClass().getResource("Assets/Images/box.png").toExternalForm());
        ImageView backgroundView = new ImageView(background);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(1000);
        backgroundView.setFitHeight(450);
        backgroundView.setTranslateX(0);
        backgroundView.setTranslateY(100);

        // Add all components to the root StackPane.
        StackPane root = new StackPane();
        root.getChildren().addAll(page_title, sub_title, welcome_msg, cancelButton, howTo_msg, petDying, msg,
                backgroundView);

        // Set alignment and configuration for components.
        StackPane.setAlignment(backgroundView, Pos.CENTER);
        backgroundView.toBack();

        // Create the scene and apply CSS styling.
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add("Styles/TutorialScreen.css");
        return scene;

    }

    /**
     * Gets the scene for the "Game Over Tutorial" screen.
     * 
     * @param stage The primary stage for this application.
     * @return The scene to be displayed on the stage.
     */
    public Scene getScene(Stage stage) {
        return createScene(stage);
    }

    
    /** 
     * @param scene
     */
    public static void setPreviousScene(Scene scene) {
        previousScene = scene;
    }

    /**
     * The main method to launch the JavaFX application.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}