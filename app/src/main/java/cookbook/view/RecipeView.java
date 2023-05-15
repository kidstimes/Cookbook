package cookbook.view;

import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
import javafx.stage.Stage;

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

  /**
   * Recipe View Constructor.
   */
  public RecipeView(String displayName) {
    this.view = new BorderPane();
    this.initialServings = 2;
    this.displayName = displayName;
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
      createButton("My Shopping List", e -> observer.goToShoppingList()),
      createButton("Messages", e -> observer.goToMessages()),
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

    Button editButton = new Button("Edit Recipe");
    editButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    editButton.setMinWidth(150);
    editButton.setStyle(
        " -fx-background-color: white; -fx-text-fill: #3D405B; -fx-effect: null;-fx-cursor: hand;");
    editButton.setOnAction(e -> editRecipe());

    // Add a title (recipe name)
    Text title = new Text(recipe.getName());
    title.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));
    title.setWrappingWidth(800);
    HBox titleBox = new HBox();
    titleBox.setMaxWidth(800);

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
    titleBox.getChildren().addAll(starButton, title, editButton);
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
        if (observer.addRecipeToWeeklyDinner(selectedDate, recipe, weekNumber)) {
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
    tagsTitleBox.setPadding(new Insets(20, 0, 20, 0));
    vbox.getChildren().add(tagsTitleBox);

    // Display tags in a single line
    tagsHBox = new HBox(); 
    tagsHBox.setSpacing(10);
    LinkedHashSet<String> uniqueTags = new LinkedHashSet<>(recipe.getTags());
    for (String tag : uniqueTags) {
      Text tagText = new Text("# " + tag);
      tagText.setFont(Font.font("ROBOTO", FontPosture.ITALIC, 16));
      HBox tagContainer = new HBox(tagText);
      tagContainer.setSpacing(5);
      tagsHBox.getChildren().add(tagContainer);
    }
    vbox.getChildren().add(tagsHBox);

    // Add "Edit Tags" button to edit tags
    Button editTagsButton = new Button("Edit Tags");
    editTagsButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    editTagsButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    vbox.getChildren().add(editTagsButton);

    // Initialize newTagBox and saveButton but don't add them to vbox yet
    HBox newTagBox = new HBox();
    newTagBox.setSpacing(5); // Set spacing between ComboBox, TextField, and Button
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
    Button addTagButton = new Button("Add a New Tag");
    addTagButton.setFont(Font.font("Roboto", 16));
    //Set the style
    addTagButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addTagButton.setOnAction(event -> {
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
        // Add a
        // Add a delete button for each tag
        Button deleteTagButton = new Button("x");
        deleteTagButton.setFont(Font.font("Roboto", 12));
        //set the style
        deleteTagButton.setStyle(
            " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
        HBox tagContainer = new HBox(tagText, deleteTagButton);
        tagContainer.setSpacing(5);
        deleteTagButton.setOnAction(ev -> {
          tagsHBox.getChildren().remove(tagContainer);
        });
        tagsHBox.getChildren().add(tagContainer);
        newTagField.clear();
      }
    });
    newTagBox.getChildren().addAll(predefinedTagsComboBox, newTagField, addTagButton);
    // Add "Save Tags" button but don't add it to vbox yet
    saveButton = new Button("Save Tags");
    saveButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    //set savebutton style
    saveButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    editTagsButton.setOnAction(e -> {
      // Add the delete buttons to each existing tag
      for (Node node : tagsHBox.getChildren()) {
        HBox tagContainer = (HBox) node;
        if (tagContainer.getChildren().size() == 1) {
          Text tagText = (Text) tagContainer.getChildren().get(0);
          // Add a delete button for each tag
          Button deleteTagButton = new Button("x");
          deleteTagButton.setFont(Font.font("Roboto", 12));
          deleteTagButton.setStyle(
                " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
                + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
          deleteTagButton.setOnAction(event -> {
              tagsHBox.getChildren().remove(tagContainer);
          });
          tagContainer.getChildren().add(deleteTagButton);
        }
      }
      // Add the newTagBox and saveButton to vbox
      vbox.getChildren().addAll(newTagBox, saveButton);
      // Disable the editTagsButton so it can't be clicked again
      editTagsButton.setDisable(true);
    });
    saveButton.setOnAction(event -> {
      if (observer != null) {
        ArrayList<String> updatedTags = getUpdatedTags();
        observer.handleSaveTagsClicked(updatedTags, recipe);
        //clear all the added tags
        tagsHBox.getChildren().clear(); 
      }
    });
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
  /**
   * Check if the given tag already exists.
   *
   * @param newTag the tag to check
   * @return true if the tag already exists, false otherwise
   */
  private boolean tagAlreadyExists(String newTag) {
    for (Node node : tagsHBox.getChildren()) {
      if (node instanceof HBox) {
        HBox tagContainer = (HBox) node;
        String tag = ((Text) tagContainer.getChildren().get(0)).getText().substring(2);
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
      if (node instanceof HBox) {
        HBox tagContainer = (HBox) node;
        String tag = ((Text) tagContainer.getChildren().get(0)).getText().substring(2);
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

  /**
   * Method to edit the recipe in a new window with changes to name, 
   * description, directions and ingredients.
   */
  private void editRecipe() {
    Stage editRecipeStage = new Stage();
    editRecipeStage.setTitle("Edit Recipe");
  
    VBox editRecipeVBox = new VBox();
    editRecipeVBox.setSpacing(10);
    editRecipeVBox.setPadding(new Insets(10));
    
    // ScrollPane for the editRecipeVBox
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setPadding(new Insets(10));

    // Create error label for displaying error messages
    Label errorLabel = new Label();
    errorLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    errorLabel.setTextFill(Color.RED);

    // Create a new HBox to hold the recipe name
    HBox recipeNameHBox = new HBox();
    recipeNameHBox.setSpacing(10);
    recipeNameHBox.setAlignment(Pos.CENTER_LEFT);
    Text recipeNameLabel = new Text("Recipe Name:");
    recipeNameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    TextField recipeNameField = new TextField(recipe.getName());
    recipeNameField.setFont(Font.font("Roboto", 18));
    recipeNameHBox.getChildren().addAll(recipeNameLabel, recipeNameField);
    editRecipeVBox.getChildren().add(recipeNameHBox);

    // Create a new HBox to hold the recipe short description
    HBox recipeShortDescHBox = new HBox();
    recipeShortDescHBox.setSpacing(10);
    recipeShortDescHBox.setAlignment(Pos.CENTER_LEFT);
    Text recipeShortDescLabel = new Text("Short Description:");
    recipeShortDescLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    TextField recipeShortDescField = new TextField(recipe.getShortDesc());
    recipeShortDescField.setFont(Font.font("Roboto", 18));
    recipeShortDescHBox.getChildren().addAll(recipeShortDescLabel, recipeShortDescField);
    editRecipeVBox.getChildren().add(recipeShortDescHBox);
    // Create a new VBox to hold the recipe ingredients
    VBox recipeIngredientsVBox = new VBox();
    recipeIngredientsVBox.setSpacing(10);
    Text ingredientsLabel = new Text("Ingredients:");
    ingredientsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    recipeIngredientsVBox.getChildren().add(ingredientsLabel);
    for (Ingredient ingredient : recipe.getIngredients()) {
      HBox ingredientHBox = new HBox();
      ingredientHBox.setSpacing(10);
      TextField ingredientQuantity = new TextField(String.format("%.1f", ingredient.getQuantity()));
      ingredientQuantity.setFont(Font.font("ROBOTO", 18));
      ingredientQuantity.setPrefWidth(100);
      TextField ingredientUnit = new TextField(ingredient.getMeasurementUnit());
      ingredientUnit.setFont(Font.font("ROBOTO", 18));
      ingredientUnit.setPrefWidth(100);
      TextField ingredientName = new TextField(ingredient.getName());
      ingredientName.setFont(Font.font("ROBOTO", 18));
      ingredientName.setPrefWidth(200);
      Button deleteIngredientButton = new Button("Delete");
      deleteIngredientButton.setOnAction(deleteEvent -> {
        recipeIngredientsVBox.getChildren().remove(ingredientHBox);
      });
      ingredientHBox.getChildren().addAll(ingredientQuantity, ingredientUnit, ingredientName, deleteIngredientButton);
      recipeIngredientsVBox.getChildren().add(ingredientHBox);
    }
    editRecipeVBox.getChildren().add(recipeIngredientsVBox);
    // Create a new HBox to add a new ingredient
    HBox addIngredientHBox = new HBox();
    addIngredientHBox.setSpacing(10);
    TextField newIngredientQuantity = new TextField();
    newIngredientQuantity.setPromptText("Quantity");
    newIngredientQuantity.setPrefWidth(100);
    TextField newIngredientUnit = new TextField();
    newIngredientUnit.setPromptText("Unit");
    newIngredientUnit.setPrefWidth(100);
    TextField newIngredientName = new TextField();
    newIngredientName.setPromptText("Name");
    newIngredientName.setPrefWidth(200);

    Button addIngredientButton = new Button("Add Ingredient");
    addIngredientButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    addIngredientButton.setOnAction(e -> {
      if (!newIngredientQuantity.getText().isEmpty() && !newIngredientUnit.getText().isEmpty() && !newIngredientName.getText().isEmpty()) {
        try {
          Float.parseFloat(newIngredientQuantity.getText());
          HBox ingredientHBox = new HBox();
          ingredientHBox.setSpacing(10);      
          TextField ingredientQuantity = new TextField(newIngredientQuantity.getText());
          ingredientQuantity.setPrefWidth(100);          
          TextField ingredientUnit = new TextField(newIngredientUnit.getText());
          ingredientUnit.setPrefWidth(100);       
          TextField ingredientName = new TextField(newIngredientName.getText());
          ingredientName.setPrefWidth(200);      
          Button deleteIngredientButton = new Button("Delete");
          deleteIngredientButton.setStyle("-fx-background-color: white; -fx-text-fill:#3D405B ; -fx-padding: 5 10 5 10;");
          deleteIngredientButton.setOnAction(deleteEvent -> {
            recipeIngredientsVBox.getChildren().remove(ingredientHBox);
          });
          ingredientHBox.getChildren().addAll(ingredientQuantity, ingredientUnit, ingredientName, deleteIngredientButton);
          recipeIngredientsVBox.getChildren().add(ingredientHBox);
          newIngredientQuantity.clear();
          newIngredientUnit.clear();
          newIngredientName.clear();
          errorLabel.setText("");
        } catch (NumberFormatException nfe) {
          errorLabel.setText("Ingredient quantity must be a number.");
        }
      } else {
        errorLabel.setText("All fields must be filled in to add an ingredient.");
      }
    });
    addIngredientHBox.getChildren().addAll(newIngredientQuantity, newIngredientUnit, newIngredientName, addIngredientButton);
    editRecipeVBox.getChildren().add(addIngredientHBox);
    // Create a new HBox to hold the recipe instructions
    VBox recipeInstructionsvBox = new VBox();
    recipeInstructionsvBox.setSpacing(10);
    recipeInstructionsvBox.setMaxHeight(200);
    recipeInstructionsvBox.setAlignment(Pos.CENTER_LEFT);
    Text recipeInstructionsLabel = new Text("Instructions:");
    recipeInstructionsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    ListView<Label> directionsListView = new ListView<Label>();
    ObservableList<Label> directionsList = FXCollections.observableArrayList();
    for (String direction : recipe.getDirections().split("\n")) {
      directionsList.add(new Label(direction.trim()));
    }
    directionsListView.setItems(directionsList);
    // Add, modify, and delete directions
    Button addDirectionButton = new Button("Add Direction");
    addDirectionButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    Button modifyDirectionButton = new Button("Modify Direction");
    modifyDirectionButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    Button deleteDirectionButton = new Button("Delete Direction");
    deleteDirectionButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    TextField newDirectionField = new TextField();
    addDirectionButton.setOnAction(e -> {
      if (!newDirectionField.getText().isEmpty()) {
        directionsList.add(new Label((directionsList.size() + 1) + ". " + newDirectionField.getText()));
        newDirectionField.clear();
      }
    });
    modifyDirectionButton.setOnAction(e -> {
      int selectedIndex = directionsListView.getSelectionModel().getSelectedIndex();
      if (selectedIndex != -1 && !newDirectionField.getText().isEmpty()) {
        directionsList.set(selectedIndex, new Label((selectedIndex + 1) + ". " + newDirectionField.getText()));
        newDirectionField.clear();
      }
    });
    deleteDirectionButton.setOnAction(e -> {
      int selectedIndex = directionsListView.getSelectionModel().getSelectedIndex();
      if (selectedIndex != -1) {
        directionsList.remove(selectedIndex);
        updateDirectionNumbers(directionsList);
      }
    });
    HBox directionButtonshBox = new HBox(addDirectionButton, modifyDirectionButton, deleteDirectionButton);
    directionButtonshBox.setSpacing(10);
    recipeInstructionsvBox.getChildren().addAll(recipeInstructionsLabel, directionsListView, newDirectionField, directionButtonshBox);
    editRecipeVBox.getChildren().add(recipeInstructionsvBox);
    // Collect all the info from the fields and create a new recipe
    // Collect all the info from the fields and create a new recipe
    Button editRecipeButton = new Button("Save Edited Recipe");
    editRecipeButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    editRecipeButton.setOnAction(e -> {
      boolean hasError = false;
      String errorMessage = "";
      // Check if all fields are filled
      if (recipeNameField.getText().isEmpty() || recipeShortDescField.getText().isEmpty() ||
          recipeIngredientsVBox.getChildren().isEmpty() || directionsList.isEmpty()) {
        hasError = true;
        errorMessage = "All fields must be filled before saving.";
      }
      ArrayList<String[]> newIngredients = new ArrayList<>();
      for (Node ingredientHBox : recipeIngredientsVBox.getChildren()) {
        if (ingredientHBox instanceof HBox) {
          HBox hBox = (HBox) ingredientHBox;
          try {
            float quantity = Float.parseFloat(((TextField) hBox.getChildren().get(0)).getText().replace(",", "."));
            String unit = ((TextField) hBox.getChildren().get(1)).getText();
            String name = ((TextField) hBox.getChildren().get(2)).getText();
            newIngredients.add(new String[]{name, Float.toString(quantity), unit});
          } catch (NumberFormatException ex) {
            hasError = true;
            errorMessage = "Ingredient quantity must be a number.";
          }
        }
      }
      if (!hasError) {
        String newName = recipeNameField.getText();
        String newDescription = recipeShortDescField.getText();
        String newInstructions = directionsList.stream()
            .map(Label::getText).collect(Collectors.joining("\n"));
        observer.editRecipe(recipe, newName, newDescription, newInstructions, newIngredients);
        observer.goToRecipe(recipe);
        editRecipeStage.close();
      } else {
        errorLabel.setText(errorMessage);
      }
    });
    editRecipeVBox.getChildren().add(editRecipeButton);
    editRecipeVBox.getChildren().add(errorLabel);
    scrollPane.setContent(editRecipeVBox);
    // Set the scene and show the stage
    Scene editRecipeScene = new Scene(scrollPane);
    editRecipeStage.setScene(editRecipeScene);
    editRecipeStage.showAndWait();
  }

  // Method to update direction numbers
  private void updateDirectionNumbers(ObservableList<Label> directionsList) {
    for (int i = 0; i < directionsList.size(); i++) {
      String currentText = directionsList.get(i).getText();
      currentText = currentText.substring(currentText.indexOf(".") + 1).trim();
      directionsList.get(i).setText((i + 1) + ". " + currentText);
    }
  }


}



