package cookbook.view;

import cookbook.model.Ingredient;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.DoubleBinding;
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
    Sidebar sidebar = new Sidebar(displayName);
    sidebar.addButton("Home", e -> {
      clearAllInput();
      observer.goToHomePage();
    }, "/images/home.png");
    sidebar.addButton("All Recipes", e -> {
      clearAllInput();
      observer.goToBrowser();
    }, "/images/recipe.png");
    sidebar.addButton("Add a Recipe", e -> {
      clearAllInput();
      observer.goToAddRecipe();
    }, "/images/add.png");
    sidebar.addButton("Weekly Dinner List", e -> {
      clearAllInput();
      observer.goToWeeklyDinner();
    }, "/images/weekly.png");
    sidebar.addButton("My Favorites", e -> {
      clearAllInput();
      observer.goToMyFavorite();
    }, "/images/favorite.png");
    sidebar.addButton("My Shopping List", e -> {
      clearAllInput();
      observer.goToShoppingList();
    }, "/images/shoppinglist.png");
    sidebar.addButton("Messages", e -> {
      clearAllInput();
      observer.goToMessages();
    }, "/images/messages.png");
    sidebar.addButton("My Account", e -> {
      clearAllInput();
      observer.goToAccount();
    }, "/images/account.png");
    sidebar.addHyperlink("Help", e -> {
      clearAllInput();
      observer.goToHelp();
    });
    sidebar.addHyperlink("Log Out", e -> {
      clearAllInput();
      observer.userLogout();
    });

    sidebar.setActiveButton("Add a Recipe");
        
    // Add the sidebar to the view
    sidebar.finalizeLayout();
    view.setLeft(sidebar);


    VBox root = new VBox();
    root.setStyle("-fx-padding: 50px; -fx-background-color: #F9F8F3;");



    // Add Recipe title
    Label titleLabel = new Label("Add a Recipe");

    titleLabel.setStyle("-fx-text-fill: #3F6250;-fx-font-size: 32;-fx-font-weight: bold;"
        + "-fx-font-family: \"Roboto\";");
    root.getChildren().add(titleLabel);

    // Error label
    errorLabel = new Label("");
    errorLabel.setPadding(new Insets(5, 0, 0, 0));
    errorLabel.setStyle("-fx-text-fill: #E07A5F; -fx-font-size: 18;-fx-font-family: \"Roboto\";");
    root.getChildren().add(errorLabel);

    // Recipe name
    Label nameLabel = new Label("Recipe Name");
    nameLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
    nameLabel.setPadding(new Insets(10, 0, 10, 0));
    nameField = new TextField();
    nameField.setStyle("-fx-font-size: 18;-fx-font-family: \"Roboto\";");
    nameField.setMinHeight(35);
    nameField.setPadding(new Insets(5, 5, 5, 5));
    root.getChildren().addAll(nameLabel, nameField);

    // Short description
    Label descLabel = new Label("Short Description");
    descLabel.setPadding(new Insets(10, 10, 10, 10));
    descLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
    descField = new TextField();
    descField.setStyle("-fx-font-size: 18;-fx-font-family: \"Roboto\"");
    descField.setMinHeight(35);
    descField.setPadding(new Insets(5, 5, 5, 5));
    root.getChildren().addAll(descLabel, descField);

    Label directionsLabel = new Label("Directions");
    directionsLabel.setPadding(new Insets(10, 0, 10, 0));
    directionsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 22));

    // Input for a single direction line
    directionLineField = new TextArea();
    directionLineField.setStyle("-fx-font: 18px \"Roboto\"");
    directionLineField.setWrapText(true);
    directionLineField.setMinHeight(35);
    directionLineField
        .setPromptText("Add a step. Each step of direction must be in a separated new line.");


    // Button to add a new direction line
    Button addDirectionLineButton = new Button("Add Direction");
    addDirectionLineButton.setPadding(new Insets(5, 20, 5, 20));
    addDirectionLineButton.setStyle(
        "-fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
          + "-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 5 0 10;");
    addDirectionLineButton.setFont(Font.font("Roboto", 14));

    // ListView for displaying added direction lines and delete the selected
    // direction line

    directionsList = new ListView<>();
    directionsList.setMinHeight(120);
    directionsList.setStyle("-fx-font-size: 18;-fx-font-family: \"Roboto\";");
    directionsList.setPlaceholder(new Label("No direction added yet."));

    Button deleteDirectionButton = new Button("Delete Direction");
    deleteDirectionButton.setPadding(new Insets(5, 0, 5, 5));
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
    DoubleBinding listWidthBinding =
        directionLineField.widthProperty().subtract(deleteDirectionButton.getWidth());
    deleteDirectionButton.setMinWidth(130);
    directionsList.prefWidthProperty().bind(listWidthBinding);

    // Create an HBox to hold the directionsList and deleteDirectionButton
    HBox directionsBox = new HBox(5);
    directionsBox.getChildren().addAll(directionsList, deleteDirectionButton);
    VBox directionsContainer = new VBox(10);
    directionsContainer.getChildren().addAll(directionsBox);
    directionsContainer.setPadding(new Insets(10, 0, 10, 0));


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
    
    VBox.setMargin(directionsGrid, new Insets(15, 0, 10, 0));

    // Ingredients
    GridPane ingredientsInput = new GridPane();
    ingredientsInput.setHgap(10);
    ingredientsInput.setVgap(10);

    Label ingredientsLabel = new Label("Ingredients");
    ingredientsLabel.setPadding(new Insets(10, 0, 10, 0));
    ingredientsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 22));

    ingredientNameField = new TextField();
    ingredientNameField.setFont(Font.font("Roboto", 18));
    ingredientNameField.setPromptText("Ingredient name");
    ingredientNameField.setAlignment(Pos.CENTER_LEFT);


    quantityField = new TextField();
    quantityField.setFont(Font.font("Roboto", 18)); 
    quantityField.setPromptText("Quantity");
    quantityField.setAlignment(Pos.CENTER_LEFT);


    measureUnitField = new TextField();
    measureUnitField.setFont(Font.font("Roboto", 18));
    measureUnitField.setPromptText("Measure unit");
    measureUnitField.setAlignment(Pos.CENTER_LEFT);


    Button addIngredientButton = new Button("Add Ingredient");
    addIngredientButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill:"
        + " white; -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
        + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 5 0 10;");
    addIngredientButton.setFont(Font.font("Roboto", 14));
    addDirectionLineButton.setPadding(new Insets(5, 10, 20, 10));

    ColumnConstraints col1 = new ColumnConstraints(400);
    ColumnConstraints col2 = new ColumnConstraints(100);
    ColumnConstraints col3 = new ColumnConstraints(150);
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
    ingredientsTable.setEditable(true);
    ingredientsTable.setPrefHeight(120);
    ingredientsTable.setMinHeight(120);
    ingredientsTable.setPrefWidth(700);

    ingredientsTable.setPlaceholder(new Label("No ingredients added yet."));
    ingredientsTable.setStyle("-fx-font-size:18px; -fx-font-family: 'Roboto';");
    Button deleteIngredientButton = new Button("Delete Ingredient");
    deleteIngredientButton.setPadding(new Insets(5, 10, 5, 10));
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

    ingredientNameColumn.setStyle("-fx-font-size: 18px;-fx-text-fill: #3D405B;"
        + "-fx-alignment: CENTER;-fx-font-family: Roboto;");
    ingredientNameColumn.setPrefWidth(400);
    quantityColumn.setStyle("-fx-font-size: 18px;-fx-text-fill: #3D405B;"
        + "-fx-alignment: CENTER;-fx-font-family: Roboto;");
    quantityColumn.setPrefWidth(120);
    measureUnitColumn.setStyle("-fx-font-size: 18px;-fx-text-fill: #3D405B;"
        + "-fx-alignment: CENTER;-fx-font-family: Roboto;");
    measureUnitColumn.setPrefWidth(180);

    List<TableColumn<Ingredient, ?>> columns = new ArrayList<>();
    columns.add(ingredientNameColumn);
    columns.add(quantityColumn);
    columns.add(measureUnitColumn);
    ingredientsTable.getColumns().setAll(columns);

    ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    ingredientsTable.setItems(ingredients);
    ingredientsTable.setMaxWidth(850);

    VBox ingredientsInputContainer = new VBox(10);
    ingredientsInputContainer.getChildren().addAll(ingredientsInput);
    ingredientsInputContainer.setPadding(new Insets(0, 0, 10, 0));

    HBox ingredientsTableContainer = new HBox(10);
    ingredientsTableContainer.getChildren().addAll(ingredientsTable, deleteIngredientButton);
    ingredientsTableContainer.setPadding(new Insets(10, 0, 0, 0));

    root.setPadding(new Insets(20)); 
    root.getChildren().addAll(ingredientsLabel,
        ingredientsInputContainer, ingredientsTableContainer);
    VBox.setMargin(ingredientsLabel, new Insets(0, 0, 5, 0));

    // Tags
    Label tagsLabel = new Label("Tags");
    tagsLabel.setAlignment(Pos.CENTER_LEFT);
    tagsLabel.setPadding(new Insets(20, 0, 20, 0));
    tagsLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 22));
    

    ComboBox<String> tagComboBox = new ComboBox<>();
    tagComboBox.setEditable(true);
    tagComboBox.setPromptText("Enter or Choose a Tag");
    tagComboBox.setPrefWidth(300);
    tagComboBox.setStyle("-fx-font-size: 18px;-fx-font-family: Roboto;");

    ObservableList<String> existingTags 
        = FXCollections.observableArrayList("vegan", "vegetarian", "lactose free",
        "gluten free", "starter", "main course", "dessert and sweets");
    tagComboBox.setItems(existingTags);

    Button addTagButton = new Button("Add Tag");
    addTagButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius: 20;"
          + "-fx-effect: null;-fx-cursor: hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    addTagButton.setFont(Font.font("Roboto", 14));
    addTagButton.setPadding(new Insets(5, 10, 5, 10));

    // Define tagslist
    tagsList = new ListView<>();
    ObservableList<String> tags = FXCollections.observableArrayList();
    tagsList.setItems(tags);
    tagsList.setMinHeight(60);
    tagsList.setPrefWidth(300);
    tagsList.setStyle("-fx-font-size: 18px;-fx-font-family: Roboto;");

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
    tagsListContainer.setPadding(new Insets(5, 0, 5, 0));
    tagsListContainer.getChildren().addAll(tagsList, deleteTagButton);
    tagsInput.getChildren().addAll(tagComboBox, addTagButton);
    root.getChildren().addAll(tagsLabel, tagsInput, tagsListContainer);

    // Save Recipe button
    Button saveButton = new Button("Save Recipe");
    saveButton.setStyle("-fx-background-color: #3D405B; -fx-text-fill: white;"
            + " -fx-background-radius: 20;-fx-effect: null;-fx-cursor:" 
            + " hand; -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    saveButton.setFont(Font.font("Roboto", 18));
    saveButton.setPadding(new Insets(5, 0, 5, 0));
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
    VBox.setMargin(saveButton, new Insets(15, 0, 0, 0));

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
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 18px;"
        + " -fx-background-color: #F9F8F3; -fx-border-color: #F9F8F3;");
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> button.setStyle("-fx-background-color: #3D405B;"
        + " -fx-text-fill: white; -fx-padding: 5 10 5 10;"));
    Label contentLabel = (Label) dialogPane.lookup(".content");
    contentLabel.setStyle("-fx-text-fill: #3D405B;");
    alert.showAndWait();
  }

}