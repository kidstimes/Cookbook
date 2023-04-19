package cookbook.view;

import cookbook.model.Recipe;
import javafx.collections.ObservableList;

public interface BrowserViewObserver {
  void handleBackToHomeClicked();
  void handleGoToRecipeClicked(Recipe recipe);
  void handleSearch(String searchType, String searchText, ObservableList<String> selectedTags);
  
}
