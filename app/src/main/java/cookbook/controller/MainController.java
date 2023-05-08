package cookbook.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.FavoriteView;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The Main controller.
 * Manages the model, view, and all the other controllers.
 */
public class MainController {

  private Stage stage;
  private BorderPane root;
  private CookbookFacade model;
  private HomePageController homePageController;
  private BrowserController browserController;
  private RecipeController recipeController;
  private AddRecipeController addRecipeController;
  private LoginController loginController;
  private SignUpController signUpController;
  private FavoriteController favoriteController;
  private Node previousView;

  /**
   * Controller Constructor.
   *
   * @param primaryStage the stage of the program
   * @param model the cookbook's model
   */
  public MainController(Stage primaryStage, CookbookFacade model) {
    // Initialize view, model, and database
    this.stage = primaryStage;
    this.model = model;
    

    //model.setRecipes(database.loadAllRecipes());

    // Initialize controllers
    this.loginController = new LoginController(model, this);
    this.signUpController = new SignUpController(model, this);
    this.homePageController = new HomePageController(model, this);
    this.addRecipeController = new AddRecipeController(model, this);
    this.favoriteController = new FavoriteController(model, this);

    // Initialize the main layout of the program

  }

  /**
   * Run the cookbook.
   */
  public void runCookbook() {
    initMainLayout();

    // Animation & login
    goToLogin();

    // Quit
    // quitCookbook();
  }


  /**
   * Initialize the program main layout.
   */
  public void initMainLayout() {
    // Initialize the root pane
    this.root = new BorderPane();
    Scene scene = new Scene(this.root, 1200, 800);
    // Initialize the stage
    this.stage.setTitle("Cookbook");
    this.stage.setScene(scene);
    this.stage.show();
  
  }

  /**
   * Go to login.
   */
  public void goToLogin() {
    root.setCenter(loginController.getView());
  }

  /**
   * Go to sign up.
   */
  public void goToSignUp() {
    root.setCenter(signUpController.getView());
  }

  /**
   * Go to the home page.
   */
  public void goToHomePage() {
    setPreviousView(root.getCenter());
    homePageController.setDisplayName(model.getUserDisplayName());
    root.setCenter(homePageController.getView());
  }

  /**
   * Go to admin.
   */
  public void goToAdmin() {
    // admin.displayView();
  }

  /**
   * Go to the browser.
   */
  public void goToBrowser() {
    setPreviousView(root.getCenter());
    this.browserController = new BrowserController(model, this);
    root.setCenter(browserController.getView());
  }

  /**
   * Go to the given recipe.
   *
   * @param recipe the chosen recipe
   */
  public void goToRecipe(Recipe recipe) {
    setPreviousView(root.getCenter());
    recipeController = new RecipeController(model, this, recipe);
    root.setCenter(recipeController.getView());
  }

  /**
   * Go to add recipe.
   */
  public void goToAddRecipe() {
    setPreviousView(root.getCenter());
    root.setCenter(addRecipeController.getView());
  }


  /**
   * Go to favorite recipes.
   */
  public void goToFavoriteRecipes() {
    setPreviousView(root.getCenter());
    favoriteController.setFavoriteRecipes(model.loadFavoriteRecipes());
    root.setCenter(favoriteController.getView());
  }
  

  public void handleStarButtonClicked(Recipe recipe) {
    try {
      String currentUser = model.getCurrentUser();
      if (model.isRecipeFavorite(currentUser, recipe)) {
        model.removeRecipeFromFavorites(currentUser, recipe);
      } else {
        model.addRecipeToFavorites(currentUser, recipe);
      }
    } catch (SQLException e) {
      System.err.println("Error while handling star button click: " + e.getMessage());
      // You may also show an error message to the user using an alert, if you wish
    }
  }
  
  public boolean isRecipeFavorite(Recipe recipe) {
    try {
      String currentUser = model.getCurrentUser();
      return model.isRecipeFavorite(currentUser, recipe);
    } catch (SQLException e) {
      System.err.println("Error while checking if recipe is favorite: " + e.getMessage());
      // You may also show an error message to the user using an alert, if you wish
      return false;
    }
  }
  
  public void refreshFavoriteList() {
    ArrayList<Recipe> favoriteRecipes = model.loadFavoriteRecipes();
    favoriteController.getFavoriteView().setFavoriteRecipes(favoriteRecipes);
  }

  public FavoriteView getFavoriteView() {
    return favoriteController.getFavoriteView();
  }

  public void setPreviousView(Node previousView) {
    this.previousView = previousView;
  }

  public void goBackToPreviousView() {
    if (previousView != null) {
        root.setCenter(previousView);
    }
}


  public void userLogout() {
    model.userLogout();
    goToLogin();
  }
  
  /**
   * Quit the cookbook.
   */
  public void quitCookbook() {

    // Close the primary stage of the view.
    stage.close();
    System.exit(0);
  }

}
