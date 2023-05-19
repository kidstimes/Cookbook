package cookbook.view;

import cookbook.model.Recipe;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * View for the favorite recipes.
 */
public class FavoriteView {
  private FavoriteViewObserver observer;
  private BorderPane view;
  private ArrayList<Recipe> favoriteRecipes;

  /** Constructor for the favorite view.
   *
   * @param favoriteRecipes the list of favorite recipes
   * @param displayName the display name of the user
   */
  public FavoriteView(ArrayList<Recipe> favoriteRecipes, String displayName) {
    this.favoriteRecipes = favoriteRecipes;
    this.view = new BorderPane();
    initLayout(displayName);
  }

  public void setObserver(FavoriteViewObserver observer) {
    this.observer = observer;
  }

  public Node getView() {
    return view;
  }

  private void initLayout(String displayName) {
    
    // create a vbox to hold the menu buttons
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
    
    sidebar.setActiveButton("My Favorites");
    sidebar.finalizeLayout();
    // Add the sidebar to the view
    view.setLeft(sidebar);
    
    VBox recipeListVbox = new VBox(10);
    recipeListVbox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");

    ScrollPane scrollPane = new ScrollPane(recipeListVbox);
    scrollPane.setFitToWidth(true); 
    scrollPane.setStyle("-fx-background-color: #F9F8F3;");
    // Add title
    Text title = new Text("My Favorite Recipes");
    title.setFont(Font.font("ROBOTO", FontWeight.BOLD, 32));
    VBox.setMargin(title, new Insets(0, 0, 20, 0));
    recipeListVbox.getChildren().add(title);

    // Add text about how many favorite recipes
    String numFavoriteRecipesText;
    if (favoriteRecipes.size() == 0) {
      numFavoriteRecipesText = "There are no favorite recipes";
    } else if (favoriteRecipes.size() == 1) {
      numFavoriteRecipesText = "There is 1 favorite recipe";
    } else {
      numFavoriteRecipesText = "There are " + favoriteRecipes.size() + " favorite recipes";
    }
    Text numFavoriteRecipes = new Text(numFavoriteRecipesText);
    numFavoriteRecipes.setFont(Font.font("ROBOTO", 18));
    VBox.setMargin(numFavoriteRecipes, new Insets(0, 0, 20, 0));
    recipeListVbox.getChildren().add(numFavoriteRecipes);

    // Add a separator line 
    Separator separator = new Separator(Orientation.HORIZONTAL);
    recipeListVbox.getChildren().add(separator);

    for (Recipe recipe : favoriteRecipes) {
      Hyperlink recipeLink = new Hyperlink(recipe.getName());
      recipeLink.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
      recipeLink.setOnAction(e -> {
        if (observer != null) {
          observer.goToRecipe(recipe);
        }
      });
      
      // Add recipe tags
      String recipeTagsString = "";
      for (int j = 0; j < recipe.getTags().size(); j++) {
        if (j == 0) {
          recipeTagsString = recipeTagsString + " # " + recipe.getTags().get(j);
        } else {
          recipeTagsString = recipeTagsString + ", # " + recipe.getTags().get(j);
        }
      }
      Text recipeTags = new Text(recipeTagsString);
      recipeTags.setFont(Font.font("ROBOTO", 18));

      // Add a tooltip with the description for the hovering effect
      Tooltip tooltip = new Tooltip(recipe.getShortDesc());
      tooltip.setFont(Font.font("ROBOTO", 18));
      tooltip.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;");
      Tooltip.install(recipeLink, tooltip);

      HBox recipeHbox = new HBox(20);
      recipeHbox.setAlignment(Pos.CENTER_LEFT);
      HBox.setHgrow(recipeHbox, Priority.ALWAYS);
    
      Button deleteButton = new Button("Delete");
      deleteButton.setStyle("-fx-font: 12px \"Roboto\";"
          + " -fx-background-color: white; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
      deleteButton.setOnAction(e -> {
        if (observer != null) {
          observer.removeRecipeFromFavorites(recipe);
          observer.goToMyFavorite();
        }
      });
      Region spacer2 = new Region();
      HBox.setHgrow(spacer2, Priority.ALWAYS);
    
      recipeHbox.getChildren().addAll(recipeLink, recipeTags, spacer2, deleteButton); 
      recipeHbox.setStyle("-fx-padding: 5 10 5 10;-fx-background-color: white;");
      recipeListVbox.getChildren().add(recipeHbox);


    }
    view.setCenter(scrollPane);

  }

}



  

