
/**
 * Phoenix class that implements the Pet interface.
 * 
 * @author William Du
 * @version 1.0
 * @since 1.0
 */
public class Phoenix implements Pet {
    private String name;
    private int grooming = 10;
    private int hunger = 10;
    private int fun = 10;
    private int sleep = 10;
    private final String type = "phoenix"; // Constant since the type is fixed for Phoenix

    // Constructor
    public Phoenix(String name) {
        this.name = name;
    }

    
    /** 
     * @return String
     */
    // Getters (from the Pet interface)
    @Override
    public String getName() {
        return name;
    }

    
    /** 
     * @return int
     */
    @Override
    public int getGrooming() {
        return grooming;
    }

    
    /** 
     * @return int
     */
    @Override
    public int getSleep() {
        return sleep;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getHunger() {
        return hunger;
    }

    @Override
    public int getFun() {
        return fun;
    }

    // Setters (from the Pet interface)
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setGrooming(int grooming) {
        this.grooming = grooming;
    }

    @Override
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public void setFun(int fun) {
        this.fun = fun;
    }

    @Override
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    // Behavior methods (from the Pet interface)
    @Override
    public void eat() {
        System.out.println(name + " is eating mystical fruits.");
        if (hunger < 10) {
            hunger++;
        } else {
            System.err.println(name + " is full!");
        }
    }

    @Override
    public void play() {
        if (hunger <= 0 || sleep <= 0) {
            System.err.println(name + " is too hungry or tired to play! Feed or let it sleep first.");
        } else {
            System.out.println(name + " is playing and having fun!");
            if (fun < 10) {
                fun += 2;
            } else {
                System.err.println(name + " is already fully entertained!");
            }
            if (hunger > 0) {
                hunger--;
            }
            if (sleep > 0) {
                sleep--;
            }
        }
    }

    @Override
    public void sleep() {
        System.out.println(name + " is sleeping to regain energy.");
        if (sleep < 10) {
            sleep++;
        } else {
            System.err.println(name + " is already fully rested!");
        }
    }

    @Override
    public void groom() {
        System.out.println(name + " is being groomed.");
        if (grooming < 10) {
            grooming++;
        } else {
            System.err.println(name + " is already perfectly groomed!");
        }
    }

    // Methods to increase each stat
    @Override
    public void increaseGrooming(int n) {
        grooming = Math.min(grooming + n, 10);
    }

    @Override
    public void increaseHunger(int n) {
        hunger = Math.min(hunger + n, 10);
    }

    @Override
    public void increaseFun(int n) {
        fun = Math.min(fun + n, 10);
    }

    @Override
    public void increaseSleep(int n) {
        sleep = Math.min(sleep + n, 10);
    }

    // Methods to decrease each stat
    @Override
    public void decreaseGrooming(int n) {
        grooming = Math.max(grooming - n, 0);
    }

    @Override
    public void decreaseHunger(int n) {
        hunger = Math.max(hunger - n, 0);
    }

    @Override
    public void decreaseFun(int n) {
        fun = Math.max(fun - n, 0);
    }

    @Override
    public void decreaseSleep(int n) {
        sleep = Math.max(sleep - n, 0);
    }
}