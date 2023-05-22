package cookbook.view;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


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
  
  /**
   * Home Page View Constructor.
   */
  public HomePageView(String displayName,
         boolean hasWeeklyDinner, boolean hasNextWeekShoppingList, int numberUnreadMessages) {
    this.view = new BorderPane();
    view.setStyle("-fx-background-color: #69a486;");
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
    
    sidebar.setActiveButton("Home Page");
    sidebar.finalizeLayout();
    view.setLeft(sidebar);

  }


  // Create the center view of the home page.
  private void createCenterView() {
    VBox centerView = new VBox(50);
    centerView.setStyle("-fx-padding: 50px; -fx-background-color: #F9F8F3;");
    centerView.setAlignment(Pos.TOP_LEFT);
    Label title = new Label("Home Page");
    title.setFont(Font.font("Roboto", 32));
    title.setStyle("-fx-text-fill: #69a486;");
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


  // Get the week number of the year.
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
