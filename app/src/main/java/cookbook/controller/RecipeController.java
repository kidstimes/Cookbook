package cookbook.controller;


import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.RecipeView;
import cookbook.view.RecipeViewObserver;
import cookbook.view.FavoriteView; 
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Alert;


/**
 * Controller for managing the detailed view of a recipe.
 */
public class RecipeController implements RecipeViewObserver {

  private RecipeView recipeView;
  private CookbookFacade model;
  private MainController mainController;
  private FavoriteView favoriteView;

  /**Recipe Controller Constructor.
  *
  * @param model the cookbook facade
  * @param mainController the main controller
  */
  public RecipeController(CookbookFacade model, MainController mainController, Recipe recipe) {
    this.model = model;
    this.recipeView = new RecipeView();
    this.recipeView.setRecipe(recipe);
    this.recipeView.setObserver(this);
    this.mainController = mainController;
    this.favoriteView = favoriteView;
  }

  /**
   * Set the recipe to be displayed.
   *
   * @param recipe the recipe to be displayed
   */
  public void setRecipe(Recipe recipe) {
    System.out.println("Recipe: " + recipe);
    this.recipeView.setRecipe(recipe);
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
  public void handleSaveTagsClicked(ArrayList<String> updatedTags, String recipeName) {
    model.addTagsToRecipe(updatedTags, recipeName);
    model.updateTagToDatabase(updatedTags, recipeName);
    mainController.goToBrowser();
  }

  @Override
  public void handleStarClicked(Recipe recipe) {
    updateFavorites(recipe);
  }

  @Override
  public void handleAddToFavorites(Recipe recipe) {
    updateFavorites(recipe);
  }

  private void updateFavorites(Recipe recipe) {
    boolean isFavorite = model.toggleFavorite(recipe); 
    favoriteView.updateFavoriteRecipe(recipe, isFavorite);
    mainController.refreshFavoriteList();
    recipeView.updateStarButton();
  }

  @Override
  public void handleRemoveFromFavorites(Recipe recipe) {
    updateFavorites(recipe);
  }

  @Override
  public void handleBackButtonClicked() {
    // Navigate back to the previous view
    mainController.goBackToPreviousView();
  }


}
