package sample;

/**
 * Created by lozog on 30.01.2018.
 */
public class Task {
    private int id;
    private String title;
    private int estimate;
    private User owner;
    private String description;

    public Task(int id, String title, int estimate, User owner, String description) {
        this.id = id;
        this.title = title;
        this.estimate = estimate;
        this.owner = owner;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
