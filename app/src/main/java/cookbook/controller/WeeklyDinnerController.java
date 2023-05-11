package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Dinner;
import cookbook.model.Recipe;
import cookbook.view.WeeklyDinnerView;
import cookbook.view.WeeklyDinnerViewObserver;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.Node;


/**
 * Controller for the weekly dinner view.
 */
public class WeeklyDinnerController extends BaseController implements WeeklyDinnerViewObserver {
  private WeeklyDinnerView weeklyDinnerView;

  /**
   * Constructor for the weekly dinner controller.
   */
  public WeeklyDinnerController(CookbookFacade model, MainController mainController) { 
    super(model, mainController);
    this.weeklyDinnerView = new WeeklyDinnerView(model.getUserDisplayName(), model.getDinnerList());
    this.weeklyDinnerView.setObserver(this);
  }


  //Get the view
  public Node getView() {
    return this.weeklyDinnerView.getView();
  }

  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }
  
  @Override
  public void removeRecipeFromWeeklyDinner(LocalDate dayDate, Recipe recipe, int week_number) {
    model.removeRecipeFromWeeklyDinner(dayDate, recipe);
    model.deleteRecipeFromShoppingList(recipe, week_number);
  }

}

