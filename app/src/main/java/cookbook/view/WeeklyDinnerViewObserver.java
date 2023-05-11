package cookbook.view;

import java.time.LocalDate;


/**
 * Interface for the WeeklyDinnerView observer.
 */
public interface WeeklyDinnerViewObserver {

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToRecipe(cookbook.model.Recipe recipe);

  void goToShoppingList();

  void goToMyFavorite();

  void removeRecipeFromWeeklyDinner(LocalDate dayDate, String recipeName);
  
  
}
