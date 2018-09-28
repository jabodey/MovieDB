import java.util.HashMap;

/**
 * This User class only has the username field in this example.
 * <p>
 * However, in the real project, this User class can contain many more things,
 * for example, the user's shopping cart items.
 */
public class User {

    private final String username;
    private HashMap<String, Integer> cart = new HashMap<String, Integer>();
    
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
    

}
