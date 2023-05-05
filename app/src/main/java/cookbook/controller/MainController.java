package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
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
  private WeeklyDinnerController weeklyDinnerController;

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

    // Initialize login and sign up controllers
    this.loginController = new LoginController(model, this);
    this.signUpController = new SignUpController(model, this);

  }


  /**
   * Initialize controllers after successful login of a user.
   */
  public void initializeControllersAfterLogin() {
    String userDisplayName = model.getUserDisplayName();
    this.homePageController = new HomePageController(model, this, userDisplayName);
    this.addRecipeController = new AddRecipeController(model, this, userDisplayName);    
    
  }

  /**
   * Run the cookbook.
   */
  public void runCookbook() {
    initMainLayout();
    // Load data from the database

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
    Scene scene = new Scene(this.root, 1440, 960);
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
    root.setCenter(homePageController.getView());
  }

  /**
   * Go to admin.
   */
  public void goToAdmin() {
    //TODO implement admin view
  }

  /**
   * Go to the browser.
   */
  public void goToBrowser() {
    model.loadAllRecipes();
    this.browserController = new BrowserController(model, this, model.getUserDisplayName());
    root.setCenter(browserController.getView());
  }

  /**
   * Go to the given recipe.
   *
   * @param recipe the chosen recipe
   */
  public void goToRecipe(Recipe recipe) {
    recipeController = new RecipeController(model, this, recipe, model.getUserDisplayName());
    root.setCenter(recipeController.getView());
  }

  /**
   * Go to add recipe.
   */
  public void goToAddRecipe() {
    root.setCenter(addRecipeController.getView());
  }

  /**
   * Go to weekly dinner lists.
   */
  public void goToWeeklyDinner() {
    this.weeklyDinnerController = new WeeklyDinnerController(model,
    this, model.getUserDisplayName(), model.getDinnerList());
    root.setCenter(weeklyDinnerController.getView());
  }

  /**
   * Log out the current user.
   */
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
