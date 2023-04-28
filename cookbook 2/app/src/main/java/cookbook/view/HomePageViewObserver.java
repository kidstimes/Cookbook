package cookbook.view;

/**
 * Interface for the HomePageView observer.
 */
public interface HomePageViewObserver {

  /**
   * Display the recipe browser.
   */
  void handleBrowseRecipesClicked();
  
  /**
   * Display the page for adding a new recipe.
   */
  void handleAddRecipeClicked();
  
}
