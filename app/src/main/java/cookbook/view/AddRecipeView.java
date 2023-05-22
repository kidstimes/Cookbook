package cookbook.view;

import cookbook.model.Ingredient;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * View for adding a recipe.
 */
public class AddRecipeView {
  private AddRecipeViewObserver observer;
  private BorderPane view;
  private TextField nameField;
  private TextField descField;
  private TextField ingredientNameField;
  private TextField quantityField;
  private TextField measureUnitField;
  private TextArea directionLineField;
  private TableView<Ingredient> ingredientsTable;
  private ListView<String> tagsList;
  private ListView<String> directionsList;
  private Label errorLabel;
  private String displayName;


  /**
   * Constructor for the add recipe view.
   */
  public AddRecipeView(String displayName) {
    this.view = new BorderPane();
    this.displayName = displayName;
    initLayout();
  }

  // Get the view of the add recipe view.
  public Node getView() {
    return this.view;
  }

  /**
   * Set the observer of the add recipe view.
   *
   * @param addRecipeViewObserver the observer
   */
  public void setObserver(AddRecipeViewObserver addRecipeViewObserver) {
    this.observer = addRecipeViewObserver;
  }


  /**
   * Initialize the add recipe view.
   */
  private void initLayout() {

    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(20);
    sidebar.setMaxWidth(120);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text title = new Text(displayName + ", welcome!");
    title.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(title);

    // Add five options to the homepage, one per row
    Button[] sidebarButtons = {
      createButton("Home Page", e -> {
        clearAllInput();
        observer.goToHomePage();
      }),
      createButton("Browse Recipes", e -> {
        clearAllInput();
        observer.goToBrowser();
      }),
      createButton("Add a Recipe", e -> {
        observer.goToAddRecipe();
      }),
      createButton("Weekly Dinner List", e -> {
        clearAllInput();
        observer.goToWeeklyDinner();
      }),
      createButton("My Favorites", e -> {
        clearAllInput();
        observer.goToMyFavorite();
      }),
      createButton("My Shopping List", e -> {
        clearAllInput();
        observer.goToShoppingList();
      }),
      createButton("Messages", e -> { 
        clearAllInput();
        observer.goToMessages();
      })
    };
    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);

    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 14));
    logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> {
      observer.userLogout();
    });
    sidebar.addButton("Browse Recipes", e -> {
      clearAllInput();
      observer.goToBrowser();
    });
    sidebar.addButton("Add a Recipe", e -> {
      clearAllInput();
      observer.goToAddRecipe();
    });
    sidebar.addButton("Weekly Dinner List", e -> {
      clearAllInput();
      observer.goToWeeklyDinner();
    });
    sidebar.addButton("My Favorites", e -> {
      clearAllInput();
      observer.goToMyFavorite();
    });
    sidebar.addButton("My Shopping List", e -> {
      clearAllInput();
      observer.goToShoppingList();
    });
    sidebar.addButton("Messages", e -> {
      clearAllInput();
      observer.goToMessages();
    });
    sidebar.addButton("My Account", e -> {
      clearAllInput();
      observer.goToAccount();
    });
    sidebar.addHyperlink("Help", e -> {
      clearAllInput();
      observer.goToHelp();
    });
    
    HBox logoutHelpBox = new HBox(10);
    logoutHelpBox.getChildren().addAll(logoutButton, hspacer, helpButton);
    logoutHelpBox.setAlignment(Pos.CENTER_LEFT);  
    
    sidebar.getChildren().add(logoutHelpBox); 
    view.setLeft(sidebar);


    VBox root = new VBox();
    root.setStyle("-fx-padding: 50px; -fx-background-color: #F9F8F3;");



    // Add Recipe title
    Label titleLabel = new Label("Add a Recipe");

    titleLabel.setStyle("-fx-text-fill: #69a486;-fx-font-size: 32;-fx-font-weight: bold;");
    titleLabel.setFont(Font.font("Roboto"));
    root.getChildren().add(titleLabel);

    // Error label
    errorLabel = new Label("");
    errorLabel.setStyle("-fx-text-fill: red;");
    errorLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 18));
    root.getChildren().add(errorLabel);

    // Recipe name
    Label nameLabel = new Label("Recipe Name");
    nameLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
    nameField = new TextField();
    nameField.setStyle("-fx-font-size: 18;");
    nameField.setMinHeight(30);
    root.getChildren().addAll(nameLabel, nameField);

    // Short description
    Label descLabel = new Label("Short Description");
    descLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
    descField = new TextField();
    descField.setStyle("-fx-font-size: 18;");
    descField.setMinHeight(30);
    root.getChildren().addAll(descLabel, descField);

    Label directionsLabel = new Label("Directions");
    directionsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 22));

    // Input for a single direction line
    directionLineField = new TextArea();
    directionLineField.setFont(Font.font("Roboto", 14));
    directionLineField.setWrapText(true);
    directionLineField.setPrefRowCount(5);
    directionLineField
        .setPromptText("Add a step. Each step of direction must be in a separated new line.");
    directionLineField.setStyle("-fx-border-color: transparent;-fx-border-style:none;");
    directionLineField.setMinHeight(50);
    directionLineField.setMinWidth(550);
    // Button to add a new direction line
    Button addDirectionLineButton = new Button("Add Direction");
    addDirectionLineButton.setStyle(
        "-fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
        + "-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addDirectionLineButton.setFont(Font.font("Roboto", 14));

    // ListView for displaying added direction lines and delete the selected
    // direction line


    Label directionsListLabel = new Label("Added directions");
    directionsListLabel.setFont(Font.font("Roboto", 14));
    directionsList = new ListView<>();
    directionsList.setMinHeight(60);

    Button deleteDirectionButton = new Button("Delete Direction");
    deleteDirectionButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
        + " 20;-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    deleteDirectionButton.setFont(Font.font("Roboto", 14));
    deleteDirectionButton.setOnAction(e -> {
      int selectedIndex = directionsList.getSelectionModel().getSelectedIndex();
      if (selectedIndex >= 0) {
        directionsList.getItems().remove(selectedIndex);
        ObservableList<String> updatedDirections = FXCollections.observableArrayList();
        int counter = 1;
        for (String direction : directionsList.getItems()) {
          updatedDirections.add(counter + ". " + direction.substring(direction.indexOf(" ") + 1));
          counter++;
        }
        directionsList.setItems(updatedDirections);
        errorLabel.setText("");
      } else {
        errorLabel.setText("Please select a direction to delete.");
      }
    });

    // Create an HBox to hold the directionsList and deleteDirectionButton
    VBox directionsContainer = new VBox(10);
    directionsContainer.getChildren().addAll(directionsListLabel);
    directionsContainer.getChildren().addAll(directionsList, deleteDirectionButton);


    ObservableList<String> directionLines = FXCollections.observableArrayList();
    directionsList.setItems(directionLines);
    directionsList.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(item);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item);
          setWrapText(true);
        }
      }
    });

    // Add event handler for add direction line button
    addDirectionLineButton.setOnAction(e -> {
      String directionLine = directionLineField.getText();
      if (!directionLine.isEmpty()) {
        int lineNumber = directionsList.getItems().size() + 1;
        directionsList.getItems().add(lineNumber + ". " + directionLine);
        directionLineField.clear();
      }
    });
    // Directions
    GridPane directionsGrid = new GridPane();
    directionsGrid.add(directionsLabel, 0, 0);
    directionsGrid.add(directionLineField, 0, 1);
    directionsGrid.add(addDirectionLineButton, 1, 1);
    directionsGrid.add(directionsContainer, 0, 2, 2, 1);
    directionsGrid.setAlignment(Pos.CENTER);
    GridPane.setHgrow(directionLineField, Priority.ALWAYS);
    GridPane.setHgrow(directionsList, Priority.ALWAYS);

    root.getChildren().add(directionsGrid);
    // add top margin to directionsGrid
    root.setMargin(directionsGrid, new Insets(15, 0, 0, 0));

    // Ingredients
    GridPane ingredientsInput = new GridPane();
    ingredientsInput.setHgap(10);
    ingredientsInput.setVgap(10);

    Label ingredientsLabel = new Label("Ingredients");
    ingredientsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 22));

    ingredientNameField = new TextField();
    ingredientNameField.setPromptText("Ingredient name");

    quantityField = new TextField();
    quantityField.setPromptText("Quantity");

    measureUnitField = new TextField();
    measureUnitField.setPromptText("Measure unit");

    Button addIngredientButton = new Button("Add Ingredient");
    addIngredientButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addIngredientButton.setFont(Font.font("Roboto", 14));

    ColumnConstraints col1 = new ColumnConstraints(250);
    ColumnConstraints col2 = new ColumnConstraints(70);
    ColumnConstraints col3 = new ColumnConstraints(90);
    ColumnConstraints col4 = new ColumnConstraints(150);

    ingredientsInput.getColumnConstraints().addAll(col1, col2, col3, col4);
    ingredientsInput.add(ingredientNameField, 0, 0);
    ingredientsInput.add(quantityField, 1, 0);
    ingredientsInput.add(measureUnitField, 2, 0);
    ingredientsInput.add(addIngredientButton, 3, 0);
    ingredientNameField.prefWidthProperty().bind(col1.prefWidthProperty());
    quantityField.prefWidthProperty().bind(col2.prefWidthProperty());
    measureUnitField.prefWidthProperty().bind(col3.prefWidthProperty());

    // Add event handler for add ingredient button
    addIngredientButton.setOnAction(e -> {
      String ingredientName = ingredientNameField.getText();
      String quantity = quantityField.getText();
      String measureUnit = measureUnitField.getText();

      if (ingredientName.isEmpty() || quantity.isEmpty() || measureUnit.isEmpty()) {
        errorLabel.setText("All ingredient fields must be filled in.");
      } else {
        try {
          float parsedQuantity = Float.parseFloat(quantity);
          Ingredient newIngredient = new Ingredient(ingredientName, parsedQuantity, measureUnit);
          ingredientsTable.getItems().add(newIngredient);
          // Clear fields
          ingredientNameField.clear();
          quantityField.clear();
          measureUnitField.clear();
          errorLabel.setText(""); // Clear the error label if there was an error before
        } catch (NumberFormatException ex) {
          errorLabel.setText("Quantity must be a number.");
        }
      }
    });

    ingredientsTable = new TableView<>();
    Button deleteIngredientButton = new Button("Delete Ingredient");
    deleteIngredientButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
             + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
             + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    deleteIngredientButton.setFont(Font.font("Roboto", 14));
    deleteIngredientButton.setOnAction(e -> {
      int selectedIndex = ingredientsTable.getSelectionModel().getSelectedIndex();
      if (selectedIndex >= 0) {
        ingredientsTable.getItems().remove(selectedIndex);
        errorLabel.setText("");
      } else {
        errorLabel.setText("Please select an ingredient to delete.");
      }
    });

    TableColumn<Ingredient, String> ingredientNameColumn = new TableColumn<>("Ingredient");
    ingredientNameColumn.setCellValueFactory(cellData 
        -> new SimpleStringProperty(cellData.getValue().getName()));
    TableColumn<Ingredient, String> quantityColumn = new TableColumn<>("Quantity");
    quantityColumn
        .setCellValueFactory(cellData 
            -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantity())));
    TableColumn<Ingredient, String> measureUnitColumn = new TableColumn<>("Measure Unit");
    measureUnitColumn
        .setCellValueFactory(cellData 
            -> new SimpleStringProperty(cellData.getValue().getMeasurementUnit()));
    List<TableColumn<Ingredient, ?>> columns = new ArrayList<>();
    columns.add(ingredientNameColumn);
    columns.add(quantityColumn);
    columns.add(measureUnitColumn);
    ingredientsTable.getColumns().setAll(columns);

    ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    ingredientsTable.setItems(ingredients);

    HBox ingredientsContainer = new HBox(10);
    ingredientsContainer.getChildren().addAll(ingredientsTable, deleteIngredientButton);
    ingredientsTable.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(ingredientsTable, Priority.ALWAYS);

    root.getChildren().addAll(ingredientsLabel, ingredientsInput, ingredientsContainer);
    root.setMargin(ingredientsLabel, new Insets(15, 0, 5, 0));

    // Tags
    Label tagsLabel = new Label("Tags");
    tagsLabel.setAlignment(Pos.CENTER_LEFT);
    tagsLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
    

    ComboBox<String> tagComboBox = new ComboBox<>();
    tagComboBox.setEditable(true);
    tagComboBox.setPromptText("Enter or Choose a Tag");

    // You can add existing tags to the combo box here
    ObservableList<String> existingTags 
        = FXCollections.observableArrayList("vegan", "vegetarian", "lactose free",
        "gluten free", "starter", "main course", "dessert and sweets");
    tagComboBox.setItems(existingTags);

    Button addTagButton = new Button("Add Tag");
    addTagButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
        + "-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addTagButton.setFont(Font.font("Roboto", 14));

    // Define tagslist
    tagsList = new ListView<>();
    ObservableList<String> tags = FXCollections.observableArrayList();
    tagsList.setItems(tags);

    // Add event handler for add tag button
    addTagButton.setOnAction(e -> {
      // Get tag from combo box
      String tag = tagComboBox.getEditor().getText();

      if (!tag.isEmpty() && !tagsList.getItems().contains(tag)) {
        tagsList.getItems().add(tag);
        tagComboBox.getEditor().clear();
      }
    });
    
    Button deleteTagButton = new Button("Delete Tag");
    deleteTagButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
          + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor: hand;" 
          + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    deleteTagButton.setFont(Font.font("Roboto", 14));
    deleteTagButton.setOnAction(e -> {
      int selectedIndex = tagsList.getSelectionModel().getSelectedIndex();
      if (selectedIndex >= 0) {
        tagsList.getItems().remove(selectedIndex);
        errorLabel.setText("");
      } else {
        errorLabel.setText("Please select a tag to delete.");
      }
    });
    HBox tagsInput = new HBox(5);
    HBox tagsListContainer = new HBox(5);
    tagsListContainer.getChildren().addAll(tagsList, deleteTagButton);
    tagsInput.getChildren().addAll(tagComboBox, addTagButton);
    root.getChildren().addAll(tagsLabel, tagsInput, tagsListContainer);

    // Save Recipe button
    Button saveButton = new Button("Save Recipe");
    saveButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white;"
            + " -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
            + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    saveButton.setFont(Font.font("Roboto", 18));
    saveButton.setOnAction(e -> {
      // Clear error label
      errorLabel.setText("");

      String name = nameField.getText();
      String description = descField.getText();
      ObservableList<String> directions = directionsList.getItems();
      ObservableList<Ingredient> ingredientsList = ingredientsTable.getItems();
      ObservableList<String> tagsListItems = tagsList.getItems();

      // Check if any input is empty
      if (name.isEmpty() || description.isEmpty() || directions.isEmpty() 
          || ingredientsList.isEmpty() || tagsListItems.isEmpty()) {
        errorLabel.setText("All fields must be filled in.");
      } else {
        if (observer != null) {
          StringBuilder directionsBuilder = new StringBuilder();
          for (String line : directions) {
            directionsBuilder.append(line).append(System.lineSeparator());
          }
          String directionsText = directionsBuilder.toString().trim();

          ArrayList<String[]> ingredientsData = new ArrayList<>();
          for (Ingredient ingredient : ingredientsList) {
            ingredientsData.add(new String[] { ingredient.getName(),
                    String.valueOf(ingredient.getQuantity()),
                ingredient.getMeasurementUnit() });
          }

          ArrayList<String> tagsData = new ArrayList<>(tagsListItems);

          String[] recipeData = new String[] { name, description, directionsText };
          if (observer.handleSaveRecipeClicked(recipeData, ingredientsData, tagsData)) {
            showInlineStyledAlert(Alert.AlertType.INFORMATION, "Success",
                String.format("Recipe %s saved successfully to cookbook.", recipeData[0]));
            clearAllInput();
          }
        }

        // Clear all fields
        clearAllInput();
      }
    });
    root.getChildren().add(saveButton);
    root.setMargin(saveButton, new Insets(15, 0, 0, 0));

    // Wrap the rootVBox in a ScrollPane so that the content can be scrolled
    ScrollPane scrollPane = new ScrollPane(root);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    // Set view
    view.setCenter(scrollPane);
  }

  // Clear all input fields
  private void clearAllInput() {
    nameField.clear();
    descField.clear();
    directionLineField.clear();
    directionsList.getItems().clear();
    ingredientNameField.clear();
    quantityField.clear();
    measureUnitField.clear();
    ingredientsTable.getItems().clear();
    tagsList.getItems().clear();
  }

  /**
   * Show an alert with the given alert type, title, and message.
   */
  public void showInlineStyledAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    // Set custom styles for the alert
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 18px; -fx-background-color: "
        + "#F9F8F3; -fx-border-color: #F9F8F3;");
    // Set custom styles for the buttons
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> {
      button.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white; "
          + "-fx-padding: 5 10 5 10;");
    });
    // Set custom styles for the content label
    Label contentLabel = (Label) dialogPane.lookup(".content");
    contentLabel.setStyle("-fx-text-fill: #3D405B;");
    alert.showAndWait();
  }

}