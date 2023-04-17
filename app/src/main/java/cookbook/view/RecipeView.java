package cookbook.view;

import cookbook.controller.RecipeController;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import cookbook.model.Recipe;


public class RecipeView {
  private RecipeController recipeController;
  private BorderPane view;
  private Recipe recipe;

  public RecipeView(RecipeController recipeController, Recipe recipe) {
    this.recipeController = recipeController;
    this.recipe = recipe;
    this.view = new BorderPane();

    initLayout();
  }

  public Node getView() {
    return view;
  }
  
  public void initLayout() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setVgap(20);
    grid.setHgap(10);

    // Add a title to the homepage
    Text title = new Text("Recipe for " + recipe.getName());
    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(title, 0, 0, 2, 1);

    // Add option to return to home
    Button backButton = new Button("Back to Browser");
    backButton.setFont(Font.font("Arial", 12));
    backButton.setOnAction(e -> {       
      recipeController.handleBackToBrowserClicked();
    });
    grid.add(backButton, 0, 1, 1, 1);
    GridPane.setHalignment(backButton, javafx.geometry.HPos.LEFT);

    // Add grid to view
    view.setTop(grid);


  }
  

  
}
