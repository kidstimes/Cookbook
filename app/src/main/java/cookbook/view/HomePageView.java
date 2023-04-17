package cookbook.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HomePageView{
    private HomePageViewObserver observer;
    private BorderPane view;
    
    public HomePageView () {
        this.view = new BorderPane();
        initLayout();
    }

    public void setObserver(HomePageViewObserver observer) {
        this.observer = observer;
    }

    public Node getView() {
        return this.view;
    }

    public void initLayout() {

        // Create a grid pane for the options
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(20);
        grid.setHgap(10);

        // Add a title to the homepage
        Text title = new Text("Welcome to Cookbook!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        grid.add(title, 0, 0, 2, 1);

        // Add five options to the homepage, one per row
        Button browseRecipesButton = new Button("Browse Recipes");
        browseRecipesButton.setFont(Font.font("Arial", 18));
        browseRecipesButton.setOnAction(e -> {       
            // Handle browse recipes action
            observer.handleBrowseRecipesClicked();
        });
        grid.add(browseRecipesButton, 0, 1, 1, 1);
        GridPane.setHalignment(browseRecipesButton, javafx.geometry.HPos.LEFT);

        Button addRecipeButton = new Button("Add a Recipe");
        addRecipeButton.setFont(Font.font("Arial", 18));
        addRecipeButton.setOnAction(e -> {
            // Handle add recipe action
        });
        grid.add(addRecipeButton, 0, 2, 1, 1);
        GridPane.setHalignment(addRecipeButton, javafx.geometry.HPos.LEFT);

        Button searchRecipeButton = new Button("Weekly Dinner List");
        searchRecipeButton.setFont(Font.font("Arial", 18));
        searchRecipeButton.setOnAction(e -> {
            // Handle search recipe action
        });
        grid.add(searchRecipeButton, 0, 3, 1, 1);
        GridPane.setHalignment(searchRecipeButton, javafx.geometry.HPos.LEFT);

        Button viewFavoritesButton = new Button("My Favorites");
        viewFavoritesButton.setFont(Font.font("Arial", 18));
        viewFavoritesButton.setOnAction(e -> {
            // Handle view favorites action
        });
        grid.add(viewFavoritesButton, 0, 4, 1, 1);
        GridPane.setHalignment(viewFavoritesButton, javafx.geometry.HPos.LEFT);

        Button viewShoppingListButton = new Button("My Shopping List");
        viewShoppingListButton.setFont(Font.font("Arial", 18));
        viewShoppingListButton.setOnAction(e -> {
            // Handle view shopping list action
        });
        grid.add(viewShoppingListButton, 0, 5, 1, 1);
        GridPane.setHalignment(viewShoppingListButton, javafx.geometry.HPos.LEFT);

        view.setCenter(grid);
    }
}
