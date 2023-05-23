package cookbook.view;

/**
 * Interface for the LoginView observer.
 */
public interface LoginViewObserver {

  /**
   * Handles the login action when the login button is clicked.
   *
   * @param username the entered username
   * @param password the entered password
   */
  void handleLogin(String username, String password);


  /**
   * Go to sign up from the login view.
   */
  void goToSignUp();

}
