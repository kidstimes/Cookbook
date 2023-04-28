package cookbook.controller;

import javafx.scene.Node;
import java.util.ArrayList;
import cookbook.model.Recipe;
import cookbook.model.CookbookFacade;
import cookbook.view.AddRecipeView;
import cookbook.view.AddRecipeViewObserver;

/**
 * Controller for managing adding a new recipe to the cookbook.
 */
public class AddRecipeController implements BaseController, AddRecipeViewObserver {

  private AddRecipeView addRecipeView;
  private ControllerManager controllerManager; 
  private CookbookFacade model;

  /**
   * Add Recipe Controller Constructor.
   *
   * @param controllerManager the main controller (used to navigate to other controllers)
   * @param model the facade to the model
   */
  public AddRecipeController(ControllerManager controllerManager, CookbookFacade model) {
    this.controllerManager = controllerManager; 
    this.model = model;
    this.addRecipeView = new AddRecipeView();
    this.addRecipeView.setObserver(this);
  }

  @Override
  public Node getView() {
    return this.addRecipeView.getView();
  }

  @Override
  public void handleBackToBrowserClicked() {
    controllerManager.showBrowserView(); 
  }

  @Override
  public void handleSaveRecipeClicked(String[] recipeData, 
      ArrayList<String[]> ingredientsData, ArrayList<String> tagsData) {
    Recipe recipe = new Recipe(recipeData[0], recipeData[1], 
        recipeData[2], ingredientsData, tagsData);
    model.addRecipe(recipe); 
    controllerManager.updateBrowserView();
    controllerManager.showBrowserView(); 
  }
}
