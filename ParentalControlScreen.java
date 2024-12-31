import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * ParentalControlScreen
 *
 * This class represents the parental control settings where the user must input
 * a password(1234) to be able to access the page.
 * The user will then be able to select time periods where they are not allowed
 * to load or start
 * a new game as well as be able to see the total play time and average play
 * time per sission.
 * The user may also choose to reset the total play time and average play time
 * per session
 * number if they wish to do so by pressing the reset button.
 * The data is saved to a JSON file when the user clicks the save button.
 * The data is loaded from the JSON file when the screen is initialized.
 *
 * @author Daniel Yuan
 * @version 1.1 Added the login page
 * @version 1.2 Added the buttons and labels
 * @version 1.3 Buttons and labels now have functionality
 * @version 1.4 Added the ability to save data to a JSON file
 * @version 1.5 Added the ability to data from a JSON file
 * @version 1.7 Added background, images, and css styling
 *
 * @since 1.0
 *
 */

public class ParentalControlScreen extends Application {
    private static Scene previousScene;
    /**
     * Path to the JSON file where settings are saved.
     */
    private static final String SETTINGS_FILE = "savedata/parentalSettings.json";

    /**
     * Default password for accessing the parental control settings.
     */
    private static final String PASSWORD = "1234";

    /**
     * Property binding for total playtime.
     */
    private final StringProperty totalPlayTime = new SimpleStringProperty("0:00");

    /**
     * Property binding for average playtime.
     */
    private final StringProperty averagePlayTime = new SimpleStringProperty("0:00");

    /**
     * Map containing time selectors for each day of the week.
     */
    private final Map<String, Spinner<Integer>[]> timeSelectorsMap = new HashMap<>();

