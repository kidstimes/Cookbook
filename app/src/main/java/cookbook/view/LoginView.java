package cookbook.view;

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
    Image animatedImageUrl = new Image(getClass().getResourceAsStream(
        "/images/CircleOverCook.png"));
    ImageView animatedImage = new ImageView(animatedImageUrl);

    // Create an ImageView and set the animated image to it
    animatedImage.setFitWidth(350); // Set the width of the image
    animatedImage.setPreserveRatio(true); // Maintain the aspect ratio
    animatedImage.setSmooth(true); // Enable smooth resizing
    introPane.getChildren().add(animatedImage);

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
    rotateTransition.setNode(animatedImage);
    rotateTransition.setByAngle(360);
    rotateTransition.setCycleCount(1);
    rotateTransition.setInterpolator(Interpolator.LINEAR);

    // Create a TranslateTransition
    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setDuration(Duration.millis(2000));
    translateTransition.setNode(animatedImage);
    translateTransition.setByX(100); // Move the image view 100 pixels to the right
    translateTransition.setCycleCount(1);
    translateTransition.setAutoReverse(false);

    // Create a ScaleTransition
    ScaleTransition scaleTransition = new ScaleTransition();
    scaleTransition.setDuration(Duration.millis(2000));
    scaleTransition.setNode(animatedImage);
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
    pathTransition.setNode(animatedImage);    
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
    ParallelTransition parallelTransition = new ParallelTransition(fadeTransition,
        rotateTransition, scaleTransition, pathTransition);

    parallelTransition.setOnFinished(e -> {
      // Create a StackPane
      StackPane heartPane = new StackPane();
      heartPane.setAlignment(Pos.CENTER);

      // Load the bottom left image
      Image bottomLeftImageUrl = new Image(getClass().getResourceAsStream("/images/King.png"));
      ImageView bottomLeftImage = new ImageView(bottomLeftImageUrl);

      // Create an ImageView and set the bottom left image to it
      bottomLeftImage.setFitWidth(250); // Set the width of the image
      bottomLeftImage.setPreserveRatio(true); // Maintain the aspect ratio
      bottomLeftImage.setSmooth(true); // Enable smooth resizing

      //Create a RotateTransition for the bottom left image
      RotateTransition bottomLeftRotateTransition = new RotateTransition();
      bottomLeftRotateTransition.setDuration(Duration.millis(2000));
      bottomLeftRotateTransition.setNode(bottomLeftImage);
      bottomLeftRotateTransition.setByAngle(360);
      bottomLeftRotateTransition.setCycleCount(1);
      bottomLeftRotateTransition.setInterpolator(Interpolator.LINEAR);

      //Start the RotateTransition
      bottomLeftRotateTransition.play();


      heartPane.getChildren().add(bottomLeftImage);
      StackPane.setAlignment(bottomLeftImage, Pos.BOTTOM_LEFT);

      // Load the heart image and create an ImageView and set the heart image to it
      Image heartImageUrl = new Image(getClass().getResourceAsStream("/images/Hamburger.png"));
      ImageView heartImage = new ImageView(heartImageUrl);
      heartImage.setFitWidth(150); // Set the width of the image
      heartImage.setPreserveRatio(true); // Maintain the aspect ratio
      heartImage.setSmooth(true); // Enable smooth resizing
      heartPane.getChildren().add(heartImage);

      //Load the bottom right image
      Image bottomRightImageUrl = new Image(getClass().getResourceAsStream("/images/Vege.png")); 
      ImageView bottomRightImage = new ImageView(bottomRightImageUrl);

      // Create an ImageView and set the bottom right image to it
      bottomRightImage.setFitWidth(300); // Set the width of the image
      bottomRightImage.setPreserveRatio(true); // Maintain the aspect ratio
      bottomRightImage.setSmooth(true); // Enable smooth resizing

      //Create a RotateTransition for the bottom right image
      RotateTransition bottomRightRotateTransition = new RotateTransition();
      bottomRightRotateTransition.setDuration(Duration.millis(2000));
      bottomRightRotateTransition.setNode(bottomRightImage);
      bottomRightRotateTransition.setByAngle(360);
      bottomRightRotateTransition.setCycleCount(1);
      bottomRightRotateTransition.setInterpolator(Interpolator.LINEAR);

      //Start the RotateTransition
      bottomRightRotateTransition.play();
      heartPane.getChildren().add(bottomRightImage);
      StackPane.setAlignment(bottomRightImage, Pos.BOTTOM_RIGHT);
      
      //Add a VBox
      VBox progressBox = new VBox();
      progressBox.setAlignment(Pos.CENTER);
      progressBox.setSpacing(0); 


      // Create a ProgressBar
      ProgressBar progressBar = new ProgressBar();
      progressBar.setPrefWidth(500);
      heartPane.getChildren().add(progressBar);

      // Create a Label
      Label progressLabel = new Label("Loading...");
      progressLabel.setTextFill(Color.BLACK);

      // Load the bottom image
      Image bottomImageUrl = new Image(getClass().getResourceAsStream("/images/Java.png"));
      ImageView bottomImage = new ImageView(bottomImageUrl);

      // Create an ImageView and set the bottom image to it
      bottomImage.setFitWidth(800); // Set the width of the image
      bottomImage.setPreserveRatio(true); // Maintain the aspect ratio
      bottomImage.setSmooth(true); // Enable smooth resizing

      // Create a TranslateTransition for the Java image
      TranslateTransition bottomImageTranslateTransition = new TranslateTransition();
      bottomImageTranslateTransition.setDuration(Duration.millis(500));  
      bottomImageTranslateTransition.setNode(bottomImage);
      bottomImageTranslateTransition.setByY(-20);  // Move 20 pixels up
      bottomImageTranslateTransition.setAutoReverse(true);  // Move back and forth
      bottomImageTranslateTransition.setCycleCount(
          TranslateTransition.INDEFINITE);  // Repeat indefinitely

      // Start the TranslateTransition
      bottomImageTranslateTransition.play();

      // Add the Label to the VBox
      progressBox.getChildren().addAll(progressBar, progressLabel, bottomImage);

      // Load the new image
      Image newImageUrl = new Image(getClass().getResourceAsStream("/images/Logo.png"));
      ImageView newImage = new ImageView(newImageUrl);

      // Create an ImageView and set the new image to it
      newImage.setFitWidth(400); // Set the width of the image
      newImage.setPreserveRatio(true); // Maintain the aspect ratio
      newImage.setSmooth(true); // Enable smooth resizing

      // Create a TranslateTransition for the new logo image
      TranslateTransition newImageTranslateTransition = new TranslateTransition();
      newImageTranslateTransition.setDuration(Duration.millis(500));  
      newImageTranslateTransition.setNode(newImage);
      newImageTranslateTransition.setByY(-20);  // Move 20 pixels up
      newImageTranslateTransition.setAutoReverse(true);  // Move back and forth
      newImageTranslateTransition.setCycleCount(
          TranslateTransition.INDEFINITE);  // Repeat indefinitely

      // Start the TranslateTransition
      newImageTranslateTransition.play();

      // Add the new Image to the VBox
      progressBox.getChildren().add(newImage);

      //Set Position
      StackPane.setAlignment(progressBox, Pos.BOTTOM_CENTER);
      heartPane.getChildren().add(progressBox);

      // Create a Path for the heart
      Path heartPath = new Path();
      heartPath.getElements().add(new MoveTo(-200, -200));  // Start from the left
      for (int i = 0; i < 5; i++) {  // Jump 5 times
        heartPath.getElements().add(
            new QuadCurveTo(-100 + i * 100, 100 * ((i % 2) * 2 - 1), i * 100, -200));
      }
      heartPath.getElements().add(new LineTo(500, -200));  // Move to the right


      // Create a PathTransition for the heart
      PathTransition heartPathTransition = new PathTransition();
      heartPathTransition.setDuration(Duration.millis(2000));
      heartPathTransition.setNode(heartImage);
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
      rotateHeartTransition.setNode(heartImage);
      rotateHeartTransition.setByAngle(360);
      rotateHeartTransition.setCycleCount(1);
      rotateHeartTransition.setInterpolator(Interpolator.LINEAR);      

      // Set the view's center to the heartPane
      view.setCenter(heartPane);

      // Load the top left image
      Image topLeftImageUrl = new Image(getClass().getResourceAsStream("/images/Moomin.png")); 
      ImageView topLeftImage = new ImageView(topLeftImageUrl);

      // Create an ImageView and set the top left image to it
      topLeftImage.setFitWidth(300); // Set the width of the image
      topLeftImage.setPreserveRatio(true); // Maintain the aspect ratio
      topLeftImage.setSmooth(true); // Enable smooth resizing

      // Add the image to the StackPane
      heartPane.getChildren().add(topLeftImage);
      StackPane.setAlignment(topLeftImage, Pos.TOP_LEFT);

      // Create a TranslateTransition for the top left image
      TranslateTransition topLeftTranslateTransition = new TranslateTransition();
      topLeftTranslateTransition.setDuration(Duration.millis(500));  
      topLeftTranslateTransition.setNode(topLeftImage);
      topLeftTranslateTransition.setByY(-20);  // Move 20 pixels up
      topLeftTranslateTransition.setAutoReverse(true);  // Move back and forth
      topLeftTranslateTransition.setCycleCount(
          TranslateTransition.INDEFINITE);  // Repeat indefinitely

      // Start the TranslateTransition
      topLeftTranslateTransition.play();

      // Load the left side image
      Image leftImageUrl = new Image(getClass().getResourceAsStream("/images/LeftMoomin.png")); 
      ImageView leftImage = new ImageView(leftImageUrl);

      // Create an ImageView and set the left side image to it
      leftImage.setFitWidth(300); // Set the width of the image
      leftImage.setPreserveRatio(true); // Maintain the aspect ratio
      leftImage.setSmooth(true); // Enable smooth resizing

      // Add the image to the StackPane
      heartPane.getChildren().add(leftImage);
      StackPane.setAlignment(leftImage, Pos.CENTER_LEFT);

      // Create a TranslateTransition for the left side image
      TranslateTransition leftTranslateTransition = new TranslateTransition();
      leftTranslateTransition.setDuration(Duration.millis(500));  
      leftTranslateTransition.setNode(leftImage);
      leftTranslateTransition.setByY(-20);  // Move 20 pixels up
      leftTranslateTransition.setAutoReverse(true);  // Move back and forth
      leftTranslateTransition.setCycleCount(TranslateTransition.INDEFINITE);  // Repeat indefinitely

      // Start the TranslateTransition
      leftTranslateTransition.play();

      // Create a FadeTransition for the leftImage
      FadeTransition leftFadeTransition = new FadeTransition(Duration.seconds(2), leftImage);
      leftFadeTransition.setFromValue(1.0);  // fully visible
      leftFadeTransition.setToValue(0.0);  // invisible

      // Load the right side image
      Image rightImageUrl = new Image(getClass().getResourceAsStream("/images/MoominRight.png")); 
      ImageView rightImage = new ImageView(rightImageUrl);

      // Create an ImageView and set the right side image to it
      rightImage.setFitWidth(200); // Set the width of the image
      rightImage.setPreserveRatio(true); // Maintain the aspect ratio
      rightImage.setSmooth(true); // Enable smooth resizing

      // Add the image to the StackPane
      heartPane.getChildren().add(rightImage);
      StackPane.setAlignment(rightImage, Pos.CENTER_RIGHT);

      // Create a TranslateTransition for the right side image
      TranslateTransition rightTranslateTransition = new TranslateTransition();
      rightTranslateTransition.setDuration(Duration.millis(500));  
      rightTranslateTransition.setNode(rightImage);
      rightTranslateTransition.setByY(-20);  // Move 20 pixels up
      rightTranslateTransition.setAutoReverse(true);  // Move back and forth
      rightTranslateTransition.setCycleCount(
          TranslateTransition.INDEFINITE);  // Repeat indefinitely

      // Start the TranslateTransition
      rightTranslateTransition.play();

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
    Image imageUrl = new Image(getClass().getResourceAsStream("/images/ColorLogo.png"));
    ImageView image = new ImageView(imageUrl);

    // Create an ImageView and set the image to it
    image.setFitWidth(800); // Set the width of the image
    image.setPreserveRatio(true); // Maintain the aspect ratio
    image.setSmooth(true); // Enable smooth resizing


    VBox mainContainer = new VBox(10);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(20));

    createAnimation(image); 
    mainContainer.getChildren().add(image);

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
    loginButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: #ffffff; "
        + "-fx-font-size: 16; -fx-font-weight: bold;");
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
    signUpLink.setOnAction(event -> observer.goToSignUp());

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
    //if username is admin, then password can be less than 5 characters
    if (username.equals("admin")) {
      return true;
    } else if (password.length() < 5) {
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
