package cookbook.view;

<<<<<<< HEAD
import cookbook.model.Recipe;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
=======

import java.util.ArrayList;
import cookbook.model.Recipe;
import javafx.geometry.HPos;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
<<<<<<< HEAD
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
import javafx.scene.layout.VBox;
=======
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
<<<<<<< HEAD

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
   * Initialize the view for the recipe browser.
   *
   * @param recipeList the recipes of the cookbook
   */
  private void initLayout(ArrayList<Recipe> recipeList) {

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

    sidebar.setActiveButton("All Recipes");
    sidebar.finalizeLayout();

    // Add the sidebar to the view
    view.setLeft(sidebar);

    rootVbox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");
    // clear any existing children from the vbox
    rootVbox.getChildren().clear();
    // clear tagsFlowPane and searchResultsVBox
    tagsFlowPane.getChildren().clear();
    searchResultsVbox.getChildren().clear();

    // Add a title to the homepage
    Label title = new Label("All Recipes");
    title.setStyle("-fx-text-fill: #3F6250;-fx-font-size: 32px;-fx-font-weight: bold;"
        + "-fx-font-family: 'Roboto';");
    VBox.setMargin(title, new Insets(0, 0, 30, 0));
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
    VBox.setMargin(searchButton, new Insets(0, 0, 30, 10));

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
    VBox.setMargin(searchInputHbox, new Insets(10, 0, 20, 0));

    // Add tags View
    tagsFlowPane.setHgap(5);
    tagsFlowPane.setVgap(5);
    // Set the preferred width before wrapping to the next row
    tagsFlowPane.setPrefWrapLength(800);

    ObservableList<String> tags = FXCollections.observableArrayList("vegan", "vegetarian",
        "lactose free", "gluten free", "starter", "main course", "dessert and sweets");

    tags.addAll(privateTags);

    for (String tag : tags) {
      CheckBox checkBox = new CheckBox(tag);
      checkBox.setFont(Font.font("ROBOTO", FontWeight.NORMAL, 16));
      checkBox.setTextFill(Color.web("#3D405B"));
      tagsFlowPane.getChildren().add(checkBox);
    }

    rootVbox.getChildren().add(tagsFlowPane);
    VBox.setMargin(tagsFlowPane, new Insets(0, 0, 20, 0));

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
      if (node instanceof CheckBox checkBox) {
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
    VBox.setMargin(recipeCount, new Insets(15, 0, 20, 0));

    // Add a separator line before the recipe buttons
    Separator separator = new Separator(Orientation.HORIZONTAL);
    searchResultsVbox.getChildren().add(separator);

    for (Recipe recipe : recipeList) {
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
      Image unstar = new Image(getClass().getResourceAsStream("/images/unstar.png"));
      ImageView unstarIcon = new ImageView(unstar);
      starIcon.setFitWidth(20);
      starIcon.setFitHeight(20);
      unstarIcon.setFitWidth(20);
      unstarIcon.setFitHeight(20);

      // Create a ToggleButton with the unstar icon as default
      ToggleButton starButton = new ToggleButton("", unstarIcon);
      starButton.setStyle("-fx-background-color: #F9F8F3; -fx-cursor: hand;");
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

      Image recipeIcon = new Image(getClass()
          .getResourceAsStream("/images/serving.png"));
      ImageView recipeIconImage = new ImageView(recipeIcon);
      recipeIconImage.setFitWidth(40);
      recipeIconImage.setFitHeight(40);

      Hyperlink recipeButton = new Hyperlink(recipe.getName());
      recipeButton.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
      recipeButton.setGraphic(recipeIconImage);
      recipeButton.setGraphicTextGap(20.0);

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
      flowPane.setStyle("-fx-padding: 5 10 5 10;");
      flowPane.getChildren().add(recipeBox);

      Separator recipeSeparator = new Separator();
      recipeSeparator.setOrientation(Orientation.HORIZONTAL);

      // Add a tooltip with the short description for hovering effect
      Tooltip tooltip = new Tooltip(recipe.getShortDesc());
      tooltip.setFont(Font.font("ROBOTO", 18));
      tooltip.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;");
      Tooltip.install(flowPane, tooltip);

      searchResultsVbox.getChildren().add(flowPane);
      VBox.setMargin(flowPane, new Insets(10, 0, 10, 0));
      searchResultsVbox.getChildren().add(recipeSeparator);

    }
  }

}
=======
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.CheckBox;

public class BrowserView{
    private BrowserViewObserver observer;
    private BorderPane view;
    private TextField searchByNameField;
    private TextField searchByIngredientField;
    private FlowPane tagsFlowPane;
    private GridPane grid;


    public BrowserView(ArrayList<Recipe> recipeList) {
        view = new BorderPane();
        this.grid = new GridPane();
        this.tagsFlowPane = new FlowPane(5, 5);
        initLayout(recipeList);
    }

    public void setObserver(BrowserViewObserver observer) {
        this.observer = observer;
    }

    public Node getView() {
        return this.view;
    }

    public void updateRecipeList(ArrayList<Recipe> updatedRecipeList) {
      displayRecipes(updatedRecipeList);
    }

