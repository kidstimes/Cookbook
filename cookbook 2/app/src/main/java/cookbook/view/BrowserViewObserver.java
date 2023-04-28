package cookbook.view;

import cookbook.model.Recipe;
import javafx.collections.ObservableList;

/**
 * Interface for the BrowserView observer.
 */
public interface BrowserViewObserver {

  /**
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
  
}
