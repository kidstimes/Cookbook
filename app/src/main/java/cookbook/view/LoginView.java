package cookbook.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

/**
 * View class for the login page.
 */
public class LoginView {
  private BorderPane view;
  private LoginViewObserver observer;
  private TextField usernameField;
  private PasswordField passwordField;

  /**
   * Constructor for the login view.
   */
  public LoginView() {
    this.view = new BorderPane();
    view.setStyle("-fx-background-color: beige;");
    createIntroAnimation();
  }

  // Create an intro animation
  private void createIntroAnimation() {
    // Create a StackPane
    StackPane introPane = new StackPane();
    introPane.setAlignment(Pos.CENTER);

    // Load an animated image
    String animatedImageUrl = "https://i.imgur.com/PhR7Ppt.png";
    Image animatedImage = new Image(animatedImageUrl);

    // Create an ImageView and set the animated image to it
    ImageView animatedImageView = new ImageView(animatedImage);
    animatedImageView.setFitWidth(600); // Set the width of the image
    animatedImageView.setPreserveRatio(true); // Maintain the aspect ratio
    animatedImageView.setSmooth(true); // Enable smooth resizing
    introPane.getChildren().add(animatedImageView);

    // Create a FadeTransition
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(6000));
    fadeTransition.setNode(introPane);
    fadeTransition.setFromValue(4.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.setAutoReverse(false);
    fadeTransition.setCycleCount(1);

    //Create a RotateTransition
    RotateTransition rotateTransition = new RotateTransition();
    rotateTransition.setDuration(Duration.millis(4000));
    rotateTransition.setNode(animatedImageView);
    rotateTransition.setByAngle(360);
    rotateTransition.setCycleCount(1);
    rotateTransition.setInterpolator(Interpolator.LINEAR);


    // Set the view's center to the introPane
    view.setCenter(introPane);

    fadeTransition.setOnFinished(e -> {
      view.getChildren().remove(introPane);
      initLayout();
    });

    // Play both animations simultaneously
    ParallelTransition parallelTransition 
        = new ParallelTransition(fadeTransition, rotateTransition);
    parallelTransition.play();
  }

  // Set an observer of the login view
  public void setObserver(LoginViewObserver observer) {
    this.observer = observer;
  }


  // Get a login view
  public Node getView() {
    return view;
  }


  private void createAnimation(Node node) {
    // Create a TranslateTransition
    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setDuration(Duration.millis(1500));
    translateTransition.setNode(node);
    translateTransition.setFromY(-100);
    translateTransition.setToY(0);
    translateTransition.setAutoReverse(false);
    translateTransition.setCycleCount(1);

    // Create a FadeTransition
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(1000));
    fadeTransition.setNode(node);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.setAutoReverse(false);
    fadeTransition.setCycleCount(1);

    // Play the animations
    translateTransition.play();
    fadeTransition.play();
  }

  
  // Initialize the layout
  private void initLayout() {

    // Load an image
    String imageUrl = "https://i.imgur.com/x8pcjxL.png";
    Image image = new Image(imageUrl);

    // Create an ImageView and set the image to it
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(700); // Set the width of the image
    imageView.setPreserveRatio(true); // Maintain the aspect ratio
    imageView.setSmooth(true); // Enable smooth resizing


    VBox mainContainer = new VBox(10);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(20));

    createAnimation(imageView); 
    mainContainer.getChildren().add(imageView);

    // Welcome message
    Label welcomeMessage = new Label("Welcome to Cookbook");
    welcomeMessage.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

    // Image section
    /*
     * ImageView imageView = new ImageView(https://www.steamgriddb.com/logo/23632);
     * Image image = new Image("images/pic.avif");
     * imageView.setImage(image);
     * imageView.setPreserveRatio(true);
     * imageView.setFitWidth(600);
     */

    // Login section
    GridPane loginGrid = new GridPane();
    loginGrid.setHgap(10);
    loginGrid.setVgap(10);
    loginGrid.setAlignment(Pos.CENTER);
    
    Label usernameLabel = new Label("Username:");
    //set username label font and size
    usernameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    usernameField = new TextField();
    loginGrid.add(usernameLabel, 0, 0);
    loginGrid.add(usernameField, 1, 0);

    Label passwordLabel = new Label("Password:");
    //set password label font and size
    passwordLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    passwordField = new PasswordField();
    loginGrid.add(passwordLabel, 0, 1);
    loginGrid.add(passwordField, 1, 1);

    
    Button loginButton = new Button("Login");
    loginButton.setMaxWidth(Double.MAX_VALUE);
    //set login button style here
    loginButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: #ffffff; -fx-font-size: 16; -fx-font-weight: bold;");
    loginButton.setOnAction(event -> {
      String username = usernameField.getText();
      String password = passwordField.getText();
      if (validateInput(username, password)) {
        observer.handleLogin(username, password);
        //clear all the input
        usernameField.clear();
        passwordField.clear();
      }
    });
    loginGrid.add(loginButton, 0, 3, 2, 1);

    /*
    Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password");
    forgotPasswordLink.setOnAction(event -> {
    });
    loginGrid.add(forgotPasswordLink, 1, 2);
    GridPane.setHalignment(forgotPasswordLink, HPos.RIGHT);
    */

    Label dontHaveAccountLabel = new Label("Don't have an account?");
    //set dont have account label font and size
    dontHaveAccountLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    Hyperlink signUpLink = new Hyperlink("Click here to sign up");
    //set sign up link font and size
    signUpLink.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    signUpLink.setOnAction(event -> {
      observer.goToSignUp();
    });

    HBox signUpContainer = new HBox(5);
    signUpContainer.setAlignment(Pos.CENTER);
    signUpContainer.getChildren().addAll(dontHaveAccountLabel, signUpLink);

    // Add the welcome message, image, login sections, and sign up link to the main
    // container
    mainContainer.getChildren().addAll(welcomeMessage, loginGrid, signUpContainer);
    view.setCenter(mainContainer);

    StackPane stackPane = new StackPane();
    VBox.setVgrow(mainContainer, Priority.ALWAYS);
    stackPane.getChildren().add(mainContainer);

    // Add this section for the new GIF on the right bottom side
    String gifUrl = "https://i.imgur.com/5S8VFrF.gif";
    Image gifImage = new Image(gifUrl, 200, 200, true, true);
    ImageView gifImageView = new ImageView(gifImage);

    StackPane.setAlignment(gifImageView, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(gifImageView, new Insets(0, 20, 20, 0));
    stackPane.getChildren().add(gifImageView);


    // Add this section for the new GIF on the left bottom side
    String gifUrlLeft = "https://i.imgur.com/NFN3ywE.gif";
    Image gifImageLeft = new Image(gifUrlLeft, 200, 200, true, true);
    ImageView gifImageViewLeft = new ImageView(gifImageLeft);

    StackPane.setAlignment(gifImageViewLeft, Pos.BOTTOM_LEFT);
    StackPane.setMargin(gifImageViewLeft, new Insets(0, 0, 20, 20));

    stackPane.getChildren().add(gifImageViewLeft);

    view.setCenter(stackPane);

    // Add a PauseTransition to delay the appearance of the login page
    PauseTransition pauseTransition = new PauseTransition(Duration.millis(3000));
    pauseTransition.setOnFinished(e -> {
      mainContainer.getChildren().addAll(welcomeMessage, loginGrid, signUpContainer);
      view.setCenter(mainContainer);
    });
    pauseTransition.play();
  }


  // Validate the input
  private boolean validateInput(String username, String password) {
    if (username.isEmpty() || password.isEmpty()) {
      showError("Both fields must be filled in.");
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
