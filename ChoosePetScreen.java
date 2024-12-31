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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Label;

/**
 * ChoosePetScreen is a JavaFX application where users can choose their desired pet and input a name for it.
 * The screen provides visual representations for each pet, difficulty stats, and an option to proceed to the next step after selecting a pet.
 * 
 * The screen allows the user to:
 * - Select a pet from three available options: Phoenix, Pegasus, and Dragon.
 * - View the difficulty stats associated with each pet (hunger, grooming, fun, sleep).
 * - Input a name for the selected pet before proceeding to the next game screen.
 * 
 * The pet's selection and name are saved into a save file.
 * 
 * @author Rahul Iyer
 * @version 1.2 Added buttons and improved UI.
 * @since 1.0
 * @see README.md
 */

public class ChoosePetScreen extends Application {

    private String chosenPet;
    private static final String SAVE_FILE_PATH = "savedata/savefile.json";

    /**
     * Updates the pet's details in the save file by replacing the old pet's name and type with the new ones.
     * 
     * @param saveID The ID of the save to be updated.
     * @param petType The new type of the pet (e.g., "phoenix", "pegasus", "dragon").
     * @param petName The new name of the pet.
     */
    private void updatePet(int saveID, String petType, String petName){
        JSONParser parser = new JSONParser();

        try(FileReader reader = new FileReader(SAVE_FILE_PATH)){
                JSONObject saveData = (JSONObject) parser.parse(reader);
                JSONArray saves = (JSONArray) saveData.get("saves");

                if(saves == null){
                        System.out.println("No 'saves' array found");
                        return;
                }
                boolean updated = false;

                for(Object saveObj : saves){
                        JSONObject save = (JSONObject) saveObj;
                        long currentSaveID = (long) save.get("save_id");

                        if (currentSaveID == saveID) {
                                JSONObject pet = (JSONObject) save.get("pet");
                                pet.put("name", petName);
                                pet.put("type", petType);
                                updated = true;
                                break;
                            }
                        }
            
                        if (updated) {
                            try (FileWriter writer = new FileWriter(SAVE_FILE_PATH)) {
                                writer.write(saveData.toJSONString());
                                System.out.println("Successfully updated pet for save_id " + saveID);
                            }
                        } else {
                            System.out.println("No save found with save_id " + saveID);
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
    }
    
    /**
     * Starts the ChoosePetScreen, setting up the UI components, displaying pet options, and handling pet selection.
     * This method sets up the pet icons, difficulty stats, and buttons for interaction.
     * 
     * @param stage The primary stage for this scene.
     */
    @Override
    public void start(Stage stage) {
        Text page_title = new Text("Choose Your Pet");
        Text phoenix_label = new Text("Phoenix");
        Text pegasus_label = new Text("Pegasus");
        Text dragon_label = new Text("Dragon");
        Text difficulty_label = new Text("Difficulty");
//---------------------------------Difficulty Stats Display----------------------------------------------
        Text phoenix_hunger = new Text("2/10");
        Text phoenix_grooming = new Text("8/10");
        Text phoenix_fun = new Text("8/10");
        Text phoenix_sleep = new Text("6/10");

        Text pegasus_hunger = new Text("4/10");
        Text pegasus_grooming = new Text("9/10");
        Text pegasus_fun = new Text("5/10");
        Text pegasus_sleep = new Text("6/10");

        Text dragon_hunger = new Text("9/10");
        Text dragon_grooming = new Text("5/10");
        Text dragon_fun = new Text("3/10");
        Text dragon_sleep = new Text("6/10");

        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 65);
        Font customFont2 = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 40);
        Font customFont3 = Font.loadFont(getClass().getResourceAsStream("Assets/Fonts/PixelifySans-SemiBold.ttf"), 20);

//---------------------------------Pet Selection Buttons----------------------------------------------
        Image phoenixDefaultButton = new Image(getClass().getResource("Assets/Buttons/DefaultButton.png").toExternalForm());
        Image phoenixSelectButton = new Image(getClass().getResource("Assets/Buttons/SelectButton.png").toExternalForm());
        ImageView phoenixButtonView = new ImageView(phoenixDefaultButton);

        Image pegasusDefaultButton = new Image(getClass().getResource("Assets/Buttons/DefaultButton.png").toExternalForm());
        Image pegasusSelectButton = new Image(getClass().getResource("Assets/Buttons/SelectButton.png").toExternalForm());
        ImageView pegasusButtonView = new ImageView(pegasusDefaultButton);

        Image dragonDefaultButton = new Image(getClass().getResource("Assets/Buttons/DefaultButton.png").toExternalForm());
        Image dragonSelectButton = new Image(getClass().getResource("Assets/Buttons/SelectButton.png").toExternalForm());
        ImageView dragonButtonView = new ImageView(dragonDefaultButton);

        phoenixButtonView.setFitWidth(100);
        phoenixButtonView.setFitHeight(100);

        pegasusButtonView.setFitWidth(100);
        pegasusButtonView.setFitHeight(100);

        dragonButtonView.setFitWidth(100);
        dragonButtonView.setFitHeight(100);

        RadioButton phoenixButton = new RadioButton();
        phoenixButton.setGraphic(phoenixButtonView);

        RadioButton pegasusButton = new RadioButton();
        pegasusButton.setGraphic(pegasusButtonView);

        RadioButton dragonButton = new RadioButton();
        dragonButton.setGraphic(dragonButtonView);

        ToggleGroup group = new ToggleGroup();
        phoenixButton.setToggleGroup(group);
        pegasusButton.setToggleGroup(group);
        dragonButton.setToggleGroup(group);

        phoenixButton.setTranslateX(130);
        phoenixButton.setTranslateY(-3650);

        pegasusButton.setTranslateX(430);
        pegasusButton.setTranslateY(-3770);

        dragonButton.setTranslateX(730);
        dragonButton.setTranslateY(-3890);
        
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (phoenixButton.isSelected()) {
                phoenixButtonView.setImage(phoenixSelectButton);
                pegasusButtonView.setImage(pegasusDefaultButton);
                dragonButtonView.setImage(dragonDefaultButton);
            } else if (pegasusButton.isSelected()) {
                phoenixButtonView.setImage(phoenixDefaultButton);
                pegasusButtonView.setImage(pegasusSelectButton);
                dragonButtonView.setImage(dragonDefaultButton);
            } else if (dragonButton.isSelected()) {
                phoenixButtonView.setImage(phoenixDefaultButton);
                pegasusButtonView.setImage(pegasusDefaultButton);
                dragonButtonView.setImage(dragonSelectButton);
            }
        });

