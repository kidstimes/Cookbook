package cookbook.view;


import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * View class for the shopping list page.
 */
public class ShoppingListView {
  private BorderPane view;
  private ShoppingListViewObserver observer;
  private String displayName;
  boolean hasShoppingList = true;


  /**
   * Constructor for the shopping list view.
   *
   * @param displayName is the displayname of the user
   * @param hasShoppingList is whether the user has a shopping list
   */
  
  public ShoppingListView(String displayName) {
    this.view = new BorderPane();
    this.displayName = displayName;
    initLayout();

  }
  

  public void setObserver(ShoppingListViewObserver observer) {
    this.observer = observer;

  }

  
  public Node getView() {
    return view;
  }

  /**
   * Initialize the layout.
   */
  public void initLayout() {

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
    logoutButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> {
      observer.userLogout();
    });
    sidebar.getChildren().add(logoutButton);
    view.setLeft(sidebar);

    
  }
  

  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setMaxWidth(Double.MAX_VALUE);
    button.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;-fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setOnAction(eventHandler);
    return button;
  }
}
