package cookbook.view;

/**
 * Interface for the AdminView observer.
 */
public interface AdminViewObserver {

  void addUser(String username, String password, String displayName);

  void deleteUser(String username);

  void editUser(String username, String password, String displayName);

  void logout();

  void goToAdmin();
  
}
