package cookbook.controller;

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
  public Node getView() {
    return this.recipeView.getView();
  }

  @Override
  public void handleBackToBrowserClicked() {
    this.controllerManager.showBrowserView();
  }

  @Override
  public void handleBackToHomeClicked() {
    this.controllerManager.showHomePageView();
  }

}