// ---------------------------------Next Button Display---------------------------------------------------
        Image nextButtonImage = new Image(getClass().getResource("Assets/Buttons/NextButton.png").toExternalForm());
        ImageView nextButtonView = new ImageView(nextButtonImage);
        nextButtonView.setFitWidth(200);
        nextButtonView.setFitHeight(200);

        Button nextButton = new Button();
        nextButton.setGraphic(nextButtonView);
        nextButton.setTranslateX(790);
        nextButton.setTranslateY(-4250);
        nextButton.setVisible(false);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        if (phoenixButton.isSelected() || pegasusButton.isSelected() || dragonButton.isSelected()) {
                nextButton.setVisible(true);
        } else {
                nextButton.setVisible(false);
        }
        });
        // Action handler for the "Next" button
        nextButton.setOnAction(event -> {
                System.out.println("Next Button Clicked");
                
                if (phoenixButton.isSelected()) {
                        System.out.println("Phoenix Selected");
                        chosenPet = "phoenix";
                } else if (pegasusButton.isSelected()) {
                        System.out.println("Pegasus Selected");
                        chosenPet = "pegasus";
                } else if (dragonButton.isSelected()) {
                        System.out.println("Dragon Selected");
                        chosenPet = "dragon";
                }
                else {
                        System.out.println("No pet is selected");
                        return;
                }


                Stage popupStage = new Stage();
                popupStage.setTitle("Name Your Pet");
                popupStage.initModality(Modality.APPLICATION_MODAL); 

                Label prompt = new Label("Enter a name for your " + chosenPet + ":");
                prompt.setFont(customFont3);
                prompt.setTextFill(Color.WHITE);
                prompt.setStyle("-fx-font-size: 12px;");

                TextField nameInput = new TextField();
                nameInput.setPromptText("Pet name");
                nameInput.setStyle("-fx-font-size: 20px;");

                Image confirmButtonImage = new Image(getClass().getResource("Assets/Buttons/ConfirmButton.png").toExternalForm());
                ImageView confirmButtonView = new ImageView(confirmButtonImage);
                confirmButtonView.setFitWidth(100);
                confirmButtonView.setFitHeight(100);

                Button confirmButton = new Button();
                confirmButton.setGraphic(confirmButtonView);
                confirmButton.setStyle("-fx-background-color: transparent;");
                
                // Confirm button action to set pet's name and update save file
                confirmButton.setOnAction(e -> {
                        String petName = nameInput.getText().trim();
                        if (!petName.isEmpty()) {
                                
                                int saveID =  NewGame.getSaveID();
                                updatePet(saveID, chosenPet, petName);
                                GamePlayScreen.setSaveId(saveID);

                                GamePlayScreen gamePlayScreen = new GamePlayScreen();
                                
                                try{
                                        gamePlayScreen.start(new Stage());
                                              
                                }
                                catch (Exception ex){
                                        ex.printStackTrace();
                                }
                                popupStage.close();
                                stage.close();
                        } 
                        else {
                                System.out.println("Pet name cannot be empty!");
                        }
                        
                });
             
                VBox layout = new VBox(10, prompt, nameInput, confirmButton);
                layout.setStyle("-fx-background-color: #282c34; -fx-padding: 20px; -fx-alignment: center;");
                popupStage.setScene(new Scene(layout, 300, 250));
                popupStage.showAndWait();
});

