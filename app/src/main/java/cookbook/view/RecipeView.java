package cookbook.view;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;

// Class for a recipe view.
public class RecipeView {
  private RecipeViewObserver observer;
  private BorderPane view;
  private Recipe recipe;
  private VBox vbox;

  //Constructor to construct a recipe view
  public RecipeView(Recipe recipe) {
    this.recipe = recipe;
    this.view = new BorderPane();
    initLayout();
  }

  public void setObserver(RecipeViewObserver observer) {
    this.observer = observer;
  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public Node getView() {
    return view;
  }
  
  public void initLayout() {
    vbox = new VBox();
    vbox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");
    vbox.setSpacing(20); // Add spacing between sections in the VBox

    view.setCenter(vbox); 

    //Add a scroll pane
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vbox);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    view.setCenter(scrollPane);


    // Add a back to broswer button
    Hyperlink backButton = new Hyperlink("← Back to Recipe Browser");
    backButton.setFont(Font.font("Roboto",20));
    backButton.setOnAction(e -> {
      if (observer != null) {
          observer.handleBackToBrowserClicked();
      }
    });
    vbox.getChildren().add(backButton); 

    // Add a title (recipe name)
    Text title = new Text(recipe.getName());
    title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
    VBox titleBox = new VBox(title);
    vbox.getChildren().add(titleBox);

    // Add short description with italics
    Text shortDescription = new Text(recipe.getShortDesc());
    shortDescription.setFont(Font.font("Arial", FontPosture.ITALIC, 28));
    shortDescription.setFill(Color.DARKSLATEGREY);
    VBox shortDescriptionBox = new VBox(shortDescription);
    shortDescriptionBox.setPadding(new Insets(0, 0, 20, 0));
    vbox.getChildren().add(shortDescriptionBox);

    // Add ingredients as a list
    Text ingredientsTitle = new Text("Ingredients:");
    ingredientsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    VBox ingredientsTitleBox = new VBox(ingredientsTitle);
    vbox.getChildren().add(ingredientsTitleBox);

    // Add ingredients as a list with reduced spacing
    VBox ingredientsVBox = new VBox();
    ingredientsVBox.setSpacing(5); // Reduce the spacing between ingredients
    for (Ingredient ingredient : recipe.getIngredients()) {
      Text ingredientText = new Text(ingredient.toString());
      ingredientText.setFont(Font.font("Arial", 20));
      VBox ingredientBox = new VBox(ingredientText);
      ingredientsVBox.getChildren().add(ingredientBox);
    }
    vbox.getChildren().add(ingredientsVBox);
    
 
    // Display directions
    Text directionsTitle = new Text("Directions:");
    directionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    vbox.getChildren().add(directionsTitle);

    Text directions = new Text(recipe.getDirections());
    directions.setFont(Font.font("Roboto", 20));
    // wrap text in vbox and add to vbox container
    VBox directionsTitleBox = new VBox(directionsTitle);
    directionsTitleBox.setPadding(new Insets(40, 0, 20, 0));
    vbox.getChildren().add(directionsTitleBox);
    
    VBox directionsBox = new VBox(directions);
    vbox.getChildren().add(directionsBox);
    

    // Display tags
    Text tagsTitle = new Text("Tags:");
    tagsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    HBox tagsTitleBox = new HBox(tagsTitle);
    tagsTitleBox.setPadding(new Insets(40, 0, 20, 0));
    vbox.getChildren().add(tagsTitleBox);

 
    // Display tags in a single line
    HBox tagsHBox = new HBox();
    tagsHBox.setSpacing(10); // Set spacing between tags
    for (String tag : recipe.getTags()) {
      Text tagText = new Text("# " + tag);
      tagText.setFont(Font.font("Arial", FontPosture.ITALIC, 20));
      tagsHBox.getChildren().add(tagText);
    }
    vbox.getChildren().add(tagsHBox);
  }


}

  
