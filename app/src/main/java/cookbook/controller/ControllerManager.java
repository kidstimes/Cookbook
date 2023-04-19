package cookbook.controller;

import javafx.stage.Stage;

import java.util.ArrayList;

import cookbook.database.Database;
import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class ControllerManager {
  private Stage stage;
  private BorderPane root;
  private CookbookFacade cookBook;
  private Database database;
  private BaseController homePageController;
  private BaseController browserController;
  private BaseController recipeController;



  public ControllerManager(Stage stage) {
    this.stage = stage;
    this.cookBook = new CookbookFacade();
    this.database = new Database();
    loadRecipesFromDatabase();
    this.initControllers();
    this.initLayout();
  }

  public CookbookFacade getCookbook() {
    return this.cookBook;
  }

  private void loadRecipesFromDatabase() {
    ArrayList<Recipe> recipes = database.loadAllRecipes();
    for (Recipe recipe : recipes) {
        cookBook.addRecipe(recipe);
    }
  }

  private void initControllers() {
    this.homePageController = new HomePageController(this);
    this.browserController = new BrowserController(this);
    this.recipeController = new RecipeController(this);
  }

  private void initLayout() {
    this.root = new BorderPane();
    Scene scene = new Scene(this.root, 800.0, 600.0);
    this.stage.setScene(scene);
    this.stage.show();
    this.showHomePageView();
  }


  private void setTitle(String title) {
    this.stage.setTitle("Cookbook " + title);
  }

  public void end() {
    this.stage.close();
    System.exit(0);
  }


  public void showHomePageView() {
    showView(this.homePageController);
  }


  public void showBrowserView() {
    showView(this.browserController);
  }

  public void showRecipeView() {
    showView(this.recipeController);
  }

  private void showView(BaseController controller) {
    this.root.setCenter(controller.getView());
    this.setTitle(controller.getTitle());
}
} 