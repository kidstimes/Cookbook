package cookbook.view;

import cookbook.model.Recipe;

/**
 * Observer for the messages view.
 */
public interface MessagesViewObserver {

  void userLogout();

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void goToWeeklyDinner();

  void goToMyFavorite();

  void goToShoppingList();

  void goToMessages();

  void goToRecipe(Recipe recipe);

  void goToHelp();

  void goToAccount();
  
}
