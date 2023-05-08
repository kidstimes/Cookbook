package cookbook.view;

import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;




/**
 * View class for the recipe page.
 */
public class RecipeView {
  private RecipeViewObserver observer;
  private BorderPane view;
  private Recipe recipe;
  private VBox vbox;
  private VBox ingredientsVBox;
  private HBox tagsHBox;
  private Spinner<Integer> servingSpinner;
  private int initialServings;
  private Button saveButton;
  private boolean isFavorite;
  private Button starButton;

  /**
   * Recipe View Constructor.
   */
  public RecipeView() {
    this.view = new BorderPane();
    this.initialServings = 2;
    this.isFavorite = false;
  }

  /**
   * Set an observer of the recipe view.
   */
  public void setObserver(RecipeViewObserver observer) {
    this.observer = observer;
  }

  /**
   * Set the recipe of the recipe view.
   */
  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
    initLayout();
    updateStarButton();
  }

  public String getRecipeName() {
    return this.recipe.getName();
  }

  public void setFavorite(boolean isFavorite) {
    this.isFavorite = isFavorite;
    updateStarButton();
  }

  /**
   * Get the view of the recipe page.
   */
  public Node getView() {
    return view;
  }
  

  /**
   * Get a list of updated tags.
   */
  public ArrayList<String> getUpdatedTags() {
    ArrayList<String> updatedTags = new ArrayList<String>();
    for (Node node : tagsHBox.getChildren()) {
      if (node instanceof Text) {
        String tag = ((Text) node).getText().substring(2); // Remove the "#" character from the tag
        updatedTags.add(tag);
      }
    }
    return updatedTags;
  }


  /**
   * init the layout of the recipe page.
   */
  public void initLayout() {

    vbox = new VBox();
    vbox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");
    // Add spacing between sections in the VBox
    vbox.setSpacing(10); 

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
    backButton.setFont(Font.font("Roboto", 20));
    backButton.setOnAction(e -> {
      if (observer != null) {
        observer.goToBrowser();
      }
    });

    // Add serving spinner
    vbox.getChildren().add(backButton);

    // Add a title (recipe name)
    Text title = new Text(recipe.getName());
    title.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));
    VBox titleBox = new VBox(title);
    vbox.getChildren().add(titleBox);

    // Add a Star button
    starButton = new Button("Star");
    starButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    starButton.setStyle(
      " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
      + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
      starButton.setOnAction(e -> {
        if (observer != null) {
          observer.handleStarClicked(recipe);
          isFavorite = !isFavorite;
          updateStarButton();
        }
      });
    vbox.getChildren().add(starButton);

    // Add short description with italics
    Text shortDescription = new Text(recipe.getShortDesc());
    shortDescription.setFont(Font.font("ARIAL", FontPosture.ITALIC, 28));
    shortDescription.setFill(Color.DARKSLATEGREY);
    VBox shortDescriptionBox = new VBox(shortDescription);
    shortDescriptionBox.setPadding(new Insets(0, 0, 20, 0));
    vbox.getChildren().add(shortDescriptionBox);

    // Add ingredients as a list
    Text ingredientsTitle = new Text("Ingredients:");
    ingredientsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    VBox ingredientsTitleBox = new VBox(ingredientsTitle);
    vbox.getChildren().add(ingredientsTitleBox);

    createServingSpinner();

    // Add ingredients as a list with reduced spacing
    ingredientsVBox = new VBox();
    ingredientsVBox.setSpacing(5); // Reduce the spacing between ingredients
    for (Ingredient ingredient : recipe.getIngredients()) {
      Text ingredientText = new Text(ingredient.toString());
      ingredientText.setFont(Font.font("ROBOTO", 20));
      VBox ingredientBox = new VBox(ingredientText);
      ingredientsVBox.getChildren().add(ingredientBox);
    }
    vbox.getChildren().add(ingredientsVBox);
    
 
    // Display directions
    Text directionsTitle = new Text("Directions:");
    directionsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
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
    tagsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
    HBox tagsTitleBox = new HBox(tagsTitle);
    tagsTitleBox.setPadding(new Insets(40, 0, 20, 0));
    vbox.getChildren().add(tagsTitleBox);


    // Display tags in a single line
    tagsHBox = new HBox(); 
    tagsHBox.setSpacing(10); 
    LinkedHashSet<String> uniqueTags = new LinkedHashSet<>(recipe.getTags());
    for (String tag : uniqueTags) {
      Text tagText = new Text("# " + tag);
      tagText.setFont(Font.font("ROBOTO", FontPosture.ITALIC, 16));
      tagsHBox.getChildren().add(tagText);
    }
    vbox.getChildren().add(tagsHBox);
    
    // Add ComboBox for predefined tags
    ComboBox<String> predefinedTagsComboBox = new ComboBox<>();
    predefinedTagsComboBox.getItems().addAll(
        "vegetarian", "vegan", "lactose free", "gluten free", 
          "starter", "main course", "dessert and sweets"
    );
    predefinedTagsComboBox.setPromptText("Choose a predefined tag...");
    predefinedTagsComboBox.setStyle("-fx-font: 16px \"Roboto\";");
 
    // Add new tag input field
    TextField newTagField = new TextField();
    newTagField.setPromptText("Or enter a new tag...");
    newTagField.setStyle("-fx-font: 16px \"Roboto\";");
 
    // Add button to add the new tag
    Button addTagButton = new Button("Add Tag");
    addTagButton.setFont(Font.font("Roboto", 16));
    //Set the style
    addTagButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addTagButton.setOnAction(e -> {
      String newTag;
      if (predefinedTagsComboBox.getValue() != null) {
        newTag = predefinedTagsComboBox.getValue().trim();
        predefinedTagsComboBox.getSelectionModel().clearSelection();
      } else {
        newTag = newTagField.getText().trim();
      }
      if (tagAlreadyExists(newTag)) {
        showAlert(Alert.AlertType.WARNING, "Warning", "The tag already exists.");
        return;
      }    
      if (!newTag.isEmpty()) {
        Text tagText = new Text("# " + newTag);
        tagText.setFont(Font.font("Roboto", FontPosture.ITALIC, 16));
        // Add a delete button for each tag
        Button deleteTagButton = new Button("x");
        deleteTagButton.setFont(Font.font("Roboto", 12));
        //set the style
        deleteTagButton.setStyle(
            " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
        deleteTagButton.setOnAction(event -> {
          tagsHBox.getChildren().removeAll(tagText, deleteTagButton);
        });
    
        tagsHBox.getChildren().addAll(tagText, deleteTagButton);
        newTagField.clear();
      }
    });

    HBox newTagBox = new HBox();
    newTagBox.setSpacing(10); // Set spacing between ComboBox, TextField, and Button
    newTagBox.getChildren().addAll(predefinedTagsComboBox, newTagField, addTagButton);
    vbox.getChildren().add(newTagBox);


    // Add "Save Tags" button
    saveButton = new Button("Save Tags");
    saveButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    //set savebutton style
    saveButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    saveButton.setOnAction(e -> {
      if (observer != null) {
        ArrayList<String> updatedTags = getUpdatedTags();
        observer.handleSaveTagsClicked(updatedTags, recipe.getName());
        //clear all the added tags
        tagsHBox.getChildren().clear(); 
      }
    });
    vbox.getChildren().add(saveButton);

    updateStarButton();
  }

  private void createServingSpinner() {
    servingSpinner = new Spinner<>();
    SpinnerValueFactory<Integer> valueFactory 
        = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, initialServings, 2);
    servingSpinner.setValueFactory(valueFactory);
    servingSpinner.setEditable(true);
    
    servingSpinner.setMinWidth(100); // Adjust as needed
    servingSpinner.setPrefWidth(100); // Adjust as needed
    servingSpinner.setMaxWidth(100); // Adjust as needed
    servingSpinner.setPadding(new Insets(10));
  
    servingSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
      updateIngredientQuantities(newValue);
    });
  
    HBox servingSpinnerContainer = new HBox();
    servingSpinnerContainer.setSpacing(5);
    servingSpinnerContainer.setStyle("-fx-spacing: 5; -fx-font: 16px \"Roboto\";");
    servingSpinnerContainer.setAlignment(Pos.CENTER_LEFT); // Set alignment for the HBox

    Label servingsLabel = new Label("Servings:");
    servingsLabel.setStyle("-fx-font-size: 20px; -fx-font-family: Roboto;");
    servingSpinnerContainer.getChildren().addAll(servingsLabel, servingSpinner);
    vbox.getChildren().add(servingSpinnerContainer);
  }


  private void updateIngredientQuantities(int servings) {
    double scaleFactor = (double) servings / initialServings;

    VBox newIngredientsVBox = new VBox();
    newIngredientsVBox.setSpacing(5);
  
    for (Ingredient ingredient : recipe.getIngredients()) {
      Ingredient adjustedIngredient 
          = new Ingredient(ingredient.getName(),
           (float) (ingredient.getQuantity() * scaleFactor), ingredient.getMeasurementUnit());
      Text ingredientText = new Text(adjustedIngredient.toString());
      ingredientText.setFont(Font.font("ROBOTO", 20));
      VBox ingredientBox = new VBox(ingredientText);
      newIngredientsVBox.getChildren().add(ingredientBox);
    }
  
    // Replace the old ingredients VBox with the new one
    int ingredientsVBoxIndex = vbox.getChildren().indexOf(ingredientsVBox);
    vbox.getChildren().remove(ingredientsVBox);
    vbox.getChildren().add(ingredientsVBoxIndex, newIngredientsVBox);
    ingredientsVBox = newIngredientsVBox;
  }
  
  private boolean tagAlreadyExists(String newTag) {
    for (Node node : tagsHBox.getChildren()) {
      if (node instanceof Text) {
        String tag = ((Text) node).getText().substring(2); // Remove the "#" character from tag
        if (tag.equalsIgnoreCase(newTag)) {
          return true;
        }
      }
    }
    return false;
  }

  public void updateStarButton() {
    if (isFavorite) {
      starButton.setText("Unstar");
    } else {
      starButton.setText("Star");
    }
  }



  /**
   * Show an alert with the given alert type, title, and message.
   */
  public void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

}



