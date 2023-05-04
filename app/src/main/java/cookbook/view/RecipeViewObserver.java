package cookbook.view;

import java.time.LocalDate;
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

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToHomePage();

  void addRecipeToWeeklyDinner(LocalDate date, Recipe recipe);



}
