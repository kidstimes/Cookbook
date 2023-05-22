package cookbook.view;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

import cookbook.model.HelpSection;
import cookbook.model.HelpSubsection;

public class HelpPageView {
    private VBox helpSectionsPane;
    private TextField searchField;
    private Button searchButton;
    private Label searchResultsLabel;
    private ScrollPane textScrollPane;
    private ArrayList<HelpSection> helpSections;
    private HelpPageViewObserver observer;
    private String displayName;
    private BorderPane view;
    private List<HelpSubsection> matchedSubsections;

    public HelpPageView(String displayName, ArrayList<HelpSection> helpSections) {
        this.helpSections = helpSections;
        this.displayName = displayName;
        this.helpSectionsPane = new VBox(10);
        this.searchField = new TextField();
        this.searchButton = new Button("Search");
        this.searchResultsLabel = new Label("");
        this.textScrollPane = new ScrollPane();
        this.matchedSubsections = new ArrayList<>();

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

        // Create the root BorderPane
        view = new BorderPane();
        view.setLeft(sidebar);

        // Create a VBox to contain search bar, search button, and search results label
        VBox searchBox = new VBox(10, searchField, searchButton, searchResultsLabel);
        searchField.setPromptText("Type keyword(s) here, separated by space");

        // Create a VBox to contain the title and search box
        VBox titleBox = new VBox(10);
        Label titleLabel = new Label("Help");
        titleLabel.setStyle("-fx-text-fill: #69a486;-fx-font-size: 32;-fx-font-weight: bold; -fx-padding: 20 10 20 50;");
        titleLabel.setFont(Font.font("Roboto"));
        titleBox.getChildren().addAll(titleLabel, searchBox);

        // Create a VBox to contain the help sections
        VBox contentBox = new VBox(10, titleBox, helpSectionsPane);

        // Set the contentBox as the center of the view
        view.setCenter(contentBox);

        // Add event listener for search button
        searchButton.setOnAction(e -> performSearch());

        // Populate the help sections
        populateHelpSections();
    }

    private void populateHelpSections() {
        helpSectionsPane.getChildren().clear();
        matchedSubsections.clear();
        searchField.clear();
        searchResultsLabel.setText("");
        
        for (HelpSection section : helpSections) {
            VBox sectionBox = new VBox(10);
            Hyperlink sectionLink = new Hyperlink(section.getTitle());
            sectionLink.setOnAction(e -> showHelpSection(section));
            sectionBox.getChildren().add(sectionLink);

            for (HelpSubsection subsection : section.getSubsections()) {
                Hyperlink subsectionLink = new Hyperlink("  " + subsection.getTitle());
                subsectionLink.setOnAction(e -> {
                    VBox subsectionContent = showHelpSubsection(subsection);
                    helpSectionsPane.getChildren().clear();
                    helpSectionsPane.getChildren().add(subsectionContent);
                });

                sectionBox.getChildren().add(subsectionLink);
            }

            helpSectionsPane.getChildren().add(sectionBox);
        }
    }

    private void showHelpSection(HelpSection section) {
        helpSectionsPane.getChildren().clear();

        VBox sectionBox = new VBox(10);
        Hyperlink backButton = new Hyperlink("Back");
        backButton.setOnAction(e -> populateHelpSections());
        sectionBox.getChildren().add(backButton);

        for (HelpSubsection subsection : section.getSubsections()) {
            Hyperlink subsectionLink = new Hyperlink("  " + subsection.getTitle());
            subsectionLink.setOnAction(e -> {
                VBox subsectionContent = showHelpSubsection(subsection);
                helpSectionsPane.getChildren().clear();
                helpSectionsPane.getChildren().add(subsectionContent);
            });
            sectionBox.getChildren().add(subsectionLink);
        }

        helpSectionsPane.getChildren().add(sectionBox);
    }

    private VBox showHelpSubsection(HelpSubsection subsection) {
        VBox subsectionBox = new VBox(10);
        Hyperlink backButton = new Hyperlink("Back");
        backButton.setOnAction(e -> showMatchedSubsections());
        Label title = new Label(subsection.getTitle());
        Label text = new Label(subsection.getText());
        text.setWrapText(true);
        subsectionBox.getChildren().addAll(backButton, title, text);
        return subsectionBox;
    }

    private void performSearch() {
        String keywords = searchField.getText();

        if (!keywords.isEmpty()) {
            matchedSubsections.clear();

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
        Hyperlink backButton = new Hyperlink("Back");
        backButton.setOnAction(e -> populateHelpSections());
        helpSectionsPane.getChildren().add(backButton);

        if (!matchedSubsections.isEmpty()) {
            for (HelpSubsection subsection : matchedSubsections) {
                Hyperlink subsectionLink = new Hyperlink(subsection.getTitle());
                subsectionLink.setOnAction(e -> {
                    VBox subsectionContent = showHelpSubsection(subsection);
                    helpSectionsPane.getChildren().clear();
                    helpSectionsPane.getChildren().add(subsectionContent);
                });

                helpSectionsPane.getChildren().add(subsectionLink);
            }

            searchResultsLabel.setText("Total results: " + matchedSubsections.size());
        } else {
            searchResultsLabel.setText("No results found");
        }
    }

    private void showMatchedSubsections() {
        searchField.clear();
        searchResultsLabel.setText("");
        updateSearchResults(matchedSubsections);
    }
}
