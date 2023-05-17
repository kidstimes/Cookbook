package cookbook.view;

import cookbook.model.Recipe;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;





/**
 * The view for the recipe browser.
 */
public class BrowserView {
  private BrowserViewObserver observer;
  private BorderPane view;
  private TextField searchByNameField;
  private TextField searchByIngredientField;
  private FlowPane tagsFlowPane;
  private VBox searchResultsVbox;
  private VBox rootVbox;
  private ArrayList<String> privateTags;
  private String displayName;


  /**
   * Browser View Constructor.
   */
  public BrowserView(ArrayList<Recipe> recipeList, 
      ArrayList<String> privateTags, String displayName) {
    view = new BorderPane();
    rootVbox = new VBox();
    this.searchResultsVbox = new VBox();
    this.tagsFlowPane = new FlowPane();
    this.privateTags = privateTags;
    this.displayName = displayName;
    initLayout(recipeList);
  }

  /**
   * Set an observer of the browser view.
   */
  public void setObserver(BrowserViewObserver observer) {
    this.observer = observer;
  }

  /**
   * Get the view.
   */
  public Node getView() {
    return view;
  }

  /**
   * Display the recipes in the browser view.
   */
  public void updateRecipes(ArrayList<Recipe> recipeList) {
    initLayout(recipeList);
  }

  /**
   * Initialize the view for the recipe browser.
   *
   * @param recipeList the recipes of the cookbook
   */
  private void initLayout(ArrayList<Recipe> recipeList) {
    
    rootVbox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");
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
      createButton("My Account", e -> observer.goToAccount())
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

    // clear any existing children from the vbox
    rootVbox.getChildren().clear();
    // clear tagsFlowPane and searchResultsVBox
    tagsFlowPane.getChildren().clear();
    searchResultsVbox.getChildren().clear();

    // Add a title to the homepage
    Text title = new Text("Recipe Browser");
    title.setFont(Font.font("ROBOTO", FontWeight.BOLD, 32));
    VBox.setMargin(title, new Insets(0, 0, 20, 0));
    rootVbox.getChildren().add(title);

    // Add search input fields for name and ingredients
    Label searchByNameLabel = new Label("Name:");
    searchByNameLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
    searchByNameField = new TextField();
    searchByNameField.setId("searchByNameField");
    searchByNameField.setStyle("-fx-font-size: 18;");
    searchByNameField.setPrefWidth(200);
    searchByNameField.setPrefHeight(30);

    Label searchByIngredientLabel = new Label("Ingredients:");
    searchByIngredientLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
    searchByIngredientField = new TextField();
    searchByIngredientField.setStyle("-fx-font-size: 18;");
    searchByIngredientField.setPrefWidth(200);
    searchByIngredientField.setPrefHeight(30);

    // Add search button
    Button searchButton = new Button("Search");
    searchButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
        + "-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    searchButton.setFont(Font.font("ROBOTO", 16));
    rootVbox.setMargin(searchButton, new Insets(0, 0, 20, 0));

    searchButton.setOnAction(e -> {
      if (observer != null) {
        searchResultsVbox.getChildren().clear();
        String searchTextByName = searchByNameField.getText();
        String searchTextByIngredient = searchByIngredientField.getText();
        ObservableList<String> selectedTags = getSelectedTags();
        observer.handleSearch(searchTextByName, searchTextByIngredient, selectedTags);
      }
    });

    // make a bar for recipe name and ingredients search fields
    HBox searchInputHbox = new HBox(20);
    searchInputHbox.setAlignment(Pos.BOTTOM_LEFT);
    searchInputHbox.getChildren().addAll(searchByNameLabel, 
          searchByNameField, searchByIngredientLabel, searchByIngredientField, searchButton);
    rootVbox.getChildren().add(searchInputHbox);
    rootVbox.setMargin(searchInputHbox, new Insets(0, 0, 10, 0));

    // Add tags View
    tagsFlowPane.setHgap(5);
    tagsFlowPane.setVgap(5);
    // Set the preferred width before wrapping to the next row
    tagsFlowPane.setPrefWrapLength(800);

    ObservableList<String> tags = 
          FXCollections.observableArrayList("vegan", "vegetarian", "lactose free",
        "gluten free", "starter", "main course", "dessert and sweets");
      
    tags.addAll(privateTags);

    for (String tag : tags) {
      CheckBox checkBox = new CheckBox(tag);
      checkBox.setFont(Font.font("ROBOTO", FontWeight.NORMAL, 16));
      checkBox.setTextFill(Color.web("#3D405B"));
      tagsFlowPane.getChildren().add(checkBox);
    }

    rootVbox.getChildren().add(tagsFlowPane);
    rootVbox.setMargin(tagsFlowPane, new Insets(0, 0, 10, 0));

    // Wrap the rootVBox in a ScrollPane so that the content can be scrolled
    ScrollPane scrollPane = new ScrollPane(rootVbox);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    // add searchResultsVBox to vbox and vbox is the content of ScrollPane
    rootVbox.getChildren().add(searchResultsVbox);

    // add scrollpane to the center of the view(borderpane)
    view.setCenter(scrollPane);

    displayRecipes(recipeList);
  }