// ---------------------------------Font Loading and Display----------------------------------------------
        page_title.setFont(customFont);
        if (customFont != null) {
            page_title.setFont(customFont);
            System.out.println("Font loaded.");

        } else {
            System.out.println("Font not loaded.");
        }
        page_title.setTranslateX(230);
        page_title.setTranslateY(50);
        page_title.setFill(Color.web("#ff3131"));
        page_title.setStroke(Color.BLACK);
        page_title.setStrokeWidth(1.5);

        phoenix_label.setFont(customFont2);
        phoenix_label.setTranslateX(120);
        phoenix_label.setTranslateY(-950);
        phoenix_label.setFill(Color.web("#ff3131"));
        phoenix_label.setStroke(Color.BLACK);
        phoenix_label.setStrokeWidth(1.5);

        pegasus_label.setFont(customFont2);
        pegasus_label.setTranslateX(420);
        pegasus_label.setTranslateY(-1017);
        pegasus_label.setFill(Color.web("#ff3131"));
        pegasus_label.setStroke(Color.BLACK);
        pegasus_label.setStrokeWidth(1.5);

        dragon_label.setFont(customFont2);
        dragon_label.setTranslateX(730);
        dragon_label.setTranslateY(-1085);
        dragon_label.setFill(Color.web("#ff3131"));
        dragon_label.setStroke(Color.BLACK);
        dragon_label.setStrokeWidth(1.5);
      
//---------------------------------Pet Icons Display----------------------------------------------
        ImageView phoenix = new ImageView(new Image(getClass().getResource("Assets/PetIcons/PhoenixIcon.gif").toExternalForm()));
        phoenix.setPreserveRatio(true);
        phoenix.setFitWidth(400);
        phoenix.setTranslateX(0);
        phoenix.setTranslateY(60);

        ImageView pegasus = new ImageView(new Image(getClass().getResource("Assets/PetIcons/PegasusIcon.gif").toExternalForm()));
        pegasus.setPreserveRatio(true);
        pegasus.setFitWidth(400);
        pegasus.setTranslateX(300);
        pegasus.setTranslateY(-360);

        ImageView dragon = new ImageView(new Image(getClass().getResource("Assets/PetIcons/DragonIcon.gif").toExternalForm()));
        dragon.setPreserveRatio(true);
        dragon.setFitWidth(400);
        dragon.setTranslateX(600);
        dragon.setTranslateY(-780);

