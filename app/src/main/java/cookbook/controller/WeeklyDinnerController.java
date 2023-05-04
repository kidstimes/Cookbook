package cookbook.controller;

import cookbook.view.WeeklyDinnerViewObserver;
import cookbook.view.WeeklyDinnerView;
import cookbook.model.CookbookFacade;
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
  public WeeklyDinnerController(CookbookFacade model, MainController mainController, String displayName, ArrayList dinnerList) { 
    super(model, mainController);
    this.weeklyDinnerView = new WeeklyDinnerView(displayName, dinnerList);
    this.weeklyDinnerView.setObserver(this);
  }


  //Get the view
  public Node getView() {
    return this.weeklyDinnerView.getView();
  }
  
}

