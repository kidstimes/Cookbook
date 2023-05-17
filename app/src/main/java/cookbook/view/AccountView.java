package cookbook.view;

import cookbook.model.User;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * View for the admin page.
 */
public class AccountView {
  private BorderPane view;
  private AccountViewObserver observer;
  private String displayName;
  private String userName;
  private VBox centerBox;

  /** Constructor for the admin view.
   *
   * @param users the list of users
   */
  public AccountView(String displayName, String userName) {
    view = new BorderPane();
    this.displayName = displayName;
    this.userName = userName;
    initLayout();
  }

  public void setObserver(AccountViewObserver observer) {
    this.observer = observer;
  }

  public Node getView() {
    return view;
  }

  private void initLayout() {
    view.setStyle("-fx-background-color: #F9F8F3;");
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(20);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text welcomeTitle = new Text(displayName + ", welcome!");
    welcomeTitle.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(welcomeTitle);
    
    Button[] sidebarButtons = {
      createButton("Home Page", e -> observer.goToHomePage()),
      createButton("Browse Recipes", e -> observer.goToBrowser()),
      createButton("Add a Recipe", e -> observer.goToAddRecipe()),
      createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
      createButton("My Favorites", e -> observer.goToMyFavorite()),
      createButton("My Shopping List", e -> observer.goToShoppingList()),
      createButton("Messages", e -> observer.goToMessages()),
      createButton("My Account", e -> observer.goToAccount())
      };
    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);
    HBox logoutHelpBox = new HBox(10);
    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 14));
    logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> {
      observer.userLogout();
    });

    Region hspacer = new Region();  // This will take up as much space as possible
    HBox.setHgrow(hspacer, Priority.ALWAYS); 
    
    Button helpButton = new Button("Help");
    helpButton.setFont(Font.font("Roboto", 14));
    helpButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    helpButton.setOnAction(e -> {
      observer.goToHelp();
    });
    
    logoutHelpBox.getChildren().addAll(logoutButton, hspacer, helpButton);
    logoutHelpBox.setAlignment(Pos.CENTER_LEFT);  
    
    sidebar.getChildren().add(logoutHelpBox); 
    view.setLeft(sidebar);

    centerBox = new VBox(30);
    centerBox.setPadding(new Insets(50, 20, 20, 20));
    centerBox.setAlignment(Pos.TOP_LEFT);

    Text accountSettingsTitle = new Text("Account Setting");
    accountSettingsTitle.setFont(Font.font("Roboto", 28));

    Text userNameText = new Text("Your username is: " + userName);
    userNameText.setFont(Font.font("Roboto", 24));

    Hyperlink changePasswordLink = new Hyperlink("Change Password");
    changePasswordLink.setFont(Font.font("Roboto", 20));
    changePasswordLink.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    changePasswordLink.setOnAction(e -> {
      showChangePasswordForm();
    });
    Hyperlink changeDisplayNameLink = new Hyperlink("Change Display Name");
    changeDisplayNameLink.setFont(Font.font("Roboto", 20));
    changeDisplayNameLink.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    changeDisplayNameLink.setOnAction(e -> {
      showChangeDisplayNameForm();
    });

    centerBox.getChildren().addAll(userNameText, accountSettingsTitle, changePasswordLink, changeDisplayNameLink);

    view.setCenter(centerBox);
  }
    
  


  private void showChangeDisplayNameForm() {
    centerBox.getChildren().clear();
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(25, 25, 25, 25));
    Label newDisplayNameLabel = new Label("New display name:");
    newDisplayNameLabel.setFont(Font.font("Roboto", 20));
    grid.add(newDisplayNameLabel, 0, 1);
    TextField newDisplayNameField = new TextField();
    newDisplayNameField.setFont(Font.font("Roboto", 20));
    grid.add(newDisplayNameField, 1, 1);
    Button btnSave = new Button("Save");
    btnSave.setFont(Font.font("Roboto", 20));
    btnSave.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    grid.add(btnSave, 1, 2);
    btnSave.setOnAction(ev -> {
      String newDisplayName = newDisplayNameField.getText();
      if (newDisplayName.isEmpty()) {
        showError("Please fill the new display name.");
      }
      if (newDisplayName.length() > 20 || newDisplayName.length() < 3) {
        showError("Display name must be between 3 and 20 characters.");
      } else {
        observer.changeDisplayName(newDisplayName);
        observer.goToAccount();
      }
    });
    centerBox.getChildren().add(grid);
  }

  private void showChangePasswordForm() {
    centerBox.getChildren().clear();
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(5,5,5,5));       
    Label oldPasswordLabel = new Label("Old password:");
    oldPasswordLabel.setFont(Font.font("Roboto", 20));
    grid.add(oldPasswordLabel, 0, 1);       
    PasswordField oldPasswordField = new PasswordField();
    oldPasswordField.setFont(Font.font("Roboto", 20));
    grid.add(oldPasswordField, 1, 1);       
    Label newPasswordLabel = new Label("New password:");
    newPasswordLabel.setFont(Font.font("Roboto", 20));
    grid.add(newPasswordLabel, 0, 2);    
    PasswordField newPasswordField = new PasswordField();
    newPasswordField.setFont(Font.font("Roboto", 20));
    grid.add(newPasswordField, 1, 2);       
    Label confirmNewPasswordLabel = new Label("Confirm new password:");
    confirmNewPasswordLabel.setFont(Font.font("Roboto", 20));
    grid.add(confirmNewPasswordLabel, 0, 3);        
    PasswordField confirmNewPasswordField = new PasswordField();
    confirmNewPasswordField.setFont(Font.font("Roboto", 20));
    grid.add(confirmNewPasswordField, 1, 3);      
    Button btnSave = new Button("Save");
    btnSave.setFont(Font.font("Roboto", 20));
    btnSave.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    grid.add(btnSave, 1, 4);        
    btnSave.setOnAction(ev -> {
      String oldPassword = oldPasswordField.getText();
      String newPassword = newPasswordField.getText();
      String confirmNewPassword = confirmNewPasswordField.getText();
    
      // Check if all fields are filled
      if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
        showError("Please fill all fields."); 
        return;
      }
      //new password must be longer than or equal to 5 characters
      if (newPassword.length() < 5) {
        showError("New password must be at least 5 characters long");
        return;
      }    
      if (!newPassword.equals(confirmNewPassword)) {
        showError("New password and confirm password do not match");
      } else {
        observer.handlePasswordChange(oldPassword, newPassword);
        observer.goToAccount();
      }
    });
    centerBox.getChildren().add(grid);
  }

  

  /**
   * set this view as a admin view by adding admin button.
   */
  public void setAdmin() {
    Text adminTitle = new Text("Admin Account");
    adminTitle.setFont(Font.font("Roboto", 28));
    Hyperlink adminButton = new Hyperlink("Admin Page");
    adminButton.setFont(Font.font("Roboto", 20));
    adminButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    adminButton.setOnAction(e -> {
      observer.goToAdmin();
    });

    centerBox.getChildren().addAll(adminTitle, adminButton);
  }

  /** Create styled button with the given text and event handler.
   *
   * @param text is the text to display on the button
   * @param eventHandler is the event handler to execute when the button is clicked.
   * @return the created button
   */
  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color:#F2CC8F ; -fx-text-fill:#3D405B; -fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setMinWidth(180);
    button.setMaxWidth(200); 
    button.setOnAction(eventHandler);
    return button;
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