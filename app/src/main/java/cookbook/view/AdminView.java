package cookbook.view;

import cookbook.model.User;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * View for the admin page.
 */
public class AdminView {
  private BorderPane view;
  private AdminViewObserver observer;
  private ArrayList<User> users;
  private VBox userListContainer;
  private String displayName;
  private VBox usersContainer;

  /** Constructor for the admin view.
   *
   * @param users the list of users
   */
  public AdminView(ArrayList<User> users, String displayName) {
    view = new BorderPane();
    this.users = users;
    this.displayName = displayName;
    initLayout();
  }

  public void setObserver(AdminViewObserver observer) {
    this.observer = observer;
  }

  public Node getView() {
    return view;
  }

  private void initLayout() {
    view.setStyle("-fx-background-color: #F9F8F3;");
    // create a vbox to hold the menu buttons
    Sidebar sidebar = new Sidebar(displayName);
    sidebar.addButton("Home Page", e -> observer.goToHomePage());
    sidebar.addButton("Browse Recipes", e -> observer.goToBrowser());
    sidebar.addButton("Add a Recipe", e -> observer.goToAddRecipe());
    sidebar.addButton("Weekly Dinner List", e -> observer.goToWeeklyDinner());
    sidebar.addButton("My Favorites", e -> observer.goToMyFavorite());
    sidebar.addButton("My Shopping List", e -> observer.goToShoppingList());
    sidebar.addButton("Messages", e -> observer.goToMessages());
    sidebar.addButton("My Account", e -> observer.goToAccount());
    sidebar.addHyperlink("Help", e -> observer.goToHelp());
    sidebar.addHyperlink("Log Out", e -> observer.userLogout());
    
    sidebar.setActiveButton("My Account");
    sidebar.finalizeLayout();
        
    // Add the sidebar to the view
    view.setLeft(sidebar);

    // Add a title to the admin page
    userListContainer = new VBox();
    userListContainer.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");
    userListContainer.setSpacing(20);

    // Add the title to the userListContainer here
    Label titleLabel = new Label("Admin Page");
    titleLabel.setStyle("-fx-font: 32px \"Roboto\";-fx-text-fill: #69a486;");
    userListContainer.getChildren().add(titleLabel);
    Label userListLabel = new Label("All Users");
    userListLabel.setStyle("-fx-font: 26px \"Roboto\";");
    userListContainer.getChildren().add(userListLabel);
    
    usersContainer = new VBox();
    userListContainer.getChildren().add(usersContainer);
    
    view.setCenter(userListContainer);

    updateUserList();
  }

  private void updateUserList() {
    usersContainer.getChildren().clear();
    usersContainer.setSpacing(15);

    HBox titleLine = new HBox(10);
    titleLine.setAlignment(Pos.CENTER_LEFT);
    Label userIdTitle = new Label("User ID");
    userIdTitle.setStyle("-fx-font: 18px \"Roboto\";");
    userIdTitle.setMinWidth(100);
    userIdTitle.setAlignment(Pos.CENTER_LEFT);
    Label userNameTitle = new Label("Username");
    userNameTitle.setStyle("-fx-font: 18px \"Roboto\";");
    userNameTitle.setMinWidth(150);
    userNameTitle.setAlignment(Pos.CENTER_LEFT);
    Label passwordTitle = new Label("Password");
    passwordTitle.setStyle("-fx-font: 18px \"Roboto\";");
    passwordTitle.setMinWidth(150);
    passwordTitle.setAlignment(Pos.CENTER_LEFT);
    Label displayNameTitle = new Label("Display Name");
    displayNameTitle.setStyle("-fx-font: 18px \"Roboto\";");
    displayNameTitle.setMinWidth(150);
    displayNameTitle.setAlignment(Pos.CENTER_LEFT);
    titleLine.getChildren().addAll(userIdTitle, userNameTitle, passwordTitle, displayNameTitle);
    userListContainer.getChildren().add(titleLine);

    for (User user : users) {
      HBox userLine = new HBox(10);
      userLine.setAlignment(Pos.CENTER_LEFT);

      Label userIdLabel = new Label(String.valueOf(user.getId()));
      userIdLabel.setStyle("-fx-font: 18px \"Roboto\";");
      userIdLabel.setMinWidth(100);
      userIdLabel.setAlignment(Pos.CENTER_LEFT);

      Label userNameLabel = new Label(user.getUsername());
      userNameLabel.setStyle("-fx-font: 18px \"Roboto\";");
      userNameLabel.setMinWidth(150);
      userNameLabel.setAlignment(Pos.CENTER_LEFT);

      Label passwordLabel = new Label("*****"); 
      passwordLabel.setStyle("-fx-font: 18px \"Roboto\";");
      passwordLabel.setMinWidth(150);
      passwordLabel.setAlignment(Pos.CENTER_LEFT);

      Label displayNameLabel = new Label(user.getDisplayName());
      displayNameLabel.setStyle("-fx-font: 18px \"Roboto\";");
      displayNameLabel.setMinWidth(150);
      displayNameLabel.setAlignment(Pos.CENTER_LEFT);

      userLine.getChildren().addAll(userIdLabel, userNameLabel, passwordLabel, displayNameLabel);

      if (!user.getUsername().equalsIgnoreCase("admin")) {
        Button editButton = new Button("Edit username and/or display name");
        editButton.setStyle("-fx-background-color: white;"
            + " -fx-text-fill: #2196F3; -fx-font: 12px \"Roboto\";");
        editButton.setOnAction(e -> {
          editUser(user);

        });
    
        Button editPasswordButton = new Button("Edit Password");
        editPasswordButton.setStyle("-fx-background-color: white;"
            + " -fx-text-fill: #2196F3; -fx-font: 12px \"Roboto\";");
        editPasswordButton.setOnAction(e -> {
          editUserPassword(user);       
        });
    
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-font: 12px \"Roboto\";"
            + " -fx-background-color: white; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
        deleteButton.setOnAction(e -> {
          observer.deleteUser(user.getId());
          observer.goToAdmin();
          updateUserList();
        });
        userLine.getChildren().addAll(editButton, editPasswordButton, deleteButton);
      } 
      userListContainer.getChildren().add(userLine);
      
    }
    // Add user button
    Button addUserButton = new Button("Add User");
    addUserButton.setStyle("-fx-background-color: #69a486;"
            + " -fx-text-fill: #F4F1DE; -fx-font: 18px \"Roboto\";");
    addUserButton.setOnAction(e -> {
      addUser();
    });
    userListContainer.getChildren().add(addUserButton);
    
  }

