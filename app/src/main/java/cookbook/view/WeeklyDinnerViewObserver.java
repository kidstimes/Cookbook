package cookbook.view;

import java.time.LocalDate;

import cookbook.model.Recipe;


/**
 * Interface for the WeeklyDinnerView observer.
 */
public interface WeeklyDinnerViewObserver {

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToRecipe(Recipe recipe);

  void goToShoppingList();

  void goToMyFavorite();

  void removeRecipeFromWeeklyDinner(LocalDate dayDate, Recipe recipe, int week_number);

  void goToMessages();
  
  
}
