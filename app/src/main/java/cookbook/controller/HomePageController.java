package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.HomePageView;
import cookbook.view.HomePageViewObserver;
import javafx.scene.Node;


/**
 * Controller for managing the home page.
 */
public class HomePageController extends BaseController implements HomePageViewObserver {
  private HomePageView homePageView;

  /** Home Page Controller Constructor.
   *
   * @param model the facade to the model
   * @param mainController the main controller
   */
  public HomePageController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    this.homePageView = new HomePageView(model.getUserDisplayName(),
        model.checkWeeklyDinner(), model.checkNextWeekDinner(),
        model.getNumberUnreadMessages(), model.getNumberOfFavoriteRecipes());
    this.homePageView.setObserver(this);
    
  }

  /**
   * Get the home page view.
   */
  public Node getView() {
    return this.homePageView.getView();
  }


}
