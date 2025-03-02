package cookbook.controller;

<<<<<<< HEAD
import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.RecipeView;
import cookbook.view.RecipeViewObserver;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.Node;


/**
 * Controller for managing the detailed view of a recipe.
 */
public class RecipeController extends BaseController implements RecipeViewObserver {
  private RecipeView recipeView;

  /**Recipe Controller Constructor.
  *
  * @param model the cookbook facade
  * @param mainController the main controller
  */
  public RecipeController(CookbookFacade model, MainController mainController,
      Recipe recipe) {
    super(model, mainController);
    this.recipeView = new RecipeView(model.getUserDisplayName(),
         model.getUserId(), model.getLoggedOutUsers());
    this.recipeView.setObserver(this);
    this.recipeView.setRecipe(recipe);
    this.recipeView.setComments(model.getComments(recipe.getId()));
  }

  /**
   * Get the recipe view.
   */
=======
import javafx.scene.Node;
import cookbook.view.RecipeView;
import cookbook.view.RecipeViewObserver;
import cookbook.model.Recipe;

/**
 * Controller for managing the view of a recipe.
 */
public class RecipeController implements RecipeViewObserver, BaseController {

  private ControllerManager controllerManager;
  private RecipeView recipeView;
  private Recipe recipe;
  
  /**
   * Recipe Controller Constructor.
   *
   * @param controllerManager the main controller
   * @param recipe the recipe to display
   */
  public RecipeController(ControllerManager controllerManager, Recipe recipe) {
    this.controllerManager = controllerManager;
    this.recipe = recipe;
    this.recipeView = new RecipeView(recipe);
    this.recipeView.setObserver(this);
  }

  /**
   * Display the given recipe.
   *
   * @param recipe the recipe to display
   */
  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
    this.recipeView.setRecipe(recipe);
  }

  @Override
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
  public Node getView() {
    return this.recipeView.getView();
  }

<<<<<<< HEAD
  /**
   * Handle the save tags event.
   */
  @Override
  public void handleSaveTagsClicked(ArrayList<String> updatedTags, Recipe recipe) {
    model.addTagsToRecipe(updatedTags, recipe);
    model.updateTagToDatabase(updatedTags, recipe);
    mainController.goToRecipe(recipe);
  }

  @Override
  public boolean addRecipeToWeeklyDinner(LocalDate date, Recipe recipe, int weekNumber) {
    if (model.addRecipeToDinnerList(date, recipe) && model.saveWeeklyDinnerToDatabase()) {
      model.addRecipeToShoppingList(recipe, weekNumber);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void addRecipeToFavorites(Recipe recipe) {
    model.addRecipeToFavorites(recipe);
  }

  @Override
  public void removeRecipeFromFavorites(Recipe recipe) {
    model.removeRecipeFromFavorites(recipe);
  }

  @Override
  public void editRecipe(Recipe recipe, String newName, String newDescription,
      String newInstructions, ArrayList<String[]> newIngredients) {
    model.editRecipe(recipe, newName, newDescription, newInstructions, newIngredients);
    goToRecipe(recipe);
  }

  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }

  @Override
  public void addComment(Recipe recipe, String comment) {
    model.addComment(recipe, comment);
  }

  @Override
  public void updateComment(int commentId, String comment) {
    model.updateComment(commentId, comment);
  }

  @Override
  public void deleteComment(int commentId) {
    model.deleteComment(commentId);
  }

  @Override
  public boolean sendMessageToUser(String selectedUser, Recipe recipe, String message) {
    return model.sendMessageToUser(selectedUser, recipe, message);
  }

  @Override
  public String getDisplayNameByUsername(String createrUsername) {
    return model.getDisplayNameByUsername(createrUsername);
  }

}

=======
  @Override
  public void handleBackToBrowserClicked() {
    this.controllerManager.showBrowserView();
  }

  @Override
  public void handleBackToHomeClicked() {
    this.controllerManager.showHomePageView();
  }

}
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
