package cookbook.controller;

import javafx.scene.Node;
import cookbook.view.RecipeView;
import cookbook.model.Recipe;

public class RecipeController implements BaseController {
  private ControllerManager controllerManager;
  private RecipeView recipeView;
  private Recipe recipe;
  

  public RecipeController(ControllerManager controllerManager) {
    this.controllerManager = controllerManager;
    this.recipe = this.controllerManager.getCookbook().getRecipes().get(0);
    this.recipeView = new RecipeView(this, recipe);
  }


  public String getTitle() {
    return "Recipe for" + this.recipe.getName();
  }

  public Node getView() {
    return this.recipeView.getView();
  }

  public void handleBackToBrowserClicked() {
    this.controllerManager.showBrowserView();
  }
  
}
