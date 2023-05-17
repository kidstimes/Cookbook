package cookbook.view;

import cookbook.model.Recipe;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    VBox sidebar = new VBox(20);
    sidebar.setMaxWidth(120);
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
      deleteButton.setStyle("-fx-font: 12px \"Roboto\"; -fx-background-color: white; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
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



  

