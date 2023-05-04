package cookbook.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;




/**
 * The view for the home page.
 */
public class HomePageView {

  private HomePageViewObserver observer;
  private BorderPane view;
  private String displayName;

  
  /**
   * Home Page View Constructor.
   */
  public HomePageView(String displayName) {
    this.view = new BorderPane();
    view.setStyle("-fx-background-color: #F9F8F3;");
    this.displayName = displayName;
    initLayout();
    
  }

  /**
   * Set an observer of the home page view..
   */
  public void setObserver(HomePageViewObserver observer) {
    this.observer = observer;
  }


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
      createButton("Browse Recipes", e -> observer.goToBrowser()),
      createButton("Add a Recipe", e -> observer.goToAddRecipe()),
      createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
      createButton("My Favorites", e -> {}),
      createButton("My Shopping List", e -> {})
      };
    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }

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
  }

  private void createCenterView() {
    VBox centerView = new VBox(30);
    centerView.setStyle("-fx-padding: 50px 20px 20px 20px;");
    centerView.setMaxWidth(300);

    LocalDate currentDate = LocalDate.now();
    int weekNumber = getWeekNumber(currentDate);

    Label dateLabel = new Label("Today's Date: " + currentDate);
    Label weekLabel = new Label("Current Week Number: " + weekNumber);
    Label weeklyDinnerLabel = new Label("You have dinners planned for this week.");
    Label shoppingListLabel = new Label("You have a shopping list for this week.");

    dateLabel.setFont(Font.font("Roboto", 18));
    weekLabel.setFont(Font.font("Roboto", 18));
    weeklyDinnerLabel.setFont(Font.font("Roboto", 18));
    shoppingListLabel.setFont(Font.font("Roboto", 18));

    centerView.getChildren().addAll(dateLabel, weekLabel, weeklyDinnerLabel, shoppingListLabel);
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

}