  /** Edit the username and display name of a user.
   *
   * @param user the user to edit
   */
  public void editUser(User user) {
    Dialog<User> editUserDialog = new Dialog<>();
    editUserDialog.setTitle("Edit User");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField userNameField = new TextField(user.getUsername());
    TextField displayNameField = new TextField(user.getDisplayName());

    grid.add(new Label("Username:"), 0, 0);
    grid.add(userNameField, 1, 0);
    grid.add(new Label("Display Name:"), 0, 1);
    grid.add(displayNameField, 1, 1);
    editUserDialog.getDialogPane().setContent(grid);

    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    editUserDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    editUserDialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        observer.editUser(user.getId(), userNameField.getText(), displayNameField.getText());
        observer.goToAdmin();
        updateUserList();
      }
      return null;
    });
    editUserDialog.showAndWait();
  }

  /** Edit the password of a user.
   *
   * @param user the user to edit
   */
  public void editUserPassword(User user) {
    Dialog<User> editUserPasswordDialog = new Dialog<>();
    editUserPasswordDialog.setTitle("Edit User Password");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    PasswordField passwordField = new PasswordField();
    grid.add(new Label("Password:"), 0, 0);
    grid.add(passwordField, 1, 0);

    editUserPasswordDialog.getDialogPane().setContent(grid);

    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    editUserPasswordDialog.getDialogPane()
        .getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    editUserPasswordDialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        observer.editUserPassword(user.getId(), passwordField.getText());
        observer.goToAdmin();
        updateUserList();
      }
      return null;
    });
    editUserPasswordDialog.showAndWait();
  }

  /**
   * Add a new user.
   */
  private void addUser() {
    Dialog<User> addUserDialog = new Dialog<>();
    addUserDialog.setTitle("Add User");
  
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
  
    TextField userNameField = new TextField();
    TextField passwordField = new TextField();
    TextField displayNameField = new TextField();
  
    grid.add(new Label("Username:"), 0, 0);
    grid.add(userNameField, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(passwordField, 1, 1);
    grid.add(new Label("Display Name:"), 0, 2);
    grid.add(displayNameField, 1, 2);
  
    addUserDialog.getDialogPane().setContent(grid);
  
    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    addUserDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
  
    addUserDialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        boolean success = observer.addUser(userNameField.getText(),
            passwordField.getText(), displayNameField.getText());
        if (success) {
          observer.goToAdmin();
          updateUserList();
        } else {
          showError("Could not add user.");
        }
      }
      return null;
    });
    addUserDialog.showAndWait();
  }
  
  /** Show an error message.
   *
   * @param string the error message
   */
  public void showError(String string) {
    showInlineStyledAlert(AlertType.ERROR, "Error", string);
  }

  /** Show an alert.
   *
   * @param alertType the type of alert
   * @param title the title of the alert
   * @param message the message of the alert
   */
  private void showInlineStyledAlert(AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStyleClass().add("myAlert");
    alert.showAndWait();
  }
  

}
