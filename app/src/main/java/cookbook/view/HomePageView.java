package cookbook.view;

<<<<<<< HEAD
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
=======
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

/**
 * The view for the home page.
 */
public class HomePageView {
<<<<<<< HEAD

  private HomePageViewObserver observer;
  private BorderPane view;
  private String displayName;
  boolean hasWeeklyDinner;
  boolean hasNextWeekShoppingList;
  private int numberUnreadMessages;
  private int numberOfFavoriteRecipes;
  
  /**
   * Home Page  View Constructor.
   */
  public HomePageView(String displayName,
         boolean hasWeeklyDinner, boolean hasNextWeekShoppingList,
          int numberUnreadMessages, int numberOfFavoriteRecipes) {
    this.view = new BorderPane();
    this.displayName = displayName;
    this.hasWeeklyDinner = hasWeeklyDinner;
    this.hasNextWeekShoppingList = hasNextWeekShoppingList;
    this.numberUnreadMessages = numberUnreadMessages;
    this.numberOfFavoriteRecipes = numberOfFavoriteRecipes;
    initLayout();
    
  }

  /**
   * Set an observer of the home page view.
   */
=======
  private HomePageViewObserver observer;
  private BorderPane view;
    
  public HomePageView() {
    this.view = new BorderPane();
    initLayout();
  }

>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
  public void setObserver(HomePageViewObserver observer) {
    this.observer = observer;
  }

<<<<<<< HEAD
  /** Get the view of the home page.
   *
   * @return the view of the home page
   */
  public Node getView() {
    return view;
=======
  public Node getView() {
    return this.view;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
  }

  /**
   * Initializes the layout of the home page.
   */
  public void initLayout() {
<<<<<<< HEAD
    createSidebar();
    createCenterView();
  }

  /**
   * Create a sidemenu for the home page.
   */
  public void createSidebar() {
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
    
    sidebar.setActiveButton("Home");
    sidebar.finalizeLayout();
    view.setLeft(sidebar);
  }

  private void createCenterView() {
    VBox centerView = new VBox(30);
    centerView.setAlignment(Pos.TOP_LEFT); 
    centerView.setStyle("-fx-padding: 50px; -fx-background-color: #F9F8F3;"
        + "-fx-border-color: lightgrey;-fx-border-width: 1px;");
    Label title = new Label("Welcome to Cookbook!");
    title.setFont(Font.font("Roboto", 32));
    title.setStyle("-fx-font-weight: bold;");
    Label summary = new Label("Here is a summary of what is happening");
    summary.setFont(Font.font("Roboto", 20));
    summary.setStyle("-fx-font-weight: bold;");
    centerView.getChildren().add(title);
    centerView.getChildren().add(summary);
    LocalDate currentDate = LocalDate.now();
    int weekNumber = getWeekNumber(currentDate);
  
    centerView.getChildren().addAll(
        createInfoHbox("Today is " + currentDate + " and current week number is "
            + weekNumber + ".", "/images/date.png"),
        createInfoHbox(hasWeeklyDinner ? "You have dinners planned for this week."
            : "You do not have dinners planned for this week.", "/images/dinner.png"),
        createInfoHbox(hasNextWeekShoppingList ? "You have a shopping list for next week."
            : "You do not have a shopping list for next week.", "/images/shopping.png"),
        createInfoHbox("You have " + numberOfFavoriteRecipes
            + " favorite recipes in your cookbook.", "/images/fav.png"),
        createInfoHbox("You have " + numberUnreadMessages + " unread messages.", "/images/mess.png")
    );

    // Set the center view of the BorderPane
    view.setCenter(centerView);
  }


  private HBox createInfoHbox(String text, String imagePath) {
    HBox hbox = new HBox();
    hbox.setPrefSize(800, 90);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setStyle("-fx-background-color:#F1EEE2; -fx-padding: 15px;");

    Label label = new Label(text);
    label.setPadding(new Insets(0, 0, 0, 20));
    Image image = new Image(getClass().getResourceAsStream(imagePath));
    ImageView icon = new ImageView(image);
    icon.setFitHeight(50);
    icon.setFitWidth(50);
    label.setGraphic(icon);
    label.setFont(Font.font("Roboto", 22));
    label.setGraphicTextGap(20.0); 
    hbox.getChildren().add(label);
    return hbox;

  }


  // Get the week number of the year.
  private int getWeekNumber(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return date.get(weekFields.weekOfWeekBasedYear());
=======

    // Create a grid pane for the options
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setVgap(20);
    grid.setHgap(10);

    // Add a title to the homepage
    Text title = new Text("Welcome to Cookbook!");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
    title.setFill(Color.GREEN);
    grid.add(title, 0, 0, 2, 1);

    // Add five options to the homepage, one per row
    Button browseRecipesButton = new Button("Browse Recipes");
    browseRecipesButton.setFont(Font.font("Arial", 18));
    browseRecipesButton.setOnAction(e -> {       
      // Handle browse recipes action
      observer.handleBrowseRecipesClicked();
    });
    grid.add(browseRecipesButton, 0, 1, 1, 1);
    GridPane.setHalignment(browseRecipesButton, javafx.geometry.HPos.LEFT);

    Button addRecipeButton = new Button("Add a Recipe");
    addRecipeButton.setFont(Font.font("Arial", 18));
    addRecipeButton.setOnAction(e -> {
      // Handle add recipe action
      observer.handleAddRecipeClicked();
    });
    grid.add(addRecipeButton, 0, 2, 1, 1);
    GridPane.setHalignment(addRecipeButton, javafx.geometry.HPos.LEFT);

    Button searchRecipeButton = new Button("Weekly Dinner List");
    searchRecipeButton.setFont(Font.font("Arial", 18));
    searchRecipeButton.setOnAction(e -> {
            // Handle search recipe action
    });
    grid.add(searchRecipeButton, 0, 3, 1, 1);
    GridPane.setHalignment(searchRecipeButton, javafx.geometry.HPos.LEFT);

    Button viewFavoritesButton = new Button("My Favorites");
    viewFavoritesButton.setFont(Font.font("Arial", 18));
    viewFavoritesButton.setOnAction(e -> {
            // Handle view favorites action
    });
    grid.add(viewFavoritesButton, 0, 4, 1, 1);
    GridPane.setHalignment(viewFavoritesButton, javafx.geometry.HPos.LEFT);

    Button viewShoppingListButton = new Button("My Shopping List");
    viewShoppingListButton.setFont(Font.font("Arial", 18));
    viewShoppingListButton.setOnAction(e -> {
            // Handle view shopping list action
    });
    grid.add(viewShoppingListButton, 0, 5, 1, 1);
    GridPane.setHalignment(viewShoppingListButton, javafx.geometry.HPos.LEFT);

    view.setCenter(grid);
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
  }
}
