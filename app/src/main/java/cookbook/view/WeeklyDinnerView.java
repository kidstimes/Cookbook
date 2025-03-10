package cookbook.view;

import cookbook.model.Dinner;
import cookbook.model.Recipe;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The view for the weekly dinner page.
 */
public class WeeklyDinnerView {
  private WeeklyDinnerViewObserver observer;
  private BorderPane view;
  private GridPane daysGrid;
  private VBox centerView;
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
    LocalDate currentDate = LocalDate.now();
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
    Sidebar sidebar = new Sidebar(displayName);
    sidebar.addButton("Home", e -> observer.goToHomePage(), "/images/home.png");
    sidebar.addButton("All Recipes", e -> observer.goToBrowser(), "/images/recipe.png");
    sidebar.addButton("Add a Recipe", e -> observer.goToAddRecipe(), "/images/add.png");
    sidebar.addButton("Weekly Dinner List", e -> observer.goToWeeklyDinner(), "/images/weekly.png");
    sidebar.addButton("My Favorites", e -> observer.goToMyFavorite(), "/images/favorite.png");
    sidebar.addButton("My Shopping List", e -> observer.goToShoppingList(),
        "/images/shoppinglist.png");
    sidebar.addButton("Messages", e -> observer.goToMessages(), "/images/messages.png");
    sidebar.addButton("My Account", e -> observer.goToAccount(), "/images/account.png");
    sidebar.addHyperlink("Help", e -> observer.goToHelp());
    sidebar.addHyperlink("Log Out", e -> observer.userLogout());
    
