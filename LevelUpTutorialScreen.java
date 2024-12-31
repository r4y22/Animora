import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.geometry.*;

public class LevelUpTutorialScreen extends Application {

    
    /** 
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        Label page_title = new Label("Tutorial \n ");
        Label sub_title = new Label("Level up");
        Label welcome_msg = new Label("Welcome to the world of Animora!");
        Label howTo_msg = new Label("Take care of your pet to help them grow stronger as they age");
        page_title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 65);
        Font customFont2 = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 40);
        Font customFont3 = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 20);

        page_title.setFont(customFont);
        if (customFont != null) {
            page_title.setFont(customFont);
            System.out.println("Font loaded.");

        } else {
            System.out.println("Font not loaded.");
        }

        page_title.setTranslateX(0);
        page_title.setTranslateY(-190);

        sub_title.setFont(customFont2);
        welcome_msg.setFont(customFont2);
        if (customFont2 != null) {
            sub_title.setFont(customFont2);
            System.out.println("Font loaded.");

        } else {
            System.out.println("Font not loaded.");
        }

        welcome_msg.setTranslateX(0);
        welcome_msg.setTranslateY(-170);
        sub_title.setUnderline(true);
        sub_title.setTranslateX(0);
        sub_title.setTranslateY(-100);

        howTo_msg.setFont(customFont3);
        if (customFont != null) {
            howTo_msg.setFont(customFont3);
            System.out.println("Font loaded.");

        } else {
            System.out.println("Font not loaded.");
        }

        howTo_msg.setTranslateX(0);
        howTo_msg.setTranslateY(-70);

        Button cancelButton = new Button("EXIT");
        cancelButton.setOnAction(e -> stage.close());
        // Align the button in the top-right corner
        StackPane.setMargin(cancelButton, new Insets(20, 20, 0, 0)); // Top, Right, Bottom, Left
        StackPane.setAlignment(cancelButton, Pos.TOP_RIGHT);

        // Center-align the page title

        StackPane root = new StackPane();
        root.getChildren().addAll(page_title, sub_title, welcome_msg, cancelButton, howTo_msg);
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add("Styles/TutorialScreen.css"); // Path to your CSS file
        stage.setScene(scene);
        stage.show();
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}