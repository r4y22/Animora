import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Main menu screen for the game
 * 
 * @author Dania Khan
 * @author William Du
 * @author Rahul Iyer
 * @version 1.0
 * @since 1.0
 * @see README.md
 */
public class MainMenuScreen extends Application {
    private static Scene previousScene;

    
    /** 
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Create the main menu scene
        Scene mainMenuScene = createScene(stage);
        // Set and show the initial scene
        stage.setScene(mainMenuScene);
        stage.show();
    }

    /**
     * Create the main menu scene
     * 
     * 
     * @param stage
     * @return Scene
     */
    private Scene createScene(Stage stage) {
        // Create the main BorderPane layout
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 30);

        // First BorderPane for the logo
        BorderPane logoPane = new BorderPane();
        Image logoImage = new Image("Assets/Images/logo.gif");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(400);
        logoView.setPreserveRatio(true);

        // Add logo to the top of logoPane
        StackPane logoContainer = new StackPane(logoView);
        logoContainer.setAlignment(Pos.TOP_CENTER);
        logoPane.setTop(logoContainer);

        // Second BorderPane for buttons and footer
        BorderPane mainPane = new BorderPane();

        VBox centerVBox = new VBox(5); // Reduce vertical spacing between elements to bring them closer
        centerVBox.setAlignment(Pos.CENTER); // Align everything at the top

        Image buttonBackground = new Image("Assets/Buttons/rectanglebtn.png");

        // Buttons
        Button newGame = createButtonWithImage("New Game", buttonBackground, customFont);
        Button loadGame = createButtonWithImage("Load Game", buttonBackground, customFont);
        Button settings = createButtonWithImage("Settings", buttonBackground, customFont);
        Button tutorial = createButtonWithImage("Tutorial", buttonBackground, customFont);
        Button exitGame = createButtonWithImage("Exit Game", buttonBackground, customFont);

        // Add buttons to VBox
        centerVBox.getChildren().addAll(newGame, loadGame, settings, tutorial, exitGame);

        // Add VBox to center of BorderPane
        mainPane.setCenter(centerVBox);

        // Footer Section
        Text creators = new Text("Created by Team 2: William, Rahul, Daniel, Dania, and Bilgunn");
        creators.setFont(customFont);
        creators.setFill(Color.WHITE);
        creators.setStroke(Color.BLACK);

        Text projectDetails = new Text("CS2212 FALL 2024 PROJECT AT WESTERN UNIVERSITY");
        projectDetails.setFont(customFont);
        projectDetails.setFill(Color.WHITE);
        projectDetails.setStroke(Color.BLACK);

        // Use a VBox to stack footer texts
        VBox footerContainer = new VBox(5); // 5px spacing between elements
        footerContainer.setAlignment(Pos.CENTER);
        footerContainer.getChildren().addAll(creators, projectDetails);

        // Add padding to move the footer higher
        footerContainer.setPadding(new javafx.geometry.Insets(20, 0, 20, 0)); // Top, Right, Bottom, Left padding
        // Add the VBox to the bottom of the BorderPane
        mainPane.setBottom(footerContainer);

        VBox root = new VBox(10);
        root.getChildren().addAll(logoPane, mainPane);

        // Set button actions to switch scenes
        newGame.setOnAction(e -> {
            Stage popupStage = new Stage();
            popupStage.setTitle("Choose Username");
            popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main menu
        
            // Custom Font (use the same font as in your main menu)
            Font customFont1 = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 20);
        
            // Label for the prompt
            Label prompt = new Label("Enter your username:");
            prompt.setFont(customFont1);
            prompt.setTextFill(Color.WHITE);
            prompt.setStyle("-fx-font-size: 16px;");
        
            // TextField for username input
            TextField usernameInput = new TextField();
            usernameInput.setPromptText("Username");
            usernameInput.setStyle("-fx-font-size: 16px;");
        
            // Confirm Button
            Button confirmButton = new Button("Confirm");
            confirmButton.setFont(customFont1);
            confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        
            // Layout for the popup
            VBox layout = new VBox(15, prompt, usernameInput, confirmButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #282c34;");
        
            Scene popupScene = new Scene(layout, 350, 200);
            popupStage.setScene(popupScene);
        
            // Button Action
            confirmButton.setOnAction(event -> {
                String username = usernameInput.getText().trim();
                if (!username.isEmpty()) {
                    // Call the method to add the new save entry with the entered username
                    NewGame.addNewSave(username);
        
                    // Transition to ChoosePetScreen
                    ChoosePetScreen choosePetScreen = new ChoosePetScreen();
                    Stage petStage = new Stage();
                    try {
                        choosePetScreen.start(petStage); // Open ChoosePetScreen
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
        
                    // Close the popup and main menu stages
                    popupStage.close();
                    stage.close();  // Assuming 'stage' is the main menu stage
                } else {
                    // Show error for empty input
                    prompt.setText("Username cannot be empty!");
                    prompt.setTextFill(Color.RED);
                }
            });
        
            popupStage.showAndWait(); // Block until popup is closed
        });

        loadGame.setOnAction(e -> {
            Scene loadGameScene = new LoadGameScreen().getScene(stage);
            LoadGameScreen.setPreviousScene(stage.getScene());
            stage.setScene(loadGameScene);
        });
        settings.setOnAction(e -> {
            Scene settingsScene = new SettingsScreen().getScene(stage);
            SettingsScreen.setPreviousScene(stage.getScene());
            stage.setScene(settingsScene);

        });
        tutorial.setOnAction(e -> {
            Scene tutorialScene = new ChoosePetTutorialScreen().getScene(stage);
            ChoosePetTutorialScreen.setPreviousScene(stage.getScene());
            TakeCareTutorialScreen.setPreviousScene(stage.getScene());
            ViewStatsTutorialScreen.setPreviousScene(stage.getScene());
            SaveGameTutorialScreen.setPreviousScene(stage.getScene());
            LoadGameTutorialScreen.setPreviousScene(stage.getScene());
            GameOverTutorialScreen.setPreviousScene(stage.getScene());
            stage.setScene(tutorialScene);
        });
        exitGame.setOnAction(e -> stage.close());

        Scene mainMenuScene = new Scene(root, 1000, 800);
        mainMenuScene.getStylesheets().add(getClass().getResource("Styles/MenuScreen.css").toExternalForm());

        return mainMenuScene;
    }

    /**
     * Create a button with an image background
     * 
     * 
     * @param buttonText
     * @param backgroundImage
     * @param font
     * @return Button
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
     * Get the main menu scene
     * 
     * @param stage
     * @return
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
     * @param args[]
     */
    public static void main(String args[]) {
        launch(args);
    }
}
