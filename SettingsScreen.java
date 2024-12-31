import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Label;

/**
 * Settings Screen where users will have different aspects of the game they would like to see
 * @author Rahul Iyer
 * @version 1.0
 * @see README.md
 */

public class SettingsScreen extends Application {
    private static Scene previousScene;

    /**
     * This method is called when the JavaFX application starts.
     * It sets up the stage with the settings screen scene, disables window resizing,
     * sets the title of the window, and then displays the window to the user.
     *
     * @param stage The primary stage (window) of the application.
     */
    @Override
    public void start(Stage stage) {

        Scene scene = createScene(stage);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.show();
    }

    /**
     * Create the settings screen
     * 
     * @param stage
     * @return Scene
     */
    private Scene createScene(Stage stage) {
        Text page_title = new Text("Settings");
        Text tutorial_title = new Text("Tutorial");
        Text gameplay_title = new Text("Gameplay Controls");
        Text parental_title = new Text("Parental Controls");
        Text main_menu = new Text("Exit to Main Menu");
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 65);
        Font customFont2 = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 20);
        page_title.setFont(customFont);
        page_title.setTranslateX(350);
        page_title.setTranslateY(50);
        page_title.setFill(Color.web("#ff3131"));

        tutorial_title.setFont(customFont2);
        tutorial_title.setTranslateX(620);
        tutorial_title.setTranslateY(-880);

        gameplay_title.setFont(customFont2);
        gameplay_title.setTranslateX(573);
        gameplay_title.setTranslateY(-810);

        parental_title.setFont(customFont2);
        parental_title.setTranslateX(575);
        parental_title.setTranslateY(-745);

        main_menu.setFont(customFont2);
        main_menu.setTranslateX(420);
        main_menu.setTranslateY(-810);

        ImageView settingsBoard = new ImageView(new Image("Assets/Inventory/setting_menu.png"));
        settingsBoard.setFitHeight(500);
        settingsBoard.setFitWidth(800);
        settingsBoard.setTranslateX(100);
        settingsBoard.setTranslateY(100);

        Image volume = new Image("Assets/Buttons/volumebtn.png");
        Image mute = new Image("Assets/Buttons/mutebtn.png");
        ImageView volumeIcon = new ImageView(volume);
        ImageView muteIcon = new ImageView(mute);
        volumeIcon.setFitHeight(70);
        volumeIcon.setFitWidth(70);
        muteIcon.setFitHeight(70);
        muteIcon.setFitWidth(70);

        Button volumeButton = new Button();
        volumeButton.setId("volumeButton");
        volumeButton.setGraphic(volumeIcon);
        volumeButton.setTranslateX(300);
        volumeButton.setTranslateY(-300);

        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setMaxWidth(100);
        volumeSlider.setTranslateX(290);
        volumeSlider.setTranslateY(-315);
        volumeSlider.setId("volumeSlider");
        volumeSlider.setVisible(false);

        Label volumeLabel = new Label("Volume: 50%");
        volumeLabel.setTranslateX(290);
        volumeLabel.setTranslateY(-340);
        volumeLabel.setFont(customFont2);
        volumeLabel.setTextFill(Color.WHITE);
        volumeLabel.setVisible(false);

        Button muteButton = new Button();
        muteButton.setId("volumeButton");
        muteButton.setGraphic(muteIcon);
        muteButton.setTranslateX(300);
        muteButton.setTranslateY(-350);

        Image tutorialButton = new Image("Assets/Buttons/rectanglebtn.png");
        ImageView tutorialIcon = new ImageView(tutorialButton);
        tutorialIcon.setFitHeight(70);
        tutorialIcon.setFitWidth(220);

        Button tutorialButton1 = new Button();
        tutorialButton1.setId("tutorialButton");
        tutorialButton1.setGraphic(tutorialIcon);
        tutorialButton1.setTranslateX(550);
        tutorialButton1.setTranslateY(-600);

        Image gameplayButton = new Image("Assets/Buttons/rectanglebtn.png");
        ImageView gameplayIcon = new ImageView(gameplayButton);
        gameplayIcon.setFitHeight(70);
        gameplayIcon.setFitWidth(220);

        Button gameplayButton1 = new Button();
        gameplayButton1.setId("tutorialButton");
        gameplayButton1.setGraphic(gameplayIcon);
        gameplayButton1.setTranslateX(550);
        gameplayButton1.setTranslateY(-590);

        Image parentalButton = new Image("Assets/Buttons/rectanglebtn.png");
        ImageView parentalIcon = new ImageView(parentalButton);
        parentalIcon.setFitHeight(70);
        parentalIcon.setFitWidth(220);

        Button parentalButton1 = new Button();
        parentalButton1.setId("tutorialButton");
        parentalButton1.setGraphic(parentalIcon);
        parentalButton1.setTranslateX(550);
        parentalButton1.setTranslateY(-580);

        Image mainMenuButton = new Image("Assets/Buttons/rectanglebtn.png");
        ImageView mainMenuIcon = new ImageView(mainMenuButton);
        mainMenuIcon.setFitHeight(90);
        mainMenuIcon.setFitWidth(240);

        Button mainMenuButton1 = new Button();
        mainMenuButton1.setId("tutorialButton");
        mainMenuButton1.setGraphic(mainMenuIcon);
        mainMenuButton1.setTranslateX(380);
        mainMenuButton1.setTranslateY(-720);

        Image closeImage = new Image("Assets/Buttons/closebtn.png");
        ImageView closeIcon = new ImageView(closeImage);
        closeIcon.setFitHeight(50);
        closeIcon.setFitWidth(50);

        Button close = new Button();
        close.setId("tutorialButton");
        close.setTranslateX(900);
        close.setTranslateY(-1450);
        close.setGraphic(closeIcon);
        close.setOnAction(e -> {
           stage.setScene(previousScene);
        });
        //Action handler for Tutorials Button Screen
        tutorialButton1.setOnAction(e -> {
            try {
                ChoosePetTutorialScreen tutorialScreen = new ChoosePetTutorialScreen();
                ChoosePetTutorialScreen.setPreviousScene(stage.getScene()); // Set previous scene before switching
                TakeCareTutorialScreen.setPreviousScene(stage.getScene());
                ViewStatsTutorialScreen.setPreviousScene(stage.getScene());
                SaveGameTutorialScreen.setPreviousScene(stage.getScene());
                LoadGameTutorialScreen.setPreviousScene(stage.getScene());
                GameOverTutorialScreen.setPreviousScene(stage.getScene());
                stage.setScene(tutorialScreen.getScene(stage)); // Use setScene instead of a new Stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
        //Action handler for Gameplay Controls button screen
        gameplayButton1.setOnAction(e -> {

            try {
                GameplayControls gameplay = new GameplayControls();
                GameplayControls.setPreviousScene(stage.getScene());
                stage.setScene(gameplay.getScene(stage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
        //Action handler for Parental Controls Screen
        parentalButton1.setOnAction(e -> {
            try {
                ParentalControlScreen parentalControl = new ParentalControlScreen();
                ParentalControlScreen.setPreviousScene(stage.getScene());
                stage.setScene(parentalControl.getScene(stage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        //Action handler for Main Menu Screen Button
        mainMenuButton1.setOnAction(e -> {
            stage.close();
            MainMenuScreen mainMenu = new MainMenuScreen();
            Stage mainMenuStage = new Stage();
            try {
                mainMenu.start(mainMenuStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        //Action handler for Volume slider to be visible
        volumeButton.setOnAction(e -> {
            boolean isVisible = volumeSlider.isVisible();
            volumeSlider.setVisible(!isVisible);
            volumeLabel.setVisible(!isVisible);
        });

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int volumeSaved = (int) volumeSlider.getValue();
            volumeLabel.setText("Volume: " + (int) volumeSlider.getValue() + "%");
            saveVolume(volumeSaved);

        });
        //Action handler for Mute Button to set volume to 0%
        muteButton.setOnAction(e -> {
            volumeSlider.setValue(0);
            volumeLabel.setText("Volume: 0%");
            saveVolume(0);

        });

        VBox layout = new VBox(20);
        layout.setId("background");
        layout.getChildren().addAll(page_title, settingsBoard, volumeButton, volumeSlider, volumeLabel, muteButton,
                tutorialButton1, gameplayButton1, parentalButton1,
                tutorial_title, gameplay_title, parental_title, mainMenuButton1, main_menu, close);

        Scene scene = new Scene(layout, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("Styles/SettingsScreen.css").toExternalForm());
        return scene;
    }
    /**
     * Saves the current volume setting to a file in JSON format.
     * The volume value is stored in a JSON object and written to a file named "volume.json".
     * 
     * @param volume The volume level to be saved (as an integer).
     */
    private void saveVolume(int volume) {
        JSONObject obj = new JSONObject();
        obj.put("volume", volume);

        try (FileWriter file = new FileWriter("volume.json")) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves the scene associated with the settings screen.
     * 
     * @param stage The primary stage (window) for which the scene is to be created.
     * @return The created scene for the settings screen.
     */
    public Scene getScene(Stage stage) {
        return createScene(stage);
    }

    /**
     * Sets the previous scene, typically used to navigate back to a prior screen.
     * 
     * @param scene The scene to be set as the previous scene.
     */
    public static void setPreviousScene(Scene scene) {
        previousScene = scene;
    }

    /**
     * The main method to launch the JavaFX application.
     * It initializes and starts the application by calling the launch() method.
     * 
     * @param args Command-line arguments passed during application startup (not used).
     */
    public static void main(String[] args) {
        launch();
    }
}