    sidebar.setActiveButton("Weekly Dinner List");
    sidebar.finalizeLayout();
    // Add the sidebar to the view
    view.setLeft(sidebar);
  }

  /**
   * Create a center view for displaying weekly dinner.
   */
  private void createCenterView() {
    centerView = new VBox(50);
    centerView.setStyle("-fx-padding: 50px; -fx-background-color: #F9F8F3;"
        + "-fx-border-color: lightgrey; -fx-border-width: 1px;");
    centerView.setAlignment(Pos.TOP_LEFT);

    ScrollPane scrollPane = new ScrollPane(centerView);
    scrollPane.setFitToWidth(true); 
    scrollPane.setStyle("-fx-background-color: #F9F8F3;");
    scrollPane.setFitToHeight(true);

    // Add title above the weekly menu
    Label titleLabel = new Label("Weekly Dinner List");
    titleLabel.setStyle("-fx-font: 32px \"Roboto\";-fx-text-fill: #3F6250;-fx-font-weight: bold;");
    centerView.getChildren().add(titleLabel);

    VBox weekNavigation = createWeekNavigation();
    centerView.getChildren().add(weekNavigation);

    updateWeekLayout(currentWeekStart);
    view.setCenter(scrollPane);
  }

  /**Week navigation section for the weekly dinner view.
   *
   * @return a VBox containing the week navigation section
   */
  private VBox createWeekNavigation() {
    Button previousWeekButton = new Button("Previous Week");
    previousWeekButton.setStyle(
        " -fx-font: 18px \"Roboto\";-fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    previousWeekButton.setOnAction(event -> handlePreviousWeek());
    Button nextWeekButton = new Button("Next Week");
    nextWeekButton.setOnAction(event -> handleNextWeek());
    nextWeekButton.setStyle(
        "-fx-font: 18px \"Roboto\"; -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    weekNumberLabel = new Label();
    yearNumberLabel = new Label();

    TextField weekNumberInput = new TextField();
    weekNumberInput.setPromptText("Current year week number");
    weekNumberInput.setStyle("-fx-font: 14px \"Roboto\";");
    weekNumberInput.setMinWidth(200);
    weekNumberInput.setTooltip(new Tooltip("Enter a week number between 1 and 52"));

    // Add Button for navigating to the entered week number
    Button goToWeekButton = new Button("Go to Week");
    goToWeekButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;-fx-font: 16px \"Roboto\";");
    goToWeekButton.setOnAction(event -> {
      try {
        int weekNumber = Integer.parseInt(weekNumberInput.getText());
        goToWeekNumber(weekNumber);
      } catch (NumberFormatException e) {
        // Handle invalid input
        showInlineStyledAlert();
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
    weekNavigation.setPadding(new Insets(10, 10, 0, 10));

    return weekNavigation;
  }

  /** Select a week of current year to display its weekly dinner.
   *
   * @param weekNumber the week number of the current year
   */
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
      showInlineStyledAlert();
    }
  }

  /** Get the total week number of the year.
   *
   * @param year the year to get the week number of
   * @return the week number of the year
   */
  private int getWeeksInYear(int year) {
    LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return lastDayOfYear.get(weekFields.weekOfWeekBasedYear());
  }

  /** Update the weekly dinner view to display another week.
   *
   * @param weekStart the date of the Monday of the week to display.
   */
  private void updateWeekLayout(LocalDate weekStart) {
    if (daysGrid != null) {
      centerView.getChildren().remove(daysGridIndex);
    }

    daysGrid = createDaysGrid(weekStart);
    daysGridIndex = centerView.getChildren().size();
    centerView.getChildren().add(daysGrid);

    int year = weekStart.getYear();
    yearNumberLabel.setText(", Year " + year);
    yearNumberLabel.setStyle("-fx-font: 20px \"Roboto\";-fx-font-weight: bold;");
    int weekNumber = getWeekNumber(weekStart);
    weekNumberLabel.setText(" Week " + weekNumber);
    weekNumberLabel.setStyle("-fx-font: 20px \"Roboto\";-fx-font-weight: bold;");

    // Check if the displayed week is the current week
    LocalDate today = LocalDate.now();
    int currentWeekNumber = getWeekNumber(today);
    if (weekNumber == currentWeekNumber && year == today.getYear()) {
      weekNumberLabel.setText(" (Current Week) Week " + weekNumber + " ");
    }
  }


  /**Create a grid for 7 days of a week.
   *
   * @param weekStart the date of the Monday of the week to display.
   * @return the week grid for 7 days of a week.
   */
  private GridPane createDaysGrid(LocalDate weekStart) {
    GridPane weekGrid = new GridPane();
    weekGrid.setHgap(10);
    weekGrid.setVgap(10);
    weekGrid.setPadding(new Insets(10));

    for (int i = 0; i < 7; i++) {
      ColumnConstraints col = new ColumnConstraints();
      col.setPercentWidth(100.0 / 7);
      weekGrid.getColumnConstraints().add(col);
      VBox dayBox = createDayBox(weekStart.plusDays(i));
      weekGrid.add(dayBox, i, 1);
    }
    return weekGrid;
  }

  // Handle the previous week button
  private void handlePreviousWeek() {
    currentWeekStart = currentWeekStart.minusWeeks(1);
    updateWeekLayout(currentWeekStart);
  }

  // Handle the next week button
  private void handleNextWeek() {
    currentWeekStart = currentWeekStart.plusWeeks(1);
    updateWeekLayout(currentWeekStart);
  }

  /** Create a VBox for a day of the week.
   *
   * @param dayDate the date of the day to display
   * @return the VBox for the day
   */
  private VBox createDayBox(LocalDate dayDate) {
    VBox dayBox = new VBox(15);

    String dayName = dayDate.getDayOfWeek().toString();
    Label dayLabel = new Label(dayName);
    dayLabel.setStyle("-fx-font: 18px \"Roboto\";-fx-font-weight: bold;");
    Label dateLabel = new Label(dayDate.toString());
    dateLabel.setStyle("-fx-font: 18px \"Roboto\";-fx-font-weight: bold;");
    dayBox.getChildren().addAll(dayLabel, dateLabel);

    updateRecipeList(dayBox, dayDate);
    return dayBox;
  }

  /** Update the recipe list for a day of the week.
   *
   * @param dayBox the VBox for the day
   * @param dayDate the date of the day to display
   */
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
          //set style for the hyperlink
          recipeLink.setStyle("-fx-font: 16px \"Roboto\"; -fx-cursor: hand; ");
          recipeLink.setOnAction(event -> {
            if (observer != null) {
              observer.goToRecipe(recipe);
            }
          });
          // Add a tooltip with the description for the hovering effect
          Tooltip tooltip = new Tooltip(recipe.getName());
          tooltip.setFont(Font.font("ROBOTO", 16));
          tooltip.setStyle("-fx-background-color: #F9F8F3; -fx-text-fill: #3D405B;");
          Tooltip.install(recipeLink, tooltip);
          recipeLink.setStyle("-fx-font: 20px \"Roboto\";");
          // Add a delete button for the recipe
          Button deleteButton = new Button("Delete");
          deleteButton.setStyle("-fx-font: 14px \"Roboto\"; -fx-background-color:"
              + "white ; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
          deleteButton.setMinWidth(70);
          deleteButton.setMaxWidth(70);
          deleteButton.setMinHeight(25);
          deleteButton.setMaxHeight(25);
          deleteButton.setOnAction(event -> {
            observer.removeRecipeFromWeeklyDinner(dayDate, recipe);
            dayBox.getChildren().removeIf(node -> dayBox.getChildren().indexOf(node) > 1);
            updateRecipeList(dayBox, dayDate);
          });
          VBox recipeBox = new VBox(recipeLink, deleteButton);
          recipeBox.setAlignment(Pos.CENTER_LEFT);
          dayBox.getChildren().add(recipeBox);          
        }
      }
    }
  }
  
  private int getWeekNumber(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return date.get(weekFields.weekOfWeekBasedYear());
  }

  /**
   * Show an alert with the given alert type, title, and message.
   */
  private void showInlineStyledAlert() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Invalid Week Number");
    alert.setHeaderText(null);
    alert.setContentText("Please enter a valid week number for the current year.");
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 18px;"
        + " -fx-background-color: #F9F8F3; -fx-border-color: #F9F8F3;");
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> button.setStyle("-fx-background-color: #3D405B;"
        + " -fx-text-fill: white; -fx-padding: 5 10 5 10;"));
    Label contentLabel = (Label) dialogPane.lookup(".content");
    contentLabel.setStyle("-fx-text-fill: #3D405B;");
    alert.showAndWait();
  }



}
     

