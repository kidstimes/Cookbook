package cookbook.view;

import java.util.ArrayList;

/**
 * Interface for the AddRecipeView observer.
 */
public interface AddRecipeViewObserver {

  /**
   * Add a new recipe to the cookbook.
   */
  void handleSaveRecipeClicked(String[] recipeData,
      ArrayList<String[]> ingredientsData, ArrayList<String> tagsData);


  /**
   * Go to the recipe browser from the add recipe page.
   */
  void goToBrowser();

}
