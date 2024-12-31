import org.junit.Ignore;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * The GameplayControls class displays the gameplay controls in the settings screen. 
 * It provides information about the keyboard keys associated with various game actions.
 * 
 * @author Rahul Iyer
 * @version 1.0
 * @see README.md
 */

public class GameplayControls extends Application {
    private static Scene previousScene;

    /**
     * The main entry point for the application, which starts the JavaFX stage.
     * It initializes the scene and displays the settings window for gameplay controls.
     * 
     * @param stage The primary stage (window) for the application.
     */
    @Override
    public void start(Stage stage) {

        Scene scene = createScene(stage);
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.show();
    }

    /**
     * Creates and configures the gameplay controls scene.
     * This includes setting the title, displaying the keyboard key images and their labels, 
     * and creating the layout for the settings screen.
     * 
     * @param stage The primary stage (window) for the scene.
     * @return The created scene for the gameplay controls screen.
     */
    
    private Scene createScene(Stage stage) {
        Text page_title = new Text("Gameplay Controls");
        Text S = new Text(" - Statistics");
        Text F = new Text(" - Feed");
        Text B = new Text(" - Sleep");
        Text P = new Text(" - Play");
        Text G = new Text(" - Groom");
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 65);
        Font customFont1 = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 35);

        page_title.setFont(customFont);
        page_title.setTranslateX(200);
        page_title.setTranslateY(50);
        page_title.setFill(Color.web("#ff3131"));

        S.setFont(customFont1);
        S.setTranslateX(350);
        S.setTranslateY(-1195);

        F.setFont(customFont1);
        F.setTranslateX(350);
        F.setTranslateY(-1175);

        B.setFont(customFont1);
        B.setTranslateX(350);
        B.setTranslateY(-1160);

        P.setFont(customFont1);
        P.setTranslateX(350);
        P.setTranslateY(-1140);

        G.setFont(customFont1);
        G.setTranslateX(350);
        G.setTranslateY(-1120);

        ImageView settingsBoard = new ImageView(new Image("Assets/Inventory/setting_menu.png"));
        settingsBoard.setFitHeight(500);
        settingsBoard.setFitWidth(800);
        settingsBoard.setTranslateX(100);
        settingsBoard.setTranslateY(100);

        ImageView S_key = new ImageView(new Image("Assets/Inventory/S_key.png"));
        S_key.setFitHeight(150);
        S_key.setFitWidth(150);
        S_key.setTranslateX(200);
        S_key.setTranslateY(-400);

        ImageView F_key = new ImageView(new Image("Assets/Inventory/F_key.png"));
        F_key.setFitHeight(150);
        F_key.setFitWidth(150);
        F_key.setTranslateX(200);
        F_key.setTranslateY(-490);

        ImageView B_key = new ImageView(new Image("Assets/Inventory/B_key.png"));
        B_key.setFitHeight(150);
        B_key.setFitWidth(150);
        B_key.setTranslateX(200);
        B_key.setTranslateY(-580);

        ImageView P_key = new ImageView(new Image("Assets/Inventory/P_key.png"));
        P_key.setFitHeight(150);
        P_key.setFitWidth(150);
        P_key.setTranslateX(200);
        P_key.setTranslateY(-670);

        ImageView G_key = new ImageView(new Image("Assets/Inventory/G_key.png"));
        G_key.setFitHeight(150);
        G_key.setFitWidth(150);
        G_key.setTranslateX(200);
        G_key.setTranslateY(-760);

        Image closeImage = new Image("Assets/Buttons/closebtn.png");
        ImageView closeIcon = new ImageView(closeImage);
        closeIcon.setFitHeight(50);
        closeIcon.setFitWidth(50);

        Button close = new Button();
        close.setId("tutorialButton");
        close.setTranslateX(900);
        close.setTranslateY(-1750);
        close.setGraphic(closeIcon);
        close.setOnAction(e -> {
            stage.setScene(previousScene);
        });

        VBox layout = new VBox(20);
        layout.setId("background");
        layout.getChildren().addAll(page_title, settingsBoard, S_key, F_key, B_key, P_key, G_key, S, F, B, P, G, close);

        Scene scene = new Scene(layout, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("Styles/SettingsScreen.css").toExternalForm());
        stage.setResizable(false);
        return scene;
    }
    /**
     * Retrieves the scene for the gameplay controls screen.
     * 
     * @param stage The primary stage (window) for the application.
     * @return The created scene for the gameplay controls screen.
     */
    public Scene getScene(Stage stage) {
        return createScene(stage);
    }
    
     /**
     * Sets the previous scene to allow navigation back to it.
     * 
     * @param scene The scene to set as the previous scene.
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
