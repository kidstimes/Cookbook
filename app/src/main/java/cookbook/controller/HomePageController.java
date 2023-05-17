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
       model.checkWeeklyDinner(), model.checkNextWeekDinner());
    this.homePageView.setObserver(this);
    System.out.println(model.getUserId());
    if (model.getUserId() == 1) {
      this.homePageView.setAdmin();
    }
    
  }

  /**
   * Get the home page view.
   */
  public Node getView() {
    return this.homePageView.getView();
  }

  
  @Override
  public void handlePasswordChange(String oldPassword, String newPassword) {
    if (model.checkPasswordForUser(oldPassword)) {
      if (model.changePasswordForUser(newPassword)) {
        mainController.goToHomePage();
      } else {
        homePageView.showError("Password change failed");
      }
    } else {
      homePageView.showError("Old password is incorrect");
    }
  }

  @Override
  public void changeDisplayName(String newDisplayName) {
    if (model.changeDisplayNameForUser(newDisplayName)) {
      mainController.goToHomePage();
    } else {
      homePageView.showError("Display name change failed");
    }
  }

  @Override
  public void goToAdmin() {
    mainController.goToAdmin();
  }
}
