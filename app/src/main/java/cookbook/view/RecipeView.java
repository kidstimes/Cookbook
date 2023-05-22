package cookbook.view;

import cookbook.model.Comment;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.RecipeEditRecord;
import cookbook.model.User;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * View class for the recipe page.
 */
public class RecipeView {
  private RecipeViewObserver observer;
  private BorderPane view;
  private Recipe recipe;
  private VBox vbox;
  private GridPane ingredientsGrid;
  private HBox tagsHbox;
  private Spinner<Integer> servingSpinner;
  private int initialServings;
  private Button saveButton;
  private String displayName;
  private int userId;
  private ArrayList<Comment> comments;
  private ArrayList<User> loggedoutUsers;

  /**
   * Recipe View Constructor.
   */
  public RecipeView(String displayName, int userId, ArrayList<User> loggedoutUsers) {
    this.view = new BorderPane();
    this.initialServings = 2;
    this.displayName = displayName;
    this.userId = userId;
    this.loggedoutUsers = loggedoutUsers;
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
  }

  /**
   * Set the comments of the recipe view.
   */
  public void setComments(ArrayList<Comment> comments) {
    this.comments = comments;
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
    
    sidebar.setActiveButton("Browse Recipes");
    sidebar.finalizeLayout();
        
    // Add the sidebar to the view
    view.setLeft(sidebar);

    vbox = new VBox();
    vbox.setStyle("-fx-padding: 50px;-fx-background-color: #F9F8F3;");
    // Add spacing between sections in the VBox
    vbox.setSpacing(10);
    view.setCenter(vbox);
    // Add a scroll pane
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vbox);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    view.setCenter(scrollPane);


    // Add a title (recipe name)
    Label title = new Label(recipe.getName());
    System.out.println(recipe);
    title.setFont(Font.font("ARIAL", FontWeight.BOLD, 36));
    title.setStyle("-fx-text-fill: #69a486;");
    
    title.setMaxWidth(800);
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
    titleBox.getChildren().addAll(starButton, title);
    vbox.getChildren().add(titleBox);

    // Add short description with italics
    Text shortDescription = new Text(recipe.getShortDesc());
    shortDescription.setFont(Font.font("ARIAL", FontPosture.ITALIC, 28));
    shortDescription.setFill(Color.DARKSLATEGREY);
    shortDescription.setWrappingWidth(900);
    VBox shortDescriptionBox = new VBox(shortDescription);
    shortDescriptionBox.setStyle("-fx-text-fill: #69a486;");
    shortDescriptionBox.setMaxWidth(900);
    shortDescriptionBox.setPadding(new Insets(10, 0, 30, 0));
    vbox.getChildren().add(shortDescriptionBox);


    // Add DatePicker for selecting the date to add the recipe to the weekly dinner
    // list
    DatePicker datePicker = new DatePicker();
    // write select a date for user to select a date
    Text selectDate = new Text("Select a date: ");
    selectDate.setFont(Font.font("ROBOTO", FontWeight.BOLD, 18));
    selectDate.setFill(Color.DARKSLATEGREY);
    HBox dateBox = new HBox(selectDate, datePicker);
    dateBox.setSpacing(0);
    dateBox.setAlignment(Pos.CENTER_LEFT);
    dateBox.setPadding(new Insets(10, 0, 0, 0));
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
            + " 20;-fx-effect: null;-fx-cursor: hand;"
            + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
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
        // clear the date picker
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
    ingredientsTitleBox.setPadding(new Insets(30, 0, 0, 0));
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
    directionsTitleBox.setPadding(new Insets(30, 0, 0, 0));
    vbox.getChildren().add(directionsTitleBox);
    VBox directionsBox = new VBox(directions);
    directionsBox.setMaxWidth(900);
    vbox.getChildren().add(directionsBox);

    // Display tags
    Text tagsTitle = new Text("Tags:");
    tagsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    HBox tagsTitleBox = new HBox(tagsTitle);
    tagsTitleBox.setPadding(new Insets(40, 0, 0, 0));

