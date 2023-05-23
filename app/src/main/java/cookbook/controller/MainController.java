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
  private LoginController loginController;
  private SignUpController signUpController;

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
   * Run the cookbook.
   */
  public void runCookbook() {
    initMainLayout();
    goToLogin();
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
    HomePageController homePageController = new HomePageController(model, this);
    root.setCenter(homePageController.getView());
  }

  /**
   * Go to the browser.
   */
  public void goToBrowser() {
    BrowserController browserController = new BrowserController(model, this);
    root.setCenter(browserController.getView());
  }

  /**
   * Go to the given recipe.
   *
   * @param recipe the chosen recipe
   */
  public void goToRecipe(Recipe recipe) {
    RecipeController recipeController = new RecipeController(model, this, recipe);
    root.setCenter(recipeController.getView());
  }

  /**
   * Go to add recipe.
   */
  public void goToAddRecipe() {
    AddRecipeController addRecipeController = new AddRecipeController(model, this);
    root.setCenter(addRecipeController.getView());
  }

  /**
   * Go to my favorite recipes.
   */
  public void goToMyFavorite() {
    FavoriteController favoriteController = new FavoriteController(model, this);
    root.setCenter(favoriteController.getView());
  }

  /**
   * Go to weekly dinner lists.
   */
  public void goToWeeklyDinner() {
    WeeklyDinnerController weeklyDinnerController = new WeeklyDinnerController(model, this);
    root.setCenter(weeklyDinnerController.getView());
  }


  /**
   * Go to shopping list.
   */
  public void goToShoppingList() {
    ShoppingListController shoppingListController = new ShoppingListController(model, this);
    root.setCenter(shoppingListController.getView());
  }


  /**
   * Go to my messsages.
   */
  public void goToMessages() {
    MessagesController messagesController = new MessagesController(model, this);
    root.setCenter(messagesController.getView());
  }


  /**
   * Go to admin.
   */
  public void goToAdmin() {
    AdminController adminController = new AdminController(model, this);
    root.setCenter(adminController.getView());
  }

  /**
   * Go to account settings.
   */
  public void goToAccount() {
    AccountController accountController = new AccountController(model, this);
    root.setCenter(accountController.getView());
  }

  /**
   * Go to help page.
   */
  public void goToHelp() {
    HelpPageController helpPageController = new HelpPageController(model, this);
    root.setCenter(helpPageController.getView());
  }


  /**
   * Log out the current user.
   */
  public void userLogout() {
    model.userLogout();
    goToLogin();
  }



}
