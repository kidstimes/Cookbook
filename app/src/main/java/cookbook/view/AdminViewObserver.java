package cookbook.view;

/**
 * Interface for the AdminView observer.
 */
public interface AdminViewObserver {

  boolean addUser(String username, String password, String displayName);

  void deleteUser(int userId);

  void userLogout();

  void goToAdmin();

  void editUser(int userId, String userName, String password, String displayName);
  
}
