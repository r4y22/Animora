/**
 * Dragon class that implements the Pet interface. This class represents a
 * Dragon pet
 * with attributes such as name, grooming, hunger, fun, and sleep stats. It
 * provides
 * behavior methods for interacting with the pet, including eating, playing,
 * sleeping,
 * and grooming.
 * 
 * @author William Du
 * @version 1.0
 * @since 1.0
 */
public class Dragon implements Pet {

    /** The name of the Dragon pet. */
    private String name;

    private int grooming = 10;
    private int hunger = 10;
    private int fun = 10;
    private int sleep = 10;

    /** The type of pet, fixed as "dragon". */
    private final String type = "dragon";

    /**
     * Constructor to initialize the Dragon with a name.
     * 
     * @param name the name of the Dragon
     */
    public Dragon(String name) {
        this.name = name;
    }

    // Getters (from the Pet interface)

    /**
     * Gets the name of the Dragon.
     * 
     * @return the name of the Dragon
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the grooming level of the Dragon.
     * 
     * @return the grooming level (0-10)
     */
    @Override
    public int getGrooming() {
        return grooming;
    }

    /**
     * Gets the sleep level of the Dragon.
     * 
     * @return the sleep level (0-10)
     */
    @Override
    public int getSleep() {
        return sleep;
    }

    /**
     * Gets the type of the pet, which is "dragon".
     * 
     * @return the type of the pet
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Gets the hunger level of the Dragon.
     * 
     * @return the hunger level (0-10)
     */
    @Override
    public int getHunger() {
        return hunger;
    }

    /**
     * Gets the fun level of the Dragon.
     * 
     * @return the fun level (0-10)
     */
    @Override
    public int getFun() {
        return fun;
    }

    // Setters (from the Pet interface)

    /**
     * Sets the name of the Dragon.
     * 
     * @param name the new name of the Dragon
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the grooming level of the Dragon.
     * 
     * @param grooming the new grooming level (0-10)
     */
    @Override
    public void setGrooming(int grooming) {
        this.grooming = grooming;
    }

    /**
     * Sets the sleep level of the Dragon.
     * 
     * @param sleep the new sleep level (0-10)
     */
    @Override
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    /**
     * Sets the fun level of the Dragon.
     * 
     * @param fun the new fun level (0-10)
     */
    @Override
    public void setFun(int fun) {
        this.fun = fun;
    }

    /**
     * Sets the hunger level of the Dragon.
     * 
     * @param hunger the new hunger level (0-10)
     */
    @Override
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    // Behavior methods (from the Pet interface)

    /**
     * Makes the Dragon eat to increase its hunger level.
     */
    @Override
    public void eat() {
        System.out.println(name + " is eating a large meal.");
        if (hunger < 10) {
            hunger++;
        } else {
            System.err.println(name + " is full!");
        }
    }

    /**
     * Makes the Dragon play to increase its fun level.
     */
    @Override
    public void play() {
        if (hunger <= 0 || sleep <= 0) {
            System.err.println(name + " is too hungry or tired to play! Feed or let it sleep first.");
        } else {
            System.out.println(name + " is playing and having fun!");
            if (fun < 10) {
                fun++;
            } else {
                System.err.println(name + " is already fully entertained!");
            }
            hunger = Math.max(hunger - 1, 0);
            sleep = Math.max(sleep - 1, 0);
        }
    }

    /**
     * Makes the Dragon sleep to regain its energy.
     */
    @Override
    public void sleep() {
        System.out.println(name + " is sleeping to regain energy.");
        sleep = Math.min(sleep + 1, 10);
    }

    /**
     * Makes the Dragon groom to increase its grooming level.
     */
    @Override
    public void groom() {
        System.out.println(name + " is being groomed.");
        grooming = Math.min(grooming + 1, 10);
    }

    // Methods to increase each stat

    /**
     * Increases the grooming level by a specified amount.
     * 
     * @param n the amount to increase
     */
    public void increaseGrooming(int n) {
        grooming = Math.min(grooming + n, 10);
    }

    /**
     * Increases the hunger level by a specified amount.
     * 
     * @param n the amount to increase
     */
    public void increaseHunger(int n) {
        hunger = Math.min(hunger + n, 10);
    }

    /**
     * Increases the fun level by a specified amount.
     * 
     * @param n the amount to increase
     */
    public void increaseFun(int n) {
        fun = Math.min(fun + n, 10);
    }

    /**
     * Increases the sleep level by a specified amount.
     * 
     * @param n the amount to increase
     */
    public void increaseSleep(int n) {
        sleep = Math.min(sleep + n, 10);
    }

    // Methods to decrease each stat

    /**
     * Decreases the grooming level by a specified amount.
     * 
     * @param n the amount to decrease
     */
    public void decreaseGrooming(int n) {
        grooming = Math.max(grooming - n, 0);
    }

    /**
     * Decreases the hunger level by a specified amount.
     * 
     * @param n the amount to decrease
     */
    public void decreaseHunger(int n) {
        hunger = Math.max(hunger - n, 0);
    }

    /**
     * Decreases the fun level by a specified amount.
     * 
     * @param n the amount to decrease
     */
    public void decreaseFun(int n) {
        fun = Math.max(fun - n, 0);
    }

    /**
     * Decreases the sleep level by a specified amount.
     * 
     * @param n the amount to decrease
     */
    public void decreaseSleep(int n) {
        sleep = Math.max(sleep - n, 0);
    }
}
