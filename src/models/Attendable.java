package models;

import java.util.List;

public abstract class Attendable extends Model {

    private User administrator;
    private List<User> pending;
    private List<User> attending;

    public void attend(User user) {
        pending.remove(user);
        attending.remove(user);
    }
}
