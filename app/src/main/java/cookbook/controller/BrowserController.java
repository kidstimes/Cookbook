package cookbook.controller;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.Arrays;
import cookbook.model.Recipe;
import cookbook.model.CookbookFacade;
import cookbook.view.BrowserView;
import cookbook.view.BrowserViewObserver;

/**
 * Controller for managing the browsing of recipes.
 */
public class BrowserController implements BrowserViewObserver, BaseController {

  private ControllerManager controllerManager;
  private BrowserView browserView;
  private CookbookFacade model;

  /**
   * Browser Controller Constructor.
   *
   * @param controllerManager the main controller (used to navigate to other controllers)
   * @param model the facade to the model
   */
  public BrowserController(ControllerManager controllerManager, CookbookFacade model) {
    // initialize attributes
    this.controllerManager = controllerManager;
    this.model = model;
    this.browserView = new BrowserView(model.getRecipes());
    this.browserView.setObserver(this);
  }

  /**
   * Add the main controller as an attribute.
   *
   * @param manager the main controller
   */
  public void setControllerManager(ControllerManager manager) {
    this.controllerManager = manager;
  }

  /**
   * Display the recipe browser.
   */
  public void handleBrowseRecipesClicked() {
    this.controllerManager.showBrowserView();
  }

  /**
   * Reset recipes in view (remove filters).
   */
  public void updateRecipeList(ArrayList<Recipe> updatedRecipeList) {
    browserView.updateRecipeList(updatedRecipeList);
  }

  @Override
  public Node getView() {
    return this.browserView.getView();
  }

  @Override
  public void handleBackToHomeClicked() {
    // reset filters and recipes
    this.browserView.resetSearchInputs();
    this.browserView.displayRecipes(model.getRecipes());
    // go back to the home page
    this.controllerManager.showHomePageView();
  }

  @Override
  public void handleGoToRecipeClicked(Recipe recipe) {
    this.controllerManager.showRecipeView(recipe);
  }

  @Override
  public void handleSearch(String searchTextByName, String searchTextByIngredient, ObservableList<String> selectedTags) {
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

}
