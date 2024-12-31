import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.Optional;

/**
 * Load Game screen for the game
 * 
 * Lets the user load a saved game from the json save file
 * and continue playing from where they left off
 * Choose save from the list of saved games
 * 
 * 
 * @author William Du
 * @version 1.0
 * @version 1.1 - Added custom font
 * @since 1.0
 * @see README.md
 */
public class LoadGameScreen extends Application {
    private static final String SAVE_FILE_PATH = "savedata/savefile.json";
    private static Scene previousScene;
    Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 15);

    
    /** 
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        Scene scene = createScene(stage);
        stage.setScene(scene);
        stage.setTitle("Load Game");
        stage.show();
    }

    /**
     * Create the Load Game screen
     * 
     * @param stage
     * @return Scene
     */
    private Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Set background image
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("Assets/Background/dark_sky.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        // anchor pane for the close button
        AnchorPane topPane = new AnchorPane();
        root.setTop(topPane);

        // Top - Close Button
        Image closeImage = new Image("Assets/Buttons/closebtn.png");
        ImageView closeBtn = new ImageView(closeImage);
        closeBtn.setFitWidth(30);
        closeBtn.setFitHeight(30);
        Image closeBtnPressed = new Image("Assets/Buttons/closebtn_pressed.png");

        closeBtn.setOnMousePressed(event -> {
            stage.setScene(previousScene);
            closeBtn.setImage(closeBtnPressed);
        });

        closeBtn.setOnMouseReleased(event -> {
            closeBtn.setImage(closeImage);
        });

        // Add to anchor pane
        topPane.getChildren().add(closeBtn);
        AnchorPane.setRightAnchor(closeBtn, 10.0);
        topPane.setPadding(new Insets(10, 10, 10, 10)); // Add 10 pixels of padding on each side

        // Center - Scrollable container for saved games
        VBox savedGamesContainer = new VBox(15);
        savedGamesContainer.setPadding(new Insets(10));
        savedGamesContainer.setAlignment(Pos.TOP_CENTER);
        savedGamesContainer.setStyle("-fx-background-color: rgba(97, 90, 182, .4);");

        // Load saved games from JSON file
        loadSavedGames(savedGamesContainer, stage);

        ScrollPane scrollPane = new ScrollPane(savedGamesContainer);
        scrollPane.setStyle("-fx-background: rgba(0, 0, 0, 0.8);");
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.setCenter(scrollPane);

        // Set up the scene
        Scene scene = new Scene(root, 1000, 800);
        return scene;
    }

    /**
     * Load saved games from the save file and display them in the container
     * 
     * @param container
     * @param stage
     */
    private void loadSavedGames(VBox container, Stage stage) {
        JSONParser parser = new JSONParser();
        try {
            // Parse the save file
            JSONObject saveData = (JSONObject) parser.parse(new FileReader(SAVE_FILE_PATH));
            JSONArray saves = (JSONArray) saveData.get("saves");

            if (saves != null) {
                for (Object obj : saves) {
                    JSONObject save = (JSONObject) obj;

                    long saveId = (long) save.get("save_id");
                    String username = (String) save.get("username");
                    JSONObject petData = (JSONObject) save.get("pet");
                    String petName = (String) petData.get("name");
                    String petType = (String) petData.get("type");
                    JSONObject gameProgress = (JSONObject) save.get("game_progress");
                    long playerLevel = (long) gameProgress.get("level");
                    String lastPlayed = (String) gameProgress.get("last_played");

                    // Create a block for each save game
                    HBox saveBlock = new HBox(15);
                    saveBlock.setPadding(new Insets(10));
                    saveBlock.setAlignment(Pos.CENTER_LEFT);
                    saveBlock.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5;");

                    // Pet Image
                    Image petImage = loadPetImage(petType);
                    ImageView petImageView = new ImageView(petImage);
                    petImageView.setFitWidth(80);
                    petImageView.setFitHeight(80);
                    petImageView.setPreserveRatio(true);

                    // Save details
                    VBox saveDetails = new VBox(5);
                    saveDetails.setAlignment(Pos.CENTER_LEFT);

                    Label usernameLabel = new Label("Username: " + username);
                    Label petNameLabel = new Label("Pet Name: " + petName);
                    Label playerLevelLabel = new Label("Player Level: " + playerLevel);
                    Label lastPlayedLabel = new Label("Last Played: " + lastPlayed);
                    usernameLabel.setFont(customFont);
                    petNameLabel.setFont(customFont);
                    playerLevelLabel.setFont(customFont);
                    lastPlayedLabel.setFont(customFont);

                    saveDetails.getChildren().addAll(usernameLabel, petNameLabel, playerLevelLabel, lastPlayedLabel);

                    // Add pet image and details to the save block
                    saveBlock.getChildren().addAll(petImageView, saveDetails);

                    // Make the entire save block clickable
                    saveBlock.setOnMouseClicked(event -> {
                        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Load Game Confirmation");
                        confirmationAlert.setHeaderText(null);
                        confirmationAlert.setContentText("Do you want to load this game?");

                        Optional<ButtonType> result = confirmationAlert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            GamePlayScreen.setSaveId((int) saveId);
                            GamePlayScreen gamePlayScreen = new GamePlayScreen();
                            try {
                                gamePlayScreen.start(new Stage());
                                stage.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    // Add save block to the container
                    container.getChildren().add(saveBlock);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load save data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load the pet image based on the pet type
     * 
     * @param petType
     * @return Image object of the pet
     */
    private Image loadPetImage(String petType) {
        String imagePath = "Assets/Sprites/" + petType.toLowerCase() + ".gif";
        try {
            return new Image(imagePath);
        } catch (Exception e) {
            System.err.println("Failed to load pet image: " + imagePath);
            return new Image("Assets/Sprites/default.png"); // Default image if pet image not found
        }
    }

    /**
     * return the scene to be displayed
     * 
     * @param stage
     * @return
     */
    public Scene getScene(Stage stage) {
        Scene scene = createScene(stage);
        return scene;
    }

    
    /** 
     * @param scene
     */
    public static void setPreviousScene(Scene scene) {
        previousScene = scene;
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
