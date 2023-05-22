package cookbook.view;

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

    private void initLayout() {
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

        view.setLeft(sidebar);

        Hyperlink backButton = new Hyperlink("All help topics");
        backButton.setOnAction(e -> observer.goToHelp());
        backButton.setStyle(" -fx-text-fill: # 3D405B; -fx-font: 18px \"Roboto\"; -fx-padding: 10 20 10 50; -fx-cursor: hand;");
        searchField.setPromptText("Type keyword(s) here, separated by space");
        HBox searchFieldBox = new HBox();
        searchFieldBox.getChildren().addAll(searchField, searchButton);
        searchFieldBox.setSpacing(10);
        searchFieldBox.setPadding(new Insets(10, 10, 10, 10));
        searchFieldBox.setPrefWidth(700);
        searchField.setPrefWidth(600);
        searchFieldBox.setAlignment(Pos.CENTER_LEFT);

        // Create a VBox to contain the title and search box
        VBox titleBox = new VBox(10);
        Label titleLabel = new Label("Help");
        titleLabel.setStyle("-fx-font: 32px \"Roboto\"; -fx-text-fill: #69a486; -fx-padding: 50 50 20 50;");
        titleBox.getChildren().addAll(titleLabel, searchFieldBox, backButton, searchResultsLabel);

        // Create a VBox to contain the help sections
        VBox contentBox = new VBox(10, titleBox, helpSectionsPane);

        // Create a ScrollPane for the help sections pane
        ScrollPane helpSectionsScrollPane = new ScrollPane(contentBox);
        helpSectionsScrollPane.setFitToWidth(true);
        helpSectionsScrollPane.setFitToHeight(true);

        // Set the contentBox as the center of the view
        view.setCenter(helpSectionsScrollPane);

        // Add event listener for search button
        searchButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
        + "-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
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
               sectionLabel.setStyle("-fx-padding: 0 0 0 30;");

                sectionLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 16));

                sectionBox.getChildren().add(sectionLabel);

                for (HelpSubsection subsection : section.getSubsections()) {
                    Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
                    subsectionLink.setStyle("-fx-padding: 0 0 0 50;");
                    subsectionLink.setOnAction(e -> showHelpSubsection(subsection));

                    subsectionLink.setFont(Font.font("Roboto", 16));
                    sectionBox.getChildren().add(subsectionLink);
                }

                helpSectionsPane.getChildren().add(sectionBox);
            }
        } else {
            for (HelpSubsection subsection : storedSearchResults) {
                Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
                subsectionLink.setStyle("-fx-padding: 0 0 0 50;");
                subsectionLink.setOnAction(e -> showHelpSubsection(subsection));
                subsectionLink.setFont(Font.font("Roboto", 16));

                helpSectionsPane.getChildren().add(subsectionLink);
            }

            searchResultsLabel.setText("Total results: " + storedSearchResults.size());
        }
    }

    private void showHelpSection(HelpSection section) {
      VBox sectionBox = new VBox(10);
      Label title = new Label(section.getTitle());
      title.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
      title.setStyle("-fx-padding: 0 0 0 30;");
  
      sectionBox.getChildren().addAll(title);
  
      for (HelpSubsection subsection : section.getSubsections()) {
          Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
          subsectionLink.setOnAction(e -> showHelpSubsection(subsection));
          subsectionLink.setStyle("-fx-padding: 0 0 0 50;");
  
          subsectionLink.setFont(Font.font("Roboto", 16));
          sectionBox.getChildren().add(subsectionLink);
      }
  
      showContent(sectionBox);
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
