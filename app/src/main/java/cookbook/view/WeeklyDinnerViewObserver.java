package cookbook.view;



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

  
  
}
