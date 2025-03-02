package cookbook.controller;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import cookbook.database.Database;
import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;

/**
 * The Main controller.
 * Manages all the other controllers that are responsible for functonalities of the cookbook.
 */
public class ControllerManager {

  private Stage stage;
  private BorderPane root;
  private CookbookFacade model;
  private Database database;
  private BaseController homePageController;
  private BaseController browserController;
  private BaseController recipeController;
  private BaseController addRecipeController;

  /**
   * Controller Manager Constructor.
   *
   * @param stage the main stage used by the view
   */
  public ControllerManager(Stage stage) {
    // initialize the view's main stage, model and database classes
    this.stage = stage;
    this.model = new CookbookFacade();
    this.database = new Database();

    // load data from the database
    loadRecipesFromDatabase();

    // initialize the rest of the controllers
    this.homePageController = new HomePageController(this);
    this.browserController = new BrowserController(this, model);
    this.addRecipeController = new AddRecipeController(this, model);

    // Initialize main stage layout
    this.initLayout();
  }

  /**
   * Get recipe classes from the Database class and add them to the model.
   */
  private void loadRecipesFromDatabase() {
    ArrayList<Recipe> recipes = database.loadAllRecipes();
    for (Recipe recipe : recipes) {
        model.addRecipe(recipe);
    }
  }

  /**
   * Initialize the scene and display the home page.
   */
  private void initLayout() {
    this.root = new BorderPane();
    Scene scene = new Scene(this.root, 1024.0, 768.0);
    this.stage.setScene(scene);
    this.stage.show();
    this.showHomePageView();
  }

  /**
   * Quit the cookbook.
   */
  public void end() {
    this.stage.close();
    System.exit(0);
  }

  /**
   * Display the home page.
   */
  public void showHomePageView() {
    showView(this.homePageController);
  }

  /**
   * Display the recipe browser.
   */
  public void showBrowserView() {
    showView(this.browserController);
  }

  /**
   * Display the given recipe.
   */
  public void showRecipeView(Recipe recipe) {
    this.recipeController = new RecipeController(this, recipe);
    showView(this.recipeController);
  }

  /**
   * Display the page for adding a new recipe.
   */
  public void showAddRecipeView() {
    showView(this.addRecipeController);
  }

  public void updateBrowserView() {
    ((BrowserController) browserController).updateRecipeList(model.getRecipes());
  }
  
  /**
   * Display the page associated with the given controller.
   */
  private void showView(BaseController controller) {
    this.root.setCenter(controller.getView());
  }

}
