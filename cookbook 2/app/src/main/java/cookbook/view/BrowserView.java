package cookbook.view;


import java.util.ArrayList;
import cookbook.model.Recipe;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
