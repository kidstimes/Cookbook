package cookbook.view;

import java.util.ArrayList;
import java.util.Optional;

import cookbook.model.User;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AdminView {
  private BorderPane view;
  private AdminViewObserver observer;
  private ArrayList<User> users;
  private VBox userListContainer;

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

    for (User user : users) {
      HBox userLine = new HBox(10);
      userLine.setAlignment(Pos.CENTER_LEFT);

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

      Label userNameLabel = new Label(user.getUsername());
      userNameLabel.setStyle("-fx-font: 18px \"Roboto\";");
      userNameLabel.setMinWidth(150);
      userNameLabel.setAlignment(Pos.CENTER_LEFT);

      Label passwordLabel = new Label("111");
      passwordLabel.setStyle("-fx-font: 18px \"Roboto\";");
      passwordLabel.setMinWidth(150);
      passwordLabel.setAlignment(Pos.CENTER_LEFT);

      Label displayNameLabel = new Label(user.getDisplayName());
      displayNameLabel.setStyle("-fx-font: 18px \"Roboto\";");
      displayNameLabel.setMinWidth(150);
      displayNameLabel.setAlignment(Pos.CENTER_LEFT);

      Button editButton = new Button("Edit");
      editButton.setOnAction(e -> {
        // Call the editUser method to open the edit dialog
        editUser(user);
      });

      Button deleteButton = new Button("Delete");
      deleteButton.setOnAction(e -> {
        users.remove(user);
        updateUserList();
      });

      Pane spacer = new Pane();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      userLine.getChildren().addAll(userNameLabel, passwordLabel, displayNameLabel, spacer, editButton, deleteButton);
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
    TextField passwordField = new TextField("111");
    TextField displayNameField = new TextField(user.getDisplayName());

    grid.add(new Label("Username:"), 0, 0);
    grid.add(userNameField, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(passwordField, 1, 1);
    grid.add(new Label("Display Name:"),0, 2);
    grid.add(displayNameField, 1, 2);
    
    editUserDialog.getDialogPane().setContent(grid);
    
    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    editUserDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    
    editUserDialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        user.modifyUsername(userNameField.getText());
        user.modifyDisplayName(displayNameField.getText());
        return user;
      }
      return null;
    });
    
    Optional<User> result = editUserDialog.showAndWait();
    
    result.ifPresent(updatedUser -> {
      int index = users.indexOf(user);
      if (index != -1) {
        users.set(index, updatedUser);
        updateUserList();
      } else {
        showInlineStyledAlert(AlertType.ERROR, "Error", "User not found.");
      }
    });
  }

  private void showInlineStyledAlert(AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStylesheets().add(getClass().getResource("inline-style.css").toExternalForm());
    dialogPane.getStyleClass().add("myAlert");
    alert.showAndWait();
  }

  public void addUser(User user) {
    users.add(user);
    updateUserList();
  }

}