    private void initLayout(ArrayList<Recipe> recipeList) {
      // Create a grid pane for the options
      grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setPrefWidth(1024);
      grid.setPadding(new Insets(20, 20, 20, 20));
      grid.setVgap(20);
      grid.setHgap(10);
    
      // Add a title to the homepage
      Text title = new Text("Recipe Browser ");
      title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
      grid.add(title, 0, 0, 2, 1);
      title.setFill(Color.GREEN);
      GridPane.setHalignment(title, HPos.CENTER);
        
      // Add option to return to home
      Button backButton = new Button("Back to Home Page");
      backButton.setFont(Font.font("Arial", 14));
      backButton.setOnAction(e -> {
        if (observer != null) {
          observer.handleBackToHomeClicked();
        }
      });
      // Add the backButton to the top of the BorderPane
      view.setTop(backButton); 
      BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
    
      // Add search input fields for name and ingredients
      Label searchByNameLabel = new Label("Search by Name:");
      grid.add(searchByNameLabel, 0, 2);
      searchByNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
      searchByNameLabel.setTextFill(Color.GREY);

    
      searchByNameField = new TextField();
      searchByNameField.setPrefWidth(500);
      grid.add(searchByNameField, 1, 2);
    
      Label searchByIngredientLabel = new Label("Search by Ingredients:");
      grid.add(searchByIngredientLabel, 0, 3);
      searchByIngredientLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
      searchByIngredientLabel.setTextFill(Color.GREY);

    
      searchByIngredientField = new TextField();
      searchByIngredientField.setPrefWidth(500);
      grid.add(searchByIngredientField, 1, 3);
    
      // Add tags View
      tagsFlowPane.setHgap(5);
      tagsFlowPane.setVgap(5);
      // Set the preferred width before wrapping to the next row
      tagsFlowPane.setPrefWrapLength(800); 
    
      ObservableList<String> tags = FXCollections.observableArrayList("vegan", "vegetarian", "lactose free", "gluten free", "starter", "main course", "dessert and sweets");
    
      for (String tag : tags) {
        CheckBox checkBox = new CheckBox(tag);
        checkBox.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        checkBox.setTextFill(Color.GREY); 
        tagsFlowPane.getChildren().add(checkBox);
      }
    
      grid.add(tagsFlowPane, 0, 4, 2, 1);
    
      // Add search button
      Button searchButton = new Button("Search");
      searchButton.setStyle("-fx-text-fill: grey;");
      searchButton.setOnAction(e -> {
        if (observer != null) {
          String searchTextByName = searchByNameField.getText();
          String searchTextByIngredient = searchByIngredientField.getText();
          ObservableList<String> selectedTags = getSelectedTags();
          observer.handleSearch(searchTextByName, searchTextByIngredient, selectedTags);
        }
      });
    
      grid.add(searchButton, 3, 2);
    
      // Wrap the grid in a ScrollPane
      ScrollPane scrollPane = new ScrollPane(grid);
      scrollPane.setFitToWidth(true);
      scrollPane.setFitToHeight(true);
    
      view.setCenter(scrollPane);
      displayRecipes(recipeList);
    }
    

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
    
    
    public void displayRecipes(ArrayList<Recipe> recipeList) {
      grid.getChildren().removeIf(node -> (node instanceof Button && !((Button) node).getText().equals("Search") && !((Button) node).getText().equals("Back to Home Page")) || (node instanceof Text && GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 6) || (node instanceof Text && node.getId() != null && node.getId().equals("recipeCount")));
    
      // Add a Text node to display the number of recipes found
      Text recipeCount = new Text(recipeList.size() + " recipes found");
      recipeCount.setFont(Font.font("Arial", FontWeight.BOLD, 16));
      recipeCount.setId("recipeCount"); // Set the unique ID for the recipe count Text node
      grid.add(recipeCount, 0, 5, 2, 1);
      GridPane.setHalignment(recipeCount, HPos.CENTER);
      recipeCount.setFill(Color.GREEN);
    
      // Add a separator line before the recipe buttons
      Separator separator = new Separator(Orientation.HORIZONTAL);
      grid.add(separator, 0, 6, 2, 1);
    
    
      for (int i = 0; i < recipeList.size(); i++) {
        Recipe recipe = recipeList.get(i);
        Button recipeButton = new Button(recipe.getName());
        recipeButton.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        recipeButton.setStyle("-fx-text-fill: green;");
        recipeButton.setOnAction(e -> {
          if (observer != null) {
            observer.handleGoToRecipeClicked(recipe);
            }
          });

      // Add a tooltip with the short description for hovering effect
      Tooltip tooltip = new Tooltip(recipe.getShortDesc()); 
      tooltip.setFont(Font.font("Arial", 22));
      tooltip.setStyle("-fx-background-color: green; -fx-text-fill: white;");
      Tooltip.install(recipeButton, tooltip);

      grid.add(recipeButton, 0, i + 7, 1, 1);
      GridPane.setHalignment(recipeButton, javafx.geometry.HPos.LEFT);
      int columnIndex = 1;
      for (int j = 0; j < recipe.getTags().size(); j++) {
        Text recipeInfo = new Text("# " + recipe.getTags().get(j));
        recipeInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
				recipeInfo.setFill(Color.GREY);
        grid.add(recipeInfo, columnIndex, i + 7, 1, 1);
        GridPane.setHalignment(recipeInfo, javafx.geometry.HPos.RIGHT);
        columnIndex++;
      }
    }
  }

		//Method to reset the search inputs.
    public void resetSearchInputs() {
    	searchByNameField.clear();
      searchByIngredientField.clear();
      for (Node node : tagsFlowPane.getChildren()) {
        if (node instanceof CheckBox) {
          CheckBox checkBox = (CheckBox) node;
          checkBox.setSelected(false);
        }
      }
    }
    
    

}
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
