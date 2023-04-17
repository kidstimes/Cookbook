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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

public class BrowserView{
    private BrowserViewObserver observer;
    private ArrayList<Recipe> recipeList;
    private BorderPane view;
    private TextField searchByNameField;
    private TextField searchByIngredientField;
    private ListView<String> tagsListView;


    public BrowserView(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        view = new BorderPane();
        initLayout();
    }

    public void setObserver(BrowserViewObserver observer) {
        this.observer = observer;
    }

    public Node getView() {
        return this.view;
    }

    public void updateRecipeList(ArrayList<Recipe> updatedRecipeList) {
        recipeList.clear();
        recipeList.addAll(updatedRecipeList);
        // Clear the existing recipe buttons and create new ones based on updatedRecipeList
    }

    private void initLayout() {
        // Create a grid pane for the options
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPrefWidth(800);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(20);
        grid.setHgap(10);
    
        // Add a title to the homepage
        Text title = new Text("Recipe Browser ");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        grid.add(title, 0, 0, 2, 1);
    
        // Add option to return to home
        Button backButton = new Button("Back to Home Page");
        backButton.setFont(Font.font("Arial", 12));
        backButton.setOnAction(e -> {
            if (observer != null) {
                observer.handleBackToHomeClicked();
            }
        });
        grid.add(backButton, 0, 1, 1, 1);
        GridPane.setHalignment(backButton, HPos.LEFT);
    

        // Add search input fields for name and ingredients
        Label searchByNameLabel = new Label("Search by Name:");
        grid.add(searchByNameLabel, 0, 2);

        searchByNameField = new TextField();
        searchByNameField.setPrefWidth(300);
        grid.add(searchByNameField, 1, 2);

        Label searchByIngredientLabel = new Label("Search by Ingredients:");
        grid.add(searchByIngredientLabel, 0, 3);

        searchByIngredientField = new TextField();
        searchByIngredientField.setPrefWidth(300);
        grid.add(searchByIngredientField, 1, 3);

        
    
        // Add tags ListView
        this.tagsListView = new ListView<>();
        FlowPane tagsFlowPane = new FlowPane(5, 5); // 5 is the horizontal and vertical spacing between CheckBoxes
        tagsFlowPane.setPrefWrapLength(250); // Set the preferred width before wrapping to the next row
    
        ObservableList<String> tags = FXCollections.observableArrayList("vegan", "vegetarian", "lactose free", "gluten free", "starter", "main course", "dessert and sweets");

        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            tagsFlowPane.getChildren().add(checkBox);
        }

        grid.add(tagsFlowPane, 0, 4, 2, 1);

        // Add search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            if (observer != null) {
                String searchTextByName = searchByNameField.getText();
                String searchTextByIngredient = searchByIngredientField.getText();
                ObservableList<String> selectedTags = tagsListView.getSelectionModel().getSelectedItems();
                observer.handleSearch(searchTextByName, searchTextByIngredient, selectedTags);
            }
        });
        
        grid.add(searchButton, 3, 2);
        view.setCenter(grid);
        displayRecipes(recipeList);
    }

    
    public void displayRecipes(ArrayList<Recipe> recipeList) {
        GridPane grid = (GridPane) view.getCenter();

        // Add a Text node to display the number of recipes found
        Text recipeCount = new Text(recipeList.size() + " recipes found");
        recipeCount.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        grid.add(recipeCount, 0, 5, 2, 1);
        GridPane.setHalignment(recipeCount, HPos.CENTER);

        // Add a separator line before the recipe buttons
        Separator separator = new Separator(Orientation.HORIZONTAL);
        grid.add(separator, 0, 6, 2, 1);
    
        for (int i = 0; i < recipeList.size(); i++) {

            Recipe recipe = recipeList.get(i);

            Button recipeButton = new Button(recipe.getName());
            recipeButton.setFont(Font.font("Arial", 12));
            recipeButton.setOnAction(e -> {
                if (observer != null) {
                    observer.handleGoToRecipeClicked(recipe);
                }
            });
            grid.add(recipeButton, 0, i + 7, 1, 1);
            GridPane.setHalignment(recipeButton, javafx.geometry.HPos.LEFT);
            for (int j = 0; j < recipe.getTags().size(); j++) {
                Text recipeInfo = new Text(recipe.getTags().get(j));
                recipeInfo.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                grid.add(recipeInfo, 1 + j, i + 7, 1, 1);
                GridPane.setHalignment(recipeInfo, javafx.geometry.HPos.RIGHT);
            }
        }
    }
    
}
