package cookbook.view;

import cookbook.model.Ingredient;
import cookbook.model.ShoppingList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * View class for the shopping list page.
 */
public class ShoppingListView {
  private BorderPane view;
  private ShoppingListViewObserver observer;
  private String displayName;
  private VBox centerView;
  private LocalDate currentDate;
  private LocalDate currentWeekStart;
  private Label weekNumberLabel;
  private Label yearNumberLabel;
  private ArrayList<ShoppingList> shoppingLists;
  private VBox ingredientListContainer;
  private Button previousWeekButton;

  /**
   * Constructor for the shopping list view.
   *
   * @param displayName is the displayname of the user
   */
  public ShoppingListView(String displayName, ArrayList<ShoppingList> shoppingLists) {
    this.view = new BorderPane();
    this.displayName = displayName;
    this.shoppingLists = shoppingLists;
    currentDate = LocalDate.now();
    currentWeekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    initLayout();
  }
  
  /** Set the observer for this view.
  *
  * @param observer the observer
  */
  public void setObserver(ShoppingListViewObserver observer) {
    this.observer = observer;
  }

  //get the view
  public Node getView() {
    return view;
  }

  /**
   * Initialize the layout of the view.
   */
  public void initLayout() {
    createSideBar();
    createCenterView();
  }

