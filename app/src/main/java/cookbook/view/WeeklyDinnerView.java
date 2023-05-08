package cookbook.view;

import cookbook.model.Dinner;
import cookbook.model.Recipe;
import cookbook.view.WeeklyDinnerViewObserver;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The view for the weekly dinner page.
 */
public class WeeklyDinnerView {
  private WeeklyDinnerViewObserver observer;
  private BorderPane view;
  private GridPane daysGrid;
  private VBox centerView;
  private LocalDate currentDate;
  private LocalDate currentWeekStart;
  private Label weekNumberLabel;
  private Label yearNumberLabel;
  private ArrayList<Dinner> dinnerList;
  private String displayName;
  private int daysGridIndex;

  /**
   * Constructor for the weekly dinner view.
   */
  public WeeklyDinnerView(String displayName, ArrayList<Dinner> dinnerList) {
    this.view = new BorderPane();
    this.displayName = displayName;
    this.dinnerList = dinnerList;
    currentDate = LocalDate.now();
    currentWeekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    initLayout();
  }


  // Set the observer for the view
  public void setObserver(WeeklyDinnerViewObserver observer) {
    this.observer = observer;
  }

  // Get the view
  public Node getView() {
    return view;
  }

  // Initialize layout
  private void initLayout() {
    createSidebar();
    createCenterView();
  }

