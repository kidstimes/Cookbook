package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.FavoriteView;
import cookbook.view.FavoriteViewObserver;
import java.util.ArrayList;
import javafx.scene.Node;

/**
 * Controller for the my favourite view.
 */
public class FavoriteController extends BaseController implements FavoriteViewObserver {
  private FavoriteView favouriteView;

  /**
   * Constructor for the my favourite controller.
   */
  public FavoriteController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    ArrayList<Recipe> favoriteRecipes = model.getFavoriteRecipes();
    this.favouriteView = new FavoriteView(favoriteRecipes, model.getUserDisplayName());
    this.favouriteView.setObserver(this);

  }

  //Get the view
  public Node getView() {
    return this.favouriteView.getView();
  }

  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }

  @Override
  public void removeFromFavorites(Recipe recipe) {
    model.removeRecipeFromFavorites(recipe);
  }

  
}
