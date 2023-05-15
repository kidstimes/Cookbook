package cookbook.view;

import cookbook.model.User;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
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

  /** Constructor for the admin view.
   *
   * @param users the list of users
   */
  public AdminView(ArrayList<User> users) {
    view = new BorderPane();
    this.users = users;
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
    VBox sidebar = new VBox(30);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text welcomeTitle = new Text("Admin, welcome!");
    welcomeTitle.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(welcomeTitle);
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);
    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 18));
    logoutButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> {
      observer.userLogout();
    });
    sidebar.getChildren().add(logoutButton);
    view.setLeft(sidebar);

    // Add a title to the admin page
    Label titleLabel = new Label("Admin Page");
    titleLabel.setStyle("-fx-font: 32px \"Roboto\";");
    view.setTop(titleLabel);
    BorderPane.setAlignment(titleLabel, Pos.CENTER);
    userListContainer = new VBox(5);
    view.setCenter(userListContainer);

    // Add a label for the Users list
    Label userListLabel = new Label("Users");
    userListLabel.setStyle("-fx-font: 20px \"Roboto\";");
    userListContainer.getChildren().add(userListLabel);
    // Display the list of users
    updateUserList();
  }

  private void updateUserList() {
    userListContainer.getChildren().clear();
    userListContainer.setSpacing(15);

    // Add user button
    Button addUserButton = new Button("Add User");
    addUserButton.setStyle("-fx-font: 18px \"Roboto\";");
    addUserButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font: 18px \"Roboto\";");
    addUserButton.setOnAction(e -> {
      addUser();
    });
    userListContainer.getChildren().add(addUserButton);

    // Add a title row
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

      Label userIDLabel = new Label(String.valueOf(user.getId()));
      userIDLabel.setStyle("-fx-font: 18px \"Roboto\";");
      userIDLabel.setMinWidth(100);
      userIDLabel.setAlignment(Pos.CENTER_LEFT);

      Label userNameLabel = new Label(user.getUsername());
      userNameLabel.setStyle("-fx-font: 18px \"Roboto\";");
      userNameLabel.setMinWidth(150);
      userNameLabel.setAlignment(Pos.CENTER_LEFT);

      Label passwordLabel = new Label(""); 
      passwordLabel.setStyle("-fx-font: 18px \"Roboto\";");
      passwordLabel.setMinWidth(150);
      passwordLabel.setAlignment(Pos.CENTER_LEFT);

      Label displayNameLabel = new Label(user.getDisplayName());
      displayNameLabel.setStyle("-fx-font: 18px \"Roboto\";");
      displayNameLabel.setMinWidth(150);
      displayNameLabel.setAlignment(Pos.CENTER_LEFT);

      userLine.getChildren().addAll(userIDLabel, userNameLabel, passwordLabel, displayNameLabel);

      if (!user.getUsername().equalsIgnoreCase("admin")) {
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-font: 18px \"Roboto\";");
        editButton.setOnAction(e -> {
          // Call the editUser method to open the edit dialog
          editUser(user);
        });
    
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-font: 18px \"Roboto\";");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font: 18px \"Roboto\";");
        deleteButton.setOnAction(e -> {
          observer.deleteUser(user.getId());
          observer.goToAdmin();
          updateUserList();
        });
        userLine.getChildren().addAll(editButton, deleteButton);
      }
      userListContainer.getChildren().add(userLine);

    }
  }


  private void editUser(User user) {
    Dialog<User> editUserDialog = new Dialog<>();
    editUserDialog.setTitle("Edit User");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField userNameField = new TextField(user.getUsername());
    TextField passwordField = new TextField("****");
    TextField displayNameField = new TextField(user.getDisplayName());

    grid.add(new Label("Username:"), 0, 0);
    grid.add(userNameField, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(passwordField, 1, 1);
    grid.add(new Label("Display Name:"), 0, 2);
    grid.add(displayNameField, 1, 2);
    editUserDialog.getDialogPane().setContent(grid);
    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    editUserDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    
    editUserDialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        observer.editUser(user.getId(), userNameField.getText(), passwordField.getText(), displayNameField.getText());
        System.out.println(user.getId()+" "+userNameField.getText()+" "+passwordField.getText()+" "+displayNameField.getText());
        observer.goToAdmin();
        updateUserList();
      }
      return null;
    });
    editUserDialog.showAndWait();
  }

  /**
   * Add a new user
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
        boolean success = observer.addUser(userNameField.getText(), passwordField.getText(), displayNameField.getText());
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
  


  public void showError(String string) {
    showInlineStyledAlert(AlertType.ERROR, "Error", string);
  }

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
