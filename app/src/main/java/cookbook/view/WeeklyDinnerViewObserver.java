package cookbook.view;

import cookbook.model.Recipe;
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

  void goToRecipe(Recipe recipe);

  void goToShoppingList();

  void goToMyFavorite();

  void removeRecipeFromWeeklyDinner(LocalDate dayDate, Recipe recipe);

  void goToMessages();

  void goToHelp();

  void goToAccount();
  
  
}
