package cookbook.view;

/**
 * Interface for the SignUpView observer.
 */
public interface SignUpViewObserver {

  /**
   * Handles the sign up action when the sign up button is clicked.
   *
   * @param username the entered username
   * @param password the entered password
   * @param confirmedPassword the confirmed password
   */
  void handleSignUp(String username, String password, String confirmedPassword, String displayname);

  /**
   * Go to login from the sign up view.
   */
  void goToLogin();

}
