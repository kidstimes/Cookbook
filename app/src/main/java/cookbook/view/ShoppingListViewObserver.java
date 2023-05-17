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

  void goToMessages();

  void editIngredientInShoppingList(String ingredientName, float newQuantity, int weekNumber);

  void deleteIngredientInShoppingList(String ingredientName, int weekNumber);

  void refreshShoppingListWithWeeklyDinnerList();

  void updateShoppingList();
  
}
