import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.geometry.*;

/**
 * This class represents the "Save Game Tutorial" screen of the Animora game.
 * It provides a tutorial for users on how to save their progress in the game.
 * The screen includes various instructional elements, images, and navigation
 * buttons.
 * 
 * @author Dania Khan
 * @version 1.0
 * @since 1.0
 */
public class SaveGameTutorialScreen extends Application {
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
     * Creates the "Save Game Tutorial" screen.
     * 
     * @param stage The primary stage for this application.
     * @return The scene to be displayed on the stage.
     */
    public Scene createScene(Stage stage) {
        // Create and configure labels for page title, subtitle, welcome message, and
        // instructions.
        Label page_title = new Label("Tutorial \n ");
        Label sub_title = new Label("Save Game");
        Label welcome_msg = new Label("Welcome to the world of Animora!");
        Label howTo_msg = new Label("Make sure to save your progress");
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

        Image nextButtonImage = new Image(getClass().getResource("Assets/Buttons/NextButton.png").toExternalForm());
        ImageView nextButtonView = new ImageView(nextButtonImage);
        nextButtonView.setFitWidth(200);
        nextButtonView.setFitHeight(200);
        Button nextButton = new Button("");
        nextButton.setTranslateX(400);
        nextButton.setTranslateY(100);
        nextButton.setGraphic(nextButtonView);

        nextButton.setOnAction(event -> {
            try {
                Scene loadGameTutorialScreen = new LoadGameTutorialScreen().getScene(stage);
                stage.setScene(loadGameTutorialScreen);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        // Create and configure an ImageView for the "Save Game Tutorial" GIF.
        ImageView saveGame = new ImageView(
                getClass().getResource("Assets/Images/saveGameTut.gif").toExternalForm());
        saveGame.setPreserveRatio(true);
        saveGame.setFitWidth(400);
        saveGame.setTranslateX(0);
        saveGame.setTranslateY(130);

        // Create and configure an ImageView for the pop-up background.
        Image background = new Image(getClass().getResource("Assets/Images/box.png").toExternalForm());
        ImageView backgroundView = new ImageView(background);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(1000); // Adjust size
        backgroundView.setFitHeight(450);
        backgroundView.setTranslateX(0);
        backgroundView.setTranslateY(100);

        // Add all components to the root StackPane.
        StackPane root = new StackPane();
        root.getChildren().addAll(page_title, sub_title, welcome_msg, cancelButton, howTo_msg, nextButton, saveGame,
                backgroundView);
        StackPane.setAlignment(backgroundView, Pos.CENTER);
        backgroundView.toBack();

        // Create the scene and apply CSS styling.
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add("Styles/TutorialScreen.css");
        return scene;

    }

    /**
     * Gets the scene for the "Save Game Tutorial" screen.
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