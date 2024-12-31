
/**
 * Represents an item in an inventory.
 * 
 * @author William Du
 * @version 1.0
 * @since 1.0
 */
public class InventoryItem {
    private String name;
    private int quantity;
    
    // Constructor with validation to avoid null values
    public InventoryItem(String name, int quantity) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        this.name = name;
        this.quantity = quantity;
    }

    
    /** 
     * @return String
     */
    // Getters and Setters with validation
    public String getName() {
        return name;
    }

    
    /** 
     * @param name
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        this.name = name;
    }

    
    /** 
     * @return int
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Item quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return (name != null ? name : "Unnamed Item") + " (x" + quantity + ")";
    }
}
