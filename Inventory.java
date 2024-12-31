import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Inventory class to manage items
 * 
 * @author William Du
 * @version 1.0
 * @since 1.0
 */
public class Inventory {
    private List<InventoryItem> items;

    /**
     * Constructor to initialize inventory
     */
    public Inventory() {
        this.items = new ArrayList<>();

    }

    /**
     * Method to add an item
     * 
     * @param name     Name of the item
     * @param quantity Quantity of the item
     */
    public void addItem(String name, int quantity) {
        if (name == null || name.isEmpty()) {
            System.err.println("Cannot add item with null or empty name");
            return;
        }

        // Check if the item already exists
        for (InventoryItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("Updated item: " + item);
                return;
            }
        }

        // If item doesn't exist, add a new one
        InventoryItem newItem = new InventoryItem(name, quantity);
        items.add(newItem);
        System.out.println("Added new item: " + newItem);
    }

    /**
     * Method to remove an item
     * 
     * @param name     Name of the item
     * @param quantity Quantity of the item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean removeItem(String name, int quantity) {
        for (InventoryItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                if (item.getQuantity() >= quantity) {
                    item.setQuantity(item.getQuantity() - quantity);
                    return true;
                }
                return false; // Not enough quantity
            }
        }
        return false; // Item not found
    }

    /**
     * Method to clear all items from the inventory
     */
    public void clear() {
        items.clear();
    }

    /**
     * Method to display all items in the inventory
     */
    public void display() {
        System.out.println("Inventory:");
        for (InventoryItem item : items) {
            System.out.println(" - " + item);
        }
    }

    /**
     * Method to get the list of items
     * 
     * @return The list of items
     */
    public List<InventoryItem> getItems() {
        return items;
    }

    /**
     * Method to get the quantity of a specific item by its name
     * 
     * @param itemName The name of the inventory item
     * @return Quantity of the item, or -1 if the item is not in the inventory
     */
    public int getItemQuantity(String itemName) {
        for (InventoryItem item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item.getQuantity();
            }
        }
        return -1; // Item not found
    }

    /**
     * Convert inventory to JSON
     * 
     * @return JSONArray containing inventory items
     */
    @SuppressWarnings("unchecked")
    public JSONArray toJSON() {
        JSONArray inventoryArray = new JSONArray();
        for (InventoryItem item : items) {
            JSONObject itemObject = new JSONObject();
            itemObject.put("name", item.getName());
            itemObject.put("quantity", item.getQuantity());
            inventoryArray.add(itemObject);
        }
        return inventoryArray;
    }

    /**
     * Load inventory from JSON
     * 
     * @param inventoryArray JSONArray containing inventory items
     */
    public void loadFromJSON(JSONArray inventoryArray) {
        items.clear(); // Clear existing items

        for (Object obj : inventoryArray) {
            JSONObject itemObject = (JSONObject) obj;

            String name = (String) itemObject.get("name");
            if (name == null) {
                System.err.println("Missing item name in JSON data");
                continue; // Skip if item name is missing
            }

            // Ensure that quantity is correctly parsed
            if (itemObject.containsKey("quantity")) {
                int quantity = ((Long) itemObject.get("quantity")).intValue();
                if (quantity >= 0) {
                    items.add(new InventoryItem(name, quantity));
                } else {
                    System.err.println("Invalid quantity for item: " + name);
                }
            } else {
                System.err.println("Missing quantity for item: " + name);
            }
        }
    }

}
