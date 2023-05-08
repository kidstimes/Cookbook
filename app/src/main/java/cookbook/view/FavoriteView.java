
package cookbook.view;

import cookbook.model.Recipe;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * View class for the favorites page.
 */
public class FavoriteView {
  private FavoriteViewObserver observer;
  private VBox view;
  private ListView<Recipe> favoriteRecipesListView;
  private Button backButton;

  /**
   * Favorite View Constructor.
   */
  public FavoriteView() {
    this.view = new VBox();
    initLayout();
  }

  /**
   * Set an observer of the favorite view.
   */
  public void setObserver(FavoriteViewObserver observer) {
    this.observer = observer;
  }

  /**
   * Get the view of the favorite page.
   */
  public Node getView() {
    return view;
  }

  /**
   * Set the favorite recipes list.
   */
  public void setFavoriteRecipes(List<Recipe> favoriteRecipes) {
    ObservableList<Recipe> observableList = FXCollections.observableArrayList(favoriteRecipes);
    favoriteRecipesListView.setItems(observableList);
  }
  

  /**
   * init the layout of the favorite page.
   */
  public void initLayout() {
    view.setSpacing(10);

    // Create a title
    Label title = new Label("Favorite Recipes");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    view.getChildren().add(title);

    // Create a back button
    backButton = new Button("Back");
    backButton.setOnAction(event -> {
      if (observer != null) {
        observer.handleBackButtonClicked();
      }
    });
    view.getChildren().add(backButton);
    

    // Create a ListView for favorite recipes
    favoriteRecipesListView = new ListView<>();
    favoriteRecipesListView.setCellFactory(recipeListView -> new ListCell<Recipe>() {
      @Override
      protected void updateItem(Recipe item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          HBox hBox = new HBox();
          Label nameLabel = new Label(item.getName());
          nameLabel.setMaxWidth(Double.MAX_VALUE);
          HBox.setHgrow(nameLabel, Priority.ALWAYS);

          //Add a click event handler for nameLabel
          nameLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
              handleRecipeClicked(item);
            }
          });

          Button removeButton = new Button("Remove");
          removeButton.setOnAction(event -> {
            if (observer != null) {
              observer.handleRemoveRecipeFromFavorites(item);
            }
          });
          hBox.getChildren().addAll(nameLabel, removeButton);
          hBox.setAlignment(Pos.CENTER_LEFT);
          hBox.setSpacing(10);
          hBox.setPadding(new Insets(0, 10, 0, 10));

          setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          setGraphic(hBox);
        }
      }
    });
    view.getChildren().add(favoriteRecipesListView);
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

  /**
   * Update the favorite recipe list.
   */
  public void updateFavoriteRecipe(Recipe recipe, boolean isFavorite) {
    if (isFavorite) {
      addFavoriteRecipe(recipe);
    } else {
      removeFavoriteRecipe(recipe);
    }
  }

  private void addFavoriteRecipe(Recipe recipe) {
    favoriteRecipesListView.getItems().add(recipe);
  }

  public void removeFavoriteRecipe(Recipe recipe) {
    favoriteRecipesListView.getItems().remove(recipe);
  }

  public void handleRecipeClicked(Recipe recipe) {
    if (observer != null) {
      observer.goToRecipe(recipe);
    }
  }
  


}

