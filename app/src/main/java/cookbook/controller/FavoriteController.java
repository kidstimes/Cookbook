
package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.FavoriteView;
import cookbook.view.FavoriteViewObserver;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for managing the favorite recipes view.
 */
public class FavoriteController implements FavoriteViewObserver {

  private FavoriteView favoriteView;
  private CookbookFacade model;
  private MainController mainController;

  /**
   * FavoriteController Constructor.
   *
   * @param model the cookbook facade
   * @param mainController the main controller
   */
  public FavoriteController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.favoriteView = new FavoriteView();
    this.favoriteView.setObserver(this);
    this.mainController = mainController;
  }

  /**
   * Set the favorite recipes to be displayed.
   *
   * @param favoriteRecipes the list of favorite recipes
   */
  public void setFavoriteRecipes(List<Recipe> favoriteRecipes) {
    this.favoriteView.setFavoriteRecipes(favoriteRecipes);
  }

  /**
   * Get the favorite view.
   */
  public Node getView() {
    return this.favoriteView.getView();
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
      // You may also show an error message to the user using an alert
      return false;
    }
  }
  
  public FavoriteView getFavoriteView() {
    return favoriteView;
  }


  @Override
  public void handleBackButtonClicked() {
  // Navigate back to the homepage
    mainController.goBackToPreviousView();
  }

  @Override
   public void handleRemoveRecipeFromFavorites(Recipe recipe) {
    mainController.handleStarButtonClicked(recipe);
    favoriteView.removeFavoriteRecipe(recipe);
  }

  @Override
  public void handleRecipeClicked(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }

  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }


}


