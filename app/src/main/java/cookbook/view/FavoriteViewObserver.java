package cookbook.view;

import cookbook.model.Recipe;

public interface FavoriteViewObserver {
  void handleStarButtonClicked(Recipe recipe);
  void handleBackButtonClicked();
  void handleRemoveRecipeFromFavorites(Recipe recipe);
  boolean isRecipeFavorite(Recipe recipe);
  void handleRecipeClicked(Recipe recipe);
  void goToRecipe(Recipe recipe);
}

