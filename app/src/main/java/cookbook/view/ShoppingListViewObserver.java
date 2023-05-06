package cookbook.view;


/**
 * Interface for the ShoppingListView observer.
 */
public interface ShoppingListViewObserver {

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToMyFavorite();

  void goToShoppingList();
  
}
