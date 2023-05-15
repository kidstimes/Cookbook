package cookbook.view;

import cookbook.Cookbook;
import cookbook.model.Comment;
import cookbook.model.CookbookFacade;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * View class for the recipe page.
 */
public class RecipeView {
  private RecipeViewObserver observer;
  private BorderPane view;
  private Recipe recipe;
  private VBox vbox;
  private GridPane ingredientsGrid;
  private HBox tagsHBox;
  private Spinner<Integer> servingSpinner;
  private int initialServings;
  private Button saveButton;
  private String displayName;
  private CookbookFacade cookbookFacade;
  private User user;

  /**
   * Recipe View Constructor.
   */
  public RecipeView(String displayName, CookbookFacade cookbookFacade, User user) {
    this.view = new BorderPane();
    this.initialServings = 2;
    this.displayName = displayName;
    this.cookbookFacade = cookbookFacade;
    this.user = user;
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
  }

  /**
   * Get the view of the recipe page.
   */
  public Node getView() {
    return view;
  }

  /**
   * init the layout of the recipe page.
   */
  public void initLayout() {
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(30);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text welcomeText = new Text(displayName + ", welcome!");
    welcomeText.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(welcomeText);

    // Add five options to the homepage, one per row
    Button[] sidebarButtons = {
      createButton("Home Page", e -> observer.goToHomePage()),
      createButton("Browse Recipes", e -> observer.goToBrowser()),
      createButton("Add a Recipe", e -> observer.goToAddRecipe()),
      createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
      createButton("My Favorites", e -> observer.goToMyFavorite()),
      createButton("My Shopping List", e -> observer.goToShoppingList())
      };

    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);
    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 18));
    logoutButton.setStyle(
        "-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> { 
      observer.userLogout(); 
    });
    sidebar.getChildren().add(logoutButton);
    view.setLeft(sidebar);

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
    
    // Add a title (recipe name) with star icon
    Text title = new Text(recipe.getName());
    title.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));
    title.setWrappingWidth(900);
    HBox titleBox = new HBox();
    titleBox.setMaxWidth(900);

    // Create star and unstar icons using ImageViews
    Image star = new Image(getClass().getResourceAsStream("/images/star.png"));
    ImageView starIcon = new ImageView(star);
    starIcon.setStyle("-fx-background-color: transparent;");
    Image unstar = new Image(getClass().getResourceAsStream("/images/unstar.png"));
    ImageView unstarIcon = new ImageView(unstar);
    unstarIcon.setStyle("-fx-background-color: transparent;");
    starIcon.setFitWidth(30);
    starIcon.setFitHeight(30);
    unstarIcon.setFitWidth(30);
    unstarIcon.setFitHeight(30);

    // Create a ToggleButton with the unstar icon as default
    ToggleButton starButton = new ToggleButton("", unstarIcon);
    starButton.setSelected(recipe.isStarred());
    if (recipe.isStarred()) {
      starButton.setGraphic(starIcon);
    }

    // Add an event handler to the ToggleButton to change the icon when clicked
    starButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        starButton.setGraphic(starIcon);
        observer.addRecipeToFavorites(recipe);
      } else {
        starButton.setGraphic(unstarIcon);
        observer.removeRecipeFromFavorites(recipe);
      }
    });

    // Add the title and star icon to the titleBox
    titleBox.setSpacing(10);
    titleBox.getChildren().addAll(starButton, title);
    vbox.getChildren().add(titleBox);


    // Add short description with italics
    Text shortDescription = new Text(recipe.getShortDesc());
    shortDescription.setFont(Font.font("ARIAL", FontPosture.ITALIC, 28));
    shortDescription.setFill(Color.DARKSLATEGREY);
    shortDescription.setWrappingWidth(900); 
    VBox shortDescriptionBox = new VBox(shortDescription);
    shortDescriptionBox.setMaxWidth(900); 
    shortDescriptionBox.setPadding(new Insets(0, 0, 20, 0));
    vbox.getChildren().add(shortDescriptionBox);

    // Add DatePicker for selecting the date to add the recipe to the weekly dinner list
    DatePicker datePicker = new DatePicker();
    //write select a date for user to select a date
    Text selectDate = new Text("Select a date: ");
    selectDate.setFont(Font.font("ROBOTO", FontWeight.BOLD, 18));
    selectDate.setFill(Color.DARKSLATEGREY);
    HBox dateBox = new HBox(selectDate, datePicker);
    dateBox.setSpacing(10);
    dateBox.setAlignment(Pos.CENTER_LEFT);
    vbox.getChildren().add(dateBox);
    datePicker.setStyle("-fx-font: 16px \"Roboto\";");

    // Set the minimum allowed date to today's date
    datePicker.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        LocalDate today = LocalDate.now();
        setDisable(empty || date.compareTo(today) < 0);
      }
    });
    // Add button to add the recipe to the weekly dinner list
    Button addToWeeklyDinnerButton = new Button("Add to Weekly Dinner");
    addToWeeklyDinnerButton.setFont(Font.font("Roboto", 16));
    addToWeeklyDinnerButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addToWeeklyDinnerButton.setOnAction(e -> {
      if (datePicker.getValue() != null) {
        LocalDate selectedDate = datePicker.getValue();
        int weekNumber = selectedDate.get(WeekFields.ISO.weekOfWeekBasedYear());    
        if (observer.addRecipeToWeeklyDinner(selectedDate, recipe)) {
          showInlineStyledAlert(Alert.AlertType.INFORMATION, "Success",
              String.format("%s added successfully to date %s, week %d dinner list.",
                 recipe.getName(), selectedDate.toString(), weekNumber));
        } else {
          showInlineStyledAlert(Alert.AlertType.WARNING, "Warning",
              String.format("%s already exists on %s of week %d dinner list.",
               recipe.getName(), selectedDate.toString(), weekNumber));
        }
        //clear the date picker
        datePicker.setValue(null);
      } else {
        showInlineStyledAlert(Alert.AlertType.WARNING, "Warning", "Please select a date.");
      }
    });
    HBox addToWeeklyDinnerBox = new HBox();
    addToWeeklyDinnerBox.setSpacing(10);
    addToWeeklyDinnerBox.getChildren().addAll(datePicker, addToWeeklyDinnerButton);
    vbox.getChildren().add(addToWeeklyDinnerBox);


    // Add ingredients as a list
    Text ingredientsTitle = new Text("Ingredients:");
    ingredientsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    VBox ingredientsTitleBox = new VBox(ingredientsTitle);
    vbox.getChildren().add(ingredientsTitleBox);
    createServingSpinner();

    // Add ingredients as a list with reduced spacing
    ingredientsGrid = new GridPane();
    ingredientsGrid.setVgap(0); // Reduce the vertical spacing between ingredients
    ingredientsGrid.setHgap(10); // Set horizontal spacing between elements

    int rowIndex = 0;
    for (Ingredient ingredient : recipe.getIngredients()) {
      Text ingredientQuantity = new Text(String.format("%.1f", ingredient.getQuantity()));
      ingredientQuantity.setFont(Font.font("ROBOTO", 18));
      Text ingredientUnit = new Text(ingredient.getMeasurementUnit());
      ingredientUnit.setFont(Font.font("ROBOTO", 18));
      Text ingredientName = new Text(ingredient.getName());
      ingredientName.setFont(Font.font("ROBOTO", 18));
      ingredientName.maxWidth(400);
      ingredientName.setWrappingWidth(400);
      ingredientsGrid.add(ingredientQuantity, 0, rowIndex);
      ingredientsGrid.add(ingredientUnit, 1, rowIndex);
      ingredientsGrid.add(ingredientName, 2, rowIndex);
      rowIndex++;
    }
    vbox.getChildren().add(ingredientsGrid);
     
    // Display directions
    Text directionsTitle = new Text("Directions:");
    directionsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    vbox.getChildren().add(directionsTitle);
    Text directions = new Text(recipe.getDirections());
    directions.setFont(Font.font("Roboto", 18));
    directions.setWrappingWidth(900); 
    // wrap text in vbox and add to vbox container
    VBox directionsTitleBox = new VBox(directionsTitle);
    directionsTitleBox.setPadding(new Insets(40, 0, 20, 0));
    vbox.getChildren().add(directionsTitleBox);
    VBox directionsBox = new VBox(directions);
    directionsBox.setMaxWidth(900);
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
        showInlineStyledAlert(Alert.AlertType.WARNING, "Warning", "The tag already exists.");
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

    // Display comments
    Text commentsTitle = new Text("Comments:");
    commentsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
    VBox commentsTitleBox = new VBox(commentsTitle);
    commentsTitleBox.setPadding(new Insets(40, 0, 20, 0));
    vbox.getChildren().add(commentsTitleBox);

    // Create a container for displaying comments
    VBox commentsContainer = new VBox();
    commentsContainer.setSpacing(10);
    vbox.getChildren().add(commentsContainer);

    try {
      int recipeId = cookbookFacade.getRecipeId(recipe.getName());
      // Load comments
      List<Comment> comments = cookbookFacade.getComments(recipeId);
  
      //Add comments to the container
      for (Comment comment : comments) {
        HBox commentPane = createCommentPane(comment.getText());
        commentsContainer.getChildren().add(commentPane);
      }
    } catch (Exception e) {
      e.printStackTrace(); // Print the exception stack trace to understand the issue
    }

    // Create a text area for users to input their comments
    TextArea commentInput = new TextArea();
    commentInput.setPromptText("Write a comment...");
    commentInput.setStyle("-fx-font: 16px \"Roboto\";");
    commentInput.setWrapText(true);
    commentInput.setMaxWidth(900);
    commentInput.setMaxHeight(100);
    vbox.getChildren().add(commentInput);

    // Add a "Post Comment" button
    Button postCommentButton = new Button("Post Comment");
    postCommentButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    postCommentButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    postCommentButton.setOnAction(e -> {
      String commentText = commentInput.getText().trim();
      if (!commentText.isEmpty()) {
        try {
          // Get recipe ID by name
          int recipeId = cookbookFacade.getRecipeId(recipe.getName());
          
          Comment comment = new Comment(0, commentText, recipeId, user.getId());
          
          cookbookFacade.addComment(comment);
    
          HBox commentPane = createCommentPane(commentText);
          commentsContainer.getChildren().add(commentPane);
          commentInput.clear();
        } catch (Exception ex) {
          ex.printStackTrace(); // Print the exception stack trace to understand the issue
        }
      }
    });
    vbox.getChildren().add(postCommentButton);
  
  }

  private void createServingSpinner() {
    servingSpinner = new Spinner<>();
    SpinnerValueFactory<Integer> valueFactory 
        = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, initialServings, 2);
    servingSpinner.setValueFactory(valueFactory);
    servingSpinner.setEditable(true);
    servingSpinner.setMinWidth(100);
    servingSpinner.setPrefWidth(100);
    servingSpinner.setMaxWidth(100); 
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
    GridPane newIngredientsGrid = new GridPane();
    newIngredientsGrid.setVgap(0); 
    newIngredientsGrid.setHgap(10);
    int rowIndex = 0;
    for (Ingredient ingredient : recipe.getIngredients()) {
      Ingredient adjustedIngredient
                = new Ingredient(ingredient.getName(),
                (float) (ingredient.getQuantity() * scaleFactor), ingredient.getMeasurementUnit());
        
      Text ingredientQuantity = new Text(String.format("%.1f", adjustedIngredient.getQuantity()));
      ingredientQuantity.setFont(Font.font("ROBOTO", 18));
      Text ingredientUnit = new Text(adjustedIngredient.getMeasurementUnit());
      ingredientUnit.setFont(Font.font("ROBOTO", 18));
      Text ingredientName = new Text(adjustedIngredient.getName());
      ingredientName.setFont(Font.font("ROBOTO", 18));
      newIngredientsGrid.add(ingredientQuantity, 0, rowIndex);
      newIngredientsGrid.add(ingredientUnit, 1, rowIndex);
      newIngredientsGrid.add(ingredientName, 2, rowIndex);
      rowIndex++;
    }
    // Replace the old ingredients GridPane with the new one
    int ingredientsGridIndex = vbox.getChildren().indexOf(ingredientsGrid);
    vbox.getChildren().remove(ingredientsGrid);
    vbox.getChildren().add(ingredientsGridIndex, newIngredientsGrid);
    ingredientsGrid = newIngredientsGrid;
  }

  /**Check if the given tag already exists.
   *
   * @param newTag the tag to check
   * @return true if the tag already exists, false otherwise
   */
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

  /**
   * Get a list of updated tags.
   */
  public ArrayList<String> getUpdatedTags() {
    ArrayList<String> updatedTags = new ArrayList<String>();
    for (Node node : tagsHBox.getChildren()) {
      if (node instanceof Text) {
        String tag = ((Text) node).getText().substring(2);
        updatedTags.add(tag);
      }
    }
    return updatedTags;
  }

  /** Create styled button with the given text and event handler.
   *
   * @param text is the text to display on the button
   * @param eventHandler is the event handler to execute when the button is clicked.
   * @return the created button
   */
  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color: #F2CC8F; -fx-text-fill: black;-fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setMinWidth(100); // Set the fixed width for each button
    button.setMaxWidth(Double.MAX_VALUE); // Ensure the button text is fully visible
    button.setOnAction(eventHandler);
    return button;
  }

  /**
   * Show an alert with the given alert type, title, and message.
   */
  private void showInlineStyledAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    // Set custom styles for the alert
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 18px; -fx-background-color: #F9F8F3; -fx-border-color: #F9F8F3;");
    // Set custom styles for the buttons
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> {
      button.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    });
    // Set custom styles for the content label
    Label contentLabel = (Label) dialogPane.lookup(".content");
    contentLabel.setStyle("-fx-text-fill: #3D405B;");
    alert.showAndWait();
  }

  private HBox createCommentPane(String text) {
    HBox commentPane = new HBox();
    commentPane.setSpacing(10);
    commentPane.setAlignment(Pos.CENTER_LEFT);
  
    Text commentText = new Text(text);
    commentText.setFont(Font.font("Roboto", 16));
    commentPane.getChildren().add(commentText);
  
    Button editButton = new Button("Edit");
    editButton.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
    editButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
    editButton.setOnAction(e -> {
      TextInputDialog editDialog = new TextInputDialog(commentText.getText());
      editDialog.setTitle("Edit Comment");
      editDialog.setHeaderText(null);
      editDialog.setContentText("Edit your comment:");
      Optional<String> result = editDialog.showAndWait();
      result.ifPresent(updatedText -> commentText.setText(updatedText));
    });
    commentPane.getChildren().add(editButton);
  
    Button deleteButton = new Button("Delete");
    deleteButton.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
    deleteButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
    deleteButton.setOnAction(e -> {
      commentPane.getChildren().clear();
      commentPane.setVisible(false);
    });
    commentPane.getChildren().add(deleteButton);
  
    return commentPane;
  }
  

}