    // Display tags in a single line
    tagsHbox = new HBox();
    tagsHbox.setSpacing(5);
    LinkedHashSet<String> uniqueTags = new LinkedHashSet<>(recipe.getTags());
    for (String tag : uniqueTags) {
      Text tagText = new Text("# " + tag);
      tagText.setFont(Font.font("ROBOTO", FontPosture.ITALIC, 16));
      HBox tagContainer = new HBox(tagText);
      tagContainer.setSpacing(5);
      tagsHbox.getChildren().add(tagContainer);
    }

    // Add "Edit Tags" button to edit tags
    Button editTagsButton = new Button("Edit Tags");
    editTagsButton.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
    editTagsButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand;"
            + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");

    // Create a separate VBox for tags
    VBox tagsVbox = new VBox();
    tagsVbox.setSpacing(15);

    // Add tags related components to tagsVBox instead of vbox
    tagsVbox.getChildren().add(tagsTitleBox);
    tagsVbox.getChildren().add(tagsHbox);
    tagsVbox.getChildren().add(editTagsButton);

    // Then add the tagsVBox to the main vbox
    vbox.getChildren().add(tagsVbox);

    HBox newTagBox = new HBox();
    newTagBox.setSpacing(10); // Set spacing between ComboBox, TextField, and Button
    // Add ComboBox for predefined tags
    ComboBox<String> predefinedTagsComboBox = new ComboBox<>();
    predefinedTagsComboBox.getItems().addAll(
        "vegetarian", "vegan", "lactose free", "gluten free",
        "starter", "main course", "dessert and sweets");
    predefinedTagsComboBox.setPromptText("Choose a predefined tag...");
    predefinedTagsComboBox.setStyle("-fx-font: 16px \"Roboto\";");
    // Add new tag input field
    TextField newTagField = new TextField();
    newTagField.setPromptText("Or enter a new tag...");
    newTagField.setStyle("-fx-font: 16px \"Roboto\";");
    // Add button to add the new tag
    Button addTagButton = new Button("Add a New Tag");
    addTagButton.setFont(Font.font("Roboto", 16));
    // Set the style
    addTagButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand;"
            + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
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
        // set the style
        deleteTagButton.setStyle(
            " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
                + " 20;-fx-effect: null;-fx-cursor: hand;"
                + " -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
        HBox tagContainer = new HBox(tagText, deleteTagButton);
        tagContainer.setSpacing(5);
        deleteTagButton.setOnAction(ev -> {
          tagsHbox.getChildren().remove(tagContainer);
        });
        tagsHbox.getChildren().add(tagContainer);
        newTagField.clear();
      }
    });
    newTagBox.getChildren().addAll(predefinedTagsComboBox, newTagField, addTagButton);
    // Add "Save Tags" button but don't add it to vbox yet
    saveButton = new Button("Save Tags");
    saveButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    // set savebutton style
    saveButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand;"
            + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    editTagsButton.setOnAction(e -> {
      // Add the delete buttons to each existing tag
      for (Node node : tagsHbox.getChildren()) {
        HBox tagContainer = (HBox) node;
        if (tagContainer.getChildren().size() == 1) {
          // Add a delete button for each tag
          Button deleteTagButton = new Button("x");
          deleteTagButton.setFont(Font.font("Roboto", 12));
          deleteTagButton.setStyle(
              " -fx-background-color: #3D405B; -fx-text-fill: white;"
                  + " -fx-background-radius: 20; -fx-effect: null;-fx-cursor: hand;"
                  + " -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
          deleteTagButton.setOnAction(event -> {
            tagsHbox.getChildren().remove(tagContainer);
          });
          tagContainer.getChildren().add(deleteTagButton);
        }
      }
      tagsVbox.getChildren().addAll(newTagBox, saveButton);
      // Disable the editTagsButton so it can't be clicked again
      editTagsButton.setDisable(true);
    });
    saveButton.setOnAction(event -> {
      if (observer != null) {
        ArrayList<String> updatedTags = getUpdatedTags();
        observer.handleSaveTagsClicked(updatedTags, recipe);
        // clear all the added tags
        tagsHbox.getChildren().clear();
      }
    });

    // Display comments
    Text commentsTitle = new Text("Comments:");
    commentsTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    VBox commentsTitleBox = new VBox(commentsTitle);
    commentsTitleBox.setPadding(new Insets(40, 0, 0, 0));
    vbox.getChildren().add(commentsTitleBox);

    // Create a container for displaying comments
    VBox commentsContainer = new VBox();
    commentsContainer.setSpacing(10);
    vbox.getChildren().add(commentsContainer);

    for (Comment comment : comments) {
      HBox commentPane = createCommentPane(comment.getId(),
          comment.getUserId(), comment.getDisplayName(), comment.getText());
      commentsContainer.getChildren().add(commentPane);
    }
    // Create a text area for users to input their comments
    TextField commentInput = new TextField();
    commentInput.setPromptText("Write a comment...");
    commentInput.setStyle("-fx-font: 16px \"Roboto\";");
    commentInput.setMaxWidth(900);
    commentInput.setMaxHeight(100);
    vbox.getChildren().add(commentInput);

    Button postCommentButton = new Button("Post Comment");
    postCommentButton.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
    postCommentButton.setMinWidth(100);
    postCommentButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand;"
            + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    postCommentButton.setOnAction(e -> {
      String commentText = commentInput.getText().trim();
      if (!commentText.isEmpty()) {
        observer.addComment(recipe, commentText);
        commentInput.clear();
        observer.goToRecipe(recipe);
      }
    });
    vbox.getChildren().add(postCommentButton);

    
    Text sendTitle = new Text("Send Recipe:");
    sendTitle.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    VBox sendTitleBox = new VBox(sendTitle);
    sendTitleBox.setPadding(new Insets(40, 0, 0, 0));
    vbox.getChildren().add(sendTitleBox);


    // Create TextField for user's message input
    TextField messageInputField = new TextField();
    messageInputField.setPromptText("Write your message here...");
    messageInputField.setStyle("-fx-font: 16px \"Roboto\";");
    messageInputField.setMaxWidth(900);
    messageInputField.setMaxHeight(100);

    // Create ComboBox to list all users
    ComboBox<User> userSelectionComboBox = new ComboBox<>();

    User defaultUser = new User(-1, "Select a user", "");
    userSelectionComboBox.getItems().add(defaultUser);
    //add arraylist of loggedoutusers to the combobox
    for (User user : loggedoutUsers) {
      userSelectionComboBox.getItems().add(user);
    }

    userSelectionComboBox.getSelectionModel().select(defaultUser);
    userSelectionComboBox.setPromptText("Select a user");
    Button sendButton = new Button("Send");

    userSelectionComboBox.setConverter(new StringConverter<User>() {
      @Override
      public String toString(User user) {
          return user.getUsername();
      }
  
      @Override
      public User fromString(String string) {
          // Not needed
          return null;
      }
    });
  
    sendButton.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
    sendButton.setMinWidth(100);
    sendButton.setPadding(new Insets(0, 0, 0, 50));
    sendButton.setStyle(
        " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
            + " 20;-fx-effect: null;-fx-cursor: hand;"
            + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    sendButton.setOnAction(e -> {
      User selectedUser = userSelectionComboBox.getSelectionModel().getSelectedItem();
      if (selectedUser.equals(defaultUser)) {
        return;
      }
      String message = messageInputField.getText();
      if (selectedUser != null) {
        if (observer.sendMessageToUser(selectedUser.getUsername(), recipe, message)) {
          showInlineStyledAlert(Alert.AlertType.INFORMATION, "Success",
              "Message sent successfully.");
        } else {
          showInlineStyledAlert(Alert.AlertType.ERROR, "Error", "Message failed to send.");
        }
        messageInputField.clear();
        userSelectionComboBox.getSelectionModel().clearSelection();
        userSelectionComboBox.getSelectionModel().select(defaultUser);
      } else {
        showInlineStyledAlert(Alert.AlertType.WARNING, "Warning", "Please select a user.");
      }
    });

    VBox messageBox = new VBox();
    messageBox.setSpacing(10);
    messageBox.getChildren().addAll(userSelectionComboBox, messageInputField, sendButton);
    vbox.getChildren().add(messageBox);

    Label recipeHistoryLabel = new Label("History:");
    recipeHistoryLabel.setPadding(new Insets(30, 0, 0, 0));
    recipeHistoryLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 24));
    String createrDisplayname = observer.getDisplayNameByUsername(recipe.getCreaterUsername());
    Label createrLabel = new Label("Recipe created by " + createrDisplayname + " on " + recipe.getCreationDate());
    createrLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 18));
    vbox.getChildren().addAll(recipeHistoryLabel, createrLabel);
    if (recipe.getEditRecords() != null) {
      for (RecipeEditRecord record : recipe.getEditRecords()) {
        Label editLabel = new Label();
        String editorDisplayname = observer.getDisplayNameByUsername(record.getUserName());
        editLabel.setText("Edited by " + editorDisplayname + " on " + record.getDate());
        editLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 18));
        vbox.getChildren().add(editLabel);
      }
    } else if (recipe.getEditRecords() == null) {
      Label noEditLabel = new Label("No edits have been made to this recipe.");
      noEditLabel.setFont(Font.font("ROBOTO", FontWeight.BOLD, 18));
      vbox.getChildren().add(noEditLabel);
    }
    Button editButton = new Button("Edit Recipe");
    editButton.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
    editButton.setPadding(new Insets(10, 0, 0, 50));
    editButton.setMinWidth(150);
    editButton.setStyle(
      " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
          + " 20;-fx-effect: null;-fx-cursor: hand;"
          + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    editButton.setOnAction(e -> editRecipe());
    vbox.getChildren().add(editButton);

  }

  /**
   * Creates a pane for displaying comment.
   *
   * @param commentId          is the id of the comment
   * @param commentUserId      is the id of the user who posted the comment
   * @param commentDisplayName is the display name of the user who posted the
   *                           comment
   * @param text               is the text of the comment
   * @return a pane for displaying comment
   */
  private HBox createCommentPane(int commentId, int commentUserId,
      String commentDisplayName, String text) {
    HBox commentPane = new HBox();
    commentPane.setSpacing(10);
    commentPane.setAlignment(Pos.CENTER_LEFT);

    Text userNameText = new Text(commentDisplayName + ": ");
    userNameText.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
    commentPane.getChildren().add(userNameText);

    Text commentText = new Text(text);
    commentText.setFont(Font.font("Roboto", 16));
    commentPane.getChildren().add(commentText);

    if (commentUserId == userId) {
      Button editCommentButton = new Button("Edit");
      editCommentButton.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
      editCommentButton.setStyle(
          " -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
              + " 20;-fx-effect: null;-fx-cursor: hand;"
              + " -fx-padding: 0 5 0 5; -fx-margin: 0 0 0 10;");
      editCommentButton.setOnAction(e -> {
        TextInputDialog editDialog = new TextInputDialog(commentText.getText());
        editDialog.setTitle("Edit Comment");
        editDialog.setHeaderText(null);
        editDialog.setContentText("Edit your comment:");
        Optional<String> result = editDialog.showAndWait();
        result.ifPresent(updatedText -> {
          commentText.setText(updatedText);
          observer.updateComment(commentId, updatedText);
        });
      });
      commentPane.getChildren().add(editCommentButton);

      Button deleteButton = new Button("Delete");
      deleteButton.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
      deleteButton.setStyle("-fx-font: 12px \"Roboto\";"
          + " -fx-background-color: white; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
      deleteButton.setOnAction(e -> {
        observer.deleteComment(commentId);
        commentPane.getChildren().clear();
        commentPane.setVisible(false);
      });
      commentPane.getChildren().add(deleteButton);
    }
    return commentPane;
  }

  /**
   * Creates a spinner for selecting the number of servings.
   */
  private void createServingSpinner() {
    servingSpinner = new Spinner<>();
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory
        .IntegerSpinnerValueFactory(2, 10, initialServings, 2);
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

  /** Updates the ingredient quantities based on the number of servings.
   *
   * @param servings is the number of servings
   */
  private void updateIngredientQuantities(int servings) {
    double scaleFactor = (double) servings / initialServings;
    GridPane newIngredientsGrid = new GridPane();
    newIngredientsGrid.setVgap(0);
    newIngredientsGrid.setHgap(10);
    int rowIndex = 0;
    for (Ingredient ingredient : recipe.getIngredients()) {
      Ingredient adjustedIngredient = new Ingredient(ingredient.getName(),
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
    for (Node node : tagsHbox.getChildren()) {
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
    for (Node node : tagsHbox.getChildren()) {
      if (node instanceof HBox) {
        HBox tagContainer = (HBox) node;
        String tag = ((Text) tagContainer.getChildren().get(0)).getText().substring(2);
        updatedTags.add(tag);
      }
    }
    return updatedTags;
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
    dialogPane.setStyle(
        "-fx-font-family: 'Roboto'; -fx-font-size: 18px;"
            + " -fx-background-color: #F9F8F3; -fx-border-color: #F9F8F3;");
    // Set custom styles for the buttons
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> {
      button.setStyle("-fx-background-color: #3D405B;"
          + " -fx-text-fill: white; -fx-padding: 5 10 5 10;");
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

    VBox editRecipeVbox = new VBox();
    editRecipeVbox.setSpacing(10);
    editRecipeVbox.setPadding(new Insets(10));

    // ScrollPane for the editRecipeVBox
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setPadding(new Insets(10));

    // Create error label for displaying error messages
    Label errorLabel = new Label();
    errorLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    errorLabel.setTextFill(Color.RED);

    // Create a new HBox to hold the recipe name
    HBox recipeNameHbox = new HBox();
    recipeNameHbox.setSpacing(10);
    recipeNameHbox.setAlignment(Pos.CENTER_LEFT);
    Text recipeNameLabel = new Text("Recipe Name:");
    recipeNameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    TextField recipeNameField = new TextField(recipe.getName());
    recipeNameField.setFont(Font.font("Roboto", 18));
    recipeNameHbox.getChildren().addAll(recipeNameLabel, recipeNameField);
    editRecipeVbox.getChildren().add(recipeNameHbox);

    // Create a new HBox to hold the recipe short description
    HBox recipeShortDescHbox = new HBox();
    recipeShortDescHbox.setSpacing(10);
    recipeShortDescHbox.setAlignment(Pos.CENTER_LEFT);
    Text recipeShortDescLabel = new Text("Short Description:");
    recipeShortDescLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    TextField recipeShortDescField = new TextField(recipe.getShortDesc());
    recipeShortDescField.setFont(Font.font("Roboto", 18));
    recipeShortDescHbox.getChildren().addAll(recipeShortDescLabel, recipeShortDescField);
    editRecipeVbox.getChildren().add(recipeShortDescHbox);
    // Create a new VBox to hold the recipe ingredients
    VBox recipeIngredientsVbox = new VBox();
    recipeIngredientsVbox.setSpacing(10);
    Text ingredientsLabel = new Text("Ingredients:");
    ingredientsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    recipeIngredientsVbox.getChildren().add(ingredientsLabel);
    for (Ingredient ingredient : recipe.getIngredients()) {
      HBox ingredientHbox = new HBox();
      ingredientHbox.setSpacing(10);
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
      deleteIngredientButton.setStyle(
          "-fx-font: 12px \"Roboto\"; -fx-background-color:"
              + " white; -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
      deleteIngredientButton.setOnAction(deleteEvent -> {
        recipeIngredientsVbox.getChildren().remove(ingredientHbox);
      });
      ingredientHbox.getChildren().addAll(ingredientQuantity,
          ingredientUnit, ingredientName, deleteIngredientButton);
      recipeIngredientsVbox.getChildren().add(ingredientHbox);
    }
    editRecipeVbox.getChildren().add(recipeIngredientsVbox);
    // Create a new HBox to add a new ingredient
    HBox addIngredientHbox = new HBox();
    addIngredientHbox.setSpacing(10);
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
    addIngredientButton.setStyle("-fx-background-color: #3D405B;"
        + " -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    addIngredientButton.setOnAction(e -> {
      if (!newIngredientQuantity.getText().isEmpty() && !newIngredientUnit.getText().isEmpty()
          && !newIngredientName.getText().isEmpty()) {
        try {
          Float.parseFloat(newIngredientQuantity.getText());
          HBox ingredientHbox = new HBox();
          ingredientHbox.setSpacing(10);
          TextField ingredientQuantity = new TextField(newIngredientQuantity.getText());
          ingredientQuantity.setPrefWidth(100);
          TextField ingredientUnit = new TextField(newIngredientUnit.getText());
          ingredientUnit.setPrefWidth(100);
          TextField ingredientName = new TextField(newIngredientName.getText());
          ingredientName.setPrefWidth(200);
          Button deleteIngredientButton = new Button("Delete");
          deleteIngredientButton.setStyle("-fx-background-color: white;"
              + " -fx-text-fill:#3D405B ; -fx-padding: 5 10 5 10;");
          deleteIngredientButton.setOnAction(deleteEvent -> {
            recipeIngredientsVbox.getChildren().remove(ingredientHbox);
          });
          ingredientHbox.getChildren().addAll(ingredientQuantity, ingredientUnit, ingredientName,
              deleteIngredientButton);
          recipeIngredientsVbox.getChildren().add(ingredientHbox);
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
    addIngredientHbox.getChildren().addAll(newIngredientQuantity,
        newIngredientUnit, newIngredientName, addIngredientButton);
    editRecipeVbox.getChildren().add(addIngredientHbox);
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
    addDirectionButton.setStyle("-fx-background-color: #3D405B;"
        + " -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    Button modifyDirectionButton = new Button("Modify Direction");
    modifyDirectionButton.setStyle("-fx-background-color: #3D405B;"
        + " -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    Button deleteDirectionButton = new Button("Delete Direction");
    deleteDirectionButton.setStyle("-fx-font: 12px \"Roboto\"; -fx-background-color: white;"
        + " -fx-text-fill: #E07A5F; -fx-cursor: hand; ");
    TextField newDirectionField = new TextField();
    addDirectionButton.setOnAction(e -> {
      if (!newDirectionField.getText().isEmpty()) {
        directionsList.add(new Label((directionsList.size()
            + 1) + ". " + newDirectionField.getText()));
        newDirectionField.clear();
      }
    });
    modifyDirectionButton.setOnAction(e -> {
      int selectedIndex = directionsListView.getSelectionModel().getSelectedIndex();
      if (selectedIndex != -1 && !newDirectionField.getText().isEmpty()) {
        directionsList.set(selectedIndex, new Label((selectedIndex + 1)
            + ". " + newDirectionField.getText()));
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
    HBox directionButtonshBox = new HBox(addDirectionButton,
        modifyDirectionButton, deleteDirectionButton);
    directionButtonshBox.setSpacing(10);
    recipeInstructionsvBox.getChildren().addAll(recipeInstructionsLabel,
        directionsListView, newDirectionField, directionButtonshBox);
    editRecipeVbox.getChildren().add(recipeInstructionsvBox);
    // Collect all the info from the fields and create a new recipe
    // Collect all the info from the fields and create a new recipe
    Button editRecipeButton = new Button("Save Edited Recipe");
    editRecipeButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    editRecipeButton.setOnAction(e -> {
      boolean hasError = false;
      String errorMessage = "";
      // Check if all fields are filled
      if (recipeNameField.getText().isEmpty() || recipeShortDescField.getText().isEmpty()
          || recipeIngredientsVbox.getChildren().isEmpty() || directionsList.isEmpty()) {
        hasError = true;
        errorMessage = "All fields must be filled before saving.";
      }
      ArrayList<String[]> newIngredients = new ArrayList<>();
      for (Node ingredientHbox : recipeIngredientsVbox.getChildren()) {
        if (ingredientHbox instanceof HBox) {
          HBox hbox = (HBox) ingredientHbox;
          try {
            float quantity = Float.parseFloat(((TextField) hbox.getChildren()
                .get(0)).getText().replace(",", "."));
            String unit = ((TextField) hbox.getChildren().get(1)).getText();
            String name = ((TextField) hbox.getChildren().get(2)).getText();
            newIngredients.add(new String[] { name, Float.toString(quantity), unit });
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
    editRecipeVbox.getChildren().add(editRecipeButton);
    editRecipeVbox.getChildren().add(errorLabel);
    scrollPane.setContent(editRecipeVbox);
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
