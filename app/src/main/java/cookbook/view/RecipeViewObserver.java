package cookbook.view;

import java.util.ArrayList;

import cookbook.model.Recipe;

/**
 * Interface for the RecipeView observer.
 */
public interface RecipeViewObserver {

  /**
   * Go to the recipe browser from the recipe view.
   */
  void goToBrowser();

  /**
   * Handles the save tags action when the save tags button is clicked.
   */
  void handleSaveTagsClicked(ArrayList<String> updatedTags, String recipeName);
  void handleStarClicked(Recipe recipe);
  void handleAddToFavorites(Recipe recipe);
  void handleRemoveFromFavorites(Recipe recipe);
  void handleBackButtonClicked();
  

}
