package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.AddRecipeView;
import cookbook.view.AddRecipeViewObserver;
import java.util.ArrayList;
import javafx.scene.Node;


/**
 * Controller for managing adding a new recipe to the cookbook.
 */
public class AddRecipeController extends BaseController implements AddRecipeViewObserver {

  private AddRecipeView addRecipeView;


  /**
   * Add Recipe Controller Constructor.
   *
   * @param model the facade to the model
   * @param mainController the main controller
   */
  public AddRecipeController(CookbookFacade model,
       MainController mainController, String displayName) {
    super(model, mainController);
    System.out.println(model);
    this.addRecipeView = new AddRecipeView(displayName);
    this.addRecipeView.setObserver(this);
  }

  /**
   * Get the add recipeview.
   */
  public Node getView() {
    return this.addRecipeView.getView();
  }

  @Override
  public boolean handleSaveRecipeClicked(String[] recipeData,
      ArrayList<String[]> ingredients, ArrayList<String> tags) {
    model.addRecipe(recipeData, ingredients, tags);
    if (model.saveRecipeToDatabase(recipeData, ingredients, tags)) {
      mainController.goToBrowser();
      return true;
    } else {
      System.out.println("Error saving recipe to database");
      return false;
    }
  }



}
