package cookbook.controller;

<<<<<<< HEAD
import cookbook.model.CookbookFacade;
import cookbook.view.AddRecipeView;
import cookbook.view.AddRecipeViewObserver;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Alert;

=======
import javafx.scene.Node;
import java.util.ArrayList;
import cookbook.model.Recipe;
import cookbook.model.CookbookFacade;
import cookbook.view.AddRecipeView;
import cookbook.view.AddRecipeViewObserver;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

/**
 * Controller for managing adding a new recipe to the cookbook.
 */
<<<<<<< HEAD
public class AddRecipeController extends BaseController implements AddRecipeViewObserver {

  private AddRecipeView addRecipeView;

=======
public class AddRecipeController implements BaseController, AddRecipeViewObserver {

  private AddRecipeView addRecipeView;
  private ControllerManager controllerManager; 
  private CookbookFacade model;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

  /**
   * Add Recipe Controller Constructor.
   *
<<<<<<< HEAD
   * @param model the facade to the model
   * @param mainController the main controller
   */
  public AddRecipeController(CookbookFacade model,
       MainController mainController) {
    super(model, mainController);
    this.addRecipeView = new AddRecipeView(model.getUserDisplayName());
    this.addRecipeView.setObserver(this);
  }

  /**
   * Get the add recipeview.
   */
=======
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
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
  public Node getView() {
    return this.addRecipeView.getView();
  }

  @Override
<<<<<<< HEAD
  public boolean handleSaveRecipeClicked(String[] recipeData,
      ArrayList<String[]> ingredients, ArrayList<String> tags) {
    //check if recipe name is already in database
    if (model.checkRecipeName(recipeData[0])) {
      addRecipeView.showInlineStyledAlert(Alert.AlertType.WARNING, "Recipe Exists",
          String.format("Recipe named %s already in cookbook. "
              + "Please check and enter again.", recipeData[0]));
      return false;
    }
    
    if (model.saveRecipeToDatabase(recipeData, ingredients, tags)) {
      model.addRecipe(recipeData, ingredients, tags);
      mainController.goToBrowser();
      return true;
    } else {
      System.out.println("Error saving recipe to database");
      return false;
    }
  }



=======
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
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}
