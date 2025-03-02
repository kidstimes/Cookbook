package cookbook.view;

import cookbook.model.Recipe;
import javafx.collections.ObservableList;

/**
 * Interface for the BrowserView observer.
 */
public interface BrowserViewObserver {

  /**
<<<<<<< HEAD
   * Flow control for applying filters during search.
   */
  void handleSearch(String searchType, String searchText, ObservableList<String> selectedTags);

  /**
   * Go to the home page from the browser.
   */
  void goToHomePage();

  /**
   * Go to the recipe page from the browser.
   *
   * @param recipe the chosen recipe
   */
  void goToRecipe(Recipe recipe);

  void goToBrowser();

  void goToWeeklyDinner();

  void goToAddRecipe();

  void userLogout();

  void goToShoppingList();

  void goToMyFavorite();

  void addRecipeToFavorite(Recipe recipe);

  void removeRecipeFromFavorite(Recipe recipe);

  void goToMessages();

  void goToHelp();

  void goToAccount();



=======
   * Go back to the home page.
   */
  void handleBackToHomeClicked();

  /**
   * Display the recipe chosen by the user.
   */
  void handleGoToRecipeClicked(Recipe recipe);

  /**
   * Flow control for applying filters during search.
   */
  void handleSearch(String searchType, String searchText, ObservableList<String> selectedTags);
  
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}