  /**
   * Get the selected tags from the tagsFlowPane.
   *
   * @return the selected tags
   */
  public ObservableList<String> getSelectedTags() {
    ObservableList<String> selectedTags = FXCollections.observableArrayList();
    for (Node node : tagsFlowPane.getChildren()) {
      if (node instanceof CheckBox) {
        CheckBox checkBox = (CheckBox) node;
        if (checkBox.isSelected()) {
          selectedTags.add(checkBox.getText());
        }
      }
    }
    return selectedTags;
  }


  /**
   * Display the recipes in the searchResultsVBox.
   *
   * @param recipeList the list of recipes to display
   */
  public void displayRecipes(ArrayList<Recipe> recipeList) {
    // Clear the previous search results (recipe count, separator, and recipe items)
    searchResultsVbox.getChildren().clear();
    // Add a Text node to display the number of recipes found
    String recipeCountText;
    if (recipeList.size() == 1) {
      recipeCountText = "1 recipe found";
    } else {
      recipeCountText = recipeList.size() + " recipes found";
    }
    Text recipeCount = new Text(recipeCountText);
    recipeCount.setFont(Font.font("ROBOTO", 16));
    recipeCount.setId("recipeCount"); 
    searchResultsVbox.getChildren().add(recipeCount);
    // add margin to the recipeCount
    searchResultsVbox.setMargin(recipeCount, new Insets(15, 0, 10, 0));

    // Add a separator line before the recipe buttons
    Separator separator = new Separator(Orientation.HORIZONTAL);
    searchResultsVbox.getChildren().add(separator);

    for (int i = 0; i < recipeList.size(); i++) {
      Recipe recipe = recipeList.get(i);
      String recipeTagsString = "";
      for (int j = 0; j < recipe.getTags().size(); j++) {
        if (j == 0) {
          recipeTagsString = recipeTagsString + " # " + recipe.getTags().get(j);
        } else {
          recipeTagsString = recipeTagsString + ", # " + recipe.getTags().get(j);
        }
      }

      // Create star and unstar icons using ImageViews
      Image star = new Image(getClass().getResourceAsStream("/images/star.png"));
      ImageView starIcon = new ImageView(star);
      starIcon.setStyle("-fx-background-color: #F9F8F3;");
      Image unstar = new Image(getClass().getResourceAsStream("/images/unstar.png"));
      ImageView unstarIcon = new ImageView(unstar);
      unstarIcon.setStyle("-fx-background-color: #F9F8F3;");
      starIcon.setFitWidth(20);
      starIcon.setFitHeight(20);
      unstarIcon.setFitWidth(20);
      unstarIcon.setFitHeight(20);

      // Create a ToggleButton with the unstar icon as default
      ToggleButton starButton = new ToggleButton("", unstarIcon);
      starButton.setSelected(recipe.isStarred());
      if (recipe.isStarred()) {
        starButton.setGraphic(starIcon);
      }

      // Add an event handler to the ToggleButton to change the icon when clicked
      starButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal) {
          starButton.setGraphic(starIcon);
          observer.addRecipeToFavorite(recipe);
        } else {
          starButton.setGraphic(unstarIcon);
          observer.removeRecipeFromFavorite(recipe);
        }
      });

      Hyperlink recipeButton = new Hyperlink(recipe.getName());
      recipeButton.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
      recipeButton.setOnAction(e -> {
        if (observer != null) {
          observer.goToRecipe(recipe);
        }
      });

      Text recipeTags = new Text(recipeTagsString);
      recipeTags.setFont(Font.font("ROBOTO", 18));

      HBox recipeBox = new HBox(10); 
      recipeBox.setAlignment(Pos.CENTER_LEFT);
      recipeBox.getChildren().addAll(starButton, recipeButton, recipeTags); 
      FlowPane flowPane = new FlowPane();
      flowPane.setStyle("-fx-padding: 5 10 5 10;-fx-background-color: white;");
      flowPane.getChildren().add(recipeBox);

      // Add a tooltip with the short description for hovering effect
      Tooltip tooltip = new Tooltip(recipe.getShortDesc());
      tooltip.setFont(Font.font("ROBOTO", 18));
      tooltip.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;");
      Tooltip.install(flowPane, tooltip);

      searchResultsVbox.getChildren().add(flowPane);
      searchResultsVbox.setMargin(flowPane, new Insets(0, 0, 10, 0));

    }
  }

  /**
   * Reset the search inputs.
   */
  public void resetSearchInputs() {
    searchResultsVbox.getChildren().clear();
    searchByIngredientField.clear();
    searchByNameField.clear();
    for (Node node : tagsFlowPane.getChildren()) {
      if (node instanceof CheckBox) {
        CheckBox checkBox = (CheckBox) node;
        checkBox.setSelected(false);
      }
    }
  }

  /** Create styled button with the given text and event handler.
   *
   * @param text is the text to display on the button
   * @param eventHandler is the event handler to execute when the button is clicked.
   * @return the created button
   */
  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color:#F2CC8F ; -fx-text-fill:#3D405B; -fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setMinWidth(180);
    button.setMaxWidth(200); 
    button.setOnAction(eventHandler);
    return button;
  }
}