
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * GamePlayScreen
 * 
 * This class represents the main game screen where the user can interact with
 * their pet
 * The user can feed, play, groom, and put their pet to sleep
 * The user can also view the pet stats and save the game
 * The user can navigate to the main menu, settings, tutorial, and game over
 * screen
 * The player can use the following keys to interact with the pet:
 * - S: View pet stats
 * - F: Feed the pet
 * - P: Play with the pet
 * - G: Groom the pet
 * - B: Put the pet to sleep
 * 
 * The game data is saved to a JSON file when the user clicks the save button
 * The game data is loaded from the JSON file when the game screen is
 * initialized
 * 
 * @author William Du
 * @version 1.1 Added the inventory system and inventory items
 * @version 1.2 Added the items ability to affect the sprite, each action has
 *          its own sprite
 * @version 1.3 Added the ability to see stats of the pet through a popup
 * @version 1.4 Added the ability to save the game data to a JSON file
 * @version 1.5 Added the ability to load the game data from a JSON file
 * @version 1.7 Added extra functionality when the pet is given a gift
 * 
 * @since 1.0
 * 
 */
public class GamePlayScreen extends Application {
    // Paths and formats for loading images
    private static final String BASE_PATH = "Assets/Sprites/";
    private static final String[] FORMATS = { ".png", ".jpg", ".gif" }; // Supported formats

    // Flags to prevent multiple popups and animations
    private boolean isStatsPopupOpen = false; // Flag to prevent multiple popups
    private boolean isAnimationRunning = false; // Flag to prevent multiple animations, and prevent stats popup from
    private boolean isSavePopupOpen = false; // Flag to prevent multiple save popups
    private boolean isShopOpen = false; // Check if the shop popup is open

    // Save data variables
    private String saveFilePath = "savedata/savefile.json";
    private static int saveId = 1;
    private String username = "";
    private String petName = "";
    private String petType = "";
    private int playerLevel;
    private Pet my_pet = null;
    private int hunger, fun, sleep, grooming;
    private Inventory inventory = new Inventory();

    // Cursor variables
    private final Image defaultCursorImage = new Image("Assets/Cursors/cursor.png");
    private final Image hoverCursorImage = new Image("Assets/Cursors/cursor_hover.png");
    private final Cursor defaultCursor = new ImageCursor(defaultCursorImage);
    private final Cursor hoverCursor = new ImageCursor(hoverCursorImage);

    // Time Line variables
    private Timeline hungerTimeline;
    private Timeline funTimeline;
    private Timeline groomingTimeline;
    private Timeline sleepTimeline;

    
    /** 
     * @param id
     */
    // initialize with a specific save_id
    public static void setSaveId(int id) {
        saveId = id;
    }

    
    /** 
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        // Load save data
        loadSaveData(saveId);
        // Set the scene
        Scene scene = createScene(stage);
        stage.setScene(scene);
        stage.setTitle("Animora");
        stage.show();

        // Start the stat decrease timers
        startStatDecreaseTimers(stage);

    }

    
    /** 
     * @param stage
     * @return Scene
     */
    private Scene createScene(Stage stage) {
        // Create a new stack pane, stack pane is a layout manager that manages all
        // nodes in a single stack with the last added node on top
        StackPane gameContent = new StackPane();
        // Load an pet sprite
        Image pet_sprite = loadImage(BASE_PATH, petType, FORMATS);

        // Add the spirte to the stack node
        ImageView defaultSprite;
        if (pet_sprite != null) {
            defaultSprite = new ImageView(pet_sprite);
            defaultSprite.setFitHeight(300);
            defaultSprite.setFitWidth(300);
            // Set position
            defaultSprite.setTranslateX(0); // X position
            defaultSprite.setTranslateY(150); // Y position
            defaultSprite.setPreserveRatio(true);
            gameContent.getChildren().add(defaultSprite);
        } else {
            defaultSprite = new ImageView();
            gameContent.getChildren().add(new Label("Image not found"));
        }

        // Create the UI buttons
        AnchorPane uiButtons = createUI(gameContent, stage);

        // Inventory at the bottom
        FlowPane inventoryPane = createInventory(gameContent, uiButtons, defaultSprite);

        // Use a BorderPane to position the game content and inventory
        // BorderPane is a layout manager that manages all nodes in 5 areas: left,
        // right, bottom, top and center
        BorderPane root = new BorderPane();

        root.setTop(uiButtons);
        root.setCenter(gameContent);
        root.setBottom(inventoryPane);

        Scene scene = new Scene(root, 1000, 800, Color.LIGHTBLUE);
        registerKeyEvents(stage, scene, gameContent, inventoryPane, uiButtons, defaultSprite);
        // Set the css
        try {
            scene.getStylesheets().add("Styles/GamePlayScreen.css");
        } catch (Exception e) {
            System.err.println("Failed to load stylesheet: Styles/GamePlayScreen.css");
        }

        // SEt the cursor
        scene.setCursor(Cursor.DEFAULT);
        return scene;
    }