  // Create the side bar menu
  private void createSidebar() {
    VBox sidebar = new VBox(30);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text title = new Text(displayName + ", welcome!");
    title.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(title);

    Button[] sidebarButtons = {
        createButton("Home Page", e -> observer.goToHomePage()),
        createButton("Browse Recipes", e -> observer.goToBrowser()),
        createButton("Add a Recipe", e -> observer.goToAddRecipe()),
        createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
        createButton("My Favorites", e -> observer.goToMyFavorite()),
        createButton("My Shopping List", e -> observer.goToShoppingList())
      };
    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);
    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 18));
    logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> observer.userLogout());
    sidebar.getChildren().add(logoutButton);
    view.setLeft(sidebar);
  }

  private void createCenterView() {
    centerView = new VBox(20);
    centerView.setPadding(new Insets(40)); 
    centerView.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");

    // Add title above the weekly menu
    Label titleLabel = new Label("Weekly Dinner List");
    titleLabel.setStyle("-fx-font: 32px \"Roboto\";");
    centerView.getChildren().add(titleLabel);

    VBox weekNavigation = createWeekNavigation();
    centerView.getChildren().add(weekNavigation);

    updateWeekLayout(currentWeekStart);
    view.setCenter(centerView);
  }

  private VBox createWeekNavigation() {
    Button previousWeekButton = new Button("Previous Week");
    previousWeekButton.setStyle("-fx-font: 20px \"Roboto\";");
    previousWeekButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-cursor: hand;");
    previousWeekButton.setOnAction(event -> handlePreviousWeek());


    Button nextWeekButton = new Button("Next Week");
    nextWeekButton.setOnAction(event -> handleNextWeek());
    nextWeekButton.setStyle("-fx-font: 20px \"Roboto\";");
    nextWeekButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-cursor: hand;");

    weekNumberLabel = new Label();
    yearNumberLabel = new Label();

    // Add TextField for entering the week number
    TextField weekNumberInput = new TextField();
    weekNumberInput.setPromptText("Current year week number");
    weekNumberInput.setMaxWidth(200);

    // Add Button for navigating to the entered week number
    Button goToWeekButton = new Button("Go to Week");
    goToWeekButton.setStyle("-fx-font: 14px \"Roboto\";");
    goToWeekButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-cursor: hand;");
    goToWeekButton.setOnAction(event -> {
      try {
        int weekNumber = Integer.parseInt(weekNumberInput.getText());
        goToWeekNumber(weekNumber);
      } catch (NumberFormatException e) {
        // Handle invalid input
        showInlineStyledAlert(AlertType.ERROR,
            "Invalid Week Number", "Please enter a valid week number for the current year.");
      }
    });
    
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    // Create HBox for the top row of the navigation bar
    HBox topRowNavigation = new HBox(previousWeekButton, leftSpacer, weekNumberLabel,
        yearNumberLabel, rightSpacer, nextWeekButton);
    topRowNavigation.setAlignment(Pos.CENTER);
    topRowNavigation.setSpacing(5);

    // Create HBox for the bottom row of the navigation bar
    HBox bottomRowNavigation = new HBox(weekNumberInput, goToWeekButton);
    bottomRowNavigation.setAlignment(Pos.CENTER);
    bottomRowNavigation.setSpacing(5);

    // Create a VBox to contain both the top and bottom rows
    VBox weekNavigation = new VBox(topRowNavigation, bottomRowNavigation);
    weekNavigation.setSpacing(10);

    return weekNavigation;
  }

  private void goToWeekNumber(int weekNumber) {
    int currentYear = LocalDate.now().getYear();
    if (weekNumber > 0 && weekNumber <= getWeeksInYear(currentYear)) {
      LocalDate firstDayOfYear = LocalDate.of(currentYear, 1, 1);
      LocalDate firstMondayOfYear 
          = firstDayOfYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
      currentWeekStart = firstMondayOfYear.plusWeeks(weekNumber - 1);
      updateWeekLayout(currentWeekStart);
    } else {
      // Handle invalid input with an alert
      showInlineStyledAlert(AlertType.ERROR,
          "Invalid Week Number", "Please enter a valid week number for the current year.");
    }
  }

  private int getWeeksInYear(int year) {
    LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return lastDayOfYear.get(weekFields.weekOfWeekBasedYear());
  }

  private void updateWeekLayout(LocalDate weekStart) {
    if (daysGrid != null) {
      centerView.getChildren().remove(daysGridIndex);
    }

    daysGrid = createDaysGrid(weekStart);
    daysGridIndex = centerView.getChildren().size();
    centerView.getChildren().add(daysGrid);

    int year = weekStart.getYear();
    yearNumberLabel.setText(", Year " + Integer.toString(year));
    yearNumberLabel.setStyle("-fx-font: 20px \"Roboto\";");
    int weekNumber = getWeekNumber(weekStart);
    weekNumberLabel.setText(" Week " + weekNumber);
    weekNumberLabel.setStyle("-fx-font: 20px \"Roboto\";");

    // Check if the displayed week is the current week
    LocalDate today = LocalDate.now();
    int currentWeekNumber = getWeekNumber(today);
    if (weekNumber == currentWeekNumber && year == today.getYear()) {
      weekNumberLabel.setText(" Current Week (Week " + weekNumber + ") ");
    }
  }


  private GridPane createDaysGrid(LocalDate weekStart) {
    GridPane weekGrid = new GridPane();
    weekGrid.setHgap(10);
    weekGrid.setVgap(10);
    weekGrid.setPadding(new Insets(20));

    for (int i = 0; i < 7; i++) {
      ColumnConstraints col = new ColumnConstraints();
      col.setPercentWidth(100.0 / 7);
      weekGrid.getColumnConstraints().add(col);
      VBox dayBox = createDayBox(weekStart.plusDays(i));
      weekGrid.add(dayBox, i, 1);
    }

    return weekGrid;
  }

  private void handlePreviousWeek() {
    currentWeekStart = currentWeekStart.minusWeeks(1);
    updateWeekLayout(currentWeekStart);
  }

  private void handleNextWeek() {
    currentWeekStart = currentWeekStart.plusWeeks(1);
    updateWeekLayout(currentWeekStart);
  }

  private VBox createDayBox(LocalDate dayDate) {
    VBox dayBox = new VBox(5);
    dayBox.setPadding(new Insets(5));

    String dayName = dayDate.getDayOfWeek().toString();
    Label dayLabel = new Label(dayName);
    dayLabel.setStyle("-fx-font: 18px \"Roboto\";");
    Label dateLabel = new Label(dayDate.toString());
    dateLabel.setStyle("-fx-font: 18px \"Roboto\";");
    dayBox.getChildren().addAll(dayLabel, dateLabel);

    updateRecipeList(dayBox, dayDate);
    return dayBox;
  }

  private void updateRecipeList(VBox dayBox, LocalDate dayDate) {
    if (dinnerList != null) {
      Dinner matchingDinner = null;
      for (Dinner dinner : dinnerList) {
        if (dinner.getDate().equals(dayDate)) {
          matchingDinner = dinner;
          break;
        }
      }
      if (matchingDinner != null) {
        for (Recipe recipe : matchingDinner.getRecipes()) {
          Hyperlink recipeLink = new Hyperlink(recipe.getName());
          recipeLink.setOnAction(event -> {
            if (observer != null) {
              observer.goToRecipe(recipe);
            }
          });
          recipeLink.setStyle("-fx-font: 16px \"Roboto\";");
          dayBox.getChildren().add(recipeLink);
        }
      }
    }
  }
  
  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setMaxWidth(Double.MAX_VALUE);
    button.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;-fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setOnAction(eventHandler);
    return button;
  }
  
  private int getWeekNumber(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return date.get(weekFields.weekOfWeekBasedYear());
  }

  /**
   * Show an alert with the given alert type, title, and message.
   */
  private void showInlineStyledAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    // Set custom styles for the alert
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 18px; -fx-background-color: #F9F8F3; -fx-border-color: #F9F8F3;");
    // Set custom styles for the buttons
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> {
      button.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    });
    // Set custom styles for the content label
    Label contentLabel = (Label) dialogPane.lookup(".content");
    contentLabel.setStyle("-fx-text-fill: #3D405B;");
    alert.showAndWait();
  }



}
     

