package cookbook.view;

import cookbook.view.HelpPageViewObserver;
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

/**
 * View for the help page.
 */
public class HelpPageView {
  private BorderPane view;
  private HelpPageViewObserver observer;
  private String displayName;


  /**
  * Constructor for the MessagesView.
  */
  public HelpPageView(String displayName) {
    this.view = new BorderPane();
    this.displayName = displayName;
    initLayout();

  }

  /** Set the observer for this view.
  *
  * @param observer the observer
  */
  public void setObserver(HelpPageViewObserver observer) {
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
    
    sidebar.setActiveButton("Help");
    sidebar.finalizeLayout();
    // Add the sidebar to the view
    view.setLeft(sidebar);
  }
  
}



