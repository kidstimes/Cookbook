package cookbook.view;

import java.util.ArrayList;

/**
 * Interface for the AddRecipeView observer.
 */
public interface AddRecipeViewObserver {

  /**
   * Go back to the browsing page.
   */
  void handleBackToBrowserClicked();

  /**
   * Add a new recipe to the cookbook.
   */
  void handleSaveRecipeClicked(String[] recipeData, 
      ArrayList<String[]> ingredientsData, ArrayList<String> tagsData);
}