    private boolean isLimitationsEnabled = false;
    private boolean isReportEnabled = false;
    private final boolean isVisible = isReportEnabled || isLimitationsEnabled;
    private long sessionStartTime;
    private long cumulativePlayTime = 0;
    private int sessionCount = 0;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {

        Scene scene = createScene(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();

        startSessionTimer();
    }

    
    /** 
     * @param primaryStage
     * @return Scene
     */
    private Scene createScene(Stage primaryStage) {
        if (!verifyPassword()) {
            System.out.println("Access Denied");
        }

        sessionCount++;

        // Initialize the layout and components.
        primaryStage.setTitle("Parental Controls");

        // Title
        Font customFont = loadFont("Assets/Fonts/PixelifySans-Bold.ttf", 65);
        Label title = new Label("Parental Controls");
        title.setStyle("-fx-text-fill: #fd7800");
        title.setFont(customFont);
        title.setAlignment(Pos.CENTER);

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        // Main Layout
        VBox mainLayout = new VBox(15); // Reduced spacing
        mainLayout.setId("background");
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Play Time Statistics Section
        Font customFont3 = loadFont("Assets/Fonts/PressStart2P-Regular.ttf", 20);
        Label totalPlayTimeLabel = new Label("Total Play Time: ");
        totalPlayTimeLabel.setStyle("-fx-text-fill: #fd7800");
        totalPlayTimeLabel.setFont(customFont3);
        Label totalPlayTimeValue = new Label();
        totalPlayTimeValue.textProperty().bind(totalPlayTime);
        totalPlayTimeValue.setStyle("-fx-text-fill: #0000ff");
        totalPlayTimeValue.setFont(customFont3);

        Label avgPlayTimeLabel = new Label("Average Play Time: ");
        avgPlayTimeLabel.setStyle("-fx-text-fill: #fd7800");
        avgPlayTimeLabel.setFont(customFont3);
        Label avgPlayTimeValue = new Label();
        avgPlayTimeValue.textProperty().bind(averagePlayTime);
        avgPlayTimeValue.setStyle("-fx-text-fill: #0000ff");
        avgPlayTimeValue.setFont(customFont3);
        Label resetButtonLabel = new Label("Reset Button");
        resetButtonLabel.setStyle("-fx-text-fill: #fd7800");
        resetButtonLabel.setFont(customFont3);

        Button resetStatsButton = createImageButton("Assets/Images/resetButton.png", 64, 32);
        resetStatsButton.setOnAction(e -> resetStatistics());

        // Limitations Section
        GridPane timeLimitGrid = new GridPane();
        timeLimitGrid.setHgap(10);
        timeLimitGrid.setVgap(8);

        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        for (int i = 0; i < days.length; i++) {
            String day = days[i];
            Label dayLabel = new Label(day);
            dayLabel.setStyle("-fx-text-fill: #fd7800");
            dayLabel.setFont(customFont3);
            Label toLabel = new Label("To");
            toLabel.setStyle("-fx-text-fill: #fd7800");
            toLabel.setFont(customFont3);

            Spinner<Integer> startHourSpinner = createSpinner(0, 23);
            Spinner<Integer> startMinuteSpinner = createSpinner(0, 59);
            Spinner<Integer> endHourSpinner = createSpinner(0, 23);
            Spinner<Integer> endMinuteSpinner = createSpinner(0, 59);

            timeLimitGrid.addRow(i, dayLabel, startHourSpinner, startMinuteSpinner, toLabel, endHourSpinner,
                    endMinuteSpinner);
            timeSelectorsMap.put(day,
                    new Spinner[] { startHourSpinner, startMinuteSpinner, endHourSpinner, endMinuteSpinner });
        }

        loadSettings();

        // Toggle Buttons
        ToggleButton displayTimeButton = new ToggleButton();
        displayTimeButton.setFont(customFont3);
        displayTimeButton.setStyle("-fx-text-fill: #000000");
        displayTimeButton.setSelected(isReportEnabled);
        displayTimeButton.setText(isReportEnabled ? "Play Time Report Enabled" : "Play Time Report Disabled");
        displayTimeButton.setId("customToggle");
        displayTimeButton.setOnAction(e -> {
            isReportEnabled = displayTimeButton.isSelected();
            displayTimeButton.setText(isReportEnabled ? "Play Time Report Enabled" : "Play Time Report Disabled");
        });

        VBox statsSection = new VBox(8, totalPlayTimeLabel, totalPlayTimeValue, avgPlayTimeLabel,
                avgPlayTimeValue, resetButtonLabel, resetStatsButton, displayTimeButton);
        statsSection.setAlignment(Pos.CENTER);

        ToggleButton enableLimitationsButton = new ToggleButton();
        enableLimitationsButton.setFont(customFont3);
        enableLimitationsButton.setStyle("-fx-text-fill: #000000");
        enableLimitationsButton.setSelected(isLimitationsEnabled);
        enableLimitationsButton
                .setText(isLimitationsEnabled ? "Time Limitations Enabled" : "Time Limitations Disabled");
        enableLimitationsButton.setId("customToggle");
        enableLimitationsButton.setOnAction(e -> {
            isLimitationsEnabled = enableLimitationsButton.isSelected();
            enableLimitationsButton
                    .setText(isLimitationsEnabled ? "Time Limitations Enabled" : "Time Limitations Disabled");
        });

        // Save and Exit Buttons
        Button saveButton = createImageButton("Assets/Images/saveButton.png", 128, 64);
        saveButton.setOnAction(e -> saveSettings());

        Button exitButton = createImageButton("Assets/Images/exitButton.png", 128, 64);
        exitButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
        });

        HBox buttonBox = new HBox(15, saveButton, exitButton); // Reduced spacing
        buttonBox.setAlignment(Pos.CENTER);

        VBox limitationsSection = new VBox(10, enableLimitationsButton, timeLimitGrid, buttonBox);
        limitationsSection.setAlignment(Pos.CENTER);

        // Adjust VBox growth
        VBox.setVgrow(statsSection, Priority.ALWAYS);
        VBox.setVgrow(limitationsSection, Priority.ALWAYS);

        mainLayout.getChildren().addAll(titleBox, statsSection, limitationsSection);

