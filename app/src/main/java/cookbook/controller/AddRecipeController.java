package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.AddRecipeView;
import cookbook.view.AddRecipeViewObserver;
import java.util.ArrayList;
import javafx.scene.Node;


/**
 * Controller for managing adding a new recipe to the cookbook.
 */
public class AddRecipeController implements AddRecipeViewObserver {

  private AddRecipeView addRecipeView;
  private CookbookFacade model;
  private MainController mainController;

  /**
   * Add Recipe Controller Constructor.
   *
   * @param model the facade to the model
   * @param mainController the main controller
   */
  public AddRecipeController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.addRecipeView = new AddRecipeView();
    this.mainController = mainController;
    this.addRecipeView.setObserver(this);
  }

  /**
   * Get the add recipeview.
   */
  public Node getView() {
    return this.addRecipeView.getView();
  }


  @Override
  public void handleSaveRecipeClicked(String[] recipeData,
    ArrayList<String[]> ingredientsData, ArrayList<String> tagsData) {
    model.addRecipe(recipeData, ingredientsData, tagsData);
  }


  @Override
  public void goToBrowser() {
    mainController.goToBrowser();
  }

}
