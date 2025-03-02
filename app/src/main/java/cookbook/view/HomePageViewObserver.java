package cookbook.view;

/**
<<<<<<< HEAD
 * Observer for the home page view.
=======
 * Interface for the HomePageView observer.
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
 */
public interface HomePageViewObserver {

  /**
<<<<<<< HEAD
   * Go to the recipe browser from the home page.
   */
  void goToBrowser();

  /**
   * Go to the add recipe page from the home page.
   */
  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToShoppingList();

  void goToMyFavorite();

  void goToMessages();

  void goToHomePage();

  void goToHelp();

  void goToAccount();


=======
   * Display the recipe browser.
   */
  void handleBrowseRecipesClicked();
  
  /**
   * Display the page for adding a new recipe.
   */
  void handleAddRecipeClicked();
  
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}