// ---------------------------------Food Icons Display----------------------------------------------

        ImageView apple = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/apple.png").toExternalForm()));
        apple.setPreserveRatio(true);
        apple.setFitWidth(50);
        apple.setTranslateX(110);
        apple.setTranslateY(-1120);

        ImageView brocolli = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/broc.png").toExternalForm()));
        brocolli.setPreserveRatio(true);
        brocolli.setFitWidth(60);
        brocolli.setTranslateX(170);
        brocolli.setTranslateY(-1190);

        ImageView chilli = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/hotChilli.png").toExternalForm()));
        chilli.setPreserveRatio(true);
        chilli.setFitWidth(60);
        chilli.setTranslateX(230);
        chilli.setTranslateY(-1270);
        
        ImageView goat = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/goat.png").toExternalForm()));
        goat.setPreserveRatio(true);
        goat.setFitWidth(60);
        goat.setTranslateX(720);
        goat.setTranslateY(-1350);

        ImageView brocolliPegasus = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/broc.png").toExternalForm()));
        brocolliPegasus.setPreserveRatio(true);
        brocolliPegasus.setFitWidth(60);
        brocolliPegasus.setTranslateX(470);
        brocolliPegasus.setTranslateY(-1430);

        ImageView pinkHerb = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/PinkHerb.png").toExternalForm()));
        pinkHerb.setPreserveRatio(true);
        pinkHerb.setFitWidth(60);
        pinkHerb.setTranslateX(530);
        pinkHerb.setTranslateY(-1510);

        ImageView goldenApple = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/goldenApple.png").toExternalForm()));
        goldenApple.setPreserveRatio(true);
        goldenApple.setFitWidth(60);
        goldenApple.setTranslateX(410);
        goldenApple.setTranslateY(-1590);

        ImageView fish = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/fish.png").toExternalForm()));
        fish.setPreserveRatio(true);
        fish.setFitWidth(60);
        fish.setTranslateX(780);
        fish.setTranslateY(-1670);

        ImageView hotChilliDragon = new ImageView(new Image(getClass().getResource("Assets/FoodIcons/hotChilli.png").toExternalForm()));
        hotChilliDragon.setPreserveRatio(true);
        hotChilliDragon.setFitWidth(60);
        hotChilliDragon.setTranslateX(830);
        hotChilliDragon.setTranslateY(-1750);

//---------------------------------Difficulty Label Display----------------------------------------------
        difficulty_label.setFont(customFont2);
        difficulty_label.setTranslateX(400);
        difficulty_label.setTranslateY(-1780);
        difficulty_label.setFill(Color.web("#ff3131"));
        difficulty_label.setStroke(Color.BLACK);
        difficulty_label.setStrokeWidth(1.5);

//---------------------------------Difficulty Stats Display----------------------------------------------
        ImageView difficultyStatsPhoenix = new ImageView(new Image(getClass().getResource("Assets/Stats/DifficultyStats.png").toExternalForm()));
        difficultyStatsPhoenix.setPreserveRatio(true);
        difficultyStatsPhoenix.setFitWidth(300);
        difficultyStatsPhoenix.setTranslateX(60);
        difficultyStatsPhoenix.setTranslateY(-1820);

        ImageView difficultyStatsPegasus = new ImageView(new Image(getClass().getResource("Assets/Stats/DifficultyStats.png").toExternalForm()));
        difficultyStatsPegasus.setPreserveRatio(true);
        difficultyStatsPegasus.setFitWidth(300);
        difficultyStatsPegasus.setTranslateX(350);
        difficultyStatsPegasus.setTranslateY(-2140);

        ImageView difficultyStatsDragon = new ImageView(new Image(getClass().getResource("Assets/Stats/DifficultyStats.png").toExternalForm()));
        difficultyStatsDragon.setPreserveRatio(true);
        difficultyStatsDragon.setFitWidth(300);
        difficultyStatsDragon.setTranslateX(650);
        difficultyStatsDragon.setTranslateY(-2460);

//---------------------------------Phoenix Difficulty Display----------------------------------------------
        phoenix_hunger.setFont(customFont3);
        phoenix_grooming.setFont(customFont3);
        phoenix_fun.setFont(customFont3);
        phoenix_sleep.setFont(customFont3);
//---------------------------------Pegasus Difficulty Display----------------------------------------------
        pegasus_hunger.setFont(customFont3);
        pegasus_grooming.setFont(customFont3);
        pegasus_fun.setFont(customFont3);
        pegasus_sleep.setFont(customFont3);
