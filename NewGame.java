import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * NewGame class is used in the Main Menu to allow users to create a new game and save their data.
 * This class handles saving new game data to a JSON file and assigning save IDs.
 * 
 * The class allows users to:
 * - Add a new save entry to the save file with a unique save ID.
 * - Generate a new save entry with initial game progress, inventory, and placeholder pet information.
 * - Store and update the save file with new save data.
 * 
 * @author Rahul Iyer
 * @version 1.0
 * @see README.md
 */

public class NewGame {

    private static final String SAVE_FILE = "savedata/savefile.json";
    private static int currentSaveID = -1;

     /**
     * Adds a new save entry to the save file with the provided username.
     * This method generates a unique save ID, creates an initial game state (progress, inventory, etc.), 
     * and writes the data to the save file.
     * 
     * @param username The username of the player creating the new game.
     * @return The generated save ID for the new game, or -1 if an error occurs.
     */
    public static int addNewSave(String username){
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(SAVE_FILE)) {
            // Parse the existing save file
            JSONObject saveData = (JSONObject) parser.parse(reader);
            JSONArray saves = (JSONArray) saveData.get("saves");

            if (saves == null) {
                System.out.println("No 'saves' array found");
                return -1;
            }

            // Generate a new save_id (based on existing save_ids in the file)
            int newSaveId = generateNewSaveId(saves);
            currentSaveID = newSaveId;

            // Create the new save object with the provided username
            JSONObject newSave = createNewSave(username, newSaveId);

            // Add the new save entry to the "saves" array
            saves.add(newSave);

            // Write the updated save data back to the file
            try (FileWriter fileWriter = new FileWriter(SAVE_FILE)) {
                fileWriter.write(saveData.toJSONString());
            }

            System.out.println("New save added with save_id: " + newSaveId);
            return newSaveId;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns the current save ID.
     * 
     * @return The current save ID.
     */
    public static int getSaveID(){
        return currentSaveID;
    }

     /**
     * Generates a unique save ID by iterating over the existing saves and finding the highest save ID.
     * The new save ID is the highest existing save ID + 1.
     * 
     * @param saves The array of existing save data in the save file.
     * @return The newly generated save ID.
     */
    private static int generateNewSaveId(JSONArray saves) {
        // Generate a unique save_id by finding the highest existing save_id and adding 1
        int highestSaveId = 0;
        for (Object obj : saves) {
            JSONObject save = (JSONObject) obj;
            int saveId = ((Long) save.get("save_id")).intValue();
            highestSaveId = Math.max(highestSaveId, saveId);
        }
        return highestSaveId + 1;  // Generate a new save_id by incrementing the highest value
    }
    /**
     * Creates a new save object with the provided username, save ID, initial game progress, inventory, and pet details.
     * The pet details are placeholders until the user selects a pet.
     * 
     * @param username The username of the player creating the new game.
     * @param newSaveId The unique save ID for the new game.
     * @return A JSONObject representing the new save data.
     */
    private static JSONObject createNewSave(String username, int newSaveId) {
        // Create a new JSON object for the new save
        JSONObject newSave = new JSONObject();

        // Add game progress (initial level and last_played time)
        JSONObject gameProgress = new JSONObject();
        gameProgress.put("level", 1);
        gameProgress.put("last_played", "2024-12-01T00:00:00");  // Initial date/time

        // Add initial inventory (you can expand this as necessary)
        JSONArray inventory = new JSONArray();
        JSONObject food1 = new JSONObject();
        food1.put("quantity", 1);
        food1.put("name", "food1");
        inventory.add(food1);
    
        JSONObject food2 = new JSONObject();
        food2.put("quantity", 1);
        food2.put("name", "food2");
        inventory.add(food2);
    
        JSONObject food3 = new JSONObject();
        food3.put("quantity", 1);
        food3.put("name", "food3");
        inventory.add(food3);
    
        JSONObject food4 = new JSONObject();
        food4.put("quantity", 1);
        food4.put("name", "food4");
        inventory.add(food4);
    
        JSONObject food5 = new JSONObject();
        food5.put("quantity", 1);
        food5.put("name", "food5");
        inventory.add(food5);
    
        JSONObject food6 = new JSONObject();
        food6.put("quantity", 1);
        food6.put("name", "food6");
        inventory.add(food6);
    
        JSONObject food7 = new JSONObject();
        food7.put("quantity", 1);
        food7.put("name", "food7");
        inventory.add(food7);
    
        JSONObject food8 = new JSONObject();
        food8.put("quantity", 1);
        food8.put("name", "food8");
        inventory.add(food8);
    
        JSONObject food9 = new JSONObject();
        food9.put("quantity", 1);
        food9.put("name", "food9");
        inventory.add(food9);
    
        JSONObject food10 = new JSONObject();
        food10.put("quantity", 1);
        food10.put("name", "food10");
        inventory.add(food10);
    
        // Bath items
        JSONObject bath1 = new JSONObject();
        bath1.put("quantity", 1);
        bath1.put("name", "bath1");
        inventory.add(bath1);
    
        JSONObject bath2 = new JSONObject();
        bath2.put("quantity", 1);
        bath2.put("name", "bath2");
        inventory.add(bath2);
    
        JSONObject bath3 = new JSONObject();
        bath3.put("quantity", 1);
        bath3.put("name", "bath3");
        inventory.add(bath3);
    
        JSONObject bath4 = new JSONObject();
        bath4.put("quantity", 1);
        bath4.put("name", "bath4");
        inventory.add(bath4);
    
        JSONObject bath5 = new JSONObject();
        bath5.put("quantity", 1);
        bath5.put("name", "bath5");
        inventory.add(bath5);
    
        // Fun items
        JSONObject fun1 = new JSONObject();
        fun1.put("quantity", 1);
        fun1.put("name", "fun1");
        inventory.add(fun1);
    
        JSONObject fun2 = new JSONObject();
        fun2.put("quantity", 1);
        fun2.put("name", "fun2");
        inventory.add(fun2);
    
        JSONObject fun3 = new JSONObject();
        fun3.put("quantity", 1);
        fun3.put("name", "fun3");
        inventory.add(fun3);
    
        JSONObject fun4 = new JSONObject();
        fun4.put("quantity", 1);
        fun4.put("name", "fun4");
        inventory.add(fun4);
    
        JSONObject fun5 = new JSONObject();
        fun5.put("quantity", 1);
        fun5.put("name", "fun5");
        inventory.add(fun5);
    
        // Sleep items
        JSONObject sleep1 = new JSONObject();
        sleep1.put("quantity", 1);
        sleep1.put("name", "sleep1");
        inventory.add(sleep1);
    
        JSONObject sleep2 = new JSONObject();
        sleep2.put("quantity", 1);
        sleep2.put("name", "sleep2");
        inventory.add(sleep2);
    
        JSONObject sleep3 = new JSONObject();
        sleep3.put("quantity", 1);
        sleep3.put("name", "sleep3");
        inventory.add(sleep3);
    
        JSONObject sleep4 = new JSONObject();
        sleep4.put("quantity", 1);
        sleep4.put("name", "sleep4");
        inventory.add(sleep4);
    
        JSONObject sleep5 = new JSONObject();
        sleep5.put("quantity", 1);
        sleep5.put("name", "sleep5");
        inventory.add(sleep5);
    
        // Gift items
        JSONObject gift1 = new JSONObject();
        gift1.put("quantity", 1);
        gift1.put("name", "gift1");
        inventory.add(gift1);
    
        JSONObject gift2 = new JSONObject();
        gift2.put("quantity", 1);
        gift2.put("name", "gift2");
        inventory.add(gift2);
    
        JSONObject gift3 = new JSONObject();
        gift3.put("quantity", 1);
        gift3.put("name", "gift3");
        inventory.add(gift3);
    
        JSONObject gift4 = new JSONObject();
        gift4.put("quantity", 1);
        gift4.put("name", "gift4");
        inventory.add(gift4);
    
        JSONObject gift5 = new JSONObject();
        gift5.put("quantity", 1);
        gift5.put("name", "gift5");
        inventory.add(gift5);

        // Add pet details (empty for now, will be set later when the user chooses a pet)
        JSONObject pet = new JSONObject();
        JSONObject petStats = new JSONObject();
        petStats.put("sleep", 10);
        petStats.put("grooming", 10);
        petStats.put("fun", 10);
        petStats.put("hunger", 10);
        pet.put("stats", petStats);
        pet.put("name", "");  // Placeholder until the user chooses a pet
        pet.put("type", "");  // Placeholder until the user chooses a pet

        // Add the username and save_id
        newSave.put("username", username);
        newSave.put("save_id", newSaveId);
        newSave.put("game_progress", gameProgress);
        newSave.put("inventory", inventory);
        newSave.put("pet", pet);

        return newSave;
    }
    
}
