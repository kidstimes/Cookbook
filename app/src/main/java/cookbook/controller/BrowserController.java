package cookbook.controller;

<<<<<<< HEAD
import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.BrowserView;
import cookbook.view.BrowserViewObserver;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.scene.Node;
=======
import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.Arrays;
import cookbook.model.Recipe;
import cookbook.model.CookbookFacade;
import cookbook.view.BrowserView;
import cookbook.view.BrowserViewObserver;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

/**
 * Controller for managing the browsing of recipes.
 */
<<<<<<< HEAD
public class BrowserController extends BaseController implements BrowserViewObserver {
  private BrowserView browserView;
  /**
  * Constructor for the browser controller.

  * @param model is the cookbookfacade model
  * @param mainController is the main controller
  */
  public BrowserController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    ArrayList<Recipe> recipes = model.getRecipes();
    this.browserView =
         new BrowserView(recipes, model.getPrivateTagsForUser(), model.getUserDisplayName());
=======
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
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
    this.browserView.setObserver(this);
  }

  /**
<<<<<<< HEAD
   * Get the browser view.
   */
=======
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
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
  public Node getView() {
    return this.browserView.getView();
  }

<<<<<<< HEAD
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
=======
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
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
    }

    // Convert ingredients string into an arraylist
    ArrayList<String> ingredients = new ArrayList<>();
    if (!searchTextByIngredient.isEmpty()) {
<<<<<<< HEAD
      ingredients.addAll(Arrays.asList(searchTextByIngredient.split(" ")));
=======
        ingredients.addAll(Arrays.asList(searchTextByIngredient.split(" ")));
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
    }

    // Convert tags observable list into an arraylist
    ArrayList<String> tags = new ArrayList<>(selectedTags);

    // Apply the filters and display the filtered recipes
    browserView.displayRecipes(model.getRecipesWithFilters(keywords, ingredients, tags));
  }

<<<<<<< HEAD


  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }


  @Override
  public void removeRecipeFromFavorite(Recipe recipe) {
    model.removeRecipeFromFavorites(recipe);
  }

  @Override
  public void addRecipeToFavorite(Recipe recipe) {
    model.addRecipeToFavorites(recipe);
  }



=======
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}
