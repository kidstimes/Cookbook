package cookbook.controller;

<<<<<<< HEAD
import cookbook.model.CookbookFacade;
import cookbook.view.HomePageView;
import cookbook.view.HomePageViewObserver;
import javafx.scene.Node;

=======
import javafx.scene.Node;
import cookbook.view.HomePageView;
import cookbook.view.HomePageViewObserver;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

/**
 * Controller for managing the home page.
 */
<<<<<<< HEAD
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


=======
public class HomePageController implements HomePageViewObserver, BaseController {

    private ControllerManager controllerManager;
    private HomePageView homePageView;

    /**
     * Home Page Controller Constructor.
     *
     * @param controllerManager the main controller (used to navigate to other controllers)
     */
    public HomePageController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.homePageView = new HomePageView();
        this.homePageView.setObserver(this);
    }

    @Override
    public Node getView() {
        return this.homePageView.getView();
    }

    @Override
    public void handleBrowseRecipesClicked() {
        this.controllerManager.showBrowserView();
    }

    @Override
    public void handleAddRecipeClicked() {
        this.controllerManager.showAddRecipeView();
    }
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}
