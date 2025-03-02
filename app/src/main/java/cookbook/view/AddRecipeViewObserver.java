package cookbook.view;

import java.util.ArrayList;

/**
 * Interface for the AddRecipeView observer.
 */
public interface AddRecipeViewObserver {

  /**
<<<<<<< HEAD
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

  void goToHelp();

  void goToAccount();

}
=======
   * Go back to the browsing page.
   */
  void handleBackToBrowserClicked();

  /**
   * Add a new recipe to the cookbook.
   */
  void handleSaveRecipeClicked(String[] recipeData, 
      ArrayList<String[]> ingredientsData, ArrayList<String> tagsData);
}


>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
