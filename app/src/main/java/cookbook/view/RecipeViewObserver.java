package cookbook.view;

import cookbook.model.Recipe;
import java.time.LocalDate;
import java.util.ArrayList;

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
  void handleSaveTagsClicked(ArrayList<String> updatedTags, Recipe recipe);

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToHomePage();

  boolean addRecipeToWeeklyDinner(LocalDate date, Recipe recipe, int weekNumber);

  void goToShoppingList();

  void goToMyFavorite();

  void addRecipeToFavorites(Recipe recipe);

  void removeRecipeFromFavorites(Recipe recipe);

  void goToRecipe(Recipe recipe);

  void editRecipe(Recipe recipe, String newName, String newDescription, String newInstructions,
      ArrayList<String[]> newIngredients);

  void goToMessages();

  void addComment(Recipe recipe, String comment);

  void updateComment(int commentId, String comment);

  void deleteComment(int commentId);

  void goToHelp();


}
