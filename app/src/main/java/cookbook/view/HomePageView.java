package cookbook.view;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;






/**
 * The view for the home page.
 */
public class HomePageView {

  private HomePageViewObserver observer;
  private BorderPane view;
  private String displayName;
  boolean hasWeeklyDinner;
  boolean hasNextWeekShoppingList;
  
  /**
   * Home Page View Constructor.
   */
  public HomePageView(String displayName,
         boolean hasWeeklyDinner, boolean hasNextWeekShoppingList) {
    this.view = new BorderPane();
    view.setStyle("-fx-background-color: #F9F8F3;");
    this.displayName = displayName;
    this.hasWeeklyDinner = hasWeeklyDinner;
    this.hasNextWeekShoppingList = hasNextWeekShoppingList;
    System.out.println(hasWeeklyDinner);
    initLayout();
    
  }

  /**
   * Set an observer of the home page view..
   */
  public void setObserver(HomePageViewObserver observer) {
    this.observer = observer;
  }

  /** Get the view of the home page.
   *
   * @return the view of the home page
   */
  public Node getView() {
    return view;
  }

  /**
   * Initializes the layout of the home page.
   */
  public void initLayout() {
    createSidebar();
    createCenterView();
  
  }

  /**
   * Create a sidemenu for the home page.
   */
  public void createSidebar() {
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(30);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");

    // Add a title to the homepage
    Text title = new Text(displayName + ", welcome!");
    title.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(title);

    // Add five options to the homepage, one per row
    Button[] sidebarButtons = {
      createButton("Home Page", null),
      createButton("Browse Recipes", e -> observer.goToBrowser()),
      createButton("Add a Recipe", e -> observer.goToAddRecipe()),
      createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
      createButton("My Favorites", e -> observer.goToMyFavorite()),
      createButton("My Shopping List", e -> observer.goToShoppingList()),
      createButton("Messages", e -> observer.goToMessages()),
      };
    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);

    //Add a Hyperlink to change the password
    Hyperlink changePasswordButton = new Hyperlink("Change Password");
    sidebar.getChildren().add(changePasswordButton);
    changePasswordButton.setFont(Font.font("Roboto", 14));
    changePasswordButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    changePasswordButton.setOnAction(e -> {
      Stage passwordStage = new Stage();
      passwordStage.initModality(Modality.APPLICATION_MODAL);
      passwordStage.setTitle("Change Password");    
      GridPane grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setVgap(10);
      grid.setHgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));       
      Label oldPasswordLabel = new Label("Old password:");
      grid.add(oldPasswordLabel, 0, 1);       
      PasswordField oldPasswordField = new PasswordField();
      grid.add(oldPasswordField, 1, 1);       
      Label newPasswordLabel = new Label("New password:");
      grid.add(newPasswordLabel, 0, 2);    
      PasswordField newPasswordField = new PasswordField();
      grid.add(newPasswordField, 1, 2);       
      Label confirmNewPasswordLabel = new Label("Confirm new password:");
      grid.add(confirmNewPasswordLabel, 0, 3);        
      PasswordField confirmNewPasswordField = new PasswordField();
      grid.add(confirmNewPasswordField, 1, 3);      
      Button btnSave = new Button("Save");
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
        if (!newPassword.equals(confirmNewPassword)) {
          showError("New password and confirm password do not match");
        } else {
          observer.handlePasswordChange(oldPassword, newPassword);
          passwordStage.close();
          observer.goToHomePage();
        }
      });
      
      Scene scene = new Scene(grid, 400, 200);
      passwordStage.setScene(scene);
      passwordStage.show();
    });

    //Add a Hyperlink to change display name, just similar with change password
    Hyperlink changeDisplayNameButton = new Hyperlink("Change Display Name");
    sidebar.getChildren().add(changeDisplayNameButton);
    changeDisplayNameButton.setFont(Font.font("Roboto", 14));
    changeDisplayNameButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    changeDisplayNameButton.setOnAction(e -> {
      Stage displayNameStage = new Stage();
      displayNameStage.initModality(Modality.APPLICATION_MODAL);
      displayNameStage.setTitle("Change Display Name");
      GridPane grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setVgap(10);
      grid.setHgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));
      Label newDisplayNameLabel = new Label("New display name:");
      grid.add(newDisplayNameLabel, 0, 1);
      TextField newDisplayNameField = new TextField();
      grid.add(newDisplayNameField, 1, 1);
      Button btnSave = new Button("Save");
      grid.add(btnSave, 1, 2);
      btnSave.setOnAction(ev -> {
        String newDisplayName = newDisplayNameField.getText();
        if (newDisplayName.isEmpty()) {
          showError("Please fill the new display name.");
        } else {
          observer.changeDisplayName(newDisplayName);
          displayNameStage.close();
          observer.goToHomePage();
        }
      });
      Scene scene = new Scene(grid, 400, 100);
      displayNameStage.setScene(scene); 
      displayNameStage.show(); 
    });

    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 14));
    logoutButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(event -> observer.userLogout());
    sidebar.getChildren().add(logoutButton);
    view.setLeft(sidebar);
  }

  private void createCenterView() {
    VBox centerView = new VBox(50);
    centerView.setStyle("-fx-padding: 50px 20px 20px 20px;");
    centerView.setAlignment(Pos.TOP_LEFT);
    Label title = new Label("Home Page");
    title.setFont(Font.font("Roboto", 32));
    centerView.getChildren().add(title);
    Label welcomLabel = new Label("Welcome to cookbook, " + displayName + "!");
    welcomLabel.setFont(Font.font("Roboto", 24));
    LocalDate currentDate = LocalDate.now();
    int weekNumber = getWeekNumber(currentDate);
    Label dateLabel = new Label("Today's Date: " + currentDate);
    Label weekLabel = new Label("Current Week Number: " + weekNumber);
    Label weeklyDinnerLabel;
    if (hasWeeklyDinner) {
      weeklyDinnerLabel = new Label("You have dinners planned for this week.");
    } else {
      weeklyDinnerLabel = new Label("You do not have dinners planned for this week.");
    }
    dateLabel.setFont(Font.font("Roboto", 22));
    weekLabel.setFont(Font.font("Roboto", 22));
    weeklyDinnerLabel.setFont(Font.font("Roboto", 22));
    Label shoppingListLabel;
    if (hasNextWeekShoppingList) {
      shoppingListLabel = new Label("You have a shopping list for next week.");
    } else {
      shoppingListLabel =
          new Label("You do not have a shopping list for next week.");
    }
    shoppingListLabel.setFont(Font.font("Roboto", 22));
    centerView.getChildren().addAll(welcomLabel,
           dateLabel, weekLabel, weeklyDinnerLabel, shoppingListLabel);


    view.setCenter(centerView);
  }

  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;-fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setMinWidth(100); // Set the fixed width for each button
    button.setMaxWidth(Double.MAX_VALUE); // Ensure the button text is fully visible
    button.setOnAction(eventHandler);
    return button;
  }

  private int getWeekNumber(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return date.get(weekFields.weekOfWeekBasedYear());
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