        // Scene and Stylesheet
        Scene scene = new Scene(mainLayout, 1000, 800); // Adjusted height
        scene.getStylesheets().add(getClass().getResource("Styles/ParentalControlsScreen.css").toExternalForm());
        return scene;
    }

    
    /** 
     * @param primaryStage
     * @return Scene
     */
    public Scene getScene(Stage primaryStage) {
        return createScene(primaryStage);
    }

    
    /** 
     * @param scene
     */
    public static void setPreviousScene(Scene scene) {
        previousScene = scene;
    }

    /**
     * Resets the playtime statistics and saves the settings.
     */
    private void resetStatistics() {
        cumulativePlayTime = 0;
        sessionCount = 0;
        totalPlayTime.set("0:00");
        averagePlayTime.set("0:00");
        saveSettings();
    }

    /**
     * Verifies the password for accessing parental controls.
     *
     * @return true if the entered password matches, false otherwise
     */
    private boolean verifyPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Parental Controls");
        dialog.setHeaderText("Enter Password");
        dialog.setContentText("Password:");

        String input = dialog.showAndWait().orElse("");
        return PASSWORD.equals(input);
    }

    /**
     * Saves the current settings to a JSON file.
     */
    @SuppressWarnings("unchecked")
    private void saveSettings() {
        try {
            // Prepare the JSON object for parental controls
            JSONObject parentalControls = new JSONObject();
            parentalControls.put("limitationsEnabled", isLimitationsEnabled);
            parentalControls.put("playTimeReportEnabled", isReportEnabled);
            parentalControls.put("totalPlayTime", cumulativePlayTime);
            parentalControls.put("averagePlayTime", sessionCount > 0 ? cumulativePlayTime / sessionCount : 0);
            parentalControls.put("sessionCount", sessionCount);

            // Prepare allowed times
            JSONArray allowedTimes = new JSONArray();
            for (Map.Entry<String, Spinner<Integer>[]> entry : timeSelectorsMap.entrySet()) {
                String day = entry.getKey();
                Spinner<Integer>[] spinners = entry.getValue();
                JSONObject dayTimes = new JSONObject();
                dayTimes.put("day", day);
                dayTimes.put("startHour", spinners[0].getValue());
                dayTimes.put("startMinute", spinners[1].getValue());
                dayTimes.put("endHour", spinners[2].getValue());
                dayTimes.put("endMinute", spinners[3].getValue());
                allowedTimes.add(dayTimes);
            }
            parentalControls.put("allowedTimes", allowedTimes);

            // Wrap parental controls in root JSON object
            JSONObject root = new JSONObject();
            root.put("parentalControls", parentalControls);

            // Write to file with pretty-printing
            File file = new File(SETTINGS_FILE);
            file.getParentFile().mkdirs(); // Create directories if needed

            try (FileWriter writer = new FileWriter(file)) {
                String prettyJson = prettyPrintJson(root.toJSONString());
                writer.write(prettyJson);
            }

            System.out.println("Settings saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Pretty-print JSON string
    private String prettyPrintJson(String jsonString) {
        StringBuilder prettyJson = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;

        for (char charFromJson : jsonString.toCharArray()) {
            switch (charFromJson) {
                case '"':
                    prettyJson.append(charFromJson);
                    if (charFromJson == '"' && prettyJson.charAt(prettyJson.length() - 2) != '\\') {
                        inQuotes = !inQuotes; // Toggle the inQuotes flag
                    }
                    break;
                case '{':
                case '[':
                    prettyJson.append(charFromJson);
                    if (!inQuotes) {
                        prettyJson.append("\n");
                        indentLevel++;
                        prettyJson.append(indentString(indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        prettyJson.append("\n");
                        indentLevel--;
                        prettyJson.append(indentString(indentLevel));
                    }
                    prettyJson.append(charFromJson);
                    break;
                case ',':
                    prettyJson.append(charFromJson);
                    if (!inQuotes) {
                        prettyJson.append("\n");
                        prettyJson.append(indentString(indentLevel));
                    }
                    break;
                case ':':
                    prettyJson.append(charFromJson);
                    if (!inQuotes) {
                        prettyJson.append(" ");
                    }
                    break;
                default:
                    prettyJson.append(charFromJson);
                    break;
            }
        }
        return prettyJson.toString();
    }

    // Indentation utility for pretty printing
    private String indentString(int level) {
        return "  ".repeat(Math.max(0, level));
    }

    /**
     * Loads the settings from a JSON file.
     */
    private void loadSettings() {
        File file = new File(SETTINGS_FILE);
        if (!file.exists()) {
            System.out.println("Settings file not found. Using default settings.");
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONObject saveData = (JSONObject) parser.parse(reader);
            JSONObject parentalControls = (JSONObject) saveData.get("parentalControls");
            if (parentalControls == null) {
                System.out.println("No parental controls found in settings file.");
                return;
            }
            // Load settings
            loadPlayTimes(parentalControls);
            loadTimeChart(parentalControls);

            // Load Limitations and Time Chart
            isReportEnabled = (boolean) parentalControls.getOrDefault("playTimeReportEnabled", false);
            isLimitationsEnabled = (boolean) parentalControls.getOrDefault("limitationsEnabled", false);
            cumulativePlayTime = (long) parentalControls.getOrDefault("totalPlayTime", 0L);

            System.out.println("Settings loaded successfully.");
        } catch (Exception e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPlayTimes(JSONObject parentalControls) {
        try {
            // Load cumulative play time and session count dynamically
            cumulativePlayTime = (long) parentalControls.getOrDefault("totalPlayTime", 0L);
            sessionCount = ((Long) parentalControls.getOrDefault("sessionCount", 0L)).intValue();

            // Update totalPlayTime and averagePlayTime bindings
            totalPlayTime.set(formatTime(cumulativePlayTime));
            averagePlayTime.set(sessionCount > 0 ? formatTime(cumulativePlayTime / sessionCount) : "0:00");

        } catch (Exception e) {
            System.err.println("Error loading play times: " + e.getMessage());
        }
    }

    /**
     * Load time chart settings from the parental controls JSON object.
     */
    private void loadTimeChart(JSONObject parentalControls) {
        try {
            JSONArray allowedTimesArray = (JSONArray) parentalControls.get("allowedTimes");
            if (allowedTimesArray != null) {
                for (Object obj : allowedTimesArray) {
                    JSONObject dayTimes = (JSONObject) obj;
                    String day = (String) dayTimes.get("day");

                    if (timeSelectorsMap.containsKey(day)) {
                        Spinner<Integer>[] spinners = timeSelectorsMap.get(day);

                        // Set spinner values dynamically
                        spinners[0].getValueFactory().setValue(((Long) dayTimes.get("startHour")).intValue());
                        spinners[1].getValueFactory().setValue(((Long) dayTimes.get("startMinute")).intValue());
                        spinners[2].getValueFactory().setValue(((Long) dayTimes.get("endHour")).intValue());
                        spinners[3].getValueFactory().setValue(((Long) dayTimes.get("endMinute")).intValue());
                    }
                }
                System.out.println("Time chart loaded successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error loading time chart: " + e.getMessage());
        }
    }

    /**
     * Starts the session timer
     */
    private void startSessionTimer() {
        sessionStartTime = System.currentTimeMillis();

        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            long elapsed = (System.currentTimeMillis() - sessionStartTime) / 1000;
            long totalElapsed = cumulativePlayTime + elapsed;

            totalPlayTime.set(formatTime(totalElapsed));
            averagePlayTime.set(sessionCount > 0 ? formatTime(totalElapsed / sessionCount) : "0:00");
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    /**
     * Formats numbers into time format
     * 
     * @return returns number of seconds in hours and minutes
     */
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes %= 60;
        return String.format("%d:%02d", hours, minutes);
    }

    /**
     * Time selector chart
     */
    private Spinner<Integer> createSpinner(int min, int max) {
        Spinner<Integer> spinner = new Spinner<>(min, max, min); // Default to `min`
        spinner.setEditable(true);
        return spinner;
    }

    /**
     * Loads custom font
     */
    private Font loadFont(String fontPath, double size) {
        try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
            if (fontStream == null) {
                System.err.println("Font file not found: " + fontPath);
                return Font.getDefault();
            }
            return Font.loadFont(fontStream, size);
        } catch (IOException e) {
            System.err.println("Failed to load font: " + fontPath);
            return Font.getDefault();
        }
    }

    /**
     * Loads images
     */
    private Image loadImage(String imagePath) {
        try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
            if (imageStream == null) {
                System.err.println("Image file not found: " + imagePath);
                return null;
            }
            return new Image(imageStream);
        } catch (IOException e) {
            System.err.println("Failed to load image: " + imagePath);
            return null;
        }
    }

    /**
     * Loads images onto buttons
     */
    private Button createImageButton(String imagePath, int width, int height) {
        Image image = loadImage(imagePath);
        if (image == null) {
            return new Button("Error"); // Fallback button
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width); // Set the image width
        imageView.setFitHeight(height); // Set the image height

        Button button = new Button();
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");
        button.setPrefSize(width, height); // Set fixed button size
        button.setMinSize(width, height); // Ensure minimum size
        button.setMaxSize(width, height); // Ensure maximum size
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
