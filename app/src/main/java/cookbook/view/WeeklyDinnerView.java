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
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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

  public void setDinnerList(ArrayList<Dinner> dinnerList) {
    this.dinnerList = dinnerList;
  }

  // Set the observer for the view
  public void setObserver(WeeklyDinnerViewObserver observer) {
    this.observer = observer;
  }

  // Get the view
  public Node getView() {
    return view;
  }

  /**
   * Set the user displayName
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  // Initialize layout
  private void initLayout() {
    createSidebar();
    createCenterView();
  }

  private void createSidebar() {
    VBox sidebar = new VBox(30);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text title = new Text(displayName + ", welcome!");
    title.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(title);

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
    logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> observer.userLogout());
    sidebar.getChildren().add(logoutButton);
    view.setLeft(sidebar);
  }

  private void createCenterView() {
    centerView = new VBox(20);
    centerView.setPadding(new Insets(10));

    Hyperlink backButton = new Hyperlink("← Back to Home Page");
    backButton.setFont(Font.font("ROBOTO", 16));
    backButton.setOnAction(e -> {
      if (observer != null) {
        observer.goToHomePage();
      }
    });
    centerView.getChildren().add(backButton);

    HBox weekNavigation = createWeekNavigation();
    centerView.getChildren().add(weekNavigation);

    updateWeekLayout(currentWeekStart);
    view.setCenter(centerView);
  }

  private HBox createWeekNavigation() {
    Button previousWeekButton = new Button("Previous Week");
    previousWeekButton.setOnAction(event -> handlePreviousWeek());

    Button nextWeekButton = new Button("Next Week");
    nextWeekButton.setOnAction(event -> handleNextWeek());

    weekNumberLabel = new Label();
    HBox weekNavigation = new HBox(20, previousWeekButton, weekNumberLabel, nextWeekButton);
    weekNavigation.setAlignment(Pos.CENTER);

    return weekNavigation;
  }

  private void updateWeekLayout(LocalDate weekStart) {
    if (daysGrid != null) {
      centerView.getChildren().remove(daysGridIndex);
    }

    daysGrid = createDaysGrid(weekStart);
    daysGridIndex = centerView.getChildren().size();
    centerView.getChildren().add(daysGrid);

    int weekNumber = getWeekNumber(weekStart);
    weekNumberLabel.setText("Week " + weekNumber);
    weekNumberLabel.setStyle("-fx-font: 16px \"Roboto\";");
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
    dayLabel.setStyle("-fx-font: 16px \"Roboto\";");
    Label dateLabel = new Label(dayDate.toString());
    dateLabel.setStyle("-fx-font: 16px \"Roboto\";");
    dayBox.getChildren().addAll(dayLabel, dateLabel);

    updateRecipeList(dayBox, dayDate);

    /*Button addRecipeButton = new Button("Add Recipe");
    addRecipeButton.setOnAction(event -> {
      // Open a new window or dialog for the user to input recipe details
      // After the user submits the new recipe, add it to the Dinner object for that date
      // Update the day box to display the new recipe
    });
    dayBox.getChildren().add(addRecipeButton);*/

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
          Label recipeLabel = new Label(recipe.getName());
          recipeLabel.setStyle("-fx-font: 14px \"Roboto\";");
          dayBox.getChildren().add(recipeLabel);
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
}
     

