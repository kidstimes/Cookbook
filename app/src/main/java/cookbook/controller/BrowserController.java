package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.BrowserView;
import cookbook.view.BrowserViewObserver;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * Controller for managing the browsing of recipes.
 */
public class BrowserController implements BrowserViewObserver {

  private BrowserView browserView;
  private CookbookFacade model;
  private MainController mainController;

  /**
  * Browser Controller Constructor.
  *
  * @param model the facade to the model
  * @param mainController the main controller
  */
  public BrowserController(CookbookFacade model, MainController mainController) {
    // initialize attributes
    ArrayList<Recipe> recipes = model.getRecipes();
    this.model = model;
    this.mainController = mainController;
    // become an observer of the browser's view.
    this.browserView = new BrowserView(recipes, model.getPrivateTagsForUser());
    this.browserView.setObserver(this);
  }

  /**
   * Get the browser view.
   */
  public Node getView() {
    browserView.updateRecipes(model.getRecipes());
    return this.browserView.getView();
  }

  /**
   *Handle the search event.
   */
  @Override
  public void handleSearch(String searchTextByName, String searchTextByIngredient,
      ObservableList<String> selectedTags) {
    // Convert keywords string into an arraylist
    ArrayList<String> keywords = new ArrayList<>();
    if (!searchTextByName.isEmpty()) {
      keywords.addAll(Arrays.asList(searchTextByName.split(" ")));
    }

    // Convert ingredients string into an arraylist
    ArrayList<String> ingredients = new ArrayList<>();
    if (!searchTextByIngredient.isEmpty()) {
      ingredients.addAll(Arrays.asList(searchTextByIngredient.split(" ")));
    }

    // Convert tags observable list into an arraylist
    ArrayList<String> tags = new ArrayList<>(selectedTags);

    // Apply the filters and display the filtered recipes
    browserView.displayRecipes(model.getRecipesWithFilters(keywords, ingredients, tags));
  }

  /**
   * Update recipes with the new filters.
   */
  public void updateDisplayedRecipes(ArrayList<Recipe> filteredResults) {
    this.browserView.displayRecipes(filteredResults);
  }


  
  @Override
  public void goToHomePage() {
    mainController.goToHomePage();
  }

  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }


}
