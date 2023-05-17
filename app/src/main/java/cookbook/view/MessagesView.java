package cookbook.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class MessagesView {
  private BorderPane view;
  private MessagesViewObserver observer;
  private String displayName;


  /**
   * Constructor for the MessagesView.
   */
  public MessagesView(String displayName) {
    this.view = new BorderPane();
    this.displayName = displayName;
    initLayout();

  }
  
  /** Set the observer for this view.
  *
  * @param observer the observer
  */
  public void setObserver(MessagesViewObserver observer) {
    this.observer = observer;
  }

  //get the view
  public Node getView() {
    return view;
  }

  /**
   * Initialize the layout.
   */
  public void initLayout() {
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(20);
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
    HBox logoutHelpBox = new HBox(10);
    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 14));
    logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> {
      observer.userLogout();
    });

    Region hspacer = new Region();  // This will take up as much space as possible
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
  

  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setMaxWidth(Double.MAX_VALUE);
    button.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;-fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setOnAction(eventHandler);
    return button;
  }
}

  