  /**
   * Create side bar menu.
   */
  public void createSideBar() {
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(30);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text welcomeTitle = new Text(displayName + ", welcome!");
    welcomeTitle.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(welcomeTitle);
    Button[] sidebarButtons = {
      createButton("Home Page", e -> observer.goToHomePage()),
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
  
  /**
   * Create a center view for displaying weekly dinner.
   */
  private void createCenterView() {
    centerView = new VBox(20);
    centerView.setPadding(new Insets(40)); 
    centerView.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");

    ScrollPane scrollPane = new ScrollPane(centerView);
    scrollPane.setFitToWidth(true); 
    scrollPane.setStyle("-fx-background-color: #F9F8F3;");

    // Add title above the weekly menu
    Label titleLabel = new Label("Shopping List");
    titleLabel.setStyle("-fx-font: 32px \"Roboto\";");

    Button refreshButton = new Button("Refresh Shopping List");

    refreshButton.setOnAction(event -> {
      observer.refreshShoppingListWithWeeklyDinnerList();
      observer.goToShoppingList();
    });
    //add a hovering text for refresh button
    Tooltip tooltip = new Tooltip("Refresh the shopping list with the updated weekly dinner list");
    Tooltip.install(refreshButton, tooltip);


    refreshButton.setStyle("-fx-font: 18px \"Roboto\";");
    refreshButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:"
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    centerView.getChildren().addAll(titleLabel, refreshButton);

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
    previousWeekButton = new Button("Previous Week");
    previousWeekButton.setOnAction(event -> handlePreviousWeek());
    previousWeekButton.setStyle("-fx-font: 20px \"Roboto\";");
    previousWeekButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:"
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    previousWeekButton.setDisable(true); 

    Button nextWeekButton = new Button("Next Week");
    nextWeekButton.setOnAction(event -> handleNextWeek());
    nextWeekButton.setStyle("-fx-font: 20px \"Roboto\";");
    nextWeekButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    weekNumberLabel = new Label();
    weekNumberLabel.setStyle("-fx-font: 22px \"Roboto\";");
    yearNumberLabel = new Label();
    yearNumberLabel.setStyle("-fx-font: 22px \"Roboto\";");

    // Add TextField for entering the week number
    TextField weekNumberInput = new TextField();
    weekNumberInput.setPromptText("Current year week number");
    weekNumberInput.setMaxWidth(200);

    // Add Button for navigating to the entered week number
    Button goToWeekButton = new Button("Go to Week");
    goToWeekButton.setStyle("-fx-font: 14px \"Roboto\";");
    goToWeekButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    goToWeekButton.setOnAction(event -> {
      try {
        int weekNumber = Integer.parseInt(weekNumberInput.getText());
        goToWeekNumber(weekNumber);
      } catch (NumberFormatException e) {
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

  // Update the week layout for each week, showing shopping list for the week
  private void updateWeekLayout(LocalDate weekStart) {
    int weekNumber = getWeekNumber(weekStart);
    //if is current week, shown as "Current Week"
    if (weekNumber == getWeekNumber(LocalDate.now())) {
      weekNumberLabel.setText("(Current Week) Week " + weekNumber);
      yearNumberLabel.setText(", Year " + weekStart.getYear());
    } else {
      weekNumberLabel.setText("Week " + weekNumber);
      yearNumberLabel.setText(", Year " + weekStart.getYear());
    }
  
    if (ingredientListContainer != null) {
      centerView.getChildren().remove(ingredientListContainer);
    }
    ingredientListContainer = new VBox(10);
    centerView.getChildren().add(ingredientListContainer);
    boolean foundWeek = false;

    int currentWeekNumber = getWeekNumber(LocalDate.now());
    previousWeekButton.setDisable(weekNumber <= currentWeekNumber); 
  
    for (ShoppingList shoppingList : shoppingLists) {
      if (shoppingList.getWeekNumber() == weekNumber) {
        if (!shoppingList.getIngredients().isEmpty()) {
          // Add title row
          HBox titleLine = new HBox(10);
          titleLine.setAlignment(Pos.CENTER_LEFT);
          titleLine.setFillHeight(false);
          Label quantityTitleLabel = new Label("Quantity");
          quantityTitleLabel.setStyle("-fx-font: 20px \"Roboto\";");
          quantityTitleLabel.setMinWidth(300);
          Label unitTitleLabel = new Label("Measurement Unit");
          unitTitleLabel.setStyle("-fx-font: 20px \"Roboto\";");
          unitTitleLabel.setMinWidth(150);
          Label nameTitleLabel = new Label("Ingredient Name");
          nameTitleLabel.setStyle("-fx-font: 20px \"Roboto\";");
          nameTitleLabel.setMinWidth(300);
          titleLine.getChildren().addAll(nameTitleLabel, quantityTitleLabel, unitTitleLabel);
          ingredientListContainer.getChildren().add(titleLine);



          for (Ingredient ingredient : shoppingList.getIngredients()) {
            HBox ingredientLine = new HBox(10);
            ingredientLine.setAlignment(Pos.CENTER_LEFT);
            ingredientLine.setFillHeight(false);
            Label quantityLabel = new Label(String.valueOf(ingredient.getQuantity()));
            quantityLabel.setStyle("-fx-font: 20px \"Roboto\";");
            HBox quantityBox = new HBox();
            quantityBox.setMinWidth(300);
            quantityBox.setAlignment(Pos.CENTER_LEFT);

            Label unitLabel = new Label(ingredient.getMeasurementUnit());
            unitLabel.setStyle("-fx-font: 20px \"Roboto\";");
            Label nameLabel = new Label(ingredient.getName());
            nameLabel.setStyle("-fx-font: 20px \"Roboto\";");
            nameLabel.setMinWidth(300);
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            quantityLabel.setMinWidth(100);
            quantityLabel.setAlignment(Pos.CENTER_LEFT);
            unitLabel.setMinWidth(120);
            unitLabel.setAlignment(Pos.CENTER_LEFT);

            Button editButton = new Button("Edit quantity");
            editButton.setStyle("-fx-background-color: white; -fx-text-fill: #3D405B; -fx-effect: null;-fx-cursor: hand");
            editButton.setFont(Font.font("Roboto", 12));
            editButton.setOnAction(e -> {
              TextInputDialog inputDialog = new TextInputDialog(String.valueOf(ingredient.getQuantity()));
              inputDialog.setTitle("Edit Ingredient Quantity");
              inputDialog.setHeaderText("Enter the new quantity for "+ ingredient.getName());
              inputDialog.setContentText("Quantity:");
  
              Optional<String> result = inputDialog.showAndWait();
              result.ifPresent(newQuantity -> {
                try {
                  float parsedQuantity = Float.parseFloat(newQuantity);
                  observer.editIngredientInShoppingList(ingredient.getName(), parsedQuantity, weekNumber);
                  quantityLabel.setText(String.valueOf(parsedQuantity));
                } catch (NumberFormatException ex) {
                  showInlineStyledAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for the ingredient quantity.");
                }
              });
            });

            quantityBox.getChildren().addAll(quantityLabel, editButton);
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-font: 12px \"Roboto\"; -fx-background-color: white; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
            deleteButton.setFont(Font.font("Roboto", 18));
            deleteButton.setOnAction(e -> {
              observer.deleteIngredientInShoppingList(ingredient.getName(), weekNumber);
              ingredientListContainer.getChildren().remove(ingredientLine);
            });


  
            // Add a pane to push the buttons to the right side
            Pane spacer = new Pane();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            ingredientLine.getChildren().addAll(nameLabel, quantityBox, unitLabel,  spacer,  deleteButton);
            ingredientListContainer.getChildren().add(ingredientLine);
          
          }
          foundWeek = true;
        } else  {
          Label noShoppingListLabel = new Label("No shopping list found for Week " + weekNumber);
          noShoppingListLabel.setStyle("-fx-font: 20px \"Roboto\";");
          ingredientListContainer.getChildren().add(noShoppingListLabel);
        }
      }
    }      
    if (!foundWeek || shoppingLists.isEmpty()) {
      Label noShoppingListLabel = new Label("No shopping list found for Week " + weekNumber);
      noShoppingListLabel.setStyle("-fx-font: 20px \"Roboto\";");
      ingredientListContainer.getChildren().add(noShoppingListLabel);
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

  private int getWeekNumber(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return date.get(weekFields.weekOfWeekBasedYear());
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
      showInlineStyledAlert(AlertType.ERROR,
          "Invalid Week Number", "Please enter a valid week number for the current year.");
    }
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

  /** Create a button with the given text and event handler.
   *
   * @param text the text to display on the button
   * @param eventHandler the event handler to handle the button click
   * @return the created button
   */
  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setMaxWidth(Double.MAX_VALUE);
    button.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;-fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setOnAction(eventHandler);
    return button;
  }


}
