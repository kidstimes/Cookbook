package cookbook.view;

/**
 * Observer for the home page view.
 */
public interface HomePageViewObserver {

  /**
   * Go to the recipe browser from the home page.
   */
  void goToBrowser();

  /**
   * Go to the add recipe page from the home page.
   */
  void goToAddRecipe();

  void userLogout();

}
