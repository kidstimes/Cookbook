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
import javafx.scene.layout.HBox;
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
  private int numberUnreadMessages;
  private VBox sidebar;
  
  /**
   * Home Page View Constructor.
   */
  public HomePageView(String displayName,
         boolean hasWeeklyDinner, boolean hasNextWeekShoppingList, int numberUnreadMessages) {
    this.view = new BorderPane();
    view.setStyle("-fx-background-color: #F9F8F3;");
    this.displayName = displayName;
    this.hasWeeklyDinner = hasWeeklyDinner;
    this.hasNextWeekShoppingList = hasNextWeekShoppingList;
    this.numberUnreadMessages = numberUnreadMessages;
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
    this.sidebar = new VBox(20);
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

    Region hspacer = new Region();
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

    Label messagesLabel = new Label("You have " + numberUnreadMessages + " unread messages.");
    messagesLabel.setFont(Font.font("Roboto", 22));

    centerView.getChildren().addAll(welcomLabel,
           dateLabel, weekLabel, weeklyDinnerLabel, shoppingListLabel, messagesLabel);


    view.setCenter(centerView);


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