    /**
     * Load the save data from the JSON file
     * 
     * The save data is loaded from the url specified in the saveFilePath variable
     * The save data is then parsed and the pet name, type, and stats are extracted
     * and stored in the class variables
     * The pet object is then initialized based on the loaded data
     * If the save data is not found or invalid, a default pet is initialized
     * 
     * 
     * @param saveId
     */
    public void loadSaveData(int saveId) {
        // Load the pet name and type from the saved file
        JSONParser parser = new JSONParser();
        try {
            // Parse the JSON file
            JSONObject saveData = (JSONObject) parser.parse(new FileReader(saveFilePath));

            // Validate and extract the "saves" array
            JSONArray saves = (JSONArray) saveData.get("saves");
            if (saves == null) {
                throw new IllegalArgumentException("Save data does not contain a 'saves' array.");
            }

            // Find the save object with save_id = saveId
            JSONObject saveObject = null;
            for (Object obj : saves) {
                JSONObject save = (JSONObject) obj;
                if (((Long) save.get("save_id")).intValue() == GamePlayScreen.saveId) {
                    saveObject = save;
                    break;
                }
            }

            if (saveObject != null) {
                // Extract username
                username = (String) saveObject.get("username");
                if (username == null)
                    throw new IllegalArgumentException("Missing 'username' in save data.");

                // Extract pet data
                JSONObject petData = (JSONObject) saveObject.get("pet");
                if (petData == null)
                    throw new IllegalArgumentException("Missing 'pet' in save data.");
                petName = (String) petData.get("name");
                petType = (String) petData.get("type");
                if (petName == null || petType == null)
                    throw new IllegalArgumentException("Missing pet name or type.");

                // Extract pet stats
                JSONObject stats = (JSONObject) petData.get("stats");
                if (stats != null) {
                    hunger = stats.containsKey("hunger") ? ((Long) stats.get("hunger")).intValue() : 0;
                    fun = stats.containsKey("fun") ? ((Long) stats.get("fun")).intValue() : 0;
                    sleep = stats.containsKey("sleep") ? ((Long) stats.get("sleep")).intValue() : 0;
                    grooming = stats.containsKey("grooming") ? ((Long) stats.get("grooming")).intValue() : 0;
                }

                // Extract inventory
                JSONArray inventoryArray = (JSONArray) saveObject.get("inventory");
                if (inventoryArray != null) {
                    inventory.loadFromJSON(inventoryArray);
                    inventory.display();
                }

                // Extract game progress
                JSONObject gameProgress = (JSONObject) saveObject.get("game_progress");
                if (gameProgress == null)
                    throw new IllegalArgumentException("Missing 'gameProgress' in save data.");
                if (gameProgress != null && gameProgress.containsKey("level")) {
                    playerLevel = ((Long) gameProgress.get("level")).intValue();
                } else {
                    playerLevel = 1; // Default value
                }

                // Log loaded data
                System.out.println("Loaded save data: Username: " + username + ", Pet Name: " + petName + ", Pet Type: "
                        + petType + ", Player Level: " + playerLevel);
            } else {
                System.err.println("Save data with save_id = " + saveId + " not found.");
            }

        } catch (Exception e) {
            System.err.println("Failed to load save data: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize the pet object based on the loaded data
        try {
            // Validate pet type
            if (petType == null || petType.isEmpty()) {
                throw new IllegalArgumentException("Invalid pet type: Pet type is null or empty.");
            }

            // Initialize pet based on type
            switch (petType.toLowerCase()) {
                case "pegasus":
                    my_pet = new Pegasus(petName);
                    break;
                case "phoenix":
                    my_pet = new Phoenix(petName);
                    break;
                case "dragon":
                    my_pet = new Dragon(petName);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid pet type: " + petType);
            }

            // Set pet stats (example)
            my_pet.setHunger(hunger);
            my_pet.setFun(fun);
            my_pet.setSleep(sleep);
            my_pet.setGrooming(grooming);

            System.out.println("Pet initialized: " + petName + " (" + petType + ")");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            // Fallback: Initialize with default pet
            my_pet = new Pegasus(petName != null ? petName : "DefaultPet");
            my_pet.setHunger(5);
            my_pet.setFun(5);
            my_pet.setSleep(5);
            my_pet.setGrooming(5);
            System.out.println("Initialized default pet: " + my_pet.getName());
        } catch (Exception e) {
            System.err.println("Unexpected error during pet initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save the game data to the JSON file
     * 
     * The game data is saved to the url specified in the saveFilePath variable
     * The save data is updated with the current pet stats, inventory, and game
     * level
     * 
     * 
     * @param saveId
     */
    @SuppressWarnings("unchecked")
    public void saveGameData(int saveId) {
        JSONParser parser = new JSONParser();
        try {
            // Parse the existing save file
            JSONObject saveData = (JSONObject) parser.parse(new FileReader(saveFilePath));
            JSONArray saves = (JSONArray) saveData.get("saves");

            // Find the save object with save_id = saveId
            JSONObject saveObject = null;
            for (Object obj : saves) {
                JSONObject save = (JSONObject) obj;
                if (((Long) save.get("save_id")).intValue() == saveId) {
                    saveObject = save;
                    System.out.println("Save object found with save_id = " + saveId);
                    break;
                }
            }

            if (saveObject != null) {
                // Update username
                saveObject.put("username", username);
                System.out.println("Username updated: " + username);

                // Update pet data
                JSONObject petData = (JSONObject) saveObject.get("pet");
                if (petData != null) {
                    petData.put("name", my_pet.getName());
                    petData.put("type", my_pet.getType());
                    System.out.println("Pet data updated: " + my_pet.getName() + " (" + my_pet.getType() + ")");

                    // Update pet stats
                    JSONObject stats = (JSONObject) petData.get("stats");
                    if (stats != null) {
                        stats.put("hunger", my_pet.getHunger());
                        stats.put("fun", my_pet.getFun());
                        stats.put("sleep", my_pet.getSleep());
                        stats.put("grooming", my_pet.getGrooming());
                        System.out.println(
                                "Pet stats updated: Hunger: " + my_pet.getHunger() + ", Fun: " + my_pet.getFun()
                                        + ", Sleep: " + my_pet.getSleep() + ", Grooming: " + my_pet.getGrooming());
                    }
                }

                // Update inventory
                if (inventory != null) {
                    // Create a new JSONArray for the updated inventory
                    JSONArray updatedInventory = new JSONArray();
                    for (InventoryItem item : inventory.getItems()) {
                        JSONObject itemObject = new JSONObject();
                        itemObject.put("name", item.getName());
                        itemObject.put("quantity", item.getQuantity());
                        updatedInventory.add(itemObject);
                    }
                    // Replace the existing inventory in saveObject with the updated one
                    saveObject.put("inventory", updatedInventory);
                    System.out.println("Inventory updated: " + updatedInventory.toJSONString());
                }

                // Update game progress
                JSONObject gameProgress = (JSONObject) saveObject.get("game_progress");
                if (gameProgress != null) {
                    gameProgress.put("level", playerLevel);
                    gameProgress.put("last_played", java.time.LocalDateTime.now().toString());
                    System.out.println("Game progress updated: Level: " + playerLevel);
                    System.out.println("Last played: " + gameProgress.get("last_played"));
                }
            }

            // Write updated data back to file
            try (FileWriter writer = new FileWriter(saveFilePath)) {
                writer.write(saveData.toJSONString());
                writer.flush();
                System.out.println("Game data saved successfully!");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Save file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to save game data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Register key events
     * 
     * @param scene
     */
    private void registerKeyEvents(Stage stage, Scene scene, StackPane gameContent, FlowPane inventory,
            AnchorPane uiButtons,
            ImageView defaultSprite) {
        // Register key press event
        scene.setOnKeyPressed(event -> {
            if (isStatsPopupOpen || isAnimationRunning || isSavePopupOpen) {
                System.out.println("Cannot use keys while a Popup or Animation is open.");
                return;
            }
            // =================== S KEY TO OPEN STATS POPUP ===================
            if (event.getCode().toString().equals("S")) { // Check if 'S' is pressed
                if (!isStatsPopupOpen) { // Ensure no duplicate popups
                    System.out.println("'S' key pressed. Opening stats popup...");
                    showStatsPopUp(gameContent, stage);
                    isStatsPopupOpen = true; // Mark popup as open
                } else {
                    System.out.println("Stats popup is already open.");
                }
                // =================== F KEY TO FEED PET ===================
            } else if (event.getCode().toString().equals("F")) { // Check if 'F' is pressed
                if (!isAnimationRunning) { // Ensure no duplicate animations
                    System.out.println("'F' key pressed. Feeding pet...");

                    my_pet.increaseHunger(1);

                    System.out.println("Pet has been fed, current hunger: " + my_pet.getHunger());
                    playerLevel++; // Increase level
                    System.out.println("User level: " + playerLevel);
                    try {
                        playAnimation(gameContent, inventory, uiButtons,
                                "Assets/Sprites/" + my_pet.getType().toLowerCase() + "_eat.gif", defaultSprite, 4);
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Index out of bounds exception: " + e.getMessage());
                    }
                } else {
                    System.out.println("Animation is already running.");
                }
                // =================== P KEY TO PLAY WITH PET ===================
            } else if (event.getCode().toString().equals("P")) { // Check if 'P' is pressed
                if (!isAnimationRunning) { // Ensure no duplicate animations
                    System.out.println("'P' key pressed. Playing with pet...");

                    my_pet.increaseFun(1);

                    System.out.println("Pet has played, current fun: " + my_pet.getFun());
                    playerLevel++; // Increase level
                    System.out.println("User level: " + playerLevel);
                    try {
                        playAnimation(gameContent, inventory, uiButtons,
                                "Assets/Sprites/" + my_pet.getType().toLowerCase() + "_play.gif", defaultSprite, 4);
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Index out of bounds exception: " + e.getMessage());
                    }
                } else {
                    System.out.println("Animation is already running.");
                }
                // ================= G KEY TO GROOM PET ===================
            } else if (event.getCode().toString().equals("G")) { // Check if 'G' is pressed
                if (!isAnimationRunning) { // Ensure no duplicate animations
                    System.out.println("'G' key pressed. Grooming pet...");

                    my_pet.increaseGrooming(1);

                    System.out.println("Pet has been groomed, current grooming: " + my_pet.getGrooming());
                    playerLevel++; // Increase level
                    System.out.println("User level: " + playerLevel);
                    try {
                        playAnimation(gameContent, inventory, uiButtons,
                                "Assets/Sprites/" + my_pet.getType().toLowerCase() + "_bath.gif", defaultSprite, 4);
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Index out of bounds exception: " + e.getMessage());
                    }
                } else {
                    System.out.println("Animation is already running.");
                }
                // ================= B KEY TO PUT PET TO SLEEP ===================
            } else if (event.getCode().toString().equals("B")) { // Check if 'B' is pressed
                if (!isAnimationRunning) { // Ensure no duplicate animations
                    System.out.println("'B' key pressed. Putting pet to sleep...");

                    my_pet.increaseSleep(1);

                    System.out.println("Pet has slept, current sleep: " + my_pet.getSleep());
                    playerLevel++; // Increase level
                    System.out.println("User level: " + playerLevel);
                    try {
                        playAnimation(gameContent, inventory, uiButtons,
                                "Assets/Sprites/" + my_pet.getType().toLowerCase() + "_sleep.gif", defaultSprite, 4);
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Index out of bounds exception: " + e.getMessage());
                    }
                } else {
                    System.out.println("Animation is already running.");
                }
            }
        });
    }

    /**
     * Show the popup
     * 
     * Create a popup window to display the pet stats
     * The popup contains the pet stats for hunger, grooming, sleep, and fun
     * The stats are displayed as images corresponding to the current value of the
     * stat
     * The popup can be closed by clicking the close button
     * 
     * @param root  The root stack pane
     * @param stage The stage
     */
    private void showStatsPopUp(StackPane root, Stage stage) {
        // Create popup container with rounded corners and shadow effect
        BorderPane popupContainer = new BorderPane();
        popupContainer.setMaxSize(400, 400);
        popupContainer.setStyle("-fx-border-radius: 15; -fx-effect: dropshadow(gaussian, lightblue, 10, 0.5, 0, 0);");

        // Set background image for popup
        Image backgroundImage = new Image("/Assets/Images/box.png");
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        popupContainer.setBackground(new Background(bgImage));

        // Header with close button
        AnchorPane headerPane = new AnchorPane();
        ImageView closeBtn = createButton("Assets/Buttons/closebtn.png", "Assets/Buttons/closebtn_pressed.png", 30, 30,
                stage, 10.0, null, 10.0, root);
        closeBtn.setOnMouseClicked(event -> {
            root.getChildren().remove(popupContainer);
            isStatsPopupOpen = false;
        });
        headerPane.getChildren().add(closeBtn);
        popupContainer.setTop(headerPane);

        // Stats container
        VBox statsContainer = new VBox(10);
        statsContainer.setAlignment(Pos.CENTER);
        statsContainer.setPadding(new Insets(0, 10, 10, 10)); // Top, Right, Bottom, Left

        // Load fonts
        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 20);

        // Create and add stat elements (Hunger, Grooming, Sleep, Fun)
        String[] statNames = { "Hunger", "Grooming", "Sleep", "Fun" };
        int[] statValues = { my_pet.getHunger(), my_pet.getGrooming(), my_pet.getSleep(), my_pet.getFun() };

        for (int i = 0; i < statNames.length; i++) {
            VBox statBox = new VBox(3);
            statBox.setAlignment(Pos.CENTER);

            Label statLabel = new Label(statNames[i]);
            statLabel.setFont(customFont);

            ImageView statImage = new ImageView(new Image("Assets/Stats/stats" + statValues[i] + ".png"));

            statBox.getChildren().addAll(statLabel, statImage);
            statsContainer.getChildren().add(statBox);
        }

        popupContainer.setCenter(statsContainer);

        // Fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), popupContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Add popup to the root StackPane
        root.getChildren().add(popupContainer);
    }

    /**
     * Create an image view
     * 
     * @param root  The root stack pane
     * @param stage The stage
     * @return
     */
    private void showSaveNotification(StackPane root, Stage stage) {
        // Load fonts
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/Assets/fonts/PixelifySans-SemiBold.ttf"), 20);

        // Create popup container
        BorderPane savePopupContainer = new BorderPane();
        savePopupContainer.setStyle("-fx-border-radius: 15;");
        savePopupContainer.setMaxSize(250, 150); // Maximum size
        StackPane.setAlignment(savePopupContainer, Pos.CENTER); // Center the popup

        // Set background image
        Image backgroundImage = new Image(getClass().getResource("/Assets/Images/box.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(250, 150, false, false, false, false));
        savePopupContainer.setBackground(new Background(bgImage));
        savePopupContainer.setStyle("-fx-effect: dropshadow(gaussian, black, 20, 0.5, 0, 0);");

        // Add close button
        AnchorPane saveNotificationUiPane = new AnchorPane();
        saveNotificationUiPane.setPrefHeight(30); // Adjust height as needed

        // Create close button
        ImageView closeBtn = createButton("Assets/Buttons/closebtn.png", "Assets/Buttons/closebtn_pressed.png", 30, 30,
                stage, 10.0, null, 10.0, root);

        // Add close button to the popup
        saveNotificationUiPane.getChildren().add(closeBtn);

        // Handle close button click event
        closeBtn.setOnMouseClicked(event -> {
            root.getChildren().remove(savePopupContainer);
            isSavePopupOpen = false; // Mark popup as closed
            // playSound("Assets/Audio/btnClickExit.mp3");
        }); // Remove popup from the root StackPane

        // Create text container to hold the text
        FlowPane textContainer = new FlowPane();
        textContainer.setPadding(new Insets(10, 25, 10, 25)); // Top, Right, Bottom, Left

        // Create text notification
        Text notification = new Text("Game Saved! You may now close the game.");
        notification.setWrappingWidth(200); // Set wrapping width to fit the container

        // Set font and color
        if (customFont != null) {
            notification.setFont(customFont);
        } else {
            System.err.println("Font not loaded. Using default font.");
        }
        notification.setFill(Color.BLACK);

        // Add text to the container
        textContainer.getChildren().add(notification);
        // Set text container position
        savePopupContainer.setTop(saveNotificationUiPane);
        // Add text container to the popup
        savePopupContainer.setCenter(textContainer);

        // Add popup to the root StackPane
        root.getChildren().add(savePopupContainer);

        // Fade-in transition
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), savePopupContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Show the shop popup
     * 
     * @param root
     * @param stage
     */
    private void showShopPopup(StackPane root, Stage stage) {

        // Create the shop popup container with rounded corners and shadow effect
        BorderPane shopPopupContainer = new BorderPane();
        shopPopupContainer.setMaxSize(600, 400);
        shopPopupContainer.setStyle("-fx-effect: dropshadow(gaussian, pink, 20, .5, 0, 0);");

        // Set background image for popup
        Image backgroundImage = new Image("/Assets/Images/box.png");
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        shopPopupContainer.setBackground(new Background(bgImage));

        // Header with close button
        AnchorPane headerPane = new AnchorPane();
        ImageView closeBtn = createButton("Assets/Buttons/closebtn.png", "Assets/Buttons/closebtn_pressed.png", 30, 30,
                stage, 10.0, null, 10.0, root);
        closeBtn.setOnMouseClicked(event -> {
            root.getChildren().remove(shopPopupContainer);
            isShopOpen = false;
        });

        Font customFont = Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 45);
        Label shopTitle = new Label("Item Shop");
        shopTitle.setFont(customFont);
        AnchorPane.setTopAnchor(shopTitle, 20.0);
        AnchorPane.setLeftAnchor(shopTitle, 195.0); // 100 pixels from the left

        headerPane.getChildren().addAll(shopTitle, closeBtn);
        shopPopupContainer.setTop(headerPane);

        // Shop items container
        FlowPane shopItemsContainer = new FlowPane();
        shopItemsContainer.setAlignment(Pos.CENTER);
        shopItemsContainer.setPadding(new Insets(10, 10, 10, 10));
        shopItemsContainer.setHgap(10);
        shopItemsContainer.setVgap(10);

        // Add shop items
        String[] itemCategories = { "food1", "food2", "food3", "food4", "food5", "food6", "food7", "food8", "food9",
                "food10", "bath1", "bath2", "bath3", "bath4", "bath5", "fun1", "fun2", "fun3", "fun4", "fun5", "sleep1",
                "sleep2", "sleep3", "sleep4", "sleep5", "gift1", "gift2", "gift3", "gift4", "gift5" };
        for (String item : itemCategories) {
            StackPane itemPane = createShopItemPane(item, root, stage);
            shopItemsContainer.getChildren().add(itemPane);
        }

        // Set the shop items container in the center
        shopPopupContainer.setCenter(shopItemsContainer);

        // Fade-in animation for popup
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), shopPopupContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Add popup to the root StackPane
        root.getChildren().add(shopPopupContainer);
    }

    // Method to create a shop item pane
    
    private StackPane createShopItemPane(String item, StackPane root, Stage stage) {
        StackPane itemPane = new StackPane();

        // Image for the slot
        ImageView slotImage = createImageView("Assets/Images/shopItemContainer.png", 50, 50);

        // Image for the item
        ImageView itemImage = createImageView("Assets/Inventory/" + item + ".png", 35, 35);

        // Quantity label
        // Label quantityLabel = new Label("x1"); // Buying 1 at a time
        // quantityLabel
        // .setFont(Font.loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"),
        // 12));
        // quantityLabel.setStyle("-fx-text-fill: #000000;");
        // StackPane.setAlignment(quantityLabel, Pos.BOTTOM_RIGHT);
        // quantityLabel.setPadding(new Insets(5));

        // Add images and quantity label to the item pane
        itemPane.getChildren().addAll(slotImage, itemImage);

        // Mouse click event to purchase the item
        itemPane.setOnMouseClicked(event -> {
            System.out.println("Item bought: " + item);
            inventory.addItem(item, 1); // Add item to inventory
            inventory.display();
        });

        itemPane.setOnMouseEntered(event -> {
            itemPane.setCursor(Cursor.HAND);
            itemImage.setScaleX(1.5);
            itemImage.setScaleY(1.5);

        });
        itemPane.setOnMouseExited(event -> {
            itemPane.setCursor(Cursor.DEFAULT);
            itemImage.setScaleX(1);
            itemImage.setScaleY(1);
        });

        return itemPane;
    }

    /**
     * Helper method to create a button
     * 
     * Create a button with the specified images and dimensions
     * 
     * 
     * @param defaultImagePath
     * @param pressedImagePath
     * @param width
     * @param height
     * @param parent
     * @param top
     * @param left
     * @param right
     * @return
     */
    private ImageView createButton(String defaultImagePath, String pressedImagePath, double width, double height,
            Stage stage, Double top, Double left, Double right, StackPane gameContent) {

        String mediaPath = getClass().getResource("Assets/Audio/btnClickExit.mp3").toExternalForm();
        // Media sound = new Media(mediaPath);
        // MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // Create ImageView
        ImageView button = createImageView(defaultImagePath, width, height);

        // Add mouse events
        button.setOnMousePressed(event -> {
            if (isAnimationRunning) {
                System.out.println("Cannot use buttons while animation is running.");
                return; // Exit the handler
            }

            // Change to pressed image
            button.setImage(new Image(pressedImagePath));
            button.setCursor(Cursor.HAND);
            // mediaPlayer.play(); // Play the sound

            if (defaultImagePath.contains("homebtn")) {
                System.out.println("Home button clicked");
                stopStatDecreaseTimers(); // Stop the timers before switching scenes
                // Go back to the main menu
                try {
                    Scene mainMenuScene = new MainMenuScreen().getScene(stage);
                    stage.setScene(mainMenuScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (defaultImagePath.contains("savebtn")) {
                System.out.println("Save button clicked");
                // Save the game to the save file
                System.out.println("Saving game...");
                saveGameData(saveId);
                if (!isSavePopupOpen) {
                    isSavePopupOpen = true; // Mark popup as open
                    showSaveNotification(gameContent, stage);
                }

            }

            if (defaultImagePath.contains("statsbtn")) {
                System.out.println("Stats button clicked");
                // Prevent multiple popups
                if (!isStatsPopupOpen) {
                    isStatsPopupOpen = true; // Mark popup as open
                    showStatsPopUp(gameContent, stage);
                }
            }

            if (defaultImagePath.contains("deathbtn")) {
                System.out.println("Death button clicked");
                // Create a PauseTransition to add a delay before switching the scene
                PauseTransition pause = new PauseTransition(Duration.seconds(0.3)); // Adjust the delay time as needed

                pause.setOnFinished(pauseEvent -> {
                    // Code to run after the delay (scene change)
                    stopStatDecreaseTimers(); // Stop the timers before switching scenes
                    try {
                        Scene gameOverScene = new GameOverScreen().getScene(stage);
                        stage.setScene(gameOverScene);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Play the pause to execute the scene transition after a delay
                pause.play();
            }

            if (defaultImagePath.contains("settingsbtn")) {
                System.out.println("Settings button clicked");

                // Create a PauseTransition to add a delay before switching the scene
                PauseTransition pause = new PauseTransition(Duration.seconds(0.3)); // Adjust the delay time as needed

                pause.setOnFinished(pauseEvent -> {
                    stopStatDecreaseTimers(); // Stop the timers before switching scenes
                    // Code to run after the delay (scene change)
                    try {
                        Scene settingsScene = new SettingsScreen().getScene(stage);
                        SettingsScreen.setPreviousScene(stage.getScene());
                        stage.setScene(settingsScene);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Play the pause to execute the scene transition after a delay
                pause.play();
            }

            if (defaultImagePath.contains("tutorialbtn")) {
                System.out.println("Tutorial button clicked");

                // Create a PauseTransition to add a delay before switching the scene
                PauseTransition pause = new PauseTransition(Duration.seconds(0.3)); // Adjust the delay time as needed

                pause.setOnFinished(pauseEvent -> {
                    stopStatDecreaseTimers(); // Stop the timers before switching scenes
                    // Code to run after the delay (scene change)
                    try {
                        Scene tutorialScene = new ChoosePetTutorialScreen().getScene(stage);
                        ChoosePetTutorialScreen.setPreviousScene(stage.getScene());
                        TakeCareTutorialScreen.setPreviousScene(stage.getScene());
                        ViewStatsTutorialScreen.setPreviousScene(stage.getScene());
                        SaveGameTutorialScreen.setPreviousScene(stage.getScene());
                        LoadGameTutorialScreen.setPreviousScene(stage.getScene());
                        GameOverTutorialScreen.setPreviousScene(stage.getScene());
                        stage.setScene(tutorialScene);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Play the pause to execute the scene transition after a delay
                pause.play();
            }

            if (defaultImagePath.contains("shopbtn")) {
                System.out.println("Shop button clicked");

                if (!isShopOpen) {
                    isShopOpen = true;
                    showShopPopup(gameContent, stage);
                }

            }
            // playSound("Assets/Audio/btnClick.mp3");

        }); // Change to default image
        button.setOnMouseReleased(event -> {
            button.setImage(new Image(defaultImagePath));
            button.setCursor(Cursor.DEFAULT);
        }); // Revert to default image
        button.setOnMouseEntered(event -> button.setCursor(Cursor.HAND)); // Change to hand cursor
        button.setOnMouseExited(event -> button.setCursor(Cursor.DEFAULT)); // Reset cursor

        // Set button position
        if (top != null)
            AnchorPane.setTopAnchor(button, top);
        if (left != null)
            AnchorPane.setLeftAnchor(button, left);
        if (right != null)
            AnchorPane.setRightAnchor(button, right);

        // Add to parent AnchorPane
        // parent.getChildren().add(button);

        return button;
    }

    /**
     * Create the UI buttons
     * 
     * Create the UI buttons for the game screen, including the home, save, stats,
     * tutorial, settings, and death buttons
     * Uses the createButton helper method to create the buttons
     * The created buttons are added to an AnchorPane and returned
     * 
     * @return The UI buttons
     */
    private AnchorPane createUI(StackPane gameContent, Stage stage) {
        AnchorPane uiButtons = new AnchorPane();

        // Create buttons using the helper method
        ImageView homeMenuBtn = createButton("Assets/Buttons/homebtn.png", "Assets/Buttons/homebtn_pressed.png", 50, 50,
                stage, 10.0, 10.0,
                null, gameContent);
        ImageView saveDataBtn = createButton("Assets/Buttons/savebtn.png", "Assets/Buttons/savebtn_pressed.png", 50, 50,
                stage, 10.0, 70.0,
                null, gameContent);
        ImageView statsBtn = createButton("Assets/Buttons/statsbtn.png", "Assets/Buttons/statsbtn_pressed.png", 50, 50,
                stage, 10.0,
                130.0, null, gameContent);
        ImageView tutorialBtn = createButton("Assets/Buttons/tutorialbtn.png", "Assets/Buttons/tutorialbtn_pressed.png",
                50, 50, stage,
                10.0, null, 10.0, gameContent);
        ImageView settingsBtn = createButton("Assets/Buttons/settingsbtn.png", "Assets/Buttons/settingsbtn_pressed.png",
                50, 50, stage,
                10.0, null, 70.0, gameContent);
        ImageView deathBtn = createButton("Assets/Buttons/deathbtn.png", "Assets/Buttons/deathbtn_pressed.png", 50, 50,
                stage,
                10.0, null, 130.0, gameContent);
        ImageView shopBtn = createButton("Assets/Buttons/shopbtn.png", "Assets/Buttons/shopbtn_pressed.png", 50, 50,
                stage,
                10.0, null, 190.0, gameContent);

        // Add the buttons to the AnchorPane
        uiButtons.getChildren().addAll(homeMenuBtn, saveDataBtn, statsBtn, tutorialBtn, settingsBtn, deathBtn, shopBtn);

        return uiButtons;
    }

    /**
     * Load an image from the specified base path and image name
     * 
     * @param basePath  The base path to the image
     * @param imageName The name of the image
     * @param formats   The supported formats
     * @return The image
     */
    private Image loadImage(String basePath, String imageName, String[] formats) {
        for (String format : formats) {
            try {
                return new Image(basePath + imageName + format);
            } catch (Exception e) {
                // Silently ignore and try the next format
            }
        }
        System.err.println("No sprite found for " + imageName);
        return null;
    }

    /**
     * Play an animation
     * 
     * @param gameContent     The game content
     * @param inventory       The inventory
     * @param animationPath   The path to the animation
     * @param defaultSprite   The default sprite
     * @param durationSeconds The duration of the animation
     */
    private void playAnimation(StackPane gameContent, FlowPane inventory, AnchorPane uiButtons, String animationPath,
            ImageView defaultSprite,
            double durationSeconds) {
        isAnimationRunning = true; // Mark animation as running
        ImageView animateSprite = createImageView(animationPath, 300, 300);
        animateSprite.setTranslateX(0);
        animateSprite.setTranslateY(150);

        gameContent.getChildren().remove(defaultSprite); // Remove the original sprite
        gameContent.getChildren().add(animateSprite);
        gameContent.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        uiButtons.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        inventory.setDisable(true); // Disable inventory while animation is playing

        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(durationSeconds),
                        e -> {
                            gameContent.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                            uiButtons.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                            defaultSprite.setTranslateX(0);
                            defaultSprite.setTranslateY(150);
                            gameContent.getChildren().remove(animateSprite); // Remove animation
                            gameContent.getChildren().add(defaultSprite); // Add original
                            inventory.setDisable(false); // Enable inventory
                            isAnimationRunning = false; // Mark animation as finished
                        }))
                .play();
    }

    /**
     * Add inventory items to the inventory
     * 
     * 
     * @param inventory The inventory to add the items to
     * @param category  The category of the items
     * @param count     The number of items to add
     */
    private void addInventoryItems(StackPane gameContent, FlowPane inventoryPane, AnchorPane uiButtons, String category,
            int count, ImageView defaultSprite) {
        for (int i = 1; i <= count; i++) {
            try {
                StackPane itemPane = new StackPane();

                // Create and configure empty slot
                ImageView emptySlot = createImageView("Assets/Inventory/empty_slot.png", 50, 50);

                // Create and configure item image
                ImageView itemImage = createImageView("Assets/Inventory/" + category + i + ".png", 30, 30);

                // ============ MOUSE HOVER EFFECTS FOR INVENTORY ITEMS ============

                Label quantityLabel = new Label();
                final int index = i;

                itemPane.setOnMouseEntered(event -> {
                    itemImage.setScaleX(1.5); // Scale up
                    itemImage.setScaleY(1.5);
                    itemImage.setCursor(Cursor.HAND); // Change to hand cursor

                    // add a pop up to show the quantity of that item
                    int itemQuantity = inventory.getItemQuantity(category + index);
                    quantityLabel.setText("Remaining: x" + itemQuantity);
                    Font customFont = Font
                            .loadFont(getClass().getResourceAsStream("Assets/fonts/PixelifySans-SemiBold.ttf"), 35);
                    quantityLabel.setFont(customFont);
                    quantityLabel.setStyle("-fx-text-fill: #aa7959;");
                    gameContent.getChildren().add(quantityLabel);
                    StackPane.setAlignment(quantityLabel, Pos.TOP_LEFT);
                    quantityLabel.setPadding(new Insets(20, 10, 10, 20)); // Padding: top, right, bottom, left

                });

                emptySlot.setOnMouseEntered(event -> {
                    emptySlot.setCursor(Cursor.HAND); // Change to hand cursor
                });

                emptySlot.setOnMouseExited(event -> {
                    emptySlot.setCursor(Cursor.DEFAULT); // Reset to default cursor
                });

                itemPane.setOnMouseExited(event -> {
                    itemImage.setScaleX(1.0); // Reset to normal size
                    itemImage.setScaleY(1.0);
                    itemImage.setCursor(Cursor.DEFAULT); // Reset to default cursor
                    gameContent.getChildren().remove(quantityLabel); // Remove the quantity label
                });

                // =================== EVENT CLICK HANDLERS FOR INVENTORY ITEMS
                // ===================

                itemPane.setOnMouseClicked(event -> {
                    if (isStatsPopupOpen || isAnimationRunning || isSavePopupOpen) {
                        System.out.println("Cannot use items while Stats Popup is open.");
                        return; // Exit the handler
                    }

                    System.out.println("Item clicked: " + category + " " + index);
                    switch (category) {
                        case "food":
                            if (inventory.getItemQuantity(category + index) > 0) {
                                if (my_pet.getType().equals("pegasus")) {
                                    my_pet.increaseHunger(3);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level
                                } else if (my_pet.getType().equals("phoenix")) {
                                    my_pet.increaseHunger(5);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level

                                } else if (my_pet.getType().equals("dragon")) {
                                    my_pet.increaseHunger(1);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level
                                }
                                System.out.println("User level: " + playerLevel);
                                System.out.println("Pet has been fed, current hunger: " + my_pet.getHunger());

                                try {
                                    playAnimation(
                                            gameContent,
                                            inventoryPane,
                                            uiButtons,
                                            "Assets/Sprites/" + petType + "_eat.gif",
                                            defaultSprite,
                                            4);

                                } catch (IndexOutOfBoundsException e) {
                                    System.err.println("Index out of bounds exception: " + e.getMessage());
                                }
                            } else {
                                System.out.println("No more food left");
                            }

                            break;
                        case "bath":
                            if (inventory.getItemQuantity(category + index) > 0) {
                                if (my_pet.getType().equals("pegasus")) {
                                    my_pet.increaseGrooming(5);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level

                                } else if (my_pet.getType().equals("phoenix")) {
                                    my_pet.increaseGrooming(2);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level

                                } else if (my_pet.getType().equals("dragon")) {
                                    my_pet.increaseGrooming(3);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level
                                }
                                System.out.println("User level: " + playerLevel);
                                System.out.println("Pet has been bathed, current cleanliness: " + my_pet.getGrooming());
                                try {
                                    playAnimation(
                                            gameContent,
                                            inventoryPane,
                                            uiButtons,
                                            "Assets/Sprites/" + petType + "_bath.gif",
                                            defaultSprite,
                                            5);
                                } catch (IndexOutOfBoundsException e) {
                                    System.err.println("Index out of bounds exception: " + e.getMessage());
                                }
                            } else {
                                System.out.println("No more bath items left");
                            }
                            break;
                        case "fun":
                            if (inventory.getItemQuantity(category + index) > 0) {
                                // If it is the pegasus playing decreases the hunger by 2, sleep by 1, increase
                                // fun by 2
                                if (my_pet.getType().equals("pegasus")) {
                                    my_pet.increaseFun(3);
                                    my_pet.decreaseHunger(1);
                                    my_pet.decreaseSleep(1);

                                    inventory.removeItem(category + index, 1);
                                    inventory.display();

                                    playerLevel++; // Increase level

                                } else if (my_pet.getType().equals("phoenix")) {
                                    my_pet.increaseFun(2);
                                    my_pet.decreaseHunger(1);
                                    my_pet.decreaseSleep(1);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level

                                } else if (my_pet.getType().equals("dragon")) {
                                    my_pet.increaseFun(4);
                                    my_pet.decreaseHunger(1);
                                    my_pet.decreaseSleep(1);

                                    inventory.removeItem(category + index, 1);

                                    playerLevel++; // Increase level
                                }
                                System.out.println("User level: " + playerLevel);
                                System.out.println("Pet has played, current fun: " + my_pet.getFun());
                                System.out.println("Pet has played, current hunger: " + my_pet.getHunger());
                                System.out.println("Pet has played, current sleep: " + my_pet.getSleep());
                                try {
                                    playAnimation(
                                            gameContent,
                                            inventoryPane,
                                            uiButtons,
                                            "Assets/Sprites/" + petType + "_play.gif",
                                            defaultSprite,
                                            5);
                                } catch (IndexOutOfBoundsException e) {
                                    System.err.println("Index out of bounds exception: " + e.getMessage());
                                }
                            } else {
                                System.out.println("No more fun items left");
                            }
                            break;
                        case "sleep":
                            if (inventory.getItemQuantity(category + index) > 0) {
                                my_pet.increaseSleep(3);

                                inventory.removeItem(category + index, 1);

                                playerLevel++; // Increase level

                                System.out.println("User level: " + playerLevel);
                                System.out.println("Pet has slept, current sleep: " + my_pet.getSleep());
                                try {
                                    playAnimation(
                                            gameContent,
                                            inventoryPane,
                                            uiButtons,
                                            "Assets/Sprites/" + petType + "_sleep.gif",
                                            defaultSprite,
                                            5);
                                } catch (IndexOutOfBoundsException e) {
                                    System.err.println("Index out of bounds exception: " + e.getMessage());
                                }
                            } else {
                                System.out.println("No more sleep items left");
                            }
                            break;
                        case "gift":
                            if (inventory.getItemQuantity(category + index) > 0) {
                                // If the user gives a gift, all the stats are increased to 10
                                my_pet.setFun(10);
                                my_pet.setHunger(10);
                                my_pet.setSleep(10);
                                my_pet.setGrooming(10);

                                inventory.removeItem(category + index, 1);

                                playerLevel += 2; // Increase level

                                System.out.println("User level: " + playerLevel);
                                System.out.println("Pet has received a gift, current fun: " + my_pet.getFun());
                                System.out.println("Pet has received a gift, current hunger: " + my_pet.getHunger());
                                System.out.println("Pet has received a gift, current sleep: " + my_pet.getSleep());
                                System.out.println(
                                        "Pet has received a gift, current cleanliness: " + my_pet.getGrooming());
                                try {
                                    playAnimation(
                                            gameContent,
                                            inventoryPane,
                                            uiButtons,
                                            "Assets/Sprites/" + petType + "_play.gif",
                                            defaultSprite,
                                            5);
                                } catch (IndexOutOfBoundsException e) {
                                    System.err.println("Index out of bounds exception: " + e.getMessage());
                                }
                            } else {
                                System.out.println("No more gift items left");
                            }
                            break;

                        default:
                            break;
                    }
                    // playSound("Assets/Audio/itemClick.mp3");
                });

                // Add images to the item
                itemPane.getChildren().addAll(emptySlot, itemImage);

                // Add item to inventory
                inventoryPane.getChildren().add(itemPane);
            } catch (Exception e) {
                System.err.println("Failed to load inventory item: " + category + i);
            }
        }
    }

    /**
     * Create an image view with the specified image path, width and height
     * 
     * @param imagePath The path to the image
     * @param width     The width of the image
     * @param height    The height of the image
     * @return The image view
     */
    private ImageView createImageView(String imagePath, double width, double height) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Create the inventory
     * 
     * Create the inventory for the game screen, including the food, bath, fun,
     * sleep, and gift items
     * The inventory items are displayed as images that can be clicked to interact
     * with the pet
     * The inventory is displayed as a horizontal flow pane at the bottom of the
     * screen
     * 
     * 
     * @return The inventory
     */
    private FlowPane createInventory(StackPane gameContent, AnchorPane uiButtons, ImageView defaultSprite) {
        FlowPane inventoryPane = new FlowPane();
        inventoryPane.setId("inventory"); // Set the id for the css

        // Add inventory items
        addInventoryItems(gameContent, inventoryPane, uiButtons, "food", 10, defaultSprite);
        addInventoryItems(gameContent, inventoryPane, uiButtons, "bath", 5, defaultSprite);
        addInventoryItems(gameContent, inventoryPane, uiButtons, "fun", 5, defaultSprite);
        addInventoryItems(gameContent, inventoryPane, uiButtons, "sleep", 5, defaultSprite);
        addInventoryItems(gameContent, inventoryPane, uiButtons, "gift", 5, defaultSprite);

        // Create scale transitions

        for (int n = 1; n <= 8; n++) {
            try {

                ImageView empty_slot = new ImageView(new Image("Assets/Inventory/empty_slot" + ".png"));
                empty_slot.setFitWidth(50);
                empty_slot.setFitHeight(50);
                empty_slot.setPreserveRatio(true);

                inventoryPane.getChildren().add(empty_slot);
            } catch (Exception e) {
                System.err.println("Failed to load inventory item: " + n);
            }

        }

        return inventoryPane;
    }

    /**
     * Play a sound
     * 
     * @param soundFile The path to the sound file
     */
    public void playSound(String soundFilePath) {
        try {
            URL resource = getClass().getResource(soundFilePath);
            if (resource == null) {
                throw new IllegalArgumentException("Sound file not found: " + soundFilePath);
            }

            String uri = resource.toURI().toString();
            Media sound = new Media(uri);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * decrease stats of the pet overtime
     * 
     * 
     */
    private void startStatDecreaseTimers(Stage stage) {
        int hungerInterval, funInterval, groomingInterval, sleepInterval;

        // Set intervals based on the pet type
        switch (my_pet.getType().toLowerCase()) {
            case "pegasus":
                hungerInterval = 30; // Hunger decreases every 30 seconds for Pegasus
                funInterval = 25; // Fun decreases every 20 seconds for Pegasus
                groomingInterval = 10; // Grooming decreases every 25 seconds for Pegasus
                sleepInterval = 35; // Sleep decreases every 30 seconds for Pegasus
                break;
            case "dragon":
                hungerInterval = 15; // Hunger decreases every 15 seconds for dragon
                funInterval = 40; // Fun decreases every 30 seconds for dragon
                groomingInterval = 25; // Grooming decreases every 35 seconds for dragon
                sleepInterval = 35; // Sleep decreases every 45 seconds for dragon
                break;
            case "phoenix":
                hungerInterval = 40; // Hunger decreases every 20 seconds for phoenix
                funInterval = 15; // Fun decreases every 40 seconds for phoenix
                groomingInterval = 15; // Grooming decreases every 45 seconds for phoenix
                sleepInterval = 35; // Sleep decreases every 50 seconds for phoenix
                break;
            default:
                hungerInterval = 35; // Default interval if pet type is not recognized
                funInterval = 35;
                groomingInterval = 35;
                sleepInterval = 35;
                break;
        }

        // Timeline for hunger decreasing
        hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(hungerInterval), event -> {
            my_pet.decreaseHunger(1);
            System.out.println("Hunger decreased: " + my_pet.getHunger());
            checkStatsAndHandleGameOver(stage); // Check if game over conditions are met

            // Optionally, check if hunger is too low and handle game over conditions
            if (my_pet.getHunger() == 0) {
                System.out.println("HUNGER IS AT 0: Pet needs food to survive!");
            } else if (my_pet.getHunger() <= 3) {
                System.out.println("Pet is starving! FEED IT!");
            }
        }));
        hungerTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        hungerTimeline.play(); // Start the timeline

        // Timeline for fun decreasing
        funTimeline = new Timeline(new KeyFrame(Duration.seconds(funInterval), event -> {
            my_pet.decreaseFun(1);
            System.out.println("Fun decreased: " + my_pet.getFun());
            checkStatsAndHandleGameOver(stage); // Check if game over conditions are met

            // Optionally, check if fun is too low and handle unhappy pet conditions
            if (my_pet.getFun() == 0) {
                System.out.println("FUN IS AT 0: Pet needs to play to survive!");
            } else if (my_pet.getFun() <= 3) {
                System.out.println("Pet is unhappy! Needs attention.");
            }
        }));
        funTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        funTimeline.play(); // Start the timeline

        // Timeline for grooming decreasing
        groomingTimeline = new Timeline(new KeyFrame(Duration.seconds(groomingInterval), event -> {
            my_pet.decreaseGrooming(1);
            System.out.println("Grooming decreased: " + my_pet.getGrooming());
            checkStatsAndHandleGameOver(stage); // Check if game over conditions are met

            // Optionally, check if grooming is too low and handle pet hygiene conditions
            if (my_pet.getHunger() == 0) {
                System.out.println("GROOMING IS AT 0: Pet needs to bathe to survive!");
            } else if (my_pet.getGrooming() <= 3) {
                System.out.println("Pet needs grooming!");
            }
        }));
        groomingTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        groomingTimeline.play(); // Start the timeline

        // Timeline for sleep decreasing
        sleepTimeline = new Timeline(new KeyFrame(Duration.seconds(sleepInterval), event -> {
            my_pet.decreaseSleep(1);
            System.out.println("Sleep decreased: " + my_pet.getSleep());
            checkStatsAndHandleGameOver(stage); // Check if game over conditions are met

            // Optionally, check if sleep is too low and handle tired pet conditions
            if (my_pet.getHunger() == 0) {
                System.out.println("SLEEP IS AT 0: Pet needs sleep to survive!");
            } else if (my_pet.getSleep() <= 3) {
                System.out.println("Pet is very tired! Put it to sleep.");
            }
        }));
        sleepTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        sleepTimeline.play(); // Start the timeline
    }

    // Method to check if all stats are at 0 and handle game over conditions
    private void checkStatsAndHandleGameOver(Stage stage) {
        if (my_pet.getHunger() == 0 && my_pet.getFun() == 0 && my_pet.getGrooming() == 0 && my_pet.getSleep() == 0) {
            System.out.println("All stats are at 0. Game Over!");

            // Create a PauseTransition to add a delay before switching the scene
            PauseTransition pause = new PauseTransition(Duration.seconds(0.3));

            pause.setOnFinished(pauseEvent -> {
                stopStatDecreaseTimers(); // Stop the timers before switching scenes
                try {
                    Scene gameOverScene = new GameOverScreen().getScene(stage);
                    stage.setScene(gameOverScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Play the pause to execute the scene transition after a delay
            pause.play();
        }
    }

    /**
     * Stop the stat decrease timers
     * 
     */
    private void stopStatDecreaseTimers() {
        if (hungerTimeline != null)
            hungerTimeline.stop();
        if (funTimeline != null)
            funTimeline.stop();
        if (groomingTimeline != null)
            groomingTimeline.stop();
        if (sleepTimeline != null)
            sleepTimeline.stop();
    }

    /* ================ GETTERS AND SETTERS ================= */
    /**
     * return the scene as a Scene object
     * 
     * @param stage
     * @return Scene
     */
    public Scene getScene(Stage stage) {
        return createScene(stage);
    }

    /**
     * return the pet object
     * 
     * @return
     */
    public Pet getMyPet() {
        return my_pet;
    }

    /**
     * return the player level
     * @return playerLevel
     */
    public int getPlayerLevel() {
        return playerLevel;
    }

    /**
     * return the username
     * 
     * @return  username
     */
    public String getUsername() {
        return username;
    }

    public static void main(String[] args) {
        launch(args);
    }

}