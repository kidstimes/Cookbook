package cookbook.view;

/**
 * Interface for the AdminView observer.
 */
public interface AdminViewObserver {

  boolean addUser(String username, String password, String displayName);

  void deleteUser(int userId);

  void userLogout();

  void goToAdmin();

  void editUser(int userId, String userName, String displayName);

  void editUserPassword(int userId, String password);

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void goToWeeklyDinner();

  void goToShoppingList();

  void goToMyFavorite();

  void goToMessages();

  void goToHelp();

  
  
}
