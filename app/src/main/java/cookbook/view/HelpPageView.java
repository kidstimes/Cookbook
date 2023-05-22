package cookbook.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.s;

import cookbook.model.HelpSection;
import cookbook.model.HelpSubsection;

/**
 * View for the help page.
 */
public class HelpPageView {
    private BorderPane view;  
    private VBox helpSectionsPane;
    private TextField searchField;
    private Button searchButton;
    private Label searchResultsLabel;
    private ScrollPane textScrollPane;
    private ArrayList<HelpSection> helpSections;
    private HelpPageViewObserver observer;
    private String displayName;
    private List<HelpSubsection> matchedSubsections;
    private List<HelpSubsection> storedSearchResults;

    public HelpPageView(String displayName, ArrayList<HelpSection> helpSections) {
       this.view = new BorderPane();
        this.helpSections = helpSections;
        this.displayName = displayName;
        this.helpSectionsPane = new VBox(10);
        this.searchField = new TextField();
        this.searchButton = new Button("Search");
        this.searchResultsLabel = new Label("");
        this.textScrollPane = new ScrollPane();
        this.matchedSubsections = new ArrayList<>();
        this.storedSearchResults = new ArrayList<>();

        initLayout();
    }

    public Node getView() {
        return view;
    }

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
    
    HBox logoutHelpBox = new HBox(10);
    logoutHelpBox.getChildren().addAll(logoutButton, hspacer, helpButton);
    logoutHelpBox.setAlignment(Pos.CENTER_LEFT);  
    
    sidebar.getChildren().add(logoutHelpBox); 
    view.setLeft(sidebar);
  }
  
  private void showHelpSubsection(HelpSubsection subsection) {
      VBox subsectionBox = new VBox(10);
      Hyperlink backButton = new Hyperlink("Back to Previous");
      backButton.setOnAction(e -> populateHelpSections());
      backButton.setStyle("-fx-padding: 0 0 0 50;");
  
      Label title = new Label(subsection.getTitle());
      title.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
      title.setStyle("-fx-padding: 0 0 0 50;");
      title.setUnderline(true);
  
      Label text = new Label(subsection.getText());
      text.setStyle("-fx-padding: 0 0 0 50;");
      text.setFont(Font.font("Roboto", 16));
      text.setWrapText(true);
  
      subsectionBox.getChildren().addAll(backButton, title, text);
      showContent(subsectionBox);
  }
  

    private void performSearch() {
        String keywords = searchField.getText();

        if (!keywords.isEmpty()) {
            matchedSubsections.clear();
            storedSearchResults.clear();

            for (HelpSection section : helpSections) {
                for (HelpSubsection subsection : section.getSubsections()) {
                    if (subsection.getTitle().toLowerCase().contains(keywords.toLowerCase())
                            || subsection.getText().toLowerCase().contains(keywords.toLowerCase())) {
                        matchedSubsections.add(subsection);
                    }
                }
            }
            updateSearchResults(matchedSubsections);
        } else {
            updateSearchResults(new ArrayList<>());
        }
    }

    private void updateSearchResults(List<HelpSubsection> matchedSubsections) {
        helpSectionsPane.getChildren().clear();
        storedSearchResults.clear();

        if (!matchedSubsections.isEmpty()) {
            storedSearchResults.addAll(matchedSubsections);

            for (HelpSubsection subsection : matchedSubsections) {
                Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
                subsectionLink.setOnAction(e -> showHelpSubsection(subsection));
                subsectionLink.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
                subsectionLink.setStyle("-fx-padding: 10 0 0 50;");

                helpSectionsPane.getChildren().add(subsectionLink);
            }
            searchResultsLabel.setStyle(" -fx-padding: 20 0 20 50; -fx-text-fill: #3D405B;-fx-font-weight: bold;-fx-font-size: 16px;-fx-font-family: Roboto;");
            searchResultsLabel.setText("Total results: " + matchedSubsections.size());
        } else {
            searchResultsLabel.setText("No results found");
        }
    }

    private void showContent(Node content) {
        helpSectionsPane.getChildren().clear();
        helpSectionsPane.getChildren().add(content);
    }
}
