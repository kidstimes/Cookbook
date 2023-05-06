package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.HomePageView;
import cookbook.view.HomePageViewObserver;
import javafx.scene.Node;


/**
 * Controller for managing the home page.
 */
public class HomePageController implements HomePageViewObserver {
  private HomePageView homePageView;
  private CookbookFacade model;
  private MainController mainController;

  /** Home Page Controller Constructor.
   *
   * @param model the facade to the model
   * @param mainController the main controller
   */
  public HomePageController(CookbookFacade model, MainController mainController,
      String displayName) {
    this.model = model;
    this.mainController = mainController;
    this.homePageView = new HomePageView(displayName, model.checkWeeklyDinner());
    this.homePageView.setObserver(this);
    
  }

  /**
   * Get the home page view.
   */
  public Node getView() {
    return this.homePageView.getView();
  }


  @Override
  public void goToBrowser() {
    mainController.goToBrowser();
  }


  @Override
  public void goToAddRecipe() {
    mainController.goToAddRecipe();
  }

  @Override
  public void userLogout() {
    mainController.userLogout();
  }

  @Override
  public void goToWeeklyDinner() {
    mainController.goToWeeklyDinner();
  }

  @Override
  public void goToShoppingList() {
    mainController.goToShoppingList();
  }

  @Override
  public void goToMyFavorite() {
    mainController.goToMyFavorite();
  }
}
