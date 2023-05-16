package cookbook.view;


import javax.swing.text.AbstractDocument.LeafElement;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
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
    view.setStyle("-fx-background-color: #F9F8F3;");
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
    animatedImageView.setFitWidth(350); // Set the width of the image
    animatedImageView.setPreserveRatio(true); // Maintain the aspect ratio
    animatedImageView.setSmooth(true); // Enable smooth resizing
    introPane.getChildren().add(animatedImageView);

    // Create a FadeTransition
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(2000));
    fadeTransition.setNode(introPane);
    fadeTransition.setFromValue(4.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.setAutoReverse(false);
    fadeTransition.setCycleCount(1);

    //Create a RotateTransition
    RotateTransition rotateTransition = new RotateTransition();
    rotateTransition.setDuration(Duration.millis(2000));
    rotateTransition.setNode(animatedImageView);
    rotateTransition.setByAngle(360);
    rotateTransition.setCycleCount(1);
    rotateTransition.setInterpolator(Interpolator.LINEAR);

    // Create a TranslateTransition
    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setDuration(Duration.millis(2000));
    translateTransition.setNode(animatedImageView);
    translateTransition.setByX(100); // Move the image view 100 pixels to the right
    translateTransition.setCycleCount(1);
    translateTransition.setAutoReverse(false);

    // Create a ScaleTransition
    ScaleTransition scaleTransition = new ScaleTransition();
    scaleTransition.setDuration(Duration.millis(2000));
    scaleTransition.setNode(animatedImageView);
    scaleTransition.setByX(0.5); // Increase the width of the image view by 50%
    scaleTransition.setByY(0.5); // Increase the height of the image view by 50%
    scaleTransition.setCycleCount(1);
    scaleTransition.setAutoReverse(false);

    // Create a Path for the path
    Path path = new Path();
    path.getElements().add(new MoveTo(0, 0));
    path.getElements().add(new QuadCurveTo(100, 200, 400, 100));

    // Create a PathTransition
    PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(2000));
    pathTransition.setNode(animatedImageView);    
    pathTransition.setPath(path);
    pathTransition.setCycleCount(1);
    pathTransition.setAutoReverse(false);


    // Set the view's center to the introPane
    view.setCenter(introPane);

    fadeTransition.setOnFinished(e -> {
      view.getChildren().remove(introPane);
      initLayout();
    });

    // Play all animations simultaneously
    ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, rotateTransition, scaleTransition, pathTransition);

    parallelTransition.setOnFinished(e -> {
      // Create a StackPane
      StackPane heartPane = new StackPane();
      heartPane.setAlignment(Pos.CENTER);
      // Load the heart image
      String heartImageUrl = "https://i.imgur.com/PJZqJbc.png";
      Image heartImage = new Image(heartImageUrl);

      //Load the bottom right image
      String bottomRightImageUrl = "https://i.imgur.com/jE4VQbO.png"; 
      Image bottomRightImage = new Image(bottomRightImageUrl);

      // Load the bottom left image
      String bottomLeftImageUrl = "https://i.imgur.com/ffTEx7e.png";
      Image bottomLeftImage = new Image(bottomLeftImageUrl);

      // Create an ImageView and set the bottom left image to it
      ImageView bottomLeftImageView = new ImageView(bottomLeftImage);
      bottomLeftImageView.setFitWidth(300); // Set the width of the image
      bottomLeftImageView.setPreserveRatio(true); // Maintain the aspect ratio
      bottomLeftImageView.setSmooth(true); // Enable smooth resizing

      //Create a RotateTransition for the bottom left image
      RotateTransition bottomLeftRotateTransition = new RotateTransition();
      bottomLeftRotateTransition.setDuration(Duration.millis(2000));
      bottomLeftRotateTransition.setNode(bottomLeftImageView);
      bottomLeftRotateTransition.setByAngle(360);
      bottomLeftRotateTransition.setCycleCount(1);
      bottomLeftRotateTransition.setInterpolator(Interpolator.LINEAR);

      //Start the RotateTransition
      bottomLeftRotateTransition.play();


      heartPane.getChildren().add(bottomLeftImageView);
      StackPane.setAlignment(bottomLeftImageView, Pos.BOTTOM_LEFT);

      // Create an ImageView and set the heart image to it
      ImageView heartImageView = new ImageView(heartImage);
      heartImageView.setFitWidth(150); // Set the width of the image
      heartImageView.setPreserveRatio(true); // Maintain the aspect ratio
      heartImageView.setSmooth(true); // Enable smooth resizing
      heartPane.getChildren().add(heartImageView);

      // Create an ImageView and set the bottom right image to it
      ImageView bottomRightImageView = new ImageView(bottomRightImage);
      bottomRightImageView.setFitWidth(300); // Set the width of the image
      bottomRightImageView.setPreserveRatio(true); // Maintain the aspect ratio
      bottomRightImageView.setSmooth(true); // Enable smooth resizing

      heartPane.getChildren().add(bottomRightImageView);
      StackPane.setAlignment(bottomRightImageView, Pos.BOTTOM_RIGHT);
      
      //Add a VBox
      VBox progressBox = new VBox();
      progressBox.setAlignment(Pos.CENTER);
      progressBox.setSpacing(10); 


      // Create a ProgressBar
      ProgressBar progressBar = new ProgressBar();
      progressBar.setPrefWidth(500);
      heartPane.getChildren().add(progressBar);

      // Create a Label
      Label progressLabel = new Label("Loading...");
      progressLabel.setTextFill(Color.BLACK);

      // Load the bottom image
      String bottomImageUrl = "https://i.imgur.com/GwBkQQN.png";
      Image bottomImage = new Image(bottomImageUrl);

      // Create an ImageView and set the bottom image to it
      ImageView bottomImageView = new ImageView(bottomImage);
      bottomImageView.setFitWidth(500); // Set the width of the image
      bottomImageView.setPreserveRatio(true); // Maintain the aspect ratio
      bottomImageView.setSmooth(true); // Enable smooth resizing

      // Add the Label to the VBox
      progressBox.getChildren().addAll(progressBar, progressLabel, bottomImageView);

      //Set Position
      StackPane.setAlignment(progressBox, Pos.BOTTOM_CENTER);
      heartPane.getChildren().add(progressBox);

      // Create a Path for the heart
      Path heartPath = new Path();
      heartPath.getElements().add(new MoveTo(-200, -200));  // Start from the left
      for (int i = 0; i < 5; i++) {  // Jump 5 times
        heartPath.getElements().add(new QuadCurveTo(-100 + i * 100, 100 * ((i % 2) * 2 - 1), i * 100, -200));
      }
      heartPath.getElements().add(new LineTo(500, -200));  // Move to the right


      // Create a PathTransition for the heart
      PathTransition heartPathTransition = new PathTransition();
      heartPathTransition.setDuration(Duration.millis(2000));
      heartPathTransition.setNode(heartImageView);
      heartPathTransition.setPath(heartPath);
      heartPathTransition.setCycleCount(1);
      heartPathTransition.setAutoReverse(false);

      // Update the progress bar as the heart moves
      heartPathTransition.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
        double progress = newValue.toMillis() / 2000;
        progressBar.setProgress(progress);
      });    

      // Create a RotateTransition for the heart
      RotateTransition rotateHeartTransition = new RotateTransition();
      rotateHeartTransition.setDuration(Duration.millis(2000));
      rotateHeartTransition.setNode(heartImageView);
      rotateHeartTransition.setByAngle(360);
      rotateHeartTransition.setCycleCount(1);
      rotateHeartTransition.setInterpolator(Interpolator.LINEAR);      

      // Set the view's center to the heartPane
      view.setCenter(heartPane);

      // Create a Timeline
      Timeline timeline = new Timeline();

      // Add key frames to the Timeline
      for (int i = 0; i < 5; i++) {
        double progress = (i + 1) / 5.0;
        timeline.getKeyFrames().add(
          new KeyFrame(Duration.seconds((i + 1) * 2),
              evt -> {
                // Update the progress of the ProgressBar and the text of the Label
                progressBar.setProgress(progress);
                progressLabel.setText("Loading... " + (int) (progress * 100) + "%");
              }
          )
        );
      }

      // Set the cycle count of the Timeline
      timeline.setCycleCount(1);

      // Start the Timeline
      timeline.play();


      // Play all animations simultaneously
      new ParallelTransition(heartPathTransition, rotateHeartTransition).play();

      heartPathTransition.setOnFinished(e2 -> {
        view.getChildren().remove(heartPane);
        initLayout();
      });
    });


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
    translateTransition.setDuration(Duration.millis(500));
    translateTransition.setNode(node);
    translateTransition.setFromY(-100);
    translateTransition.setToY(0);
    translateTransition.setAutoReverse(false);
    translateTransition.setCycleCount(1);

    // Create a FadeTransition
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(500));
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
    imageView.setFitWidth(800); // Set the width of the image
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

  }


  // Validate the input
  private boolean validateInput(String username, String password) {
    if (username.isEmpty() || password.isEmpty()) {
      showError("Both fields must be filled in.");
      return false;
    }
    if  (password.length() < 5) {
      showError("Password must be at least 5 characters long.");
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
