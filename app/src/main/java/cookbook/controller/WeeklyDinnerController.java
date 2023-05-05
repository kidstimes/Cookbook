package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Dinner;
import cookbook.model.Recipe;
import cookbook.view.WeeklyDinnerView;
import cookbook.view.WeeklyDinnerViewObserver;
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
  public WeeklyDinnerController(CookbookFacade model, MainController mainController,
        String displayName, ArrayList<Dinner> dinnerList) { 
    super(model, mainController);
    this.weeklyDinnerView = new WeeklyDinnerView(displayName, dinnerList);
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
  
}

