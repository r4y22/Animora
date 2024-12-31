/**
 * Interface for a pet object. All pets must implement these methods.
 * 
 * This interface is used to define the common properties and behaviors of all pets.
 * 
 * @author William Du
 * @version 1.0 - Initial version
 * @version 1.1 - Added increase and decrease methods for each property
 * @version 1.2 - Added setters for each property
 * 
 * @since 1.0
 * 
 */
public interface Pet {

    // Getters for common pet properties
    String getName();
    String getType();
    int getGrooming();
    int getSleep();
    int getFun(); 
    int getHunger();

    // Setters for common pet properties (if mutability is required)
    void setName(String name);
    void setGrooming(int grooming);
    void setSleep(int sleep);
    void setFun(int fun); 
    void setHunger(int hunger);

    // Behavior methods that all pets must implement
    void eat();
    void play();
    void sleep();
    void groom();

    void increaseHunger(int value);
    void increaseFun(int value);
    void increaseSleep(int value);
    void increaseGrooming(int value);

    void decreaseHunger(int value);
    void decreaseFun(int value);
    void decreaseSleep(int value);
    void decreaseGrooming(int value);

}
