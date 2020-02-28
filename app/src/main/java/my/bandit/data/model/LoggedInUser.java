package my.bandit.data.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    @Setter
    private int userId;
    private String displayName;
    @Getter
    @Setter
    private ArrayList<Integer> favourites = new ArrayList<>();
    @Getter
    @Setter
    private ArrayList<Integer> liked = new ArrayList<>();
    @Getter
    @Setter
    private ArrayList<Integer> disliked = new ArrayList<>();

    public LoggedInUser(String displayName) {
        this.displayName = displayName;
    }

    public int getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
