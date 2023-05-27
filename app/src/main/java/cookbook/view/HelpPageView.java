package cookbook.view;

import cookbook.model.HelpSection;
import cookbook.model.HelpSubsection;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * View for the help page.
 */
public class HelpPageView {
  private BorderPane view;
  private VBox helpSectionsPane;
  private TextField searchField;
  private Button searchButton;
  private Label searchResultsLabel;
  private ArrayList<HelpSection> helpSections;
  private HelpPageViewObserver observer;
  private String displayName;
  private List<HelpSubsection> matchedSubsections;
  private List<HelpSubsection> storedSearchResults;

  /**
   * Help Page View Constructor.
   *
   * @param displayName the display name of the user
   * @param helpSections the sections of the help page
   */
  public HelpPageView(String displayName, ArrayList<HelpSection> helpSections) {
    this.view = new BorderPane();
    this.helpSections = helpSections;
    this.displayName = displayName;
    this.helpSectionsPane = new VBox(10);
    this.searchField = new TextField();
    this.searchButton = new Button("Search");
    this.searchResultsLabel = new Label("");
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

  private void initLayout() {
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
    sidebar.setActiveButton("Help");
    sidebar.finalizeLayout();

    view.setLeft(sidebar);

    Hyperlink backButton = new Hyperlink("All help topics");
    backButton.setOnAction(e -> observer.goToHelp());
    backButton.setStyle(" -fx-font: 24px \"Roboto\"; -fx-padding: 10 30 10 50; "
        + "-fx-cursor: hand;-fx-font-weight: bold;");
    searchField.setPromptText("Type keyword(s) here, separated by space");
    searchField.setStyle("-fx-font: 18px \"Roboto\"; -fx-padding: 10 10 10 10;");
    HBox searchFieldBox = new HBox();
    searchFieldBox.getChildren().addAll(searchField, searchButton);
    searchFieldBox.setSpacing(10);
    searchFieldBox.setPadding(new Insets(10, 30, 10, 30));
    searchFieldBox.setPrefWidth(700);
    searchField.setPrefWidth(600);
    searchFieldBox.setAlignment(Pos.CENTER_LEFT);

    // Create a VBox to contain the title and search box
    VBox titleBox = new VBox(10);
    Label titleLabel = new Label("Help");
    titleLabel.setStyle("-fx-font: 32px \"Roboto\"; "
        + "-fx-text-fill: #3F6250; -fx-padding: 50 50 20 50;-fx-font-weight: bold;");
    titleBox.getChildren().addAll(titleLabel, searchFieldBox, backButton, searchResultsLabel);

    // Create a VBox to contain the help sections
    VBox contentBox = new VBox(10, titleBox, helpSectionsPane);
    contentBox.setPadding(new Insets(0, 50, 50, 50));
    contentBox.setStyle("-fx-background-color: #F9F8F3;");

    // Create a ScrollPane for the help sections pane
    ScrollPane helpSectionsScrollPane = new ScrollPane(contentBox);
    helpSectionsScrollPane.setFitToWidth(true);
    helpSectionsScrollPane.setFitToHeight(true);

    // Set the contentBox as the center of the view
    view.setCenter(helpSectionsScrollPane);

    // Add event listener for search button
    searchButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
        + "-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;-fx-font-size: 18px;");
    searchButton.setOnAction(e -> performSearch());

    // Populate the help sections
    populateHelpSections();
  }

  private void populateHelpSections() {
    helpSectionsPane.getChildren().clear();
    searchField.clear();
    searchResultsLabel.setText("");

    if (storedSearchResults.isEmpty()) {
      for (HelpSection section : helpSections) {
        VBox sectionBox = new VBox(10);
        Label sectionLabel = new Label(section.getTitle());
        sectionLabel.setStyle("-fx-padding: 0 20 0 30;");

        sectionLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));

        sectionBox.getChildren().add(sectionLabel);

        for (HelpSubsection subsection : section.getSubsections()) {
          Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
          subsectionLink.setStyle("-fx-padding: 0 20 0 50;");
          subsectionLink.setOnAction(e -> showHelpSubsection(subsection));

          subsectionLink.setFont(Font.font("Roboto", 20));
          sectionBox.getChildren().add(subsectionLink);
        }

        helpSectionsPane.getChildren().add(sectionBox);
      }
    } else {
      for (HelpSubsection subsection : storedSearchResults) {
        Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
        subsectionLink.setStyle("-fx-padding: 0 20 0 50;");
        subsectionLink.setOnAction(e -> showHelpSubsection(subsection));
        subsectionLink.setFont(Font.font("Roboto", 20));

        helpSectionsPane.getChildren().add(subsectionLink);
      }

      searchResultsLabel.setText("Total results: " + storedSearchResults.size());
      searchResultsLabel.setStyle(" -fx-padding: 20 0 20 50; -fx-text-fill: #3D405B;"
              + "-fx-font-weight: bold;-fx-font-size: 16px;-fx-font-family: Roboto;");
    }
  }

  private void showHelpSubsection(HelpSubsection subsection) {
    Hyperlink backButton = new Hyperlink("Back to Previous");
    backButton.setOnAction(e -> populateHelpSections());
    backButton.setStyle("-fx-padding: 0 20 0 50;");

    Label title = new Label(subsection.getTitle());
    title.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
    title.setStyle("-fx-padding: 0 20 0 50;");
    title.setUnderline(true);

    Label text = new Label(subsection.getText());
    text.setStyle("-fx-padding: 0 20 0 50;");
    text.setFont(Font.font("Roboto", 20));
    text.setWrapText(true);
    text.setPrefWidth(900);

    VBox subsectionBox = new VBox(10);
    subsectionBox.getChildren().addAll(backButton, title, text);
    showContent(subsectionBox);
  }

  private void performSearch() {
    String keywords = searchField.getText();

    if (!keywords.isEmpty()) {
      matchedSubsections.clear();
      storedSearchResults.clear();
      ArrayList<HelpSubsection> resultSubsections = observer.search(keywords);
      for (HelpSubsection subsection : resultSubsections) {
        matchedSubsections.add(subsection);
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
        subsectionLink.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        subsectionLink.setStyle("-fx-padding: 10 20 0 50;");

        helpSectionsPane.getChildren().add(subsectionLink);
      }
      searchResultsLabel.setStyle(" -fx-padding: 20 20 20 50; -fx-text-fill: #3D405B;"
          + "-fx-font-weight: bold;-fx-font-size: 20px;-fx-font-family: Roboto;");
      searchResultsLabel.setText("Total results: " + matchedSubsections.size());
    } else {
      searchResultsLabel.setStyle(" -fx-padding: 20 20 20 50; -fx-text-fill: #3D405B;"
              + "-fx-font-weight: bold;-fx-font-size: 20px;-fx-font-family: Roboto;");
      searchResultsLabel.setText("No results found");
    }
  }

  private void showContent(Node content) {
    helpSectionsPane.getChildren().clear();
    helpSectionsPane.getChildren().add(content);
  }
}
