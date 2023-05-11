package cookbook.controller;


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
    this.recipeView = new RecipeView(model.getUserDisplayName());
    this.recipeView.setRecipe(recipe);
    this.recipeView.setObserver(this);
    this.mainController = mainController;
  }


  /**
   * Get the recipe view.
   */
  public Node getView() {
    return this.recipeView.getView();
  }



  public void goToBrowser() {
    mainController.goToBrowser();
  }


  /**
   * Handle the save tags event.
   */
  @Override
  public void handleSaveTagsClicked(ArrayList<String> updatedTags, Recipe recipe) {
    model.addTagsToRecipe(updatedTags, recipe);
    model.updateTagToDatabase(updatedTags, recipe);
    mainController.goToBrowser();
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
      String newInstructions, ArrayList<String[]> newIngredients, ArrayList<String> newTags) {
    model.editRecipe(recipe, newName, newDescription, newInstructions, newIngredients, newTags);
  }

  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }
}

