package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.LoginView;
import cookbook.view.LoginViewObserver;
import javafx.scene.Node;


/**
 * Controller for managing the login view.
 */
public class LoginController implements LoginViewObserver {
  private LoginView loginView;
  private CookbookFacade model;
  private MainController mainController;

  /**
   * Login Controller Constructor.
   *
   * @param model the facade to the model
   * @param mainController the main controller
   */
  public LoginController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.loginView = new LoginView();
    loginView.setObserver(this);
    this.mainController = mainController;
  }

  /**
   * Get the login view.
   */
  public Node getView() {
    return this.loginView.getView();
  }


  // Login logic, check if username and password match in database
  @Override
  public void handleLogin(String username, String password) {
    if (model.isDeletedUser(username)) {
      loginView.showError("This user has been deleted");
      return;
    }
    if (model.checkIfUserNameExists(username)) {
      if (model.userLogin(username, password)) {
        model.setCurrentUser(username);
        model.loadAllRecipes();
        model.loadWeeklyDinnerFromDatabase();
        model.loadFavoriteRecipes();
        mainController.goToHomePage();
      } else {
        loginView.showError("Incorrect password");
      }
    } else {
      loginView.showError("Username does not exist");
    }
  }


  @Override
  public void goToHomePage() {
    mainController.goToHomePage();
  }

  @Override
  public void goToSignUp() {
    mainController.goToSignUp();
  }


}
