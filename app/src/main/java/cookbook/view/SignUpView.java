package cookbook.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;





/**
 * View class for the sign up page.
 */
public class SignUpView {
  private TextField usernameField;
  private PasswordField passwordField;
  private PasswordField confirmedPasswordField;
  private BorderPane view;
  private SignUpViewObserver observer;

  /**
   * Constructor for the sign up view.
   */
  public SignUpView() {
    this.view = new BorderPane();
    initLayout();
  }

  /**
   * Set an observer of the sign up view.
   */
  public void setObserver(SignUpViewObserver observer) {
    this.observer = observer;
  }

  /**
   * Get a sign up view.
   */
  public Node getView() {
    return view;
  }

  /**
   * Initialize the layout.
   */
  private void initLayout() {

    view = new BorderPane();
    view.setStyle("-fx-background-color: beige;");
    VBox mainContainer = new VBox(10);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(20));

    // Create the ImageView for the image
    Image image = new Image("https://i.imgur.com/bsyAZZc.png");
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(600); // Adjust the width of the image view
    imageView.setFitHeight(150); // Adjust the height of the image view

    // Create the fade animation
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500), imageView);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
    fadeTransition.setAutoReverse(true);
    fadeTransition.play();

    // Add the ImageView to the main container
    mainContainer.getChildren().add(imageView);


    Label signUpMessage = new Label("Sign Up to Cookbook");
    signUpMessage.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

    GridPane signUpGrid = new GridPane();
    signUpGrid.setHgap(10);
    signUpGrid.setVgap(10);
    signUpGrid.setAlignment(Pos.CENTER);


    Label usernameLabel = new Label("Username:");
    //set the usernameLabel font and size
    usernameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    usernameField = new TextField();
    signUpGrid.add(usernameLabel, 0, 0);
    signUpGrid.add(usernameField, 1, 0);
    Label passwordLabel = new Label("Password:");
    //set the passwordLabel font and size
    passwordLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    passwordField = new PasswordField();
    signUpGrid.add(passwordLabel, 0, 1);
    signUpGrid.add(passwordField, 1, 1);
    Label confirmedPasswordLabel = new Label("Confirm Password:");
    //set the confirmedPasswordLabel font and size
    confirmedPasswordLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    confirmedPasswordField = new PasswordField();
    signUpGrid.add(confirmedPasswordLabel, 0, 2);
    signUpGrid.add(confirmedPasswordField, 1, 2);
    Label displayNameLabel = new Label("Display Name:");
    //set the displayNameLabel font and size
    displayNameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    TextField displayNameField = new TextField();
    signUpGrid.add(displayNameLabel, 0, 3);
    signUpGrid.add(displayNameField, 1, 3);
    Button signUpButton = new Button("Sign Up");
    //set signup button style
    signUpButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
    signUpButton.setMaxWidth(Double.MAX_VALUE);
    signUpButton.setOnAction(event -> {
      String username = usernameField.getText();
      String password = passwordField.getText();
      String confirmedPassword = confirmedPasswordField.getText();
      String displayName = displayNameField.getText();
      if (!validateInput(username, password, confirmedPassword, displayName)) {
        return;
      }
      observer.handleSignUp(username, password, confirmedPassword, displayName);
      //clear the text fields
      usernameField.clear();
      passwordField.clear();
      confirmedPasswordField.clear();
      displayNameField.clear();

    });
    signUpGrid.add(signUpButton, 0, 4, 2, 1);

    Label alreadyHaveAccountLabel = new Label("Already have an account?");
    //set the alreadyHaveAccountLabel font and size
    alreadyHaveAccountLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    Hyperlink loginLink = new Hyperlink("Click here to log in");
    //set the loginLink font and size
    loginLink.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    loginLink.setOnAction(event -> {
      // Handle the login link click event
      observer.goToLogin();
      //clear the text fields
      usernameField.clear();
      passwordField.clear();
      confirmedPasswordField.clear();
      displayNameField.clear();
    });

    HBox loginContainer = new HBox(5);
    loginContainer.setAlignment(Pos.CENTER);
    loginContainer.getChildren().addAll(alreadyHaveAccountLabel, loginLink);

    mainContainer.getChildren().addAll(signUpMessage, signUpGrid, loginContainer);
    view.setCenter(mainContainer);

    // Create the ImageView for the GIF
    Image gifImage = new Image("https://i.imgur.com/EfVT7cL.gif");
    ImageView gifImageView = new ImageView(gifImage);
    gifImageView.setFitWidth(200); // Adjust the width of the image view
    gifImageView.setFitHeight(200); // Adjust the height of the image view

    // Set alignment and layout constraints for the GIF ImageView
    BorderPane.setAlignment(gifImageView, Pos.BOTTOM_RIGHT);
    BorderPane.setMargin(gifImageView, new Insets(0, 10, 10, 0));

    // Add the ImageView to the view
    view.setBottom(gifImageView);
    BorderPane.setAlignment(gifImageView, Pos.BOTTOM_RIGHT);

  }

  /**
   * Validate the input .
   */
  private boolean validateInput(String username, String password, String confirmedPassword, 
      String displayName) {
    if (username.trim().isEmpty() || password.isEmpty() || confirmedPassword.isEmpty() 
        || displayName.isEmpty()) {
      showError("All fields must be filled in.");
      return false;
    } 
    if (!password.equals(confirmedPassword)) {
      showError("Passwords do not match.");
      return false;
    }
    if (password.length() < 3) {
      showError("Password must be at least 3 characters long.");
      return false;
    }
    if (username.equalsIgnoreCase("admin")) {
      showError("Username cannot be admin.");
      return false;
    }
    return true;
  }

  /**
   * Show error message for the user if anything is wrong.
   */
  public void showError(String message) {
    // Show the error message using an alert or any other way you prefer.
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

}