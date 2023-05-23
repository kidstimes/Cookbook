package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.AccountView;
import cookbook.view.AccountViewObserver;
import javafx.scene.Node;


/**
 * Controller for managing the account page of a user.
 */
public class AccountController extends BaseController implements AccountViewObserver {
  private AccountView accountView;

  /** Constructor for the account controller.
   *
   * @param model the cookbook facade
   * @param mainController the main controller
   */

  public AccountController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    this.accountView = new AccountView(model.getUserDisplayName(), model.getUserName());
    this.accountView.setObserver(this);
    if (model.getUserId() == 1) {
      this.accountView.setAdmin();
    }
  }

  /**
   * Get the account view.
   */
  public Node getView() {
    return this.accountView.getView();
  }



  @Override
  public void handlePasswordChange(String oldPassword, String newPassword) {
    if (model.checkPasswordForUser(oldPassword)) {
      if (model.changePasswordForUser(newPassword)) {
        mainController.goToHomePage();
      } else {
        accountView.showError("Password change failed");
      }
    } else {
      accountView.showError("Old password is incorrect");
    }
  }

  @Override
  public void changeDisplayName(String newDisplayName) {
    if (model.changeDisplayNameForUser(newDisplayName)) {
      mainController.goToHomePage();
    } else {
      accountView.showError("Display name change failed");
    }
  }

  @Override
  public void goToAdmin() {
    mainController.goToAdmin();
  }



  
}
