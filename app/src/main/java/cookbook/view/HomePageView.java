package cookbook.view;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;




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
  public HomePageView() {
    this.view = new BorderPane();
    view.setStyle("-fx-background-color: #F9F8F3;");
    
  }

  /**
   * Set an observer of the home page view..
   */
  public void setObserver(HomePageViewObserver observer) {
    this.observer = observer;
  }

  /**
   * Set the user displayName and initialize the layout of the home page view.
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
    initLayout(displayName);
  }


  public Node getView() {
    return view;
  }

  /**
   * Initializes the layout of the home page.
   */
  public void initLayout(String displayName) {

    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(20);
    sidebar.setMaxWidth(150);
    sidebar.setStyle("-fx-padding: 20px;");

    // Add a title to the homepage
    Text title = new Text(displayName + ", welcome!");
    title.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(title);

    // Add five options to the homepage, one per row
    Button browseRecipesButton = new Button("Browse Recipes");
    browseRecipesButton.setStyle(
          "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    browseRecipesButton.setFont(Font.font("Roboto", 18));
    browseRecipesButton.setOnAction(e -> {
      // Handle browse recipes action
      observer.goToBrowser();
    });
    sidebar.getChildren().add(browseRecipesButton);

    Button addRecipeButton = new Button("Add a Recipe");
    addRecipeButton.setFont(Font.font("Roboto", 18));
    addRecipeButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    addRecipeButton.setOnAction(e -> {
      // Handle add recipe action
      observer.goToAddRecipe();
    });
    sidebar.getChildren().add(addRecipeButton);

    Button weeklyDinnerButton = new Button("Weekly Dinner List");
    weeklyDinnerButton.setFont(Font.font("Roboto", 18));
    weeklyDinnerButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    weeklyDinnerButton.setOnAction(e -> {
      // Handle search recipe action
    });
    sidebar.getChildren().add(weeklyDinnerButton);

    Button viewFavoritesButton = new Button("My Favorites");
    viewFavoritesButton.setFont(Font.font("Roboto", 18));
    viewFavoritesButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    viewFavoritesButton.setOnAction(e -> {
      // Handle view favorites action
    });
    sidebar.getChildren().add(viewFavoritesButton);

    Button viewShoppingListButton = new Button("My Shopping List");
    viewShoppingListButton.setFont(Font.font("Roboto", 18));
    viewShoppingListButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    viewShoppingListButton.setOnAction(e -> {
      // Handle view shopping list action
    });
    sidebar.getChildren().add(viewShoppingListButton);

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


}
