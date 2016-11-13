package enti;

public class CurrentUser {

    private static User userRegistry;

    public void logIn(User user) {
        userRegistry = user;
        user.setActive(true);
    }

    public boolean isLogIn(User user) {
        if (userRegistry.equals(user))
            return userRegistry.isActive();
        return false;
    }
    public User isLogIn(long id) {
        if (userRegistry.getId()==id)
            return userRegistry;
        return null;
    }
}
