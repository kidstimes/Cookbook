package cookbook.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cookbook");

        // Create a grid pane for the options
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Add a title to the homepage
        Text title = new Text("Welcome to Cookbook!");
        title.setFont(Font.font("Arial", 24));
        grid.add(title, 0, 0, 2, 1);

        // Add five options to the homepage
        Button browseRecipesButton = new Button("Browse Recipes");
        browseRecipesButton.setOnAction(e -> {
            // Handle browse recipes action
        });
        grid.add(browseRecipesButton, 0, 1);

        Button addRecipeButton = new Button("Add a Recipe");
        addRecipeButton.setOnAction(e -> {
            // Handle add recipe action
        });
        grid.add(addRecipeButton, 1, 1);

        Button searchRecipeButton = new Button("Search for a Recipe");
        searchRecipeButton.setOnAction(e -> {
            // Handle search recipe action
        });
        grid.add(searchRecipeButton, 0, 2);

        Button viewFavoritesButton = new Button("View Favorites");
        viewFavoritesButton.setOnAction(e -> {
            // Handle view favorites action
        });
        grid.add(viewFavoritesButton, 1, 2);

        Button viewShoppingListButton = new Button("View Shopping List");
        viewShoppingListButton.setOnAction(e -> {
            // Handle view shopping list action
        });
        grid.add(viewShoppingListButton, 0, 3);

        // Create a scene with the grid pane
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



