import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import javafx.scene.input.KeyEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestGame {

    private GamePlayScreen gamePlayScreen;
    private Stage stage;

    @Before
    public void setUp() {
        // Initialize with default save data (assuming saveId = 1)
        gamePlayScreen = new GamePlayScreen();
        gamePlayScreen.loadSaveData(1);

    }

    
    /** 
     * @param key
     */
    // Method to simulate key press events
    private void press(KeyCode key) {
        Scene scene = stage.getScene();
        if (scene != null) {
            scene.getOnKeyPressed().handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", key, false, false, false, false));
        }
    }

    // Helper method to wait for JavaFX events to finish
    private void waitForFxEvents() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * @throws InterruptedException
     */
    @Test
    public void testPetSleepStatDecreasesOverTime() throws InterruptedException {
        // Get initial sleep value
        int initialSleep = gamePlayScreen.getMyPet().getSleep();

        // Sleep for a duration longer than the sleep decrease interval
        Thread.sleep(36000); // Wait for 40 seconds to allow at least one decrease in sleep stat

        // Verify sleep decreased
        int updatedSleep = gamePlayScreen.getMyPet().getSleep();

        assertEquals("Sleep should decrease over time", initialSleep - 1, updatedSleep);
    }

    @Test
    public void testSaveGame() {
        // Invoke save game
        Platform.runLater(() -> gamePlayScreen.saveGameData(1));

        // Wait for the save to complete
        waitForFxEvents();

        // Check that the save data is saved correctly
        JSONParser parser = new JSONParser();
        try {
            JSONObject saveData = (JSONObject) parser.parse(new FileReader("savedata/savefile.json"));
            JSONArray saves = (JSONArray) saveData.get("saves");

            // Find the save object with save_id = 1
            JSONObject saveObject = null;
            for (Object obj : saves) {
                JSONObject save = (JSONObject) obj;
                if (((Long) save.get("save_id")).intValue() == 1) {
                    saveObject = save;
                    break;
                }
            }

            assertNotNull("Save data should be present", saveObject);

            // Verify the saved data
            JSONObject petData = (JSONObject) saveObject.get("pet");
            assertEquals("Pet name should be saved correctly", gamePlayScreen.getMyPet().getName(), petData.get("name"));
            assertEquals("Pet type should be saved correctly", gamePlayScreen.getMyPet().getType(), petData.get("type"));

            JSONObject petStats = (JSONObject) petData.get("stats");
            assertEquals("Pet hunger should be saved correctly", gamePlayScreen.getMyPet().getHunger(), ((Long) petStats.get("hunger")).intValue());
            assertEquals("Pet fun should be saved correctly", gamePlayScreen.getMyPet().getFun(), ((Long) petStats.get("fun")).intValue());
            assertEquals("Pet sleep should be saved correctly", gamePlayScreen.getMyPet().getSleep(), ((Long) petStats.get("sleep")).intValue());
            assertEquals("Pet grooming should be saved correctly", gamePlayScreen.getMyPet().getGrooming(), ((Long) petStats.get("grooming")).intValue());

            JSONObject gameProgress = (JSONObject) saveObject.get("gameProgress");
            assertEquals("Player level should be saved correctly", gamePlayScreen.getPlayerLevel(), ((Long) gameProgress.get("level")).intValue());

        } catch (IOException | ParseException e) {
            fail("Failed to read save data: " + e.getMessage());
        }
    }

    @Test
    public void testLoadSaveData() {
        // Create a sample save data JSON object
        JSONObject saveData = new JSONObject();
        JSONArray saves = new JSONArray();
        JSONObject saveObject = new JSONObject();
        saveObject.put("save_id", 1);
        saveObject.put("username", "Player1");

        JSONObject petData = new JSONObject();
        petData.put("name", "Starlight");
        petData.put("type", "pegasus");

        JSONObject petStats = new JSONObject();
        petStats.put("hunger", 7);
        petStats.put("fun", 8);
        petStats.put("sleep", 9);
        petStats.put("grooming", 10);
        petData.put("stats", petStats);

        saveObject.put("pet", petData);

        JSONObject gameProgress = new JSONObject();
        gameProgress.put("level", 5);
        saveObject.put("gameProgress", gameProgress);

        saves.add(saveObject);
        saveData.put("saves", saves);

        // Write the sample save data to the save file
        try (FileWriter file = new FileWriter("savedata/savefile.json")) {
            file.write(saveData.toJSONString());
            file.flush();
        } catch (IOException e) {
            fail("Failed to write save data: " + e.getMessage());
        }

        // Load the save data
        Platform.runLater(() -> gamePlayScreen.loadSaveData(1));

        // Wait for the load to complete
        waitForFxEvents();

        // Verify the loaded data
        assertEquals("Player1", gamePlayScreen.getUsername(), "Username should be loaded correctly");
        assertEquals("Starlight", gamePlayScreen.getMyPet().getName(), "Pet name should be loaded correctly");
        assertEquals("pegasus", gamePlayScreen.getMyPet().getType(), "Pet type should be loaded correctly");
        assertEquals("Pet hunger should be loaded correctly", 7, gamePlayScreen.getMyPet().getHunger());
        assertEquals("Pet fun should be loaded correctly", 8, gamePlayScreen.getMyPet().getFun());
        assertEquals("Pet sleep should be loaded correctly", 9, gamePlayScreen.getMyPet().getSleep());
        assertEquals("Pet grooming should be loaded correctly", 10, gamePlayScreen.getMyPet().getGrooming());
        assertEquals("Player level should be loaded correctly", 5, gamePlayScreen.getPlayerLevel());
    }
}
