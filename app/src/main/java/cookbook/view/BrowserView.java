package cookbook.view;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.CheckBox;
import cookbook.model.Recipe;

public class BrowserView {
  private BrowserViewObserver observer;
  private BorderPane view;
  private TextField searchByNameField;
  private TextField searchByIngredientField;
  private FlowPane tagsFlowPane;
  private VBox searchResultsVBox;
  private VBox rootVBox;
  private ArrayList<String> privateTags;


  /**
   * Browser View Constructor.
   */
  public BrowserView(ArrayList<Recipe> recipeList, ArrayList<String> privateTags) {
    view = new BorderPane();
    rootVBox = new VBox();
    this.searchResultsVBox = new VBox();
    this.tagsFlowPane = new FlowPane();
    this.privateTags = privateTags;
    initLayout(recipeList);
  }

  /**
   * Set an observer of the browser view.
   */
  public void setObserver(BrowserViewObserver observer) {
    this.observer = observer;
  }

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
    
    rootVBox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");

    // clear any existing children from the vbox
    rootVBox.getChildren().clear();
    // clear tagsFlowPane and searchResultsVBox
    tagsFlowPane.getChildren().clear();
    searchResultsVBox.getChildren().clear();
    // Add option to return to home
    Hyperlink backButton = new Hyperlink("← Back to Home Page");
    backButton.setFont(Font.font("ROBOTO", 20));
    backButton.setOnAction(e -> {
      if (observer != null) {
        observer.goToHomePage();
      }
    });
    // Add the backButton to the top of the BorderPane
    rootVBox.getChildren().add(backButton);

    // Add a title to the homepage
    Text title = new Text("Recipe Browser");
    title.setFont(Font.font("ROBOTO", FontWeight.BOLD, 32));
    rootVBox.setMargin(title, new Insets(0, 0, 20, 0));
    rootVBox.getChildren().add(title);

    // Add search input fields for name and ingredients
    Label searchByNameLabel = new Label("Name:");
    searchByNameLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
    searchByNameField = new TextField();
    searchByNameField.setId("searchByNameField");
    searchByNameField.setStyle("-fx-font-size: 18;");
    searchByNameField.setPrefWidth(350);
    searchByNameField.setPrefHeight(30);

    Label searchByIngredientLabel = new Label("Ingredients:");
    searchByIngredientLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
    searchByIngredientField = new TextField();
    searchByIngredientField.setStyle("-fx-font-size: 18;");
    searchByIngredientField.setPrefWidth(380);
    searchByIngredientField.setPrefHeight(30);

    // Add search button
    Button searchButton = new Button("Search");
    searchButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    searchButton.setFont(Font.font("ROBOTO", 20));
    rootVBox.setMargin(searchButton, new Insets(0, 0, 20, 0));

    searchButton.setOnAction(e -> {
      if (observer != null) {
        searchResultsVBox.getChildren().clear();
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
    rootVBox.getChildren().add(searchInputHbox);
    rootVBox.setMargin(searchInputHbox, new Insets(0, 0, 10, 0));

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

    rootVBox.getChildren().add(tagsFlowPane);
    rootVBox.setMargin(tagsFlowPane, new Insets(0, 0, 10, 0));

    // Wrap the rootVBox in a ScrollPane so that the content can be scrolled
    ScrollPane scrollPane = new ScrollPane(rootVBox);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    // add searchResultsVBox to vbox and vbox is the content of ScrollPane
    rootVBox.getChildren().add(searchResultsVBox);

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
    searchResultsVBox.getChildren().clear();
    // Add a Text node to display the number of recipes found
    Text recipeCount = new Text(recipeList.size() + " recipes found");
    recipeCount.setFont(Font.font("ROBOTO", 16));
    recipeCount.setId("recipeCount"); // Set the unique ID for the recipe count Text node
    searchResultsVBox.getChildren().add(recipeCount);
    // add margin to the recipeCount
    searchResultsVBox.setMargin(recipeCount, new Insets(15, 0, 10, 0));

    // Add a separator line before the recipe buttons
    Separator separator = new Separator(Orientation.HORIZONTAL);
    searchResultsVBox.getChildren().add(separator);

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

      Hyperlink recipeButton = new Hyperlink(recipe.getName());
      recipeButton.setFont(Font.font("ROBOTO", FontWeight.BOLD, 18));
      recipeButton.setOnAction(e -> {
        if (observer != null) {
          observer.goToRecipe(recipe);
        }
      });

      Text recipeTags = new Text(recipeTagsString);
      recipeTags.setFont(Font.font("ROBOTO", 16));


      Button button = new Button("Add to favorites");

      FlowPane flowPane = new FlowPane();
      flowPane.setStyle("-fx-padding: 5 10 5 10;-fx-background-color: white;");
      flowPane.getChildren().add(recipeButton);
      flowPane.getChildren().add(recipeTags);

      // Add a tooltip with the short description for hovering effect
      Tooltip tooltip = new Tooltip(recipe.getShortDesc());
      tooltip.setFont(Font.font("ROBOTO", 18));
      tooltip.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;");
      Tooltip.install(flowPane, tooltip);

      searchResultsVBox.getChildren().add(flowPane);
      searchResultsVBox.setMargin(flowPane, new Insets(0, 0, 10, 0));

    }
  }



  /**
   * Reset the search inputs.
   */
  public void resetSearchInputs() {
    searchResultsVBox.getChildren().clear();
    searchByIngredientField.clear();
    searchByNameField.clear();
    for (Node node : tagsFlowPane.getChildren()) {
      if (node instanceof CheckBox) {
        CheckBox checkBox = (CheckBox) node;
        checkBox.setSelected(false);
      }
    }
  }
}