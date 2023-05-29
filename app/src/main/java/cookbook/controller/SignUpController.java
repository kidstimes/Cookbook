package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.SignUpView;
import cookbook.view.SignUpViewObserver;
import javafx.scene.Node;

/**
 * Controller for managing the sign up view.
 */
public class SignUpController implements SignUpViewObserver {
  private SignUpView signUpView;
  private CookbookFacade model;
  private MainController mainController;

  /**
   * Sign Up Controller Constructor.
   */
  public SignUpController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.signUpView = new SignUpView();
    this.signUpView.setObserver(this);
    this.mainController = mainController;
  }

  /**
   * Get the sign up view.
   */
  public Node getView() {
    return this.signUpView.getView();
  }

  @Override
  public void handleSignUp(String username, String password,
         String confirmedPassword, String displayname) {
    if (model.userSignUp(username, password, displayname)) {
      signUpView.showInfo("You have successfully signed up! Please log in.");
      goToLogin();
    }
  }

  @Override
  public void goToLogin() {
    mainController.goToLogin();
  }

  @Override
  public boolean checkUsernameAlreadyExists(String username) {
    return model.checkIfUserNameExists(username);
  }
  
}