//---------------------------------Dragon Difficulty Display----------------------------------------------
        dragon_hunger.setFont(customFont3);
        dragon_grooming.setFont(customFont3);
        dragon_fun.setFont(customFont3);
        dragon_sleep.setFont(customFont3);
//---------------------------------Phoenix Hunger Display----------------------------------------------
        phoenix_hunger.setFill(Color.web("#ffffff"));
        phoenix_hunger.setTranslateX(220);
        phoenix_hunger.setTranslateY(-2720);
//---------------------------------Phoenix Grooming Display----------------------------------------------
        phoenix_grooming.setFill(Color.web("#ffffff"));
        phoenix_grooming.setTranslateX(220);
        phoenix_grooming.setTranslateY(-2710);
//---------------------------------Phoenix Fun Display----------------------------------------------
        phoenix_fun.setFill(Color.web("#ffffff"));
        phoenix_fun.setTranslateX(220);
        phoenix_fun.setTranslateY(-2700);
//---------------------------------Phoenix Sleep Display----------------------------------------------
        phoenix_sleep.setFill(Color.web("#ffffff"));
        phoenix_sleep.setTranslateX(220);
        phoenix_sleep.setTranslateY(-2690);
//---------------------------------Pegasus Hunger Display----------------------------------------------
        pegasus_hunger.setFill(Color.web("#ffffff"));
        pegasus_hunger.setTranslateX(510);
        pegasus_hunger.setTranslateY(-2895);
//---------------------------------Pegasus Grooming Display----------------------------------------------
        pegasus_grooming.setFill(Color.web("#ffffff"));
        pegasus_grooming.setTranslateX(510);
        pegasus_grooming.setTranslateY(-2885);

//---------------------------------Pegasus Fun Display----------------------------------------------
        pegasus_fun.setFill(Color.web("#ffffff"));
        pegasus_fun.setTranslateX(510);
        pegasus_fun.setTranslateY(-2875);
//---------------------------------Pegasus Sleep Display----------------------------------------------
        pegasus_sleep.setFill(Color.web("#ffffff"));
        pegasus_sleep.setTranslateX(510);
        pegasus_sleep.setTranslateY(-2865);
//---------------------------------Dragon Hunger Display----------------------------------------------
        dragon_hunger.setFill(Color.web("#ffffff"));
        dragon_hunger.setTranslateX(810);
        dragon_hunger.setTranslateY(-3070);
//---------------------------------Dragon Grooming Display-------------------------------------------
        dragon_grooming.setFill(Color.web("#ffffff"));
        dragon_grooming.setTranslateX(810);
        dragon_grooming.setTranslateY(-3060);
//---------------------------------Dragon Fun Display------------------------------------------------
        dragon_fun.setFill(Color.web("#ffffff"));
        dragon_fun.setTranslateX(810);
        dragon_fun.setTranslateY(-3050);
//---------------------------------Dragon Sleep Display----------------------------------------------
        dragon_sleep.setFill(Color.web("#ffffff"));
        dragon_sleep.setTranslateX(810);
        dragon_sleep.setTranslateY(-3040);

//---------------------------------Scene Layout------------------------------------------------------
        VBox layout = new VBox(20);
        layout.setId("background");
        layout.getChildren().addAll(page_title, phoenix, pegasus, dragon, phoenix_label, pegasus_label, 
        dragon_label, apple, brocolli, chilli, goat, brocolliPegasus, pinkHerb, goldenApple, fish, hotChilliDragon, difficulty_label, difficultyStatsPhoenix,difficultyStatsPegasus, difficultyStatsDragon,
        phoenix_hunger, phoenix_grooming, phoenix_fun, phoenix_sleep, pegasus_hunger, pegasus_grooming, pegasus_fun, pegasus_sleep, dragon_hunger, dragon_grooming, dragon_fun, dragon_sleep, 
        phoenixButton, pegasusButton, dragonButton, nextButton
        );
       
        Scene scene = new Scene(layout, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("Styles/ChoosePetScreen.css").toExternalForm());
        stage.setResizable(false);

        stage.setScene(scene);
        stage.setTitle("Choose Your Pet");
        stage.show();
    }
    /**
     * The main entry point for launching the ChoosePetScreen.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}