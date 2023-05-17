package cookbook.view;

import cookbook.model.Recipe;

/**
 * Observer interface for the my favourite view.
 */
public interface FavoriteViewObserver {

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToRecipe(cookbook.model.Recipe recipe);

  void goToShoppingList();

  void goToMyFavorite();

  void goToMessages();

  void removeRecipeFromFavorites(Recipe recipe);

  void goToHelp();
  
}
