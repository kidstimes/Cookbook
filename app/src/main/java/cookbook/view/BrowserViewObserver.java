package cookbook.view;

import cookbook.model.Recipe;
import javafx.collections.ObservableList;

/**
 * Interface for the BrowserView observer.
 */
public interface BrowserViewObserver {

  /**
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



}
