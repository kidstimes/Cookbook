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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * View for adding a new recipe to the cookbook.
 */
public class AddRecipeView {
  private AddRecipeViewObserver observer;
  private BorderPane view;
  private TextField nameField;
  private TextField descField;
  private TextField ingredientNameField;
  private TextField quantityField;
  private TextField measureUnitField;
  private TextField directionLineField;
  private TableView<Ingredient> ingredientsTable;
  private ListView<String> tagsList;
  private ListView<String> directionsList;
  private Label errorLabel;

  public AddRecipeView() {
    view = new BorderPane();
    initLayout();
  }

  public void setObserver(cookbook.view.AddRecipeViewObserver addRecipeViewObserver) {
    this.observer = addRecipeViewObserver;
  }

  public Node getView() {
    return view;
  }

  private void initLayout() {
    VBox root = new VBox(10);
    root.setPadding(new Insets(10));
    root.setAlignment(Pos.CENTER_LEFT);

    // Add Recipe title
    Label titleLabel = new Label("Add a Recipe");
    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
    titleLabel.setTextFill(Color.GREEN);
    HBox titleContainer = new HBox();
    titleContainer.setAlignment(Pos.CENTER);
    titleContainer.getChildren().add(titleLabel);
    root.getChildren().add(titleContainer);
    
    // Error label
    errorLabel = new Label("");
    errorLabel.setStyle("-fx-text-fill: red;");
    errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
    root.getChildren().add(errorLabel);

    // Back to Browser button
    Button backButton = new Button("Back to Recipe Browser");
    backButton.setAlignment(Pos.TOP_LEFT);
    backButton.setOnAction(e -> {
        if (observer != null) {
            observer.handleBackToBrowserClicked();
        }
        // Clear all fields
        nameField.clear();
        descField.clear();
        directionLineField.clear();
        directionsList.getItems().clear();
        ingredientNameField.clear();
        quantityField.clear();
        measureUnitField.clear();
        ingredientsTable.getItems().clear();
        tagsList.getItems().clear();
    });


    // Recipe name
    Label nameLabel = new Label("Recipe Name");
    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    nameLabel.setTextFill(Color.GREEN);
    nameField = new TextField();
    root.getChildren().addAll(nameLabel, nameField);

    // Short description
    Label descLabel = new Label("Short Description");
    descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    descLabel.setTextFill(Color.GREEN);
    descField = new TextField();
    root.getChildren().addAll(descLabel, descField);

    // Directions
    GridPane directionsGrid = new GridPane();
    directionsGrid.setHgap(10);
    directionsGrid.setVgap(5);
    directionsGrid.setPadding(new Insets(5, 5, 5, 5));

    Label directionsLabel = new Label("Directions");
    directionsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    directionsLabel.setTextFill(Color.GREEN);

    // Input for a single direction line
    directionLineField = new TextField();
    directionLineField.setPromptText("Add direction line. Each step of direction must be in a separated new line.");

    // Button to add a new direction line
    Button addDirectionLineButton = new Button("Add Direction");

    // ListView for displaying added direction lines and delete the selected direction line
    directionsList = new ListView<>();
    directionsList.setPrefHeight(400);
    Button deleteDirectionButton = new Button("Delete Direction");
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
    HBox directionsContainer = new HBox(10);
    directionsContainer.setAlignment(Pos.CENTER_LEFT);
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
          setMaxWidth(param.getWidth());
        }
      }
    });
    view.widthProperty().addListener((observable, oldValue, newValue) -> {
      directionsList.setPrefWidth(newValue.doubleValue() * 0.85); 
      directionLineField.setPrefWidth(newValue.doubleValue() * 0.7); 
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

    directionsGrid.add(directionsLabel, 0, 0);
    directionsGrid.add(directionLineField, 0, 1);
    directionsGrid.add(addDirectionLineButton, 1, 1);
    directionsGrid.add(directionsContainer, 0, 2, 2, 1);
    directionsGrid.setAlignment(Pos.CENTER);
    directionLineField.setMaxWidth(Double.MAX_VALUE);
    directionsList.setMaxWidth(Double.MAX_VALUE);
    GridPane.setHgrow(directionLineField, Priority.ALWAYS);
    GridPane.setHgrow(directionsList, Priority.ALWAYS);

    root.getChildren().add(directionsGrid);
    // Ingredients
    GridPane ingredientsInput = new GridPane();
    ingredientsInput.setHgap(5);

    Label ingredientsLabel = new Label("Ingredients");
    ingredientsLabel.setAlignment(Pos.CENTER_LEFT);
    ingredientsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    ingredientsLabel.setTextFill(Color.GREEN);

    ingredientNameField = new TextField();
    ingredientNameField.setPromptText("Ingredient name");
    quantityField = new TextField();
    quantityField.setPromptText("Quantity");
    measureUnitField = new TextField();
    measureUnitField.setPromptText("Measure unit");
    Button addIngredientButton = new Button("Add Ingredient");
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(40); // Adjust the percentage as needed
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(30); // Adjust the percentage as needed
    ColumnConstraints col3 = new ColumnConstraints();
    col3.setPercentWidth(20); // Adjust the percentage as needed
    ColumnConstraints col4 = new ColumnConstraints();
    col4.setPercentWidth(10); // Adjust the percentage as needed

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
    ingredientNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    TableColumn<Ingredient, String> quantityColumn = new TableColumn<>("Quantity");
    quantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantity())));
    TableColumn<Ingredient, String> measureUnitColumn = new TableColumn<>("Measure Unit");
    measureUnitColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeasurementUnit()));
    List<TableColumn<Ingredient, ?>> columns = new ArrayList<>();
    columns.add(ingredientNameColumn);
    columns.add(quantityColumn);
    columns.add(measureUnitColumn);
    ingredientsTable.getColumns().setAll(columns);

    ingredientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    ingredientsTable.setItems(ingredients);

    HBox ingredientsContainer = new HBox(10);
    ingredientsContainer.getChildren().addAll(ingredientsTable, deleteIngredientButton);
    ingredientsTable.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(ingredientsTable, Priority.ALWAYS);

    root.getChildren().addAll(ingredientsLabel, ingredientsInput, ingredientsContainer);

    // Tags
    Label tagsLabel = new Label("Tags");
    tagsLabel.setAlignment(Pos.CENTER_LEFT);
    tagsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    tagsLabel.setTextFill(Color.GREEN);
    HBox tagsInput = new HBox(5);

    ComboBox<String> tagComboBox = new ComboBox<>();
    tagComboBox.setEditable(true);
    tagComboBox.setPromptText("Enter or Choose a Tag");

    // You can add existing tags to the combo box here
    ObservableList<String> existingTags = FXCollections.observableArrayList("Vegan", "vegetarian", "lactose free", "gluten free", "starter", "main course", "dessert and sweets");
    tagComboBox.setItems(existingTags);

    Button addTagButton = new Button("Add Tag");

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
    deleteTagButton.setOnAction(e -> {
    int selectedIndex = tagsList.getSelectionModel().getSelectedIndex();
    if (selectedIndex >= 0) {
      tagsList.getItems().remove(selectedIndex);
      errorLabel.setText("");
    } else {
      errorLabel.setText("Please select a tag to delete.");
    }
    });
    HBox tagsListContainer = new HBox(5);
    tagsListContainer.getChildren().addAll(tagsList, deleteTagButton);
    tagsInput.getChildren().addAll(tagComboBox, addTagButton);
    root.getChildren().addAll(tagsLabel, tagsInput, tagsListContainer);

    // Save Recipe button
    Button saveButton = new Button("Save Recipe");
    saveButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    saveButton.setOnAction(e -> {
      // Clear error label
      errorLabel.setText("");

      String name = nameField.getText();
      String description = descField.getText();
      ObservableList<String> directions = directionsList.getItems();
      ObservableList<Ingredient> ingredientsList = ingredientsTable.getItems();
      ObservableList<String> tagsListItems = tagsList.getItems();

      // Check if any input is empty
      if (name.isEmpty() || description.isEmpty() || directions.isEmpty() || ingredientsList.isEmpty() || tagsListItems.isEmpty()) {
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
            ingredientsData.add(new String[]{ingredient.getName(), String.valueOf(ingredient.getQuantity()), ingredient.getMeasurementUnit()});
          }

          ArrayList<String> tagsData = new ArrayList<>(tagsListItems);

          String[] recipeData = new String[]{name, description, directionsText};
          observer.handleSaveRecipeClicked(recipeData, ingredientsData, tagsData);
        }

        // Clear all fields
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
    });
    root.getChildren().add(saveButton);

    // Wrap the root in a new BorderPane and center the content
    BorderPane wrapper = new BorderPane();
    wrapper.setCenter(root);

    // Set view
    view.setCenter(wrapper);
    view.setTop(backButton);

  }

}

