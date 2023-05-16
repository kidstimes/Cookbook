package cookbook.view;

import java.util.ArrayList;

/**
 * Interface for the AddRecipeView observer.
 */
public interface AddRecipeViewObserver {

  /**
   * Add a new recipe to the cookbook.
   */
  boolean handleSaveRecipeClicked(String[] recipeData,
      ArrayList<String[]> ingredientsData, ArrayList<String> tagsData);


  /**
   * Go to the recipe browser from the add recipe page.
   */
  void goToBrowser();

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToHomePage();

  void goToShoppingList();

  void goToMyFavorite();

  void goToMessages();

}
