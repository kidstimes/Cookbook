package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.AdminView;
import cookbook.view.AdminViewObserver;
import javafx.scene.Node;

/**
 * Controller for managing the admin view.
 */
public class AdminController extends BaseController implements AdminViewObserver {

  private AdminView adminView;

  /** Constructor for the admin controller.
   *
   * @param model the cookbook facade
   * @param mainController the main controller
   */
  public AdminController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    this.adminView = new AdminView(model.loadAllUsers(), model.getUserDisplayName());
    this.adminView.setObserver(this);
  }

  public Node getView() {
    return this.adminView.getView();
  }

  /**
   * Add a user to the database.
   */
  public boolean addUser(String username, String password, String displayName) {
    if (model.checkIfUserNameExists(username)) {
      adminView.showError("Username already exists");
      return false;
    } else {
      model.userSignUp(username, password, displayName);
      return true;
    }
  }

  public void deleteUser(String username) {
    
  }

  @Override
  public void deleteUser(int userId) {
    model.deleteUser(userId);
  }

  public void userLogout() {
    mainController.userLogout();
  }

  public void goToAdmin() {
    mainController.goToAdmin();
  }

  @Override
  public void editUser(int userId, String userName,  String displayName) {
    if (model.checkIfUserNameExistsExceptSelf(userName, userId)) {
      adminView.showError("Username already exists");
    } else {
      model.editUser(userId, userName, displayName);
    }
  }

  @Override
  public void editUserPassword(int userId, String password) {
    model.editUserPassword(userId, password);
  }



    
}


  

